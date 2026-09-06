"""Módulo de Servicios y Protocolos de Who Animal."""

from .identification import IdentificationService
from .card_generator import CardGeneratorService
from .collection import CollectionService

__all__ = [
    "IdentificationService",
    "CardGeneratorService",
    "CollectionService",
]
