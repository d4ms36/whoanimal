import json
from typing import Dict, List, Optional

class TaxonomyIndex:
    """Official taxonomy index to organize and verify species hierarchy."""

    def __init__(self):
        self._index: Dict = {}

    def load_index(self, filepath: str) -> None:
        """Loads the JSON taxonomy index into memory."""
        with open(filepath, 'r', encoding='utf-8') as f:
            self._index = json.load(f)
            
    def load_from_dict(self, data: Dict) -> None:
        """Loads the taxonomy index from a dictionary (useful for testing)."""
        self._index = data

    def search_by_kingdom(self, kingdom: str) -> Optional[Dict]:
        """Returns the taxonomic tree for a given kingdom, or None."""
        return self._index.get(kingdom)

    def search_by_class(self, target_class: str) -> List[Dict]:
        """Returns a list of branches that contain the given class."""
        results = []
        for kingdom, phylums in self._index.items():
            for phylum, classes in phylums.items():
                if target_class in classes:
                    results.append(classes[target_class])
        return results

    def search_by_family(self, target_family: str) -> List[Dict]:
        """Returns a list of branches that contain the given family."""
        results = []
        for kingdom, phylums in self._index.items():
            for phylum, classes in phylums.items():
                for cls, orders in classes.items():
                    for order, families in orders.items():
                        if target_family in families:
                            results.append(families[target_family])
        return results

    def verify_route(self, route: List[str]) -> bool:
        """
        Verifies if a taxonomic route exists.
        The route should be [kingdom, phylum, class, order, family, genus].
        """
        current_level = self._index
        for node in route:
            if not isinstance(current_level, dict):
                return False
            if node not in current_level:
                return False
            current_level = current_level[node]
        return True
