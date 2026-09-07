import unittest
from whoanimal.services.taxonomy_index import TaxonomyIndex
from whoanimal.services.dataset_validator import DatasetValidator, DatasetValidatorError

class TestTaxonomyIndex(unittest.TestCase):

    def setUp(self):
        self.taxonomy = TaxonomyIndex()
        self.mock_data = {
            "Animalia": {
                "Chordata": {
                    "Mammalia": {
                        "Carnivora": {
                            "Canidae": {
                                "Canis": {}
                            },
                            "Felidae": {
                                "Felis": {},
                                "Panthera": {}
                            },
                            "Ursidae": {
                                "Ailuropoda": {}
                            }
                        }
                    }
                }
            }
        }
        self.taxonomy.load_from_dict(self.mock_data)

    def test_search_by_kingdom(self):
        res = self.taxonomy.search_by_kingdom("Animalia")
        self.assertIsNotNone(res)
        self.assertIn("Chordata", res)
        self.assertIsNone(self.taxonomy.search_by_kingdom("Plantae"))

    def test_search_by_class(self):
        res = self.taxonomy.search_by_class("Mammalia")
        self.assertEqual(len(res), 1)
        self.assertIn("Carnivora", res[0])

    def test_search_by_family(self):
        res = self.taxonomy.search_by_family("Felidae")
        self.assertEqual(len(res), 1)
        self.assertIn("Panthera", res[0])
        self.assertIn("Felis", res[0])

    def test_verify_route_valid(self):
        route = ["Animalia", "Chordata", "Mammalia", "Carnivora", "Felidae", "Panthera"]
        self.assertTrue(self.taxonomy.verify_route(route))

    def test_verify_route_invalid(self):
        route = ["Animalia", "Chordata", "Mammalia", "Carnivora", "Canidae", "Panthera"]
        self.assertFalse(self.taxonomy.verify_route(route))
        
        route_short = ["Animalia", "Chordata"]
        self.assertTrue(self.taxonomy.verify_route(route_short))
        
        route_wrong = ["Plantae", "Chordata"]
        self.assertFalse(self.taxonomy.verify_route(route_wrong))

    def test_integration_with_dataset_validator(self):
        valid_species = {
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
        
        # Valid route
        self.assertTrue(DatasetValidator.validate_data([valid_species], self.taxonomy))
        
        # Invalid route
        invalid_species = valid_species.copy()
        invalid_species["taxonomy"] = valid_species["taxonomy"].copy()
        invalid_species["taxonomy"]["genus"] = "UnknownGenus"
        
        with self.assertRaisesRegex(DatasetValidatorError, "does not exist in the official taxonomy index"):
            DatasetValidator.validate_data([invalid_species], self.taxonomy)

if __name__ == '__main__':
    unittest.main()
