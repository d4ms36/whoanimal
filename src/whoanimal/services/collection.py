"""
Protocolo e interfaces para el servicio de colección y álbum de cartas.
"""

from typing import Protocol, List, Optional
from ..domain.models.card import AnimalCard


class CollectionService(Protocol):
    """Contrato para la gestión del inventario y álbum de colección del usuario."""

    def add_card(self, user_id: str, card: AnimalCard) -> bool:
        """Registra una nueva carta en el álbum del usuario."""
        ...

    def get_user_cards(self, user_id: str) -> List[AnimalCard]:
        """Obtiene el listado completo de cartas coleccionadas por el usuario."""
        ...

    def get_card_by_id(self, user_id: str, card_id: str) -> Optional[AnimalCard]:
        """Recupera una carta específica perteneciente a la colección del usuario."""
        ...
