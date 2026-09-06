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


@dataclass(frozen=True)
class AnimalProfile:
    """Perfil biológico completo de un espécimen animal."""
    id: str
    category: AnimalCategory
    scientific_info: ScientificInfo
    conservation: ConservationIndicators = field(default_factory=ConservationIndicators)
    danger: DangerAssessment = field(default_factory=DangerAssessment)
    curiosities: List[Curiosity] = field(default_factory=list)
