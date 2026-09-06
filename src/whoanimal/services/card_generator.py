"""
Protocolo e interfaces para el generador de cartas coleccionables de Who Animal.
"""

from typing import Protocol, Optional
from ..domain.models.animal import AnimalProfile
from ..domain.models.lore import LoreProfile
from ..domain.models.card import AnimalCard


class CardGeneratorService(Protocol):
    """Contrato para la composición y emisión de cartas de doble cara."""

    def create_card(
        self,
        profile: AnimalProfile,
        image_uri: str,
        lore: Optional[LoreProfile] = None,
        visual_theme: str = "standard",
    ) -> AnimalCard:
        """
        Ensambla una carta de animal vinculando su información científica,
        sus indicadores secundarios, su cara visual y, opcionalmente, su Lore.
        """
        ...
