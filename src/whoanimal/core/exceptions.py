"""
Excepciones base del dominio y servicios de Who Animal.
"""


class WhoAnimalError(Exception):
    """Excepción base para todos los errores de la aplicación Who Animal."""
    pass


class ValidationError(WhoAnimalError):
    """Lanzada cuando un dato de dominio o metadato no cumple con las especificaciones."""
    pass


class IdentificationError(WhoAnimalError):
    """Lanzada cuando un proceso de identificación visual falla o no es concluyente."""
    pass


class CardCreationError(WhoAnimalError):
    """Lanzada cuando no es posible construir una carta válida a partir de un perfil animal."""
    pass
