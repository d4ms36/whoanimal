import re
import uuid
from dataclasses import dataclass, field
from datetime import datetime, timezone
from enum import Enum
from typing import Optional, Any, List, Dict, Union
from ..enums import AnimalCategory, CardRarity, VerificationStatus
from ...core.exceptions import ValidationError
from .animal import AnimalProfile
from .lore import LoreProfile

_UNSET = object()


@dataclass(init=False)
class Card:
    """
    Modelo de Dominio Formal de la Carta Coleccionable (19 Campos Canónicos).

    Representa un artefacto individual de colección emitido por WHO Animal
    a partir de una observación válida en campo (Capture). Conforme a la
    especificación formal en docs/CARD_SPEC.md y las decisiones arquitectónicas
    aprobadas (DEC-013 a DEC-035).

    Pilares y Contrato:
    - 19 campos canónicos exactos (sin campos inventados ni PII ni GPS exacto).
    - Inmutabilidad post-emisión para los 13 atributos históricos.
    - Mutabilidad controlada para rank, visual_effects, artwork, owner_id y verification_status.
    - Semántica especial para display_location (origen histórico protegido, presentación evolutiva).
    - Únicos defaults contractuales aprobados: verification_status = UNVERIFIED, visual_effects = [].
    """
    card_id: str
    animal_id: str
    specimen_number: int
    schema_version: str
    edition: Optional[str]
    generation: str
    issued_at: datetime
    population_at_issuance: int
    rarity: str
    capture_id: str
    identification_method: str
    identification_confidence: Optional[float]
    rank: Union[int, str]
    display_location: Optional[str]
    visual_effects: List[str] = field(default_factory=list)
    artwork: Optional[Any]
    owner_id: Optional[str]
    serial: str
    verification_status: VerificationStatus = field(default=VerificationStatus.UNVERIFIED)

    _IMMUTABLE_FIELDS = frozenset({
        "card_id",
        "animal_id",
        "specimen_number",
        "schema_version",
        "edition",
        "generation",
        "issued_at",
        "population_at_issuance",
        "rarity",
        "capture_id",
        "identification_method",
        "identification_confidence",
        "serial",
    })

    _MUTABLE_FIELDS = frozenset({
        "rank",
        "visual_effects",
        "artwork",
        "owner_id",
        "verification_status",
        "display_location",
    })

    @staticmethod
    def _validate_uuidv4(val: Any, field_name: str) -> str:
        """Valida que un campo contenga estrictamente un UUIDv4 canónico."""
        if val is _UNSET:
            raise ValidationError(f"Field '{field_name}' is required")
        if val is None:
            raise ValidationError(f"Field '{field_name}' cannot be None")
        if isinstance(val, bool):
            raise ValidationError(f"Field '{field_name}' must be a valid UUIDv4 string, got {val!r}")
        if isinstance(val, uuid.UUID):
            if val.version != 4:
                raise ValidationError(
                    f"Field '{field_name}' must be UUIDv4, got UUIDv{val.version}"
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
            if u.version != 4:
                raise ValidationError(
                    f"Field '{field_name}' must be UUIDv4, got UUIDv{u.version}"
                )
            return str(u)
        raise ValidationError(
            f"Field '{field_name}' must be a valid UUIDv4 string, got {val!r}"
        )

    @staticmethod
    def _validate_display_location(loc: str) -> None:
        """Protección razonable contra la inclusión accidental de coordenadas exactas."""
        if not isinstance(loc, str) or not loc.strip():
            raise ValidationError("display_location must be a non-empty string when present")
        if re.search(r"[-+]?\d{1,3}\.\d{4,}\s*,\s*[-+]?\d{1,3}\.\d{4,}", loc) or re.search(
            r"\b(lat|latitude|lon|longitude)\s*[:=]\s*[-+]?\d+", loc, re.IGNORECASE
        ):
            raise ValidationError(
                f"display_location cannot contain exact GPS coordinates (privacy / anti-poaching protection): {loc!r}"
            )

    def __init__(
        self,
        card_id: str = _UNSET,
        animal_id: str = _UNSET,
        specimen_number: int = _UNSET,
        schema_version: str = _UNSET,
        edition: Optional[str] = _UNSET,
        generation: str = _UNSET,
        issued_at: datetime = _UNSET,
        population_at_issuance: int = _UNSET,
        rarity: Any = _UNSET,
        capture_id: str = _UNSET,
        identification_method: str = _UNSET,
        identification_confidence: Optional[float] = _UNSET,
        rank: Any = _UNSET,
        display_location: Optional[str] = _UNSET,
        visual_effects: List[str] = _UNSET,
        artwork: Optional[Any] = _UNSET,
        owner_id: Optional[str] = _UNSET,
        serial: str = _UNSET,
        verification_status: Any = _UNSET,
        **extra_kwargs: Any,
    ):
        if extra_kwargs:
            raise ValidationError(
                f"Unexpected extra fields not permitted in Card: {list(extra_kwargs.keys())}"
            )

        # 1. card_id (Required + non-null, estrictamente UUIDv4)
        self.card_id = self._validate_uuidv4(card_id, "card_id")

        # 2. animal_id (Required + non-null, string no vacía)
        if animal_id is _UNSET:
            raise ValidationError("Field 'animal_id' is required")
        if animal_id is None:
            raise ValidationError("Field 'animal_id' cannot be None")
        if not isinstance(animal_id, str) or not animal_id.strip():
            raise ValidationError(f"animal_id must be a non-empty string, got {animal_id!r}")
        self.animal_id = animal_id.strip()

        # 3. specimen_number (Required + non-null, integer >= 1)
        if specimen_number is _UNSET:
            raise ValidationError("Field 'specimen_number' is required")
        if specimen_number is None:
            raise ValidationError("Field 'specimen_number' cannot be None")
        if not isinstance(specimen_number, int) or isinstance(specimen_number, bool) or specimen_number < 1:
            raise ValidationError(f"specimen_number must be an integer >= 1, got {specimen_number!r}")
        self.specimen_number = specimen_number

        # 4. schema_version (Required + non-null, string no vacía, sin default inventado)
        if schema_version is _UNSET:
            raise ValidationError("Field 'schema_version' is required")
        if schema_version is None:
            raise ValidationError("Field 'schema_version' cannot be None")
        if not isinstance(schema_version, str) or not schema_version.strip():
            raise ValidationError(f"schema_version must be a non-empty string, got {schema_version!r}")
        self.schema_version = schema_version.strip()

        # 5. edition (Optional + non-null: se omite o string no vacía; no admite None)
        if edition is not _UNSET:
            if edition is None:
                raise ValidationError("edition cannot be None (Optional but non-nullable; omit the field if absent)")
            elif not isinstance(edition, str) or not edition.strip():
                raise ValidationError(f"edition must be a non-empty string when present, got {edition!r}")
            else:
                self.edition = edition.strip()

        # 6. generation (Required + non-null, string no vacía, sin default universal)
        if generation is _UNSET:
            raise ValidationError("Field 'generation' is required")
        if generation is None:
            raise ValidationError("Field 'generation' cannot be None")
        if not isinstance(generation, str) or not generation.strip():
            raise ValidationError(f"generation must be a non-empty string, got {generation!r}")
        self.generation = generation.strip()

        # 7. issued_at (Required + non-null, datetime o ISO 8601, sin default automático)
        if issued_at is _UNSET:
            raise ValidationError("Field 'issued_at' is required")
        if issued_at is None:
            raise ValidationError("Field 'issued_at' cannot be None")
        if isinstance(issued_at, datetime):
            self.issued_at = issued_at
        elif isinstance(issued_at, str) and issued_at.strip():
            try:
                self.issued_at = datetime.fromisoformat(issued_at.strip())
            except Exception:
                raise ValidationError(f"issued_at must be a valid datetime or ISO 8601 string, got {issued_at!r}")
        else:
            raise ValidationError(f"issued_at must be a valid datetime or ISO 8601 string, got {issued_at!r}")

        # 8. population_at_issuance (Required + non-null, integer >= 1)
        if population_at_issuance is _UNSET:
            raise ValidationError("Field 'population_at_issuance' is required")
        if population_at_issuance is None:
            raise ValidationError("Field 'population_at_issuance' cannot be None")
        if not isinstance(population_at_issuance, int) or isinstance(population_at_issuance, bool) or population_at_issuance < 1:
            raise ValidationError(f"population_at_issuance must be an integer >= 1, got {population_at_issuance!r}")
        self.population_at_issuance = population_at_issuance

        # 9. rarity (Required + non-null, tipo abierto, sin enum cerrado fijo, DEC-022-PENDING)
        if rarity is _UNSET:
            raise ValidationError("Field 'rarity' is required")
        if rarity is None:
            raise ValidationError("Field 'rarity' cannot be None")
        if isinstance(rarity, Enum):
            self.rarity = rarity.value if isinstance(rarity.value, str) else str(rarity.value)
        elif isinstance(rarity, str) and rarity.strip():
            self.rarity = rarity.strip()
        else:
            raise ValidationError(f"rarity must be a non-empty string or Enum, got {rarity!r}")

        # 10. capture_id (Required + non-null, estrictamente UUIDv4)
        self.capture_id = self._validate_uuidv4(capture_id, "capture_id")

        # 11. identification_method (Required + non-null, string no vacía)
        if identification_method is _UNSET:
            raise ValidationError("Field 'identification_method' is required")
        if identification_method is None:
            raise ValidationError("Field 'identification_method' cannot be None")
        if not isinstance(identification_method, str) or not identification_method.strip():
            raise ValidationError(f"identification_method must be a non-empty string, got {identification_method!r}")
        self.identification_method = identification_method.strip()

        # 12. identification_confidence (Optional + nullable, 0.0 <= val <= 1.0)
        if identification_confidence is _UNSET or identification_confidence is None:
            self.identification_confidence = None
        elif isinstance(identification_confidence, bool) or not isinstance(identification_confidence, (float, int)) or not (0.0 <= identification_confidence <= 1.0):
            raise ValidationError(f"identification_confidence must be between 0.0 and 1.0, got {identification_confidence!r}")
        else:
            self.identification_confidence = float(identification_confidence)

        # 13. rank (Required + non-null, tipo abierto int o str, sin niveles inventados, DEC-037-PENDING)
        if rank is _UNSET:
            raise ValidationError("Field 'rank' is required")
        if rank is None:
            raise ValidationError("Field 'rank' cannot be None")
        if isinstance(rank, bool) or not isinstance(rank, (int, str)) or (isinstance(rank, str) and not rank.strip()):
            raise ValidationError(f"rank must be an integer or non-empty string, got {rank!r}")
        self.rank = rank if isinstance(rank, int) else rank.strip()

        # 14. display_location (Optional + nullable, origen generalizado protegido, sin GPS exacto)
        if display_location is _UNSET or display_location is None:
            self.display_location = None
        elif not isinstance(display_location, str):
            raise ValidationError(f"display_location must be a string when present, got {display_location!r}")
        else:
            self._validate_display_location(display_location)
            self.display_location = display_location.strip()

        # 15. visual_effects (Optional + non-null, default contractual: [])
        if visual_effects is _UNSET:
            self.visual_effects = []
        elif visual_effects is None:
            raise ValidationError("visual_effects cannot be None (Optional but non-nullable, default is [])")
        elif not isinstance(visual_effects, list) or not all(isinstance(x, str) for x in visual_effects):
            raise ValidationError(f"visual_effects must be a list of strings, got {visual_effects!r}")
        else:
            self.visual_effects = list(visual_effects)

        # 16. artwork (Optional + nullable, object / str / None, tipo abierto)
        if artwork is _UNSET or artwork is None:
            self.artwork = None
        elif isinstance(artwork, (str, dict)) or hasattr(artwork, "__dict__"):
            if isinstance(artwork, str):
                if not artwork.strip():
                    raise ValidationError("artwork cannot be an empty string when present")
                self.artwork = artwork.strip()
            else:
                self.artwork = artwork
        else:
            raise ValidationError(f"artwork must be a string, object/dict, or None, got {artwork!r}")

        # 17. owner_id (Optional + nullable, string o None)
        if owner_id is _UNSET or owner_id is None:
            self.owner_id = None
        elif not isinstance(owner_id, str) or not owner_id.strip():
            raise ValidationError(f"owner_id must be a non-empty string when present, got {owner_id!r}")
        else:
            self.owner_id = owner_id.strip()

        # 18. serial (Required + non-null, string no vacía)
        if serial is _UNSET:
            raise ValidationError("Field 'serial' is required")
        if serial is None:
            raise ValidationError("Field 'serial' cannot be None")
        if not isinstance(serial, str) or not serial.strip():
            raise ValidationError(f"serial must be a non-empty string, got {serial!r}")
        self.serial = serial.strip()

        # 19. verification_status (Required in schema + non-null, default contractual: UNVERIFIED)
        if verification_status is _UNSET:
            self.verification_status = VerificationStatus.UNVERIFIED
        elif verification_status is None:
            raise ValidationError("verification_status cannot be None in current schema")
        elif isinstance(verification_status, VerificationStatus):
            self.verification_status = verification_status
        elif isinstance(verification_status, str):
            try:
                self.verification_status = VerificationStatus(verification_status)
            except ValueError:
                raise ValidationError(
                    f"Invalid verification_status: {verification_status!r}. "
                    f"Expected one of: {[s.value for s in VerificationStatus]}"
                )
        else:
            raise ValidationError(
                f"verification_status must be a VerificationStatus or str, got {verification_status!r}"
            )

        object.__setattr__(self, "_initialized", True)

    def __getattr__(self, name: str) -> Any:
        # Permite acceso a edition como None en Python cuando fue omitida sin exponer None en el contrato
        if name == "edition":
            return None
        raise AttributeError(f"'{type(self).__name__}' object has no attribute '{name}'")

    @property
    def has_edition(self) -> bool:
        """Indica si la carta posee una edición asignada en emisión (ausencia = False)."""
        return "edition" in self.__dict__

    def __setattr__(self, name: str, value: Any) -> None:
        if getattr(self, "_initialized", False):
            if name in self._IMMUTABLE_FIELDS:
                raise ValidationError(
                    f"Field '{name}' is immutable after issuance and cannot be modified."
                )
            if name not in self._MUTABLE_FIELDS and not name.startswith("_"):
                raise ValidationError(
                    f"Cannot add or modify unexpected attribute '{name}' on Card."
                )

            # Validaciones para mutaciones en caliente de campos mutables
            if name == "rank":
                if value is None or isinstance(value, bool) or not isinstance(value, (int, str)) or (isinstance(value, str) and not value.strip()):
                    raise ValidationError(f"rank must be an integer or non-empty string, got {value!r}")
                value = value if isinstance(value, int) else value.strip()
            elif name == "visual_effects":
                if value is None or not isinstance(value, list) or not all(isinstance(x, str) for x in value):
                    raise ValidationError(f"visual_effects must be a list of strings, got {value!r}")
                value = list(value)
            elif name == "artwork":
                if value is not None and not isinstance(value, (str, dict)) and not hasattr(value, "__dict__"):
                    raise ValidationError(f"artwork must be a string, object/dict, or None, got {value!r}")
                if isinstance(value, str):
                    if not value.strip():
                        raise ValidationError("artwork cannot be an empty string when present")
                    value = value.strip()
            elif name == "owner_id":
                if value is not None and (not isinstance(value, str) or not value.strip()):
                    raise ValidationError(f"owner_id must be a non-empty string or None, got {value!r}")
                if isinstance(value, str):
                    value = value.strip()
            elif name == "verification_status":
                if value is None:
                    raise ValidationError("verification_status cannot be None in current schema")
                elif isinstance(value, VerificationStatus):
                    pass
                elif isinstance(value, str):
                    try:
                        value = VerificationStatus(value)
                    except ValueError:
                        raise ValidationError(
                            f"Invalid verification_status: {value!r}. "
                            f"Expected one of: {[s.value for s in VerificationStatus]}"
                        )
                else:
                    raise ValidationError(
                        f"verification_status must be a VerificationStatus or str, got {value!r}"
                    )
            elif name == "display_location":
                if value is not None:
                    if not isinstance(value, str):
                        raise ValidationError(f"display_location must be a string or None, got {value!r}")
                    self._validate_display_location(value)
                    value = value.strip()

        super().__setattr__(name, value)

    def to_dict(self) -> Dict[str, Any]:
        """
        Serializa la entidad Card a un diccionario conforme al contrato.
        Si 'edition' fue omitida en emisión, se omite de la serialización
        (no aparece como null) respetando Optional != Nullable.
        """
        data: Dict[str, Any] = {
            "card_id": self.card_id,
            "animal_id": self.animal_id,
            "specimen_number": self.specimen_number,
            "schema_version": self.schema_version,
        }
        if self.has_edition and "edition" in self.__dict__:
            data["edition"] = self.edition
        data.update({
            "generation": self.generation,
            "issued_at": (
                self.issued_at.isoformat()
                if isinstance(self.issued_at, datetime)
                else self.issued_at
            ),
            "population_at_issuance": self.population_at_issuance,
            "rarity": self.rarity.value if isinstance(self.rarity, Enum) else self.rarity,
            "capture_id": self.capture_id,
            "identification_method": self.identification_method,
            "identification_confidence": self.identification_confidence,
            "rank": self.rank,
            "display_location": self.display_location,
            "visual_effects": list(self.visual_effects),
            "artwork": (
                self.artwork.to_dict()
                if hasattr(self.artwork, "to_dict") and callable(self.artwork.to_dict)
                else self.artwork
            ),
            "owner_id": self.owner_id,
            "serial": self.serial,
            "verification_status": (
                self.verification_status.value
                if isinstance(self.verification_status, Enum)
                else self.verification_status
            ),
        })
        return data

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "Card":
        """
        Reconstruye una instancia de Card a partir de un diccionario validando el contrato.
        - Rechaza 'edition: null' porque es Optional pero Non-nullable (debe omitirse si está ausente).
        - Rechaza 'verification_status: null' en el esquema actual.
        - Rechaza 'visual_effects: null' porque su default es [].
        """
        clean_data = dict(data)

        if "edition" in clean_data and clean_data["edition"] is None:
            raise ValidationError(
                "Field 'edition' cannot be null in contract (Optional but non-nullable; omit the key if absent)"
            )

        if "verification_status" in clean_data and clean_data["verification_status"] is None:
            raise ValidationError(
                "Field 'verification_status' cannot be null in current schema"
            )

        if "visual_effects" in clean_data and clean_data["visual_effects"] is None:
            raise ValidationError(
                "Field 'visual_effects' cannot be null (Optional but non-nullable; default is [])"
            )

        return cls(**clean_data)




@dataclass(frozen=True)
class CardMetadata:
    """Metadatos de trazabilidad y coleccionismo de la carta."""
    card_id: str
    animal_id: str
    card_code: str
    created_at: datetime = field(default_factory=lambda: datetime.now(timezone.utc))
    schema_version: str = "1.0.0"
    is_collectible: bool = True
    rarity_tier: Optional[CardRarity] = None


@dataclass(frozen=True)
class CardFront:
    """
    Frente de la carta.
    Objetivo: Visual, atractivo, claro (Reconocer + Atraer + Coleccionar).
    """
    image_uri: str
    common_name: str
    scientific_name_secondary: str
    category: AnimalCategory
    visual_theme: str = "standard"
    card_code: str = ""


@dataclass(frozen=True)
class CardBack:
    """
    Reverso de la carta.
    Objetivo: Información organizada, fácil de leer y educativa.
    Mantiene aislada la información biológica real del Lore.
    """
    profile: AnimalProfile
    lore: Optional[LoreProfile] = None


@dataclass(frozen=True)
class AnimalCard:
    """
    Entidad raíz de la Carta Coleccionable de Who Animal.
    Compuesta por Metadatos, Frente visual y Reverso informativo.
    """
    metadata: CardMetadata
    front: CardFront
    back: CardBack
