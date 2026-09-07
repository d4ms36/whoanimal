"""
Pruebas unitarias para el Servicio de Ensamblaje y Generación de Cartas (WHO-014).
Verifica:
1. Generación válida de AnimalCard a partir de Capture respetando los 19 campos canónicos.
2. Rechazo de Capture nulo o de tipo inválido.
3. Rechazo de Capture inconsistente o corrupto.
4. Rechazo explícito ante AnimalProfile inexistente (sin inventar datos zoológicos).
5. No contaminación ontológica (Capture ≠ Card, Capture permanece inmutable y sin campos de Card).
6. Serialización y deserialización fidedigna (to_dict / from_dict).
7. Estado canónico de verificación estricto (UNVERIFIED, sin falsificar verificación).
8. Reproducibilidad exacta de referencias (capture_id y animal_id).
9. Protección contra duplicación (1 captura -> <= 1 carta, DEC-014 / CARD_SPEC.md).
10. Función de conveniencia create_card_from_capture.
11. Acumulación correcta de population_at_issuance para la especie.
12. Protección contra inclusión accidental de coordenadas GPS exactas en display_location.
13. Retrocompatibilidad con la interfaz compuesta legacy de AnimalCard.
"""

import unittest
import uuid
from datetime import datetime, timezone

from whoanimal.core.exceptions import ValidationError
from whoanimal.domain.enums import CardRarity, SpecimenSex, VerificationStatus
from whoanimal.domain.models import (
    AnimalCard,
    AnimalProfile,
    Capture,
    Card,
    CardBack,
    CardFront,
    CardMetadata,
    LoreProfile,
    ScientificInfo,
)
from whoanimal.services.card_generator import (
    AnimalProfileNotFoundError,
    CardGenerationError,
    CardGeneratorService,
    DuplicateCardError,
    InvalidCaptureError,
    create_card_from_capture,
)


