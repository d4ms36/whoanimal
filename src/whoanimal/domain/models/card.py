"""
Modelos de dominio para las cartas coleccionables de doble cara en Who Animal.
"""

from dataclasses import dataclass, field
from datetime import datetime, timezone
from typing import Optional
from ..enums import AnimalCategory, CardRarity
from .animal import AnimalProfile
from .lore import LoreProfile


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
