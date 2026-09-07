"""
Modelo de Dominio para Captura / Espécimen Observado (Capture / Specimen).
Conforme a las decisiones arquitectónicas DEC-036, DEC-039 y DEC-046.
"""

import uuid
from dataclasses import dataclass
from typing import Any, Dict, Optional, Union
from ..enums import SpecimenSex, BiologicalSex
from ...core.exceptions import ValidationError

_UNSET = object()


class CaptureCreationError(ValidationError):
    """Lanzada cuando la creación o el puente hacia Capture no cumple las reglas de dominio."""
    pass


@dataclass(init=False)
class Capture:
    """
    Entidad de Dominio: Captura / Observación de Espécimen (DEC-036, DEC-039, DEC-046).

    Representa la observación y registro concreto de un individuo animal en campo.
    Es la fuente primaria de verdad de los atributos observacionales del espécimen,
    específicamente su especie seleccionada, la trazabilidad del resultado de identificación
    y su sexo biológico (sex ∈ Capture, sex ∉ Animal, sex ∉ Card).

    Frontera Ontológica:
    - Animal: Conocimiento zoológico universal de una especie/taxón.
    - Observation: Evidencia fotográfica efímera pendiente de confirmación.
    - IdentificationResult: Salida bruta con confianza del proceso de identificación.
    - IdentificationDecision: Decisión explícita (ACCEPTED, REJECTED, CANCELLED).
    - Capture: Registro concreto del espécimen identificado tras una decisión ACCEPTED.
    - Card: Artefacto coleccionable generado posteriormente a partir de una Capture.

    Reglas de Dominio:
    - Identidad única e inmutable mediante UUIDv4 canónico (capture_id).
    - Trazabilidad obligatoria e inmutable con el resultado de identificación (identification_id).
    - Especie zoológica confirmada e inmutable (animal_id).
    - Sexo biológico del individuo observado opcional y nullable sin valor por defecto (DEC-039).
    - Prohibido inferir MALE o FEMALE sin evidencia diagnóstica sólida; ante incertidumbre, el valor es UNKNOWN.
    - Cero campos no aprobados (geolocalización, timestamp, telemetría o rareza no pertenecen a este modelo).
    """

    capture_id: str
    animal_id: str
    identification_id: str
    sex: Optional[SpecimenSex]

    _IMMUTABLE_FIELDS = frozenset({"capture_id", "animal_id", "identification_id", "sex"})

    @staticmethod
    def _validate_uuidv4(val: Any, field_name: str) -> str:
        """Valida que el identificador sea estrictamente un UUIDv4 canónico no nulo."""
        if val is _UNSET or val is None:
            raise ValidationError(f"Field '{field_name}' is required and cannot be None")
        if isinstance(val, bool):
            raise ValidationError(f"Field '{field_name}' must be a valid UUIDv4 string, got {val!r}")
        if isinstance(val, uuid.UUID):
            if val.version != 4 or val.int == 0:
                raise ValidationError(
                    f"Field '{field_name}' must be UUIDv4 (non-nil), got UUIDv{val.version}"
                )
            return str(val)
        if isinstance(val, str) and val.strip():
            cleaned = val.strip()
            try:
                u = uuid.UUID(cleaned)
            except (ValueError, AttributeError):
                raise ValidationError(
                    f"Field '{field_name}' must be a valid UUIDv4 string, got {val!r}"
                )
            if u.version != 4 or u.int == 0:
                raise ValidationError(
                    f"Field '{field_name}' must be UUIDv4 (non-nil), got UUIDv{u.version}"
                )
            return str(u)
        raise ValidationError(
            f"Field '{field_name}' must be a valid UUIDv4 string, got {val!r}"
        )

    @staticmethod
    def _validate_animal_id(val: Any, field_name: str) -> str:
        """Valida que el identificador de animal sea una cadena no vacía obligatoria."""
        if val is _UNSET or val is None:
            raise ValidationError(f"Field '{field_name}' is required and cannot be None")
        if not isinstance(val, str) or not val.strip():
            raise ValidationError(f"Field '{field_name}' must be a non-empty string, got {val!r}")
        return val.strip()

    def __init__(
        self,
        capture_id: Any = _UNSET,
        animal_id: Any = _UNSET,
        identification_id: Any = _UNSET,
        sex: Any = _UNSET,
        **extra_kwargs: Any,
    ):
        if extra_kwargs:
            raise ValidationError(
                f"Unexpected extra fields not permitted in Capture: {list(extra_kwargs.keys())}"
            )

        # 1. capture_id (Required + non-null, estrictamente UUIDv4 no nulo)
        self.capture_id = self._validate_uuidv4(capture_id, "capture_id")

        # 2. animal_id (Required + non-null, cadena no vacía)
        self.animal_id = self._validate_animal_id(animal_id, "animal_id")

        # 3. identification_id (Required + non-null, estrictamente UUIDv4 no nulo)
        self.identification_id = self._validate_uuidv4(identification_id, "identification_id")

        # 4. sex (Optional + nullable, SpecimenSex: MALE, FEMALE, UNKNOWN, sin valor por defecto)
        if sex is not _UNSET:
            if sex is None:
                self.sex = None
            elif isinstance(sex, SpecimenSex):
                self.sex = sex
            elif isinstance(sex, str):
                cleaned_sex = sex.strip().upper()
                try:
                    self.sex = SpecimenSex(cleaned_sex)
                except ValueError:
                    raise ValidationError(
                        f"Invalid sex: {sex!r}. "
                        f"Expected one of: {[s.value for s in SpecimenSex]}"
                    )
            else:
                raise ValidationError(
                    f"sex must be a SpecimenSex, str or None, got {sex!r}"
                )

        object.__setattr__(self, "_initialized", True)

    def __setattr__(self, name: str, value: Any) -> None:
        if getattr(self, "_initialized", False):
            raise ValidationError(
                f"Capture is immutable after recording: cannot modify attribute '{name}'."
            )
        super().__setattr__(name, value)

    def to_dict(self) -> Dict[str, Any]:
        """Serializa la entidad Capture a diccionario conforme al contrato de dominio."""
        result: Dict[str, Any] = {
            "capture_id": self.capture_id,
            "animal_id": self.animal_id,
            "identification_id": self.identification_id,
        }
        if hasattr(self, "sex"):
            result["sex"] = self.sex.value if self.sex is not None else None
        return result

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "Capture":
        """Reconstruye una entidad Capture validando estrictamente el contrato de dominio."""
        if not isinstance(data, dict):
            raise ValidationError("data must be a dictionary")
        return cls(**data)

    @classmethod
    def create_from_decision(
        cls,
        decision: Any,
        identification_result: Any,
        sex: Any = _UNSET,
    ) -> "Capture":
        """
        Crea una Capture válida a partir de una IdentificationDecision en estado ACCEPTED
        y su correspondiente IdentificationResult.

        Valida:
        1. La decisión pertenece al IdentificationResult recibido (identification_id coincide).
        2. La decisión está en estado ACCEPTED (rechaza REJECTED, CANCELLED o cualquier otro).
        3. selected_animal_id existe y es no vacío.
        4. selected_animal_id pertenece a los candidatos del IdentificationResult.
        5. Genera un nuevo capture_id UUIDv4.
        6. animal_id es exactamente selected_animal_id.
        7. identification_id es exactamente el de la decisión.
        8. sex respeta DEC-039 (opcional, nullable, sin inferencias).
        """
        if decision is None:
            raise ValidationError("IdentificationDecision is required to create a Capture")
        if identification_result is None:
            raise ValidationError("IdentificationResult is required to create a Capture")

        # 1. Validar estado de la decisión (debe ser ACCEPTED)
        decision_state = getattr(decision, "decision", None)
        if decision_state is None:
            raise ValidationError("Invalid decision object: missing 'decision' attribute")

        decision_val = decision_state.value if hasattr(decision_state, "value") else str(decision_state)
        if decision_val != "ACCEPTED":
            raise ValidationError(
                f"Cannot create Capture from decision with state '{decision_val}'. "
                "Only ACCEPTED decisions can originate a Capture."
            )

        # 2. Validar identificadores y trazabilidad
        dec_ident_id = getattr(decision, "identification_id", None)
        res_ident_id = getattr(identification_result, "identification_id", None)
        if not dec_ident_id:
            raise ValidationError("IdentificationDecision is missing 'identification_id'")
        if not res_ident_id:
            raise ValidationError("IdentificationResult is missing 'identification_id'")
        if str(dec_ident_id) != str(res_ident_id):
            raise ValidationError(
                f"IdentificationDecision identification_id '{dec_ident_id}' does not match "
                f"IdentificationResult identification_id '{res_ident_id}'"
            )

        # 3. Validar selected_animal_id
        selected_animal_id = getattr(decision, "selected_animal_id", None)
        if not selected_animal_id or not isinstance(selected_animal_id, str) or not selected_animal_id.strip():
            raise ValidationError("IdentificationDecision is missing a valid 'selected_animal_id'")
        selected_animal_id = selected_animal_id.strip()

        # 4. Validar que selected_animal_id pertenezca a los candidatos del IdentificationResult
        candidates = getattr(identification_result, "candidate_species", None)
        if candidates is None or not isinstance(candidates, list):
            raise ValidationError("IdentificationResult is missing valid 'candidate_species' list")

        valid_candidate_ids = set()
        for cand in candidates:
            if isinstance(cand, dict) and "animal_id" in cand:
                aid = cand.get("animal_id")
                if isinstance(aid, str) and aid.strip():
                    valid_candidate_ids.add(aid.strip())
            elif isinstance(cand, str) and cand.strip():
                valid_candidate_ids.add(cand.strip())

        if selected_animal_id not in valid_candidate_ids:
            raise ValidationError(
                f"selected_animal_id '{selected_animal_id}' does not exist among the candidates "
                f"of the IdentificationResult: {sorted(valid_candidate_ids)}"
            )

        # 5. Generar nuevo capture_id UUIDv4
        new_capture_id = str(uuid.uuid4())

        # 6. Construir la Capture
        kwargs = {
            "capture_id": new_capture_id,
            "animal_id": selected_animal_id,
            "identification_id": str(dec_ident_id),
        }
        if sex is not _UNSET:
            kwargs["sex"] = sex

        return cls(**kwargs)

    @classmethod
    def from_decision(
        cls,
        decision: Any,
        identification_result: Any,
        sex: Any = _UNSET,
    ) -> "Capture":
        """Alias semántico para create_from_decision."""
        return cls.create_from_decision(decision, identification_result, sex=sex)


def create_capture_from_decision(
    decision: Any,
    identification_result: Any,
    sex: Any = _UNSET,
) -> Capture:
    """Función de servicio de dominio para crear Capture a partir de una decisión aceptada."""
    return Capture.create_from_decision(decision, identification_result, sex=sex)


# Alias semántico y conceptual para trazabilidad en DDD (DEC-036)
Specimen = Capture
