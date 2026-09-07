"""Módulo de Servicios y Protocolos de Who Animal."""

from .identification import (
    IdentificationService,
    IdentificationProvider,
    DeterministicIdentificationProvider,
    IdentificationServiceError,
    InvalidObservationError,
    IdentificationProviderError,
)
from .card_generator import (
    CardGeneratorService,
    create_card_from_capture,
    CardGenerationError,
    InvalidCaptureError,
    AnimalProfileNotFoundError,
    DuplicateCardError,
)
from .collection import CollectionService

__all__ = [
    "IdentificationService",
    "IdentificationProvider",
    "DeterministicIdentificationProvider",
    "IdentificationServiceError",
    "InvalidObservationError",
    "IdentificationProviderError",
    "CardGeneratorService",
    "create_card_from_capture",
    "CardGenerationError",
    "InvalidCaptureError",
    "AnimalProfileNotFoundError",
    "DuplicateCardError",
    "CollectionService",
]
