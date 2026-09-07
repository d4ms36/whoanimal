import unittest
import json
from whoanimal.services.dataset_validator import DatasetValidator, DatasetValidatorError

class TestDatasetValidator(unittest.TestCase):

    def setUp(self):
        self.valid_species = {
            "animal_id": "123e4567-e89b-12d3-a456-426614174000",
            "scientific_name": "Panthera onca",
            "common_name": "Jaguar",
            "taxonomy": {
                "kingdom": "Animalia",
                "phylum": "Chordata",
                "class": "Mammalia",
                "order": "Carnivora",
                "family": "Felidae",
                "genus": "Panthera",
                "species": "Panthera onca"
            }
        }

    def test_valid_dataset(self):
        data = [self.valid_species]
        self.assertTrue(DatasetValidator.validate_data(data))

    def test_missing_required_field(self):
        data = [self.valid_species.copy()]
        del data[0]["common_name"]
        with self.assertRaisesRegex(DatasetValidatorError, "missing required fields"):
            DatasetValidator.validate_data(data)

    def test_illegal_extra_field(self):
        data = [self.valid_species.copy()]
        data[0]["invented_field"] = "value"
        with self.assertRaisesRegex(DatasetValidatorError, "illegal fields"):
            DatasetValidator.validate_data(data)

    def test_empty_string_invalid(self):
        data = [self.valid_species.copy()]
        data[0]["scientific_name"] = "   "
        with self.assertRaisesRegex(DatasetValidatorError, "cannot be empty"):
            DatasetValidator.validate_data(data)

    def test_incorrect_type(self):
        data = [self.valid_species.copy()]
        data[0]["is_rare_species"] = "true"  # Should be boolean
        with self.assertRaisesRegex(DatasetValidatorError, "must be a boolean"):
            DatasetValidator.validate_data(data)

    def test_missing_taxonomy_field(self):
        data = [self.valid_species.copy()]
        data[0]["taxonomy"] = data[0]["taxonomy"].copy()
        del data[0]["taxonomy"]["family"]
        with self.assertRaisesRegex(DatasetValidatorError, "Taxonomy at index 0 must have exactly"):
            DatasetValidator.validate_data(data)

    def test_serialization_correctness(self):
        data = [self.valid_species, self.valid_species]
        json_str = json.dumps(data)
        loaded = json.loads(json_str)
        self.assertTrue(DatasetValidator.validate_data(loaded))

    def test_scientific_fields_valid(self):
        data = [self.valid_species.copy()]
        data[0].update({
            "habitat": "Bosques tropicales",
            "diet": "Carnívoro",
            "lifespan_years": 15,
            "size_cm": 170,
            "weight_kg": 95.5,
            "activity_cycle": "Diurno",
            "native_regions": ["América del Sur", "América Central"]
        })
        self.assertTrue(DatasetValidator.validate_data(data))

    def test_scientific_fields_invalid_types(self):
        data = [self.valid_species.copy()]
        data[0]["lifespan_years"] = "15"  # Should be int
        with self.assertRaisesRegex(DatasetValidatorError, "must be an integer"):
            DatasetValidator.validate_data(data)
            
        data[0]["lifespan_years"] = 15
        data[0]["weight_kg"] = "heavy"
        with self.assertRaisesRegex(DatasetValidatorError, "must be a number"):
            DatasetValidator.validate_data(data)

    def test_native_regions_malformed(self):
        data = [self.valid_species.copy()]
        data[0]["native_regions"] = "América del Sur"  # Should be a list
        with self.assertRaisesRegex(DatasetValidatorError, "must be a list of strings"):
            DatasetValidator.validate_data(data)
            
        data[0]["native_regions"] = ["América del Sur", 123]  # Contains non-string
        with self.assertRaisesRegex(DatasetValidatorError, "contains a non-string element"):
            DatasetValidator.validate_data(data)

    def test_backward_compatibility(self):
        # Original species without new fields should still pass
        data = [self.valid_species]
        self.assertTrue(DatasetValidator.validate_data(data))

if __name__ == '__main__':
    unittest.main()
