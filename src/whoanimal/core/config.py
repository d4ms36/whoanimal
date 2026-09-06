"""
Configuración general y constantes del proyecto Who Animal.
"""

from dataclasses import dataclass


@dataclass(frozen=True)
class AppConfig:
    """Configuraciones base de la aplicación y políticas de producto."""
    app_name: str = "WHO Animal"
    version: str = "0.1.0"
    is_pet_friendly: bool = True
    enforce_lore_separation: bool = True
    max_curiosities_per_card: int = 3


# Instancia por defecto
DEFAULT_CONFIG = AppConfig()
