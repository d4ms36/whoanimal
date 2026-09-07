import unittest
import os
import glob
from whoanimal.services.dataset_validator import DatasetValidator
from whoanimal.services.taxonomy_index import TaxonomyIndex

class TestCatalog(unittest.TestCase):
    def setUp(self):
        self.species_dir = os.path.join("data", "species")
        self.taxonomy_path = os.path.join("data", "taxonomy", "taxonomy_index.json")
        self.taxonomy_index = TaxonomyIndex()
        self.taxonomy_index.load_index(self.taxonomy_path)

    def test_catalog_mass_validation(self):
        files = glob.glob(os.path.join(self.species_dir, "*.json"))
        self.assertTrue(len(files) >= 20, "Catalog must have at least 20 species")
        
        seen_ids = set()
        
        for file in files:
            # Validates using DatasetValidator which will also check against TaxonomyIndex
            self.assertTrue(DatasetValidator.validate_file(file, self.taxonomy_index))
            
            # Additional check for duplicated IDs
            import json
            with open(file, 'r', encoding='utf-8') as f:
                data = json.load(f)
                
            for item in data:
                animal_id = item["animal_id"]
                self.assertNotIn(animal_id, seen_ids, f"Duplicated animal_id {animal_id} found in {file}")
                seen_ids.add(animal_id)

if __name__ == '__main__':
    unittest.main()
