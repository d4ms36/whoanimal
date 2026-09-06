"""
Pruebas unitarias para la entidad de dominio AnimalProfile (WHO-007).
Verifica obligatoriedad, tipos y restricciones de los campos.
"""

import unittest
from whoanimal.domain import AnimalProfile
from whoanimal.core.exceptions import ValidationError


class TestAnimalProfile(unittest.TestCase):
    """Suite de pruebas unitarias para AnimalProfile."""

    def test_01_valid_instantiation(self):
        """Verifica que AnimalProfile pueda instanciarse con campos válidos."""
        profile = AnimalProfile(
            animal_id="vulpes_vulpes",
            scientific_name="Vulpes vulpes",
            common_name="Red Fox",
            taxonomy={"kingdom": "Animalia", "family": "Canidae"},
            conservation_status="LC",
            is_rare_species=False
        )
        self.assertEqual(profile.animal_id, "vulpes_vulpes")
        self.assertEqual(profile.scientific_name, "Vulpes vulpes")
        self.assertEqual(profile.common_name, "Red Fox")
        self.assertEqual(profile.taxonomy, {"kingdom": "Animalia", "family": "Canidae"})
        self.assertEqual(profile.conservation_status, "LC")
        self.assertFalse(profile.is_rare_species)

    def test_02_optional_fields(self):
        """Verifica que conservation_status e is_rare_species puedan omitirse."""
        profile = AnimalProfile(
            animal_id="vulpes_vulpes",
            scientific_name="Vulpes vulpes",
            common_name="Red Fox",
            taxonomy={"kingdom": "Animalia"}
        )
        self.assertIsNone(profile.conservation_status)
        self.assertIsNone(profile.is_rare_species)

    def test_03_empty_strings_rejected(self):
        """Verifica que los strings vacíos levanten error en campos obligatorios."""
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="",
                scientific_name="Vulpes vulpes",
                common_name="Red Fox",
                taxonomy={}
            )
        
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="vulpes_vulpes",
                scientific_name="   ",
                common_name="Red Fox",
                taxonomy={}
            )
            
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="vulpes_vulpes",
                scientific_name="Vulpes vulpes",
                common_name="",
                taxonomy={}
            )

    def test_04_taxonomy_invalid_type_rejected(self):
        """Verifica que taxonomy deba ser un diccionario."""
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="vulpes_vulpes",
                scientific_name="Vulpes vulpes",
                common_name="Red Fox",
                taxonomy="Animalia"
            )

    def test_05_extra_fields_rejected(self):
        """Verifica que se rechacen campos no definidos (ej. gps, sex)."""
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="vulpes_vulpes",
                scientific_name="Vulpes vulpes",
                common_name="Red Fox",
                taxonomy={},
                gps="123,456"
            )
            
        with self.assertRaises(ValidationError):
            AnimalProfile(
                animal_id="vulpes_vulpes",
                scientific_name="Vulpes vulpes",
                common_name="Red Fox",
                taxonomy={},
                sex="MALE"
            )

    def test_06_immutability(self):
        """Verifica que los campos no puedan modificarse luego de crearse."""
        profile = AnimalProfile(
            animal_id="vulpes_vulpes",
            scientific_name="Vulpes vulpes",
            common_name="Red Fox",
            taxonomy={}
        )
        with self.assertRaises(ValidationError):
            profile.animal_id = "new_id"
        with self.assertRaises(ValidationError):
            profile.scientific_name = "New Name"
        with self.assertRaises(ValidationError):
            profile.extra = "extra"

    def test_07_serialization_roundtrip(self):
        """Verifica la serialización a y desde diccionario."""
        profile = AnimalProfile(
            animal_id="vulpes_vulpes",
            scientific_name="Vulpes vulpes",
            common_name="Red Fox",
            taxonomy={"order": "Carnivora"},
            conservation_status="LC"
        )
        
        data = profile.to_dict()
        self.assertEqual(data["animal_id"], "vulpes_vulpes")
        self.assertEqual(data["conservation_status"], "LC")
        self.assertNotIn("is_rare_species", data)
        
        restored = AnimalProfile.from_dict(data)
        self.assertEqual(restored.animal_id, profile.animal_id)
        self.assertEqual(restored.scientific_name, profile.scientific_name)
        self.assertEqual(restored.taxonomy, profile.taxonomy)
        self.assertEqual(restored.conservation_status, profile.conservation_status)
        self.assertIsNone(restored.is_rare_species)


if __name__ == "__main__":
    unittest.main()
