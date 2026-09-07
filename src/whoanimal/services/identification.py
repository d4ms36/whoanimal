"""
Servicio de Identificación Zoológica para WHO Animal.

Responsabilidad:
Observation → IdentificationService → IdentificationResult

Reglas Fundamentales de Dominio:
- Observation != IdentificationResult != IdentificationDecision != Capture != Card.
- IdentificationService NO crea Capture automáticamente.
- IdentificationService NO crea Card automáticamente.
- Los candidatos deben corresponder a AnimalProfile / especies reales del catálogo oficial (data/species/).
- No se inventa información científica ni confidence artificial no documentada.
- DeterministicIdentificationProvider es una implementación Alpha/testable, reemplazable por visión real (WHO-016).
"""

from abc import ABC, abstractmethod
from datetime import datetime, timezone
import json
from pathlib import Path
from typing import Any, Dict, List, Optional
import uuid

from ..domain.models.observation import Observation
from ..domain.models.identification_result import IdentificationResult, IdentificationResultError


class IdentificationServiceError(Exception):
    """Clase base para errores del servicio de identificación."""
    pass


class InvalidObservationError(IdentificationServiceError):
    """Indica que la observación proporcionada es nula, de tipo incorrecto o inválida."""
    pass


class IdentificationProviderError(IdentificationServiceError):
    """Indica que el proveedor de identificación falló o no pudo producir candidatos."""
    pass


class IdentificationProvider(ABC):
    """Contrato base para proveedores de identificación zoológica."""

    @abstractmethod
    def identify(self, observation: Observation) -> List[Dict[str, Any]]:
        """
        Analiza una Observation y produce una lista ordenada de candidatos.
        Cada candidato debe ser un diccionario con las claves 'animal_id' (str) y 'confidence' (float entre 0.0 y 1.0).
        """
        pass


class DeterministicIdentificationProvider(IdentificationProvider):
    """
    Proveedor determinista de identificación para Alpha 0.1 y pruebas de contrato.

    Utiliza exclusivamente especies reales del catálogo oficial del proyecto (data/species/).
    Es una implementación controlada y auditable, diseñada para ser reemplazada posteriormente
    por un motor de visión artificial (VisionIdentificationProvider) sin alterar el contrato superior.
    """

    def __init__(
        self,
        catalog_path: Optional[Path] = None,
        custom_catalog: Optional[Dict[str, Dict[str, Any]]] = None,
    ):
        self._catalog: Dict[str, Dict[str, Any]] = {}
        if custom_catalog is not None:
            self._catalog = dict(custom_catalog)
        else:
            self._load_catalog(catalog_path)

    def _load_catalog(self, catalog_path: Optional[Path] = None) -> None:
        """Carga las especies oficiales desde data/species/."""
        if catalog_path is not None:
            species_dir = Path(catalog_path)
        else:
            # Ruta relativa estándar del repositorio
            base_dir = Path(__file__).resolve().parent.parent.parent.parent
            species_dir = base_dir / "data" / "species"

        if species_dir.exists() and species_dir.is_dir():
            for json_file in species_dir.glob("*.json"):
                try:
                    with open(json_file, "r", encoding="utf-8") as f:
                        data = json.load(f)
                        if isinstance(data, list) and len(data) > 0 and isinstance(data[0], dict):
                            entry = data[0]
                            animal_id = entry.get("animal_id")
                            if animal_id:
                                self._catalog[animal_id] = entry
                                # Indexar también por nombre científico normalizado para búsqueda por clave
                                sci_name = entry.get("scientific_name", "").strip().lower().replace(" ", "_")
                                if sci_name:
                                    self._catalog[sci_name] = entry
                except Exception:
                    continue

    @property
    def catalog_size(self) -> int:
        return len(self._catalog)

    def identify(self, observation: Observation) -> List[Dict[str, Any]]:
        """
        Produce candidatos deterministas a partir de una observación válida.
        Si la observación contiene una pista en image_path o candidate_species, la respeta;
        en caso contrario, devuelve especies principales del catálogo oficial.
        """
        if not self._catalog:
            raise IdentificationProviderError("Official species catalog is empty or not loaded.")

        candidates: List[Dict[str, Any]] = []

        # 1. Verificar si image_path sugiere una especie específica del catálogo
        image_key = (observation.image_path or "").lower().strip()
        matched_entry = None
        for key, entry in self._catalog.items():
            if key in image_key:
                matched_entry = entry
                break

        # 2. Si hay coincidencia directa en catálogo
        if matched_entry:
            primary_id = matched_entry["animal_id"]
            candidates.append({"animal_id": primary_id, "confidence": 0.95})

            # Añadir un candidato alternativo del catálogo si está disponible
            for key, entry in self._catalog.items():
                if entry["animal_id"] != primary_id:
                    candidates.append({"animal_id": entry["animal_id"], "confidence": 0.40})
                    break
        else:
            # 3. Determinismo por defecto: seleccionar candidatos estables del catálogo
            # Ordenar entradas por animal_id para garantizar determinismo estricto
            unique_entries = {entry["animal_id"]: entry for entry in self._catalog.values()}
            sorted_ids = sorted(unique_entries.keys())

            if len(sorted_ids) >= 1:
                candidates.append({"animal_id": sorted_ids[0], "confidence": 0.92})
            if len(sorted_ids) >= 2:
                candidates.append({"animal_id": sorted_ids[1], "confidence": 0.55})
            if len(sorted_ids) >= 3:
                candidates.append({"animal_id": sorted_ids[2], "confidence": 0.20})

        if not candidates:
            raise IdentificationProviderError("Provider was unable to produce candidates.")

        # Asegurar orden estrictamente descendente por confidence
        candidates.sort(key=lambda c: c["confidence"], reverse=True)
        return candidates


