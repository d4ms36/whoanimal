"""
Modelos de dominio para la información biológica y científica real del animal.
"""

from dataclasses import dataclass, field
from typing import List, Optional
from ..enums import AnimalCategory, DangerLevel


@dataclass(frozen=True)
class ConservationIndicators:
    """
    Indicadores secundarios de estatus de conservación y rareza natural.
    Diseñados para ser informativos y discretos, sin sobrecargar la tarjeta.
    """
    is_protected: bool = False
    is_rare_species: bool = False
    official_status_reference: Optional[str] = None


@dataclass(frozen=True)
class DangerAssessment:
    """
    Evaluación de seguridad y advertencias para avistamiento responsable.
    Enfocado en la prudencia pedagógica, sin exageraciones sensacionalistas.
    """
    level: DangerLevel = DangerLevel.NONE
    notice_text: Optional[str] = None
    risk_factors: List[str] = field(default_factory=list)

    @property
    def has_warning(self) -> bool:
        """Indica si debe renderizarse un bloque de advertencia en la carta."""
        return self.level in (DangerLevel.PRECAUTION, DangerLevel.DANGER)


@dataclass(frozen=True)
class Curiosity:
    """Hecho curioso y verídico sobre el comportamiento o biología del animal."""
    fact: str
    source: Optional[str] = None


@dataclass(frozen=True)
class ScientificInfo:
    """
    Información científica factual y contrastable.
    PROHIBIDO inventar o alterar datos biológicos reales.
    """
    common_name: str
    scientific_name: str
    description: str
    habitat: str
    distribution: str
    diet: str
    behavior: str
    size: str
    weight: Optional[str] = None
    sources: List[str] = field(default_factory=list)


from typing import Any, Dict, Optional
from ...core.exceptions import ValidationError

_UNSET = object()

class AnimalProfile:
    """
    Entidad de Dominio: Perfil biológico completo de la especie (WHO-007).
    Representa el conocimiento zoológico, NO un individuo concreto ni una carta.
    """
    animal_id: str
    scientific_name: str
    common_name: str
    taxonomy: Dict[str, Any]
    conservation_status: Optional[str]
    is_rare_species: Optional[bool]
    
    _IMMUTABLE_FIELDS = frozenset({
        "animal_id", "scientific_name", "common_name", "taxonomy", 
        "conservation_status", "is_rare_species"
    })

    def __init__(
        self,
        animal_id: Any = _UNSET,
        scientific_name: Any = _UNSET,
        common_name: Any = _UNSET,
        taxonomy: Any = _UNSET,
        conservation_status: Any = None,
        is_rare_species: Any = None,
        **extra_kwargs: Any
    ):
        if extra_kwargs:
            raise ValidationError(
                f"Unexpected extra fields not permitted in AnimalProfile: {list(extra_kwargs.keys())}"
            )
            
        if animal_id is _UNSET or not isinstance(animal_id, str) or not animal_id.strip():
            raise ValidationError("Field 'animal_id' is required and must be a non-empty string.")
        
        if scientific_name is _UNSET or not isinstance(scientific_name, str) or not scientific_name.strip():
            raise ValidationError("Field 'scientific_name' is required and must be a non-empty string.")
            
        if common_name is _UNSET or not isinstance(common_name, str) or not common_name.strip():
            raise ValidationError("Field 'common_name' is required and must be a non-empty string.")
            
        if taxonomy is _UNSET or not isinstance(taxonomy, dict):
            raise ValidationError("Field 'taxonomy' is required and must be a dict (object).")
            
        if conservation_status is not None and (not isinstance(conservation_status, str) or not conservation_status.strip()):
            raise ValidationError("Field 'conservation_status' must be a non-empty string if provided.")
            
        if is_rare_species is not None and not isinstance(is_rare_species, bool):
            raise ValidationError("Field 'is_rare_species' must be a boolean if provided.")

        self.animal_id = animal_id.strip()
        self.scientific_name = scientific_name.strip()
        self.common_name = common_name.strip()
        self.taxonomy = taxonomy
        self.conservation_status = conservation_status.strip() if conservation_status else None
        self.is_rare_species = is_rare_species
        
        object.__setattr__(self, "_initialized", True)

    def __setattr__(self, name: str, value: Any) -> None:
        if getattr(self, "_initialized", False):
            raise ValidationError(
                f"AnimalProfile is immutable after creation: cannot modify attribute '{name}'."
            )
        super().__setattr__(name, value)
        
    def to_dict(self) -> Dict[str, Any]:
        result = {
            "animal_id": self.animal_id,
            "scientific_name": self.scientific_name,
            "common_name": self.common_name,
            "taxonomy": self.taxonomy,
        }
        if self.conservation_status is not None:
            result["conservation_status"] = self.conservation_status
        if self.is_rare_species is not None:
            result["is_rare_species"] = self.is_rare_species
        return result
        
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "AnimalProfile":
        return cls(**data)