class TestCardGeneratorService(unittest.TestCase):
    """Suite de pruebas de dominio para CardGeneratorService (WHO-014)."""

    def setUp(self):
        self.service = CardGeneratorService()
        self.valid_capture_id = str(uuid.uuid4())
        self.valid_ident_id = str(uuid.uuid4())
        # Canis lupus familiaris está presente en data/species/canis_lupus_familiaris.json
        self.valid_animal_id = "99fcbd5c-911d-4cee-af0c-9b5d4fb1e53f"
        self.species_slug = "canis_lupus_familiaris"

        self.valid_capture = Capture(
            capture_id=self.valid_capture_id,
            animal_id=self.valid_animal_id,
            identification_id=self.valid_ident_id,
            sex=SpecimenSex.MALE,
        )

    # -------------------------------------------------------------------------
    # Caso 1 — Generación válida y completitud de campos canónicos
    # -------------------------------------------------------------------------
    def test_01_valid_card_generation_from_capture(self):
        """Verifica que un Capture válido produzca una AnimalCard completa con los 19 campos canónicos."""
        card = self.service.generate(self.valid_capture)

        self.assertIsInstance(card, AnimalCard)
        self.assertIsInstance(card, Card)

        # Referencias y trazabilidad
        self.assertEqual(card.capture_id, self.valid_capture.capture_id)
        self.assertEqual(card.animal_id, self.valid_capture.animal_id)
        self.assertNotEqual(card.card_id, self.valid_capture.capture_id)

        # Validación estricta de card_id como UUIDv4
        u = uuid.UUID(card.card_id)
        self.assertEqual(u.version, 4)

        # Campos de emisión
        self.assertIsInstance(card.issued_at, datetime)
        self.assertEqual(card.generation, "genesis")
        self.assertGreaterEqual(card.population_at_issuance, 1)
        self.assertEqual(card.schema_version, "1.0")
        self.assertGreaterEqual(card.specimen_number, 1)

        # Autenticación y verificación
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)
        self.assertTrue(card.serial.startswith("WA-"))
        self.assertEqual(card.auth_serial, card.serial)

        # Presentación y estado por defecto
        self.assertEqual(card.rank, 1)
        self.assertEqual(card.visual_effects, [])
        self.assertIsNone(card.display_location)
        self.assertIsNone(card.artwork)
        self.assertIsNone(card.owner_id)

    # -------------------------------------------------------------------------
    # Caso 2 — Rechazo de Capture inválido
    # -------------------------------------------------------------------------
    def test_02_rejects_none_capture(self):
        """El servicio debe rechazar None con una excepción explícita de dominio."""
        with self.assertRaises(InvalidCaptureError):
            self.service.generate(None)

    def test_03_rejects_invalid_capture_types(self):
        """El servicio debe rechazar objetos que no sean instancias de Capture."""
        invalid_types = [
            "cadena_de_texto",
            12345,
            {"capture_id": str(uuid.uuid4())},
            [str(uuid.uuid4())],
            object(),
        ]
        for item in invalid_types:
            with self.subTest(item_type=type(item).__name__):
                with self.assertRaises(InvalidCaptureError):
                    self.service.generate(item)

    # -------------------------------------------------------------------------
    # Caso 3 — Rechazo de Capture inconsistente o corrupto
    # -------------------------------------------------------------------------
    def test_04_rejects_inconsistent_capture_missing_fields(self):
        """El servicio debe rechazar objetos Capture con atributos obligatorios vacíos o ausentes."""
        # Creamos un objeto falso/mock que burla el __init__ de Capture
        class CorruptCapture:
            capture_id = ""
            animal_id = "canis_lupus_familiaris"
            identification_id = str(uuid.uuid4())

        with self.assertRaises(InvalidCaptureError):
            self.service.generate(CorruptCapture())

    # -------------------------------------------------------------------------
    # Caso 4 — Rechazo ante AnimalProfile inexistente
    # -------------------------------------------------------------------------
    def test_05_rejects_non_existent_animal_profile(self):
        """Debe fallar de forma explícita si la especie no existe en el catálogo, sin inventar datos."""
        ghost_capture = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id="especie_fantasma_inexistente_xyz_999",
            identification_id=str(uuid.uuid4()),
        )
        with self.assertRaises(AnimalProfileNotFoundError):
            self.service.generate(ghost_capture)

    # -------------------------------------------------------------------------
    # Caso 5 — No contaminación ontológica (Capture ≠ Card)
    # -------------------------------------------------------------------------
    def test_06_no_ontological_contamination_of_capture(self):
        """Verifica que Capture permanece estrictamente inmutable y no recibe campos de Card."""
        capture_id_before = self.valid_capture.capture_id
        animal_id_before = self.valid_capture.animal_id
        ident_id_before = self.valid_capture.identification_id
        sex_before = self.valid_capture.sex

        card = self.service.generate(self.valid_capture)

        # Atributos de Capture permanecen inalterados
        self.assertEqual(self.valid_capture.capture_id, capture_id_before)
        self.assertEqual(self.valid_capture.animal_id, animal_id_before)
        self.assertEqual(self.valid_capture.identification_id, ident_id_before)
        self.assertEqual(self.valid_capture.sex, sex_before)

        # Ningún atributo de Card se introduce en Capture
        for card_field in [
            "card_id", "rarity", "serial", "auth_serial", "generation",
            "issued_at", "population_at_issuance", "rank", "verification_status",
            "visual_effects", "artwork", "display_location", "specimen_number"
        ]:
            self.assertFalse(
                hasattr(self.valid_capture, card_field),
                f"Capture was contaminated with Card field '{card_field}'!"
            )

    # -------------------------------------------------------------------------
    # Caso 6 — Serialización y deserialización fidedigna
    # -------------------------------------------------------------------------
    def test_07_serialization_and_deserialization_roundtrip(self):
        """Una Card generada debe serializarse a dict y restaurarse fidedignamente respetando el contrato."""
        card = self.service.generate(
            self.valid_capture,
            display_location="Parque Nacional Corcovado, Costa Rica",
            owner_id="user_naturalist_01",
        )

        data = card.to_dict()
        self.assertIsInstance(data, dict)
        self.assertEqual(data["card_id"], card.card_id)
        self.assertEqual(data["capture_id"], card.capture_id)
        self.assertEqual(data["animal_id"], card.animal_id)
        self.assertEqual(data["verification_status"], "UNVERIFIED")
        self.assertEqual(data["display_location"], "Parque Nacional Corcovado, Costa Rica")

        restored = AnimalCard.from_dict(data)
        self.assertIsInstance(restored, AnimalCard)
        self.assertEqual(restored.card_id, card.card_id)
        self.assertEqual(restored.capture_id, card.capture_id)
        self.assertEqual(restored.animal_id, card.animal_id)
        self.assertEqual(restored.serial, card.serial)
        self.assertEqual(restored.auth_serial, card.auth_serial)
        self.assertEqual(restored.verification_status, VerificationStatus.UNVERIFIED)

    # -------------------------------------------------------------------------
    # Caso 7 — Verificación canónica estricta (UNVERIFIED)
    # -------------------------------------------------------------------------
    def test_08_verification_status_is_strictly_unverified(self):
        """La emisión estándar nunca debe falsificar una verificación científica oficial (DEC-034, DEC-035)."""
        card = self.service.generate(self.valid_capture)
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)
        self.assertNotEqual(card.verification_status, VerificationStatus.VERIFIED)

    # -------------------------------------------------------------------------
    # Caso 8 — Reproducibilidad exacta de referencias
    # -------------------------------------------------------------------------
    def test_09_reference_integrity_between_capture_and_card(self):
        """Verifica la vinculación exacta entre las claves foráneas de Card y la Capture."""
        card = self.service.generate(self.valid_capture)
        self.assertEqual(card.capture_id, self.valid_capture.capture_id)
        self.assertEqual(card.animal_id, self.valid_capture.animal_id)

    # -------------------------------------------------------------------------
    # Caso 9 — Protección contra duplicación (1 captura -> <= 1 carta)
    # -------------------------------------------------------------------------
    def test_10_duplication_protection_prevents_multiple_cards_from_same_capture(self):
        """El servicio debe impedir emitir dos cartas a partir de la misma Capture (CARD_SPEC.md)."""
        card1 = self.service.generate(self.valid_capture)
        self.assertIsNotNone(card1)

        # Intentar generar de nuevo para el mismo Capture
        with self.assertRaises(DuplicateCardError):
            self.service.generate(self.valid_capture)

    # -------------------------------------------------------------------------
    # Caso 10 — Función de conveniencia create_card_from_capture
    # -------------------------------------------------------------------------
    def test_11_convenience_function_create_card_from_capture(self):
        """Verifica que la función de conveniencia genere cartas válidas."""
        new_capture = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id=self.valid_animal_id,
            identification_id=str(uuid.uuid4()),
        )
        card = create_card_from_capture(new_capture)
        self.assertIsInstance(card, AnimalCard)
        self.assertEqual(card.capture_id, new_capture.capture_id)
        self.assertEqual(card.animal_id, new_capture.animal_id)

    # -------------------------------------------------------------------------
    # Caso 11 — Acumulación de population_at_issuance
    # -------------------------------------------------------------------------
    def test_12_population_at_issuance_increments_per_species(self):
        """Verifica que el conteo acumulado de cartas emitidas para una especie aumente correctamente (DEC-033)."""
        service = CardGeneratorService()
        cap1 = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id=self.valid_animal_id,
            identification_id=str(uuid.uuid4()),
        )
        cap2 = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id=self.valid_animal_id,
            identification_id=str(uuid.uuid4()),
        )

        card1 = service.generate(cap1)
        card2 = service.generate(cap2)

        self.assertEqual(card1.population_at_issuance, 1)
        self.assertEqual(card2.population_at_issuance, 2)

    # -------------------------------------------------------------------------
    # Caso 12 — Protección anti-GPS en display_location
    # -------------------------------------------------------------------------
    def test_13_anti_gps_protection_in_display_location(self):
        """El servicio debe rechazar coordenadas GPS exactas en display_location para proteger a la fauna (DEC-030)."""
        cap = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id=self.valid_animal_id,
            identification_id=str(uuid.uuid4()),
        )
        with self.assertRaises(ValidationError):
            self.service.generate(
                cap,
                display_location="lat: 9.7489, lon: -83.7534",
            )

    # -------------------------------------------------------------------------
    # Caso 13 — Soporte de resolución por slug de catálogo
    # -------------------------------------------------------------------------
    def test_14_resolution_by_species_slug(self):
        """Verifica que una Capture con el slug de especie pueda resolverse contra el catálogo."""
        slug_capture = Capture(
            capture_id=str(uuid.uuid4()),
            animal_id=self.species_slug,
            identification_id=str(uuid.uuid4()),
        )
        card = self.service.generate(slug_capture)
        self.assertEqual(card.animal_id, self.species_slug)
        self.assertIsInstance(card, AnimalCard)

    # -------------------------------------------------------------------------
    # Caso 14 — Retrocompatibilidad con interfaz legacy de AnimalCard
    # -------------------------------------------------------------------------
    def test_15_legacy_animal_card_compatibility(self):
        """Verifica que el constructor compuesto legacy siga funcionando sin regresiones."""
        metadata = CardMetadata(
            card_id="card_0042",
            animal_id="animal_canis",
            card_code="WA-MA-0042",
        )
        front = CardFront(
            image_uri="file:///photo.jpg",
            common_name="Lobo",
            scientific_name_secondary="Canis lupus",
            category="MAMMAL",
        )
        back = CardBack(
            profile=AnimalProfile(
                animal_id="animal_canis",
                scientific_name="Canis lupus",
                common_name="Lobo",
                taxonomy={"kingdom": "Animalia"},
            ),
        )
        legacy_card = AnimalCard(metadata=metadata, front=front, back=back)
        self.assertEqual(legacy_card.metadata.card_code, "WA-MA-0042")
        self.assertEqual(legacy_card.front.common_name, "Lobo")
        self.assertEqual(legacy_card.back.profile.scientific_name, "Canis lupus")


if __name__ == "__main__":
    unittest.main()
