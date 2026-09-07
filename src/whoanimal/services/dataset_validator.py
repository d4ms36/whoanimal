import json
from typing import Dict, Any, List, Optional

# TYPE_CHECKING used for optional type hints to avoid circular imports if any
from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from whoanimal.services.taxonomy_index import TaxonomyIndex

class DatasetValidatorError(Exception):
    pass

class DatasetValidator:
    """Validates zoological dataset JSON files according to DATASET_SPEC.md"""
    
    REQUIRED_FIELDS = {"animal_id", "scientific_name", "common_name", "taxonomy"}
    OPTIONAL_FIELDS = {"conservation_status", "is_rare_species"}
    ALLOWED_FIELDS = REQUIRED_FIELDS | OPTIONAL_FIELDS
    
    REQUIRED_TAXONOMY_FIELDS = {"kingdom", "phylum", "class", "order", "family", "genus", "species"}

    @classmethod
    def validate_file(cls, filepath: str, taxonomy_index: Optional['TaxonomyIndex'] = None) -> bool:
        with open(filepath, 'r', encoding='utf-8') as f:
            data = json.load(f)
        return cls.validate_data(data, taxonomy_index)
        
    @classmethod
    def validate_data(cls, data: List[Dict[str, Any]], taxonomy_index: Optional['TaxonomyIndex'] = None) -> bool:
        if not isinstance(data, list):
            raise DatasetValidatorError("Root must be a JSON array.")
            
        for index, item in enumerate(data):
            cls._validate_item(item, index, taxonomy_index)
            
        return True
        
    @classmethod
    def _validate_item(cls, item: Dict[str, Any], index: int, taxonomy_index: Optional['TaxonomyIndex'] = None):
        if not isinstance(item, dict):
            raise DatasetValidatorError(f"Item at index {index} is not an object.")
            
        # Check for illegal fields
        item_keys = set(item.keys())
        illegal_fields = item_keys - cls.ALLOWED_FIELDS
        if illegal_fields:
            raise DatasetValidatorError(f"Item at index {index} contains illegal fields: {illegal_fields}")
            
        # Check required fields
        missing_fields = cls.REQUIRED_FIELDS - item_keys
        if missing_fields:
            raise DatasetValidatorError(f"Item at index {index} is missing required fields: {missing_fields}")
            
        # Validate string types and non-emptiness
        for field in ["animal_id", "scientific_name", "common_name"]:
            val = item[field]
            if not isinstance(val, str):
                raise DatasetValidatorError(f"Field '{field}' at index {index} must be a string.")
            if not val.strip():
                raise DatasetValidatorError(f"Field '{field}' at index {index} cannot be empty.")
                
        # Validate taxonomy
        taxonomy = item["taxonomy"]
        if not isinstance(taxonomy, dict):
            raise DatasetValidatorError(f"Field 'taxonomy' at index {index} must be an object.")
            
        tax_keys = set(taxonomy.keys())
        if tax_keys != cls.REQUIRED_TAXONOMY_FIELDS:
            raise DatasetValidatorError(f"Taxonomy at index {index} must have exactly {cls.REQUIRED_TAXONOMY_FIELDS}, got {tax_keys}")
            
        for k, v in taxonomy.items():
            if not isinstance(v, str):
                raise DatasetValidatorError(f"Taxonomy field '{k}' at index {index} must be a string.")
            if not v.strip():
                raise DatasetValidatorError(f"Taxonomy field '{k}' at index {index} cannot be empty.")
                
        # Validate optionals
        if "conservation_status" in item:
            if not isinstance(item["conservation_status"], str):
                raise DatasetValidatorError(f"Field 'conservation_status' at index {index} must be a string.")
                
        if "is_rare_species" in item:
            if not isinstance(item["is_rare_species"], bool):
                raise DatasetValidatorError(f"Field 'is_rare_species' at index {index} must be a boolean.")
                
        # Optional: Validate taxonomy route using TaxonomyIndex
        if taxonomy_index is not None:
            route = [
                taxonomy["kingdom"],
                taxonomy["phylum"],
                taxonomy["class"],
                taxonomy["order"],
                taxonomy["family"],
                taxonomy["genus"]
            ]
            if not taxonomy_index.verify_route(route):
                raise DatasetValidatorError(f"Taxonomic route {route} at index {index} does not exist in the official taxonomy index.")
