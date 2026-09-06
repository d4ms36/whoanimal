"""
Modelos de dominio para el Lore y la narrativa de fantasía en Who Animal.

REGLA FUNDAMENTAL:
El Lore es una capa de ficción creativa completamente independiente de la información científica.
NUNCA debe mezclarse con hechos biológicos reales ni presentarse como verdad zoológica.
"""

from dataclasses import dataclass, field
from typing import List


@dataclass(frozen=True)
class LoreProfile:
    """
    Perfil narrativo de ficción para un animal dentro del universo Who Animal.
    """
    title: str
    narrative: str
    world_tags: List[str] = field(default_factory=list)
    disclaimer: str = "Contenido narrativo y ficticio del universo Who Animal."
    is_fictional: bool = True
