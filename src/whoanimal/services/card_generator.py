"""
Servicio de Dominio para Ensamblaje y Generación de Cartas (WHO-014).
Permite emitir y ensamblar formalmente una AnimalCard a partir de una Capture validada.
Conforme a docs/CARD_SPEC.md, docs/PRODUCT_RULES.md, docs/PROJECT_CONTEXT.md y DEC-048.
"""

import glob
import json
import os
import uuid
from collections import defaultdict
from datetime import datetime, timezone
from typing import Any, Dict, List, Optional, Set, Union

from ..core.exceptions import CardCreationError, ValidationError
from ..domain.enums import CardRarity, VerificationStatus
from ..domain.models.animal import AnimalProfile
from ..domain.models.capture import Capture
from ..domain.models.card import AnimalCard, Card


class CardGenerationError(ValidationError, CardCreationError):
    """Excepción base para errores durante la generación de cartas desde una captura."""
    pass


class InvalidCaptureError(CardGenerationError):
    """Lanzada cuando el Capture proporcionado es nulo, de tipo erróneo o tiene campos inválidos."""
    pass


class AnimalProfileNotFoundError(CardGenerationError):
    """Lanzada cuando el animal_id del Capture no corresponde a ninguna especie del catálogo."""
    pass


class DuplicateCardError(CardGenerationError):
    """Lanzada cuando se intenta generar más de una carta para el mismo Capture (regla: 1 captura -> <= 1 carta)."""
    pass


