"""
Enumeraciones fundamentales del dominio Who Animal.
"""

from enum import Enum


class AnimalCategory(str, Enum):
    """Categorías zoológicas principales para clasificación de especies."""
    MAMMAL = "mammal"
    BIRD = "bird"
    REPTILE = "reptile"
    AMPHIBIAN = "amphibian"
    FISH = "fish"
    INVERTEBRATE = "invertebrate"
    OTHER = "other"


class DangerLevel(str, Enum):
    """Nivel de precaución o peligro responsable para la interacción/avistamiento."""
    NONE = "none"
    PRECAUTION = "precaution"
    DANGER = "danger"


class CardRarity(str, Enum):
    """
    Niveles de rareza coleccionable en la aplicación (Ficticio/Lúdico).
    NOTA: Esto NO representa el estado de conservación ni la rareza biológica real de la especie.
    """
    COMMON = "common"
    UNCOMMON = "uncommon"
    RARE = "rare"
    EPIC = "epic"
    LEGENDARY = "legendary"


class VerificationStatus(str, Enum):
    """
    Estados canónicos del ciclo de verificación y autenticación de una Card (DEC-034, DEC-035).
    """
    UNVERIFIED = "UNVERIFIED"
    VERIFIED = "VERIFIED"
    FLAGGED = "FLAGGED"
    REVOKED = "REVOKED"

