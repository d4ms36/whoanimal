"""
Modelo de Dominio para Captura / Espécimen Observado (Capture / Specimen).
Conforme a la decisión arquitectónica DEC-036.
"""

import uuid
from dataclasses import dataclass
from typing import Any, Dict, Union
from ..enums import SpecimenSex, BiologicalSex
from ...core.exceptions import ValidationError

_UNSET = object()


@dataclass(init=False)
class Capture:
    """
    Entidad de Dominio: Captura / Observación de Espécimen (DEC-036).

    Representa la observación y registro concreto de un individuo animal en campo.
    Es la fuente primaria de verdad de los atributos observacionales del espécimen,
    específicamente su sexo biológico (sex ∈ Capture, sex ∉ Animal, sex ∉ Card).

    Frontera Ontológica:
    - Animal: Conocimiento zoológico universal de una especie/taxón.
    - Capture: Observación/captura concreta de un individuo físico.
    - Card: Artefacto coleccionable generado a partir de una Capture.

    Reglas de Dominio:
    - Identidad única e inmutable mediante UUIDv4 canónico (capture_id).
    - Sexo biológico del individuo observado (sex ∈ {MALE, FEMALE, UNKNOWN}).
    - Prohibido inferir MALE o FEMALE sin evidencia diagnóstica sólida; ante incertidumbre, el valor es UNKNOWN.
    - Cero campos inventados en el modelo mínimo actual (geolocalización, telemetría o imagen se modelarán en fases posteriores).
    """

    capture_id: str
    sex: SpecimenSex

    _IMMUTABLE_FIELDS = frozenset({"capture_id", "sex"})

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

    def __init__(
        self,
        capture_id: Any = _UNSET,
        sex: Any = _UNSET,
        **extra_kwargs: Any,
    ):
        if extra_kwargs:
            raise ValidationError(
                f"Unexpected extra fields not permitted in Capture: {list(extra_kwargs.keys())}"
            )

        # 1. capture_id (Required + non-null, estrictamente UUIDv4 no nulo)
        self.capture_id = self._validate_uuidv4(capture_id, "capture_id")

        # 2. sex (Optional + nullable, SpecimenSex: MALE, FEMALE, UNKNOWN)
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
        }
        if hasattr(self, "sex"):
            result["sex"] = self.sex.value if self.sex is not None else None
        return result

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "Capture":
        """Reconstruye una entidad Capture validando estrictamente el contrato de dominio."""
        return cls(**data)


# Alias semántico y conceptual para trazabilidad en DDD (DEC-036)
Specimen = Capture