class CardGeneratorService:
    """
    Servicio de Dominio: Ensamblaje y Generación de Cartas (Capture -> AnimalCard).

    Responsabilidades:
    - Valida que el Capture de entrada sea una entidad válida, consistente y no nula.
    - Rechaza la generación de cartas a partir de estados inválidos u objetos parcialmente construidos.
    - Resuelve el AnimalProfile verídico a partir de animal_id, sin inventar datos científicos.
    - Aplica la regla formal de unicidad '1 captura -> <= 1 carta' (CARD_SPEC.md).
    - Acuña los 19 campos canónicos del contrato formal (Card / CARD_SPEC.md):
      card_id (UUIDv4 único), capture_id, animal_id, issued_at (UTC), generation,
      population_at_issuance (conteo acumulado), serial / auth_serial único,
      verification_status (UNVERIFIED), etc.
    - Preserva de forma absoluta la frontera ontológica Capture != Card.
    """

    def __init__(
        self,
        catalog: Optional[Dict[str, AnimalProfile]] = None,
        species_dir: Optional[str] = "data/species",
        auto_load_catalog: bool = True,
    ) -> None:
        self._catalog: Dict[str, AnimalProfile] = {}
        self._issued_captures: Set[str] = set()
        self._species_population: Dict[str, int] = defaultdict(int)
        self._next_specimen_number: int = 1

        if auto_load_catalog and species_dir and os.path.isdir(species_dir):
            self._load_catalog_from_dir(species_dir)

        if catalog:
            for key, profile in catalog.items():
                if isinstance(profile, AnimalProfile):
                    self.register_animal_profile(profile, key=key)

    def _load_catalog_from_dir(self, species_dir: str) -> None:
        """Carga perfiles animales verídicos desde los datasets JSON del catálogo zoológico oficial."""
        pattern = os.path.join(species_dir, "*.json")
        for filepath in glob.glob(pattern):
            try:
                with open(filepath, "r", encoding="utf-8") as f:
                    data = json.load(f)
                if isinstance(data, list):
                    for item in data:
                        if isinstance(item, dict) and "animal_id" in item:
                            profile = AnimalProfile(
                                animal_id=item["animal_id"],
                                scientific_name=item["scientific_name"],
                                common_name=item["common_name"],
                                taxonomy=item["taxonomy"],
                                conservation_status=item.get("conservation_status"),
                                is_rare_species=item.get("is_rare_species"),
                            )
                            self.register_animal_profile(profile)
                            # Registrar también por slug y nombre científico para compatibilidad de resolución
                            sc_name = item.get("scientific_name", "")
                            slug = sc_name.strip().lower().replace(" ", "_")
                            if slug and slug not in self._catalog:
                                self._catalog[slug] = profile
                            if sc_name and sc_name not in self._catalog:
                                self._catalog[sc_name] = profile
            except Exception:
                pass

    def register_animal_profile(self, profile: AnimalProfile, key: Optional[str] = None) -> None:
        """Registra un perfil animal en el catálogo en memoria del servicio."""
        if not isinstance(profile, AnimalProfile):
            raise ValidationError(f"Expected AnimalProfile instance, got {type(profile).__name__}")
        self._catalog[profile.animal_id] = profile
        if key and key != profile.animal_id:
            self._catalog[key] = profile

    def get_animal_profile(self, animal_id: str) -> Optional[AnimalProfile]:
        """Obtiene el AnimalProfile asociado al animal_id indicado."""
        if not isinstance(animal_id, str):
            return None
        cleaned = animal_id.strip()
        if cleaned in self._catalog:
            return self._catalog[cleaned]
        slug = cleaned.lower().replace(" ", "_")
        if slug in self._catalog:
            return self._catalog[slug]
        return None

    def has_card_for_capture(self, capture_id: str) -> bool:
        """Indica si ya se ha emitido una Card para el capture_id especificado."""
        return capture_id in self._issued_captures

    def generate(
        self,
        capture: Capture,
        *,
        specimen_number: Optional[int] = None,
        generation: str = "genesis",
        population_at_issuance: Optional[int] = None,
        rarity: Optional[Union[str, CardRarity]] = None,
        identification_method: str = "in_app_capture",
        identification_confidence: Optional[float] = None,
        rank: Union[int, str] = 1,
        display_location: Optional[str] = None,
        edition: Optional[str] = None,
        visual_effects: Optional[List[str]] = None,
        artwork: Optional[Any] = None,
        owner_id: Optional[str] = None,
        auth_serial: Optional[str] = None,
        serial: Optional[str] = None,
        issued_at: Optional[datetime] = None,
    ) -> AnimalCard:
        """
        Emite y ensambla formalmente una AnimalCard a partir de una Capture válida.

        Reglas de Dominio:
        - Rechaza Capture nulo, de tipo erróneo o con campos requeridos ausentes.
        - Rechaza la duplicación: una Capture no puede generar múltiples cartas.
        - Requiere que el animal_id del Capture exista en el catálogo (no inventa datos científicos).
        - Genera un nuevo card_id UUIDv4 estricto (diferente de capture_id).
        - Vincula de forma inmutable capture_id y animal_id con la Capture de origen.
        - verification_status nace estrictamente en UNVERIFIED (sin falsificar auditoría científica).
        - Genera un auth_serial único y limpio sin coordenadas GPS ni datos privados.
        - Mantiene Capture y Card ontológicamente desacoplados (Capture no se muta).
        """
        # 1. Validar que capture no sea None y sea instancia de Capture
        if capture is None:
            raise InvalidCaptureError("Capture is required and cannot be None.")
        if not isinstance(capture, Capture):
            raise InvalidCaptureError(
                f"Expected Capture instance, got {type(capture).__name__}."
            )

        # 2. Validar campos obligatorios de Capture
        capture_id = getattr(capture, "capture_id", None)
        animal_id = getattr(capture, "animal_id", None)
        identification_id = getattr(capture, "identification_id", None)

        if not capture_id or not isinstance(capture_id, str) or not capture_id.strip():
            raise InvalidCaptureError("Capture has missing or empty 'capture_id'.")
        if not animal_id or not isinstance(animal_id, str) or not animal_id.strip():
            raise InvalidCaptureError("Capture has missing or empty 'animal_id'.")
        if not identification_id or not isinstance(identification_id, str) or not identification_id.strip():
            raise InvalidCaptureError("Capture has missing or empty 'identification_id'.")

        capture_id = capture_id.strip()
        animal_id = animal_id.strip()

        # 3. Protección contra duplicación (CARD_SPEC: 1 captura -> <= 1 carta)
        if capture_id in self._issued_captures:
            raise DuplicateCardError(
                f"Card has already been generated for capture_id '{capture_id}'. "
                "Contractual rule: 1 capture -> <= 1 card."
            )

        # 4. Resolver AnimalProfile en catálogo
        profile = self.get_animal_profile(animal_id)
        if profile is None:
            raise AnimalProfileNotFoundError(
                f"AnimalProfile with animal_id '{animal_id}' does not exist in catalog. "
                "Cannot invent zoological data."
            )

        # 5. Generar card_id (UUIDv4 único, diferente a capture_id)
        new_card_id = str(uuid.uuid4())
        while new_card_id == capture_id:
            new_card_id = str(uuid.uuid4())

        # 6. specimen_number
        if specimen_number is not None:
            if not isinstance(specimen_number, int) or isinstance(specimen_number, bool) or specimen_number < 1:
                raise ValidationError(f"specimen_number must be an integer >= 1, got {specimen_number!r}")
            card_specimen_number = specimen_number
        else:
            card_specimen_number = self._next_specimen_number
            self._next_specimen_number += 1

        # 7. population_at_issuance (DEC-033: conteo acumulado para la especie al momento de emitir)
        if population_at_issuance is not None:
            if not isinstance(population_at_issuance, int) or isinstance(population_at_issuance, bool) or population_at_issuance < 1:
                raise ValidationError(f"population_at_issuance must be an integer >= 1, got {population_at_issuance!r}")
            card_population = population_at_issuance
            self._species_population[animal_id] = max(self._species_population[animal_id], population_at_issuance)
        else:
            self._species_population[animal_id] += 1
            card_population = self._species_population[animal_id]

        # 8. rarity (DEC-016, DEC-022-PENDING: tipo abierto, desacoplado de rank)
        if rarity is not None:
            card_rarity = rarity
        else:
            card_rarity = "RARE" if getattr(profile, "is_rare_species", False) else "COMMON"

        # 9. serial / auth_serial (DEC-018: serial visible alfanumérico limpio sin GPS ni PII)
        card_serial = serial or auth_serial
        if not card_serial:
            card_serial = f"WA-{uuid.uuid4().hex[:8].upper()}"

        # 10. issued_at (UTC real, sin default silencioso automático en modelo)
        card_issued_at = issued_at if issued_at is not None else datetime.now(timezone.utc)

        # 11. Preparar argumentos canónicos para la AnimalCard
        card_kwargs: Dict[str, Any] = {
            "card_id": new_card_id,
            "animal_id": animal_id,
            "specimen_number": card_specimen_number,
            "schema_version": "1.0",
            "generation": generation,
            "issued_at": card_issued_at,
            "population_at_issuance": card_population,
            "rarity": card_rarity,
            "capture_id": capture_id,
            "identification_method": identification_method,
            "identification_confidence": identification_confidence,
            "rank": rank,
            "display_location": display_location,
            "visual_effects": visual_effects if visual_effects is not None else [],
            "artwork": artwork,
            "owner_id": owner_id,
            "serial": card_serial,
            "verification_status": VerificationStatus.UNVERIFIED,
        }
        if edition is not None:
            card_kwargs["edition"] = edition

        # 12. Construir la AnimalCard formal
        card = AnimalCard(**card_kwargs)

        # 13. Registrar capture_id para prevenir duplicación
        self._issued_captures.add(capture_id)

        return card

    def create_card(
        self,
        profile: AnimalProfile,
        image_uri: str,
        lore: Optional[Any] = None,
        visual_theme: str = "standard",
    ) -> AnimalCard:
        """Método de conveniencia retrocompatible con los prototipos tempranos de Foundation."""
        from ..domain.models.card import CardBack, CardFront, CardMetadata
        metadata = CardMetadata(
            card_id=f"card_{uuid.uuid4().hex[:8]}",
            animal_id=profile.animal_id,
            card_code=f"WA-{profile.animal_id[:4].upper()}",
        )
        front = CardFront(
            image_uri=image_uri,
            common_name=profile.common_name,
            scientific_name_secondary=profile.scientific_name,
            category=getattr(profile, "category", None) or "MAMMAL",
            visual_theme=visual_theme,
        )
        back = CardBack(
            profile=profile,
            lore=lore,
        )
        return AnimalCard(metadata=metadata, front=front, back=back)


# Instancia por defecto para conveniencia
_default_generator: Optional[CardGeneratorService] = None


def get_default_generator() -> CardGeneratorService:
    """Devuelve o inicializa la instancia singleton por defecto de CardGeneratorService."""
    global _default_generator
    if _default_generator is None:
        _default_generator = CardGeneratorService()
    return _default_generator


def create_card_from_capture(
    capture: Capture,
    generator: Optional[CardGeneratorService] = None,
    **kwargs: Any,
) -> AnimalCard:
    """
    Función de conveniencia para emitir una AnimalCard a partir de un Capture.
    Utiliza el CardGeneratorService proporcionado o el servicio por defecto.
    """
    svc = generator or get_default_generator()
    return svc.generate(capture, **kwargs)
