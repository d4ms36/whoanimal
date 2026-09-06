"""
Pruebas unitarias para la entidad de dominio Capture / Specimen (WHO-006C).
Verifica identidad UUIDv4 estricta, semántica de sex (DEC-036),
límites ontológicos frente a Animal y Card, e inmutabilidad.
"""

import unittest
import uuid
from whoanimal.domain import (
    Capture,
    Specimen,
    SpecimenSex,
    BiologicalSex,
    AnimalProfile,
    ScientificInfo,
    Card,
)
from whoanimal.core.exceptions import ValidationError


class TestCaptureDomainModel(unittest.TestCase):
    """Suite de pruebas unitarias para Capture / Specimen."""

    def test_01_valid_capture_instantiation(self):
        """Verifica que una Capture válida pueda construirse con capture_id UUIDv4 y sex."""
        valid_id = str(uuid.uuid4())
        capture = Capture(capture_id=valid_id, sex=SpecimenSex.MALE)

        self.assertEqual(capture.capture_id, valid_id)
        self.assertEqual(capture.sex, SpecimenSex.MALE)
        self.assertIs(Specimen, Capture)  # Alias conceptual Specimen == Capture

    def test_02_identity_uuidv4_strict_acceptance(self):
        """Verifica que capture_id acepte strings canónicos y objetos UUIDv4."""
        # String UUIDv4
        id_str = str(uuid.uuid4())
        c1 = Capture(capture_id=id_str, sex=SpecimenSex.FEMALE)
        self.assertEqual(c1.capture_id, id_str)

        # Objeto uuid.UUID versión 4
        id_obj = uuid.uuid4()
        c2 = Capture(capture_id=id_obj, sex=SpecimenSex.UNKNOWN)
        self.assertEqual(c2.capture_id, str(id_obj))

    def test_03_identity_uuidv5_rejected(self):
        """Verifica que UUIDv5 sea estrictamente rechazado."""
        v5_id = str(uuid.uuid5(uuid.NAMESPACE_DNS, "whoanimal.org"))
        with self.assertRaises(ValidationError):
            Capture(capture_id=v5_id, sex=SpecimenSex.MALE)

        v5_obj = uuid.uuid5(uuid.NAMESPACE_URL, "https://whoanimal.org")
        with self.assertRaises(ValidationError):
            Capture(capture_id=v5_obj, sex=SpecimenSex.FEMALE)

    def test_04_identity_uuidv1_rejected(self):
        """Verifica que UUIDv1 sea estrictamente rechazado."""
        v1_id = str(uuid.uuid1())
        with self.assertRaises(ValidationError):
            Capture(capture_id=v1_id, sex=SpecimenSex.UNKNOWN)

    def test_05_identity_uuid_nil_rejected(self):
        """Verifica que UUID nil (00000000-0000-0000-0000-000000000000) sea rechazado."""
        nil_id = "00000000-0000-0000-0000-000000000000"
        with self.assertRaises(ValidationError):
            Capture(capture_id=nil_id, sex=SpecimenSex.MALE)

        nil_obj = uuid.UUID(int=0)
        with self.assertRaises(ValidationError):
            Capture(capture_id=nil_obj, sex=SpecimenSex.FEMALE)

    def test_06_identity_invalid_uuid_rejected(self):
        """Verifica que formatos inválidos, cadenas vacías, números o tipos no UUID sean rechazados."""
        invalid_values = [
            "not-a-uuid",
            "12345",
            "",
            "   ",
            "c84d7285-807e-468a-b5a8",
            None,
            True,
            False,
            123456,
            [],
        ]
        for inv in invalid_values:
            with self.subTest(invalid_id=inv):
                with self.assertRaises(ValidationError):
                    Capture(capture_id=inv, sex=SpecimenSex.MALE)

    def test_07_sex_allowed_values_accepted(self):
        """Verifica que MALE, FEMALE y UNKNOWN sean aceptados tanto como Enum como str."""
        valid_id = str(uuid.uuid4())

        # Enum SpecimenSex
        c_male = Capture(capture_id=valid_id, sex=SpecimenSex.MALE)
        self.assertEqual(c_male.sex, SpecimenSex.MALE)

        c_female = Capture(capture_id=valid_id, sex=SpecimenSex.FEMALE)
        self.assertEqual(c_female.sex, SpecimenSex.FEMALE)

        c_unknown = Capture(capture_id=valid_id, sex=SpecimenSex.UNKNOWN)
        self.assertEqual(c_unknown.sex, SpecimenSex.UNKNOWN)

        # Strings correspondientes (normalizadas a mayúsculas)
        c_str_male = Capture(capture_id=valid_id, sex="MALE")
        self.assertEqual(c_str_male.sex, SpecimenSex.MALE)

        c_str_female = Capture(capture_id=valid_id, sex="female")
        self.assertEqual(c_str_female.sex, SpecimenSex.FEMALE)

        c_str_unknown = Capture(capture_id=valid_id, sex="Unknown")
        self.assertEqual(c_str_unknown.sex, SpecimenSex.UNKNOWN)

        # Verificación del alias BiologicalSex
        self.assertIs(BiologicalSex, SpecimenSex)
        self.assertEqual(BiologicalSex.MALE, SpecimenSex.MALE)

    def test_08_sex_arbitrary_values_rejected(self):
        """Verifica que valores arbitrarios, categorías zoológicas ajenas o números sean rechazados."""
        valid_id = str(uuid.uuid4())
        arbitrary_values = [
            "HERMAPHRODITE",
            "INTERSEX",
            "ASEXUAL",
            "OTHER",
            "MACHO",
            "HEMBRA",
            1,
            0,
            True,
            "",
            "   ",
        ]
        for arb in arbitrary_values:
            with self.subTest(arbitrary_sex=arb):
                with self.assertRaises(ValidationError):
                    Capture(capture_id=valid_id, sex=arb)

    def test_09_sex_none_accepted_as_null(self):
        """Verifica que sex=None sea aceptado (nullable)."""
        valid_id = str(uuid.uuid4())
        capture = Capture(capture_id=valid_id, sex=None)
        self.assertIsNone(capture.sex)

    def test_10_missing_sex_allowed_as_absence(self):
        """Verifica que sex puede omitirse (optional) y capture_id sigue siendo requerido."""
        valid_id = str(uuid.uuid4())

        # Falta capture_id
        with self.assertRaises(ValidationError):
            Capture(sex=SpecimenSex.MALE)

        # Falta sex (permitido)
        capture = Capture(capture_id=valid_id)
        self.assertFalse(hasattr(capture, "sex"))

    def test_11_no_unexpected_extra_fields_permitted(self):
        """
        Verifica que no se admitan campos no aprobados en Capture
        (ej. latitude, longitude, image_url, timestamp, metadata).
        """
        valid_id = str(uuid.uuid4())
        forbidden_fields = [
            {"latitude": 40.4168},
            {"longitude": -3.7038},
            {"gps": "40.4168, -3.7038"},
            {"image_url": "https://whoanimal.org/img.png"},
            {"timestamp": "2026-09-06T12:00:00Z"},
            {"metadata": {"foo": "bar"}},
            {"animal_id": "animal_vulpes_vulpes"},
        ]
        for extra in forbidden_fields:
            with self.subTest(extra_field=extra):
                with self.assertRaises(ValidationError):
                    Capture(capture_id=valid_id, sex=SpecimenSex.MALE, **extra)

    def test_12_immutability_post_recording(self):
        """Verifica que una Capture registrada sea inmutable."""
        capture = Capture(capture_id=str(uuid.uuid4()), sex=SpecimenSex.MALE)

        with self.assertRaises(ValidationError):
            capture.capture_id = str(uuid.uuid4())

        with self.assertRaises(ValidationError):
            capture.sex = SpecimenSex.FEMALE

        with self.assertRaises(ValidationError):
            capture.extra_attribute = "test"

    def test_13_serialization_roundtrip(self):
        """Verifica la serialización to_dict() y reconstrucción from_dict() para distintos estados de sex."""
        valid_id = str(uuid.uuid4())
        
        # 1. Campo explícitamente con valor
        capture_val = Capture(capture_id=valid_id, sex=SpecimenSex.FEMALE)
        data_val = capture_val.to_dict()
        self.assertEqual(data_val, {"capture_id": valid_id, "sex": "FEMALE"})
        restored_val = Capture.from_dict(data_val)
        self.assertEqual(restored_val.sex, SpecimenSex.FEMALE)

        # 2. Campo explícitamente null
        capture_null = Capture(capture_id=valid_id, sex=None)
        data_null = capture_null.to_dict()
        self.assertEqual(data_null, {"capture_id": valid_id, "sex": None})
        restored_null = Capture.from_dict(data_null)
        self.assertIsNone(restored_null.sex)

        # 3. Campo ausente
        capture_absent = Capture(capture_id=valid_id)
        data_absent = capture_absent.to_dict()
        self.assertEqual(data_absent, {"capture_id": valid_id})
        self.assertNotIn("sex", data_absent)
        restored_absent = Capture.from_dict(data_absent)
        self.assertFalse(hasattr(restored_absent, "sex"))

    def test_14_ontological_boundaries_animal_and_card(self):
        """
        Verifica estrictamente DEC-036:
        - sex pertenece a Capture/Specimen.
        - sex NO pertenece a Animal (ScientificInfo / AnimalProfile).
        - sex / specimen_sex NO pertenece a los 19 campos canónicos de Card.
        - Card.capture_id puede referenciar formalmente a Capture.capture_id.
        """
        from dataclasses import fields

        # 1. AnimalProfile y ScientificInfo no tienen 'sex'
        animal_field_names = list(AnimalProfile._IMMUTABLE_FIELDS)
        scientific_field_names = [f.name for f in fields(ScientificInfo)]
        self.assertNotIn("sex", animal_field_names)
        self.assertNotIn("specimen_sex", animal_field_names)
        self.assertNotIn("sex", scientific_field_names)
        self.assertNotIn("specimen_sex", scientific_field_names)

        # 2. Card no tiene 'sex' ni 'specimen_sex' entre sus 19 campos canónicos
        card_field_names = [f.name for f in fields(Card)]
        self.assertEqual(len(card_field_names), 19)
        self.assertNotIn("sex", card_field_names)
        self.assertNotIn("specimen_sex", card_field_names)
        self.assertIn("capture_id", card_field_names)

        # 3. Card.capture_id se vincula coherentemente a Capture.capture_id
        capture = Capture(capture_id=str(uuid.uuid4()), sex=SpecimenSex.MALE)
        self.assertIsInstance(capture.capture_id, str)
        # La Card puede ser creada usando capture.capture_id sin errores
        from datetime import datetime, timezone
        card = Card(
            card_id=str(uuid.uuid4()),
            animal_id="animal_vulpes_vulpes",
            specimen_number=1,
            schema_version="1.0",
            generation="gen_1",
            issued_at=datetime.now(timezone.utc),
            population_at_issuance=1,
            rarity="COMMON",
            capture_id=capture.capture_id,
            identification_method="onnx_local_v1",
            rank=1,
            serial="WA-MAM-0001-0001",
        )
        self.assertEqual(card.capture_id, capture.capture_id)


if __name__ == "__main__":
    unittest.main()
