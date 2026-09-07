"""
Pruebas unitarias para la entidad de dominio Capture / Specimen (WHO-006C, WHO-012D).
Verifica identidad UUIDv4 estricta, semántica de sex (DEC-036, DEC-039),
puente de dominio IdentificationDecision (ACCEPTED) -> Capture (WHO-012D / DEC-046),
límites ontológicos frente a Animal y Card, e inmutabilidad.
"""

import unittest
import uuid
from datetime import datetime, timezone
from whoanimal.domain import (
    Capture,
    Specimen,
    SpecimenSex,
    BiologicalSex,
    CaptureCreationError,
    create_capture_from_decision,
    AnimalProfile,
    ScientificInfo,
    Card,
)
from whoanimal.domain.models.identification_result import IdentificationResult
from whoanimal.domain.models.identification_decision import (
    IdentificationDecision,
    DecisionState,
)
from whoanimal.core.exceptions import ValidationError


class TestCaptureDomainModel(unittest.TestCase):
    """Suite de pruebas unitarias para Capture / Specimen."""

    def setUp(self):
        self.valid_capture_id = str(uuid.uuid4())
        self.valid_ident_id = str(uuid.uuid4())
        self.valid_animal_id = "canis_lupus_familiaris"
        self.alt_animal_id = "felis_catus"
        self.candidates = [
            {"animal_id": self.valid_animal_id, "confidence": 0.95},
            {"animal_id": self.alt_animal_id, "confidence": 0.05},
        ]
        self.ident_result = IdentificationResult(
            identification_id=self.valid_ident_id,
            observation_id=str(uuid.uuid4()),
            candidate_species=self.candidates,
            identification_method="mock_vision_v1",
            created_at=datetime.now(timezone.utc).isoformat(),
        )

    # -------------------------------------------------------------------------
    # 1. Creación válida
    # -------------------------------------------------------------------------
    def test_01_valid_capture_instantiation_with_sex(self):
        """Verifica que una Capture válida pueda construirse con capture_id, animal_id, identification_id y sex."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=SpecimenSex.MALE,
        )
        self.assertEqual(capture.capture_id, self.valid_capture_id)
        self.assertEqual(capture.animal_id, self.valid_animal_id)
        self.assertEqual(capture.identification_id, self.valid_ident_id)
        self.assertEqual(capture.sex, SpecimenSex.MALE)
        self.assertIs(Specimen, Capture)  # Alias conceptual Specimen == Capture

    def test_02_valid_capture_with_sex_absent(self):
        """Verifica que sex pueda omitirse completamente (optional)."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
        )
        self.assertEqual(capture.capture_id, self.valid_capture_id)
        self.assertEqual(capture.animal_id, self.valid_animal_id)
        self.assertEqual(capture.identification_id, self.valid_ident_id)
        self.assertFalse(hasattr(capture, "sex"))

    def test_03_valid_capture_with_sex_none(self):
        """Verifica que sex=None sea aceptado explícitamente (nullable)."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=None,
        )
        self.assertIsNone(capture.sex)

    def test_04_valid_capture_with_all_sex_enum_values(self):
        """Verifica que todos los valores válidos de SpecimenSex sean aceptados."""
        for s in [SpecimenSex.MALE, SpecimenSex.FEMALE, SpecimenSex.UNKNOWN]:
            with self.subTest(sex=s):
                c = Capture(
                    capture_id=str(uuid.uuid4()),
                    animal_id=self.valid_animal_id,
                    identification_id=self.valid_ident_id,
                    sex=s,
                )
                self.assertEqual(c.sex, s)

    # -------------------------------------------------------------------------
    # 2. Identidad y validación de campos obligatorios
    # -------------------------------------------------------------------------
    def test_05_identity_uuidv4_strict_acceptance(self):
        """Verifica que capture_id e identification_id acepten strings canónicos y objetos UUIDv4."""
        # String UUIDv4
        id_str = str(uuid.uuid4())
        ident_str = str(uuid.uuid4())
        c1 = Capture(
            capture_id=id_str,
            animal_id=self.valid_animal_id,
            identification_id=ident_str,
            sex=SpecimenSex.FEMALE,
        )
        self.assertEqual(c1.capture_id, id_str)
        self.assertEqual(c1.identification_id, ident_str)

        # Objeto uuid.UUID versión 4
        id_obj = uuid.uuid4()
        ident_obj = uuid.uuid4()
        c2 = Capture(
            capture_id=id_obj,
            animal_id=self.valid_animal_id,
            identification_id=ident_obj,
            sex=SpecimenSex.UNKNOWN,
        )
        self.assertEqual(c2.capture_id, str(id_obj))
        self.assertEqual(c2.identification_id, str(ident_obj))

    def test_06_identity_uuidv5_and_uuidv1_rejected(self):
        """Verifica que UUIDv5 y UUIDv1 sean estrictamente rechazados."""
        v5_id = str(uuid.uuid5(uuid.NAMESPACE_DNS, "whoanimal.org"))
        v1_id = str(uuid.uuid1())

        with self.assertRaises(ValidationError):
            Capture(
                capture_id=v5_id,
                animal_id=self.valid_animal_id,
                identification_id=self.valid_ident_id,
            )

        with self.assertRaises(ValidationError):
            Capture(
                capture_id=self.valid_capture_id,
                animal_id=self.valid_animal_id,
                identification_id=v1_id,
            )

    def test_07_identity_uuid_nil_rejected(self):
        """Verifica que UUID nil sea rechazado para capture_id e identification_id."""
        nil_id = "00000000-0000-0000-0000-000000000000"
        with self.assertRaises(ValidationError):
            Capture(
                capture_id=nil_id,
                animal_id=self.valid_animal_id,
                identification_id=self.valid_ident_id,
            )
        with self.assertRaises(ValidationError):
            Capture(
                capture_id=self.valid_capture_id,
                animal_id=self.valid_animal_id,
                identification_id=nil_id,
            )

    def test_08_identity_invalid_uuid_rejected(self):
        """Verifica que formatos inválidos, vacíos, números o booleanos sean rechazados."""
        invalid_values = ["not-a-uuid", "12345", "", "   ", None, True, False, 123456, []]
        for inv in invalid_values:
            with self.subTest(invalid_id=inv):
                with self.assertRaises(ValidationError):
                    Capture(
                        capture_id=inv,
                        animal_id=self.valid_animal_id,
                        identification_id=self.valid_ident_id,
                    )
                with self.assertRaises(ValidationError):
                    Capture(
                        capture_id=self.valid_capture_id,
                        animal_id=self.valid_animal_id,
                        identification_id=inv,
                    )

    def test_09_animal_id_mandatory_and_validated(self):
        """Verifica que animal_id sea estrictamente obligatorio, no nulo y no vacío."""
        invalid_animals = [None, "", "   ", 12345, True, False, []]
        for inv in invalid_animals:
            with self.subTest(invalid_animal=inv):
                with self.assertRaises(ValidationError):
                    Capture(
                        capture_id=self.valid_capture_id,
                        animal_id=inv,
                        identification_id=self.valid_ident_id,
                    )
        # Falta por completo
        with self.assertRaises(ValidationError):
            Capture(
                capture_id=self.valid_capture_id,
                identification_id=self.valid_ident_id,
            )

    def test_10_identification_id_mandatory(self):
        """Verifica que identification_id sea estrictamente obligatorio."""
        with self.assertRaises(ValidationError):
            Capture(
                capture_id=self.valid_capture_id,
                animal_id=self.valid_animal_id,
            )

    # -------------------------------------------------------------------------
    # 3. Semántica de sexo (DEC-036, DEC-039)
    # -------------------------------------------------------------------------
    def test_11_sex_string_normalization_and_alias(self):
        """Verifica normalización de cadenas de sexo y alias BiologicalSex."""
        c_male = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex="male",
        )
        self.assertEqual(c_male.sex, SpecimenSex.MALE)

        c_female = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex="FEMALE",
        )
        self.assertEqual(c_female.sex, SpecimenSex.FEMALE)

        c_unknown = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex="Unknown",
        )
        self.assertEqual(c_unknown.sex, SpecimenSex.UNKNOWN)

        self.assertIs(BiologicalSex, SpecimenSex)

    def test_12_sex_arbitrary_values_rejected(self):
        """Verifica que valores arbitrarios para sex sean rechazados."""
        arbitrary_values = ["HERMAPHRODITE", "OTHER", "MACHO", "HEMBRA", 1, 0, True, "", "   "]
        for arb in arbitrary_values:
            with self.subTest(arbitrary_sex=arb):
                with self.assertRaises(ValidationError):
                    Capture(
                        capture_id=self.valid_capture_id,
                        animal_id=self.valid_animal_id,
                        identification_id=self.valid_ident_id,
                        sex=arb,
                    )

    # -------------------------------------------------------------------------
    # 4. Puente de dominio: IdentificationDecision -> Capture (WHO-012D)
    # -------------------------------------------------------------------------
    def test_13_bridge_accepted_decision_creates_capture(self):
        """Verifica que una decisión ACCEPTED válida crea exitosamente una Capture."""
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.valid_animal_id,
        )

        capture = Capture.create_from_decision(decision, self.ident_result)

        self.assertIsInstance(capture, Capture)
        self.assertEqual(capture.animal_id, self.valid_animal_id)
        self.assertEqual(capture.identification_id, self.valid_ident_id)
        self.assertFalse(hasattr(capture, "sex"))

        # Validar que capture_id sea un UUIDv4 generado
        u = uuid.UUID(capture.capture_id)
        self.assertEqual(u.version, 4)

    def test_14_bridge_accepted_decision_with_optional_sex(self):
        """Verifica que create_from_decision soporte sex con valor, None o ausente."""
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.alt_animal_id,
        )

        # 1. Con valor
        c_sex = Capture.create_from_decision(decision, self.ident_result, sex=SpecimenSex.FEMALE)
        self.assertEqual(c_sex.sex, SpecimenSex.FEMALE)
        self.assertEqual(c_sex.animal_id, self.alt_animal_id)

        # 2. Con None
        c_none = Capture.create_from_decision(decision, self.ident_result, sex=None)
        self.assertIsNone(c_none.sex)

        # 3. Función independiente de servicio
        c_func = create_capture_from_decision(decision, self.ident_result, sex=SpecimenSex.UNKNOWN)
        self.assertEqual(c_func.sex, SpecimenSex.UNKNOWN)

    def test_15_bridge_rejected_decision_does_not_create_capture(self):
        """Verifica que una decisión REJECTED sea rechazada y no genere Capture."""
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.REJECTED,
        )
        with self.assertRaises(ValidationError) as ctx:
            Capture.create_from_decision(decision, self.ident_result)
        self.assertIn("Only ACCEPTED decisions can originate a Capture", str(ctx.exception))

    def test_16_bridge_cancelled_decision_does_not_create_capture(self):
        """Verifica que una decisión CANCELLED sea rechazada y no genere Capture."""
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.CANCELLED,
        )
        with self.assertRaises(ValidationError) as ctx:
            Capture.create_from_decision(decision, self.ident_result)
        self.assertIn("Only ACCEPTED decisions can originate a Capture", str(ctx.exception))

    def test_17_bridge_mismatched_identification_id_rejected(self):
        """Verifica que una decisión con identification_id diferente del resultado sea rechazada."""
        other_ident_id = str(uuid.uuid4())
        decision = IdentificationDecision(
            decision_id=str(uuid.uuid4()),
            identification_id=other_ident_id,
            decision=DecisionState.ACCEPTED,
            decided_at=datetime.now(timezone.utc).isoformat(),
            selected_animal_id=self.valid_animal_id,
            candidates=self.candidates,
        )
        with self.assertRaises(ValidationError) as ctx:
            Capture.create_from_decision(decision, self.ident_result)
        self.assertIn("does not match IdentificationResult identification_id", str(ctx.exception))

    def test_18_bridge_animal_not_in_candidates_rejected(self):
        """Verifica que un animal seleccionado que no pertenezca a los candidatos genere error."""
        # Creamos una decisión con trampa o кандидаты manipulados
        foreign_animal = "panthera_onca"
        decision = IdentificationDecision(
            decision_id=str(uuid.uuid4()),
            identification_id=self.valid_ident_id,
            decision=DecisionState.ACCEPTED,
            decided_at=datetime.now(timezone.utc).isoformat(),
            selected_animal_id=foreign_animal,
            candidates=[foreign_animal],  # Decisión internamente válida con sus propios candidatos
        )
        # Al validar contra self.ident_result (que no contiene panthera_onca), debe fallar
        with self.assertRaises(ValidationError) as ctx:
            Capture.create_from_decision(decision, self.ident_result)
        self.assertIn("does not exist among the candidates", str(ctx.exception))

    def test_19_bridge_invalid_inputs_rejected(self):
        """Verifica rechazo ante parámetros None o inválidos en la factoría."""
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.valid_animal_id,
        )
        with self.assertRaises(ValidationError):
            Capture.create_from_decision(None, self.ident_result)
        with self.assertRaises(ValidationError):
            Capture.create_from_decision(decision, None)

    # -------------------------------------------------------------------------
    # 5. Inmutabilidad
    # -------------------------------------------------------------------------
    def test_20_immutability_post_creation(self):
        """Verifica que ningún atributo de Capture pueda modificarse tras su creación."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=SpecimenSex.MALE,
        )

        with self.assertRaises(ValidationError):
            capture.capture_id = str(uuid.uuid4())

        with self.assertRaises(ValidationError):
            capture.animal_id = "canis_lupus_dingo"

        with self.assertRaises(ValidationError):
            capture.identification_id = str(uuid.uuid4())

        with self.assertRaises(ValidationError):
            capture.sex = SpecimenSex.FEMALE

        with self.assertRaises(ValidationError):
            capture.extra_field = "unexpected"

    # -------------------------------------------------------------------------
    # 6. Serialización (to_dict / from_dict)
    # -------------------------------------------------------------------------
    def test_21_serialization_roundtrip_with_sex(self):
        """Verifica to_dict y from_dict con sex presente."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=SpecimenSex.FEMALE,
        )
        data = capture.to_dict()
        expected = {
            "capture_id": self.valid_capture_id,
            "animal_id": self.valid_animal_id,
            "identification_id": self.valid_ident_id,
            "sex": "FEMALE",
        }
        self.assertEqual(data, expected)

        restored = Capture.from_dict(data)
        self.assertEqual(restored.capture_id, self.valid_capture_id)
        self.assertEqual(restored.animal_id, self.valid_animal_id)
        self.assertEqual(restored.identification_id, self.valid_ident_id)
        self.assertEqual(restored.sex, SpecimenSex.FEMALE)

    def test_22_serialization_roundtrip_with_sex_null(self):
        """Verifica to_dict y from_dict con sex=None explícito."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=None,
        )
        data = capture.to_dict()
        self.assertIsNone(data["sex"])

        restored = Capture.from_dict(data)
        self.assertIsNone(restored.sex)

    def test_23_serialization_roundtrip_with_sex_absent(self):
        """Verifica to_dict y from_dict con sex ausente."""
        capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
        )
        data = capture.to_dict()
        self.assertNotIn("sex", data)

        restored = Capture.from_dict(data)
        self.assertFalse(hasattr(restored, "sex"))

    # -------------------------------------------------------------------------
    # 7. No admisión de campos no aprobados
    # -------------------------------------------------------------------------
    def test_24_no_unexpected_extra_fields_permitted(self):
        """Verifica que campos no aprobados (geolocalización, imagen, cartas, etc.) sean rechazados."""
        forbidden_fields = [
            {"latitude": 40.4168},
            {"longitude": -3.7038},
            {"gps": "40.4168, -3.7038"},
            {"image_url": "https://whoanimal.org/img.png"},
            {"timestamp": "2026-09-06T12:00:00Z"},
            {"metadata": {"foo": "bar"}},
            {"card_id": str(uuid.uuid4())},
            {"rarity": "COMMON"},
            {"rank": 1},
            {"serial": "WA-MAM-0001"},
        ]
        for extra in forbidden_fields:
            with self.subTest(extra_field=extra):
                with self.assertRaises(ValidationError):
                    Capture(
                        capture_id=self.valid_capture_id,
                        animal_id=self.valid_animal_id,
                        identification_id=self.valid_ident_id,
                        **extra,
                    )

    # -------------------------------------------------------------------------
    # 8. Fronteras ontológicas (DEC-036)
    # -------------------------------------------------------------------------
    def test_25_ontological_boundaries_animal_and_card(self):
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
        decision = IdentificationDecision.create(
            identification=self.ident_result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.valid_animal_id,
        )
        capture = Capture.create_from_decision(decision, self.ident_result, sex=SpecimenSex.MALE)

        card = Card(
            card_id=str(uuid.uuid4()),
            animal_id=capture.animal_id,
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
        self.assertEqual(card.animal_id, capture.animal_id)


if __name__ == "__main__":
    unittest.main()