class IdentificationService:
    """
    Servicio de dominio para la identificación de fauna sobre una Observation.

    Responsabilidad:
    Observation → IdentificationResult

    Regla de Integridad:
    NO crea Capture.
    NO crea Card.
    """

    def __init__(self, provider: Optional[IdentificationProvider] = None):
        self._provider = provider or DeterministicIdentificationProvider()

    @property
    def provider(self) -> IdentificationProvider:
        return self._provider

    def identify(self, observation: Observation) -> IdentificationResult:
        """
        Ejecuta la identificación zoológica sobre una observación.

        Args:
            observation: Objeto Observation válido y no nulo.

        Returns:
            IdentificationResult canónico con candidatos ordenados por confianza.

        Raises:
            InvalidObservationError: Si la observación es nula, inválida o de tipo erróneo.
            IdentificationProviderError: Si el proveedor falla o no produce candidatos.
        """
        if observation is None:
            raise InvalidObservationError("Observation cannot be None.")

        if not isinstance(observation, Observation):
            raise InvalidObservationError(
                f"Expected Observation instance, got {type(observation).__name__}."
            )

        observation_id = getattr(observation, "observation_id", None)
        if not observation_id or not isinstance(observation_id, str) or not observation_id.strip():
            raise InvalidObservationError("Observation is missing a valid 'observation_id'.")

        # Invocar proveedor
        try:
            candidates = self._provider.identify(observation)
        except IdentificationServiceError:
            raise
        except Exception as e:
            raise IdentificationProviderError(f"Identification provider failed: {str(e)}") from e

        if not candidates:
            raise IdentificationProviderError("Provider produced an empty candidate list.")

        # Construir IdentificationResult canónico
        try:
            result = IdentificationResult(
                identification_id=str(uuid.uuid4()),
                observation_id=observation.observation_id,
                candidate_species=candidates,
                identification_method="DETERMINISTIC_ALPHA",
                created_at=datetime.now(timezone.utc).isoformat(),
            )
        except IdentificationResultError as e:
            raise IdentificationServiceError(f"Failed to assemble valid IdentificationResult: {str(e)}") from e

        return result

    def identify_from_image(self, image_data: Any) -> Optional[Any]:
        """
        Método de conveniencia retrocompatible con el protocolo inicial de Foundation.
        Crea una observación temporal y ejecuta la identificación.
        """
        image_path = str(image_data) if image_data else "unknown_image.jpg"
        obs = Observation.create(image_path=image_path)
        try:
            result = self.identify(obs)
            return result
        except IdentificationServiceError:
            return None
