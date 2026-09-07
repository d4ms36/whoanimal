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

if __name__ == '__main__':
    unittest.main()
