"""
Pruebas de modelos de dominio y validación de reglas de producto.
Compatible tanto con unittest (stdlib) como con pytest.
"""

import unittest
from datetime import datetime, timezone
from whoanimal.domain.enums import AnimalCategory, DangerLevel, CardRarity
from whoanimal.domain.models import (
    ScientificInfo,
    ConservationIndicators,
    DangerAssessment,
    Curiosity,
    AnimalProfile,
    LoreProfile,
    CardFront,
    CardBack,
    CardMetadata,
    AnimalCard,
)


class TestDomainModels(unittest.TestCase):
    """Pruebas de integridad para modelos de dominio de Who Animal."""

    def test_scientific_profile_creation(self):
        """Verifica que el perfil científico factual se construya con datos inmutables y verídicos."""
        sci_info = ScientificInfo(
            common_name="Zorro Rojo",
            scientific_name="Vulpes vulpes",
            description="Mamífero cánido omnívoro de amplia distribución adaptado a diversos hábitats.",
            habitat="Bosques templados, matorrales y zonas agrícolas.",
            distribution="Hemisferio Norte (América del Norte, Europa, Asia).",
            diet="Omnívoro: pequeños roedores, bayas, insectos y carroña.",
            behavior="Crepuscular y nocturno, altamente adaptable y solitario.",
            size="45 a 90 cm de longitud corporal.",
            weight="3 a 14 kg.",
            sources=["UICN Red List - Least Concern"],
        )

        indicators = ConservationIndicators(
            is_protected=False,
            is_rare_species=False,
        )

        danger = DangerAssessment(
            level=DangerLevel.PRECAUTION,
            notice_text="⚠️ Precaución: No alimentar ejemplares silvestres para evitar habituación y riesgo de mordedura.",
            risk_factors=["bite_risk", "rabies_vector"],
        )

        curiosity = Curiosity(
            fact="Los zorros utilizan el campo magnético de la Tierra para calcular la distancia al cazar bajo la nieve."
        )

        profile = AnimalProfile(
            animal_id="animal_vulpes_vulpes",
            scientific_name="Vulpes vulpes",
            common_name="Zorro Rojo",
            taxonomy={"kingdom": "Animalia", "class": "Mammalia"},
            conservation_status="LC",
            is_rare_species=False
        )

        self.assertEqual(profile.common_name, "Zorro Rojo")
        self.assertEqual(profile.scientific_name, "Vulpes vulpes")
        self.assertEqual(profile.conservation_status, "LC")
        self.assertFalse(profile.is_rare_species)

    def test_lore_separation_and_fictional_nature(self):
        """Verifica que el Lore se mantenga aislado, etiquetado y con advertencia de ficción."""
        lore = LoreProfile(
            title="El Astuto Caminante de las Brasas",
            narrative="Dicen los antiguos relatos de las colinas que cuando el sol se oculta, su cola enciende el primer destello crepuscular.",
            world_tags=["bosque_ancestral", "astucia", "crepusculo"],
        )

        self.assertTrue(lore.is_fictional)
        self.assertIn("ficticio", lore.disclaimer.lower())
        self.assertEqual(lore.title, "El Astuto Caminante de las Brasas")

    def test_card_composition_front_and_back(self):
        """Verifica que la carta contenga correctamente frente visual, reverso con ciencia y lore opcional."""
        sci_info = ScientificInfo(
            common_name="Búho Real",
            scientific_name="Bubo bubo",
            description="Gran rapaz nocturna con característicos mechones en la cabeza.",
            habitat="Cantiles rocosos y bosques.",
            distribution="Eurasia.",
            diet="Carnívoro estricto: conejos, roedores y aves medianas.",
            behavior="Territorial y predominantemente nocturno.",
            size="60 a 75 cm de longitud.",
            weight="1.5 a 4.2 kg.",
        )

        profile = AnimalProfile(
            animal_id="animal_bubo_bubo",
            scientific_name="Bubo bubo",
            common_name="Búho Real",
            taxonomy={"kingdom": "Animalia", "class": "Aves"},
            conservation_status="Protected",
            is_rare_species=False
        )

        lore = LoreProfile(
            title="El Centinela de Medianoche",
            narrative="Guardián silencioso de las cumbres que vigila los secretos olvidados de la noche.",
        )

        front = CardFront(
            image_uri="assets/cards/bubo_bubo_front.webp",
            common_name="Búho Real",
            scientific_name_secondary="Bubo bubo",
            category=AnimalCategory.BIRD,
            visual_theme="nocturnal_azure",
            card_code="WA-AV-0012",
        )

        back = CardBack(
            profile=profile,
            lore=lore,
        )

        metadata = CardMetadata(
            card_id="card_0012_bubo",
            animal_id="animal_bubo_bubo",
            card_code="WA-AV-0012",
            rarity_tier=CardRarity.RARE,
        )

        card = AnimalCard(
            metadata=metadata,
            front=front,
            back=back,
        )

        self.assertEqual(card.metadata.card_code, "WA-AV-0012")
        self.assertEqual(card.front.common_name, "Búho Real")
        self.assertEqual(card.back.profile.scientific_name, "Bubo bubo")
        self.assertEqual(card.back.profile.conservation_status, "Protected")
        self.assertFalse(card.back.profile.is_rare_species)
        self.assertIsNotNone(card.back.lore)
        self.assertTrue(card.back.lore.is_fictional)

    def test_version_and_config_consistency(self):
        """Verifica que la versión oficial sea 0.0.1 y que DEFAULT_CONFIG la sincronice sin duplicar."""
        from whoanimal import __version__
        from whoanimal.core.config import DEFAULT_CONFIG

        self.assertEqual(__version__, "0.0.1")
        self.assertEqual(DEFAULT_CONFIG.version, "0.0.1")
        self.assertTrue(DEFAULT_CONFIG.is_pet_friendly)
        self.assertTrue(DEFAULT_CONFIG.enforce_lore_separation)


class TestCardDomainModel(unittest.TestCase):
    """
    Pruebas exhaustivas para el modelo formal Card (19 campos canónicos)
    y validación del contrato formal aprobado en WHO-006A.1 / WHO-006B.
    """

    def _make_valid_required_kwargs(self):
        """Genera un diccionario con exactamente los campos requeridos válidos sin defaults."""
        return {
            "card_id": "c84d7285-807e-468a-b5a8-ef075a3e1201",
            "animal_id": "animal_vulpes_vulpes",
            "specimen_number": 42,
            "schema_version": "1.0",
            "generation": "gen_1",
            "issued_at": datetime.now(timezone.utc),
            "population_at_issuance": 10,
            "rarity": "RARE",
            "capture_id": "a32f6b8c-5721-419b-a5d9-31728f3a5e82",
            "identification_method": "onnx_local_v1",
            "rank": 1,
            "serial": "WA-MAM-0042-0010",
        }

    def test_01_valid_card_can_be_built(self):
        """1. Card válida puede construirse con los campos requeridos y opcionales."""
        from whoanimal.domain import Card, VerificationStatus

        kwargs = self._make_valid_required_kwargs()
        kwargs.update({
            "edition": "1st Edition",
            "identification_confidence": 0.95,
            "display_location": "Sierra de Guadarrama, España",
            "visual_effects": ["foil_silver"],
            "artwork": "assets/artworks/vulpes_custom.webp",
            "owner_id": "user_explorer_01",
            "verification_status": VerificationStatus.VERIFIED,
        })
        card = Card(**kwargs)
        self.assertEqual(card.card_id, "c84d7285-807e-468a-b5a8-ef075a3e1201")
        self.assertEqual(card.animal_id, "animal_vulpes_vulpes")
        self.assertEqual(card.specimen_number, 42)
        self.assertEqual(card.schema_version, "1.0")
        self.assertEqual(card.edition, "1st Edition")
        self.assertEqual(card.generation, "gen_1")
        self.assertEqual(card.population_at_issuance, 10)
        self.assertEqual(card.rarity, "RARE")
        self.assertEqual(card.capture_id, "a32f6b8c-5721-419b-a5d9-31728f3a5e82")
        self.assertEqual(card.identification_method, "onnx_local_v1")
        self.assertEqual(card.identification_confidence, 0.95)
        self.assertEqual(card.rank, 1)
        self.assertEqual(card.display_location, "Sierra de Guadarrama, España")
        self.assertEqual(card.visual_effects, ["foil_silver"])
        self.assertEqual(card.artwork, "assets/artworks/vulpes_custom.webp")
        self.assertEqual(card.owner_id, "user_explorer_01")
        self.assertEqual(card.serial, "WA-MAM-0042-0010")
        self.assertEqual(card.verification_status, VerificationStatus.VERIFIED)

    def test_02_missing_each_required_field_raises_error(self):
        """2. Falta de cada campo required produce error (ValidationError)."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        required_fields = [
            "card_id",
            "animal_id",
            "specimen_number",
            "schema_version",
            "generation",
            "issued_at",
            "population_at_issuance",
            "rarity",
            "capture_id",
            "identification_method",
            "rank",
            "serial",
        ]
        for f in required_fields:
            kwargs = self._make_valid_required_kwargs()
            del kwargs[f]
            with self.subTest(missing_required_field=f):
                with self.assertRaises(ValidationError):
                    Card(**kwargs)

    def test_03_nullable_fields_accept_none(self):
        """3. Campos nullable aceptan None explícitamente."""
        from whoanimal.domain import Card

        kwargs = self._make_valid_required_kwargs()
        kwargs.update({
            "identification_confidence": None,
            "display_location": None,
            "artwork": None,
            "owner_id": None,
        })
        card = Card(**kwargs)
        self.assertIsNone(card.identification_confidence)
        self.assertIsNone(card.display_location)
        self.assertIsNone(card.artwork)
        self.assertIsNone(card.owner_id)

    def test_04_optional_fields_can_be_omitted(self):
        """4. Campos optional pueden omitirse en la instanciación."""
        from whoanimal.domain import Card, VerificationStatus

        # Se proporcionan únicamente los requeridos sin defaults
        card = Card(**self._make_valid_required_kwargs())
        self.assertIsNone(card.edition)
        self.assertIsNone(card.identification_confidence)
        self.assertIsNone(card.display_location)
        self.assertEqual(card.visual_effects, [])
        self.assertIsNone(card.artwork)
        self.assertIsNone(card.owner_id)
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)

    def test_05_visual_effects_none_fails(self):
        """5. visual_effects=None falla (es opcional pero non-nullable; default es [])."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["visual_effects"] = None
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_06_population_at_issuance_zero_or_negative_fails(self):
        """6. population_at_issuance=0 o negativo falla (requiere >= 1)."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["population_at_issuance"] = 0
        with self.assertRaises(ValidationError):
            Card(**kwargs)

        kwargs["population_at_issuance"] = -5
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_07_specimen_number_zero_or_negative_fails(self):
        """7. specimen_number=0 o negativo falla (requiere >= 1)."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["specimen_number"] = 0
        with self.assertRaises(ValidationError):
            Card(**kwargs)

        kwargs["specimen_number"] = -1
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_08_identification_confidence_negative_fails(self):
        """8. identification_confidence < 0 falla."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["identification_confidence"] = -0.05
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_09_identification_confidence_greater_than_one_fails(self):
        """9. identification_confidence > 1 falla."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["identification_confidence"] = 1.05
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_10_verification_status_none_fails_in_current_schema(self):
        """10. verification_status=None falla en el esquema actual."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["verification_status"] = None
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_11_invalid_verification_status_fails(self):
        """11. Estados inválidos de verification_status fallan."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        for invalid in ["AUTHENTICATED", "PENDING", "ACTIVE", 123, ""]:
            with self.subTest(invalid_status=invalid):
                kwargs["verification_status"] = invalid
                with self.assertRaises(ValidationError):
                    Card(**kwargs)

    def test_12_rarity_not_restricted_to_invented_tiers(self):
        """12. rarity no queda restringida a una lista cerrada de tiers (DEC-022-PENDING abierta)."""
        from whoanimal.domain import Card

        kwargs = self._make_valid_required_kwargs()
        custom_tiers = ["MYTHIC_TIER", "TIER_OMEGA", "pionero_2026", "rareza_especial"]
        for tier in custom_tiers:
            with self.subTest(tier=tier):
                kwargs["rarity"] = tier
                card = Card(**kwargs)
                self.assertEqual(card.rarity, tier)

    def test_13_rank_not_restricted_to_invented_levels(self):
        """13. rank no queda restringido a niveles inventados (DEC-037-PENDING abierta)."""
        from whoanimal.domain import Card

        kwargs = self._make_valid_required_kwargs()
        levels = [1, 0, 99, 1000, "Novice", "Maestro", "Rango Plata"]
        for r in levels:
            with self.subTest(rank=r):
                kwargs["rank"] = r
                card = Card(**kwargs)
                self.assertEqual(card.rank, r)

    def test_14_no_unapproved_defaults_exist_in_model(self):
        """14. No existen defaults no aprobados en los campos del modelo."""
        from dataclasses import fields, MISSING
        from whoanimal.domain import Card, VerificationStatus

        for f in fields(Card):
            has_default = (f.default is not MISSING) or (f.default_factory is not MISSING)
            if f.name in ("verification_status", "visual_effects"):
                self.assertTrue(has_default, f"Se esperaba default aprobado para {f.name}")
            else:
                self.assertFalse(has_default, f"Campo {f.name} tiene un default no aprobado: {f.default}")

        # Comprobar además que los defaults aprobados tienen los valores correctos
        card = Card(**self._make_valid_required_kwargs())
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)
        self.assertEqual(card.visual_effects, [])

    def test_15_verification_status_defaults_to_unverified_when_omitted(self):
        """15. verification_status inicia en UNVERIFIED cuando se omite."""
        from whoanimal.domain import Card, VerificationStatus

        card = Card(**self._make_valid_required_kwargs())
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)

    def test_16_visual_effects_defaults_to_empty_list_when_omitted(self):
        """16. visual_effects inicia como lista vacía cuando se omite."""
        from whoanimal.domain import Card

        card = Card(**self._make_valid_required_kwargs())
        self.assertEqual(card.visual_effects, [])
        self.assertIsInstance(card.visual_effects, list)

    def test_17_model_contains_no_fields_beyond_canonical_19(self):
        """17. El modelo no contiene campos adicionales a los 19 canónicos."""
        from dataclasses import fields
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        canonical_19 = [
            "card_id",
            "animal_id",
            "specimen_number",
            "schema_version",
            "edition",
            "generation",
            "issued_at",
            "population_at_issuance",
            "rarity",
            "capture_id",
            "identification_method",
            "identification_confidence",
            "rank",
            "display_location",
            "visual_effects",
            "artwork",
            "owner_id",
            "serial",
            "verification_status",
        ]
        card_field_names = [f.name for f in fields(Card)]
        self.assertEqual(len(card_field_names), 19)
        self.assertEqual(card_field_names, canonical_19)

        # Probar que pasar specimen_sex falla (DEC-036: specimen_sex ∈ Capture, specimen_sex ∉ Card)
        kwargs = self._make_valid_required_kwargs()
        kwargs["specimen_sex"] = "MALE"
        with self.assertRaises(ValidationError):
            Card(**kwargs)

        # Probar campo arbitrario no permitido
        kwargs_arbitrary = self._make_valid_required_kwargs()
        kwargs_arbitrary["combat_power"] = 9000
        with self.assertRaises(ValidationError):
            Card(**kwargs_arbitrary)

    def test_18_immutability_of_historical_fields_after_issuance(self):
        """Verifica que los campos históricos inmutables no puedan modificarse post-emisión."""
        from whoanimal.domain import Card, VerificationStatus
        from whoanimal.core.exceptions import ValidationError

        card = Card(**self._make_valid_required_kwargs())

        # Intentos de mutar campos históricos inmutables
        immutable_attempts = [
            ("card_id", "00000000-0000-0000-0000-000000000000"),
            ("animal_id", "animal_canis_lupus"),
            ("specimen_number", 999),
            ("schema_version", "2.0"),
            ("edition", "2nd Edition"),
            ("generation", "gen_2"),
            ("issued_at", datetime.now(timezone.utc)),
            ("population_at_issuance", 999),
            ("rarity", "EPIC"),
            ("capture_id", "00000000-0000-0000-0000-000000000001"),
            ("identification_method", "manual_curator"),
            ("identification_confidence", 0.99),
            ("serial", "WA-NEW-SERIAL"),
        ]
        for field_name, new_val in immutable_attempts:
            with self.subTest(immutable_field=field_name):
                with self.assertRaises(ValidationError):
                    setattr(card, field_name, new_val)

        # Campos mutables deben permitir modificación controlada y validada
        card.rank = 5
        self.assertEqual(card.rank, 5)

        card.visual_effects = ["foil_gold", "textured"]
        self.assertEqual(card.visual_effects, ["foil_gold", "textured"])

        card.artwork = "assets/custom_art.png"
        self.assertEqual(card.artwork, "assets/custom_art.png")

        card.owner_id = "user_custodian_42"
        self.assertEqual(card.owner_id, "user_custodian_42")

        card.verification_status = VerificationStatus.VERIFIED
        self.assertEqual(card.verification_status, VerificationStatus.VERIFIED)

    def test_19_edition_none_fails(self):
        """Verifica que edition=None falle explícitamente (Optional pero non-nullable)."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        kwargs["edition"] = None
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_20_card_to_dict_and_from_dict_roundtrip(self):
        """Verifica la serialización a diccionario y deserialización validada."""
        from whoanimal.domain import Card, VerificationStatus

        # 1. Caso sin edition (omisión en emisión)
        card = Card(**self._make_valid_required_kwargs())
        data = card.to_dict()
        self.assertIsInstance(data, dict)
        self.assertEqual(len(data), 18)
        self.assertNotIn("edition", data)
        self.assertEqual(data["card_id"], "c84d7285-807e-468a-b5a8-ef075a3e1201")
        self.assertEqual(data["verification_status"], "UNVERIFIED")

        # Reconstrucción mediante from_dict respetando ausencia
        restored = Card.from_dict(data)
        self.assertEqual(restored.card_id, card.card_id)
        self.assertFalse(restored.has_edition)
        self.assertIsNone(restored.edition)
        self.assertEqual(restored.verification_status, VerificationStatus.UNVERIFIED)

        # 2. Caso con edition
        kwargs = self._make_valid_required_kwargs()
        kwargs["edition"] = "1st Edition"
        card_with_edition = Card(**kwargs)
        data_with_edition = card_with_edition.to_dict()
        self.assertEqual(len(data_with_edition), 19)
        self.assertEqual(data_with_edition["edition"], "1st Edition")

        restored_with_edition = Card.from_dict(data_with_edition)
        self.assertTrue(restored_with_edition.has_edition)
        self.assertEqual(restored_with_edition.edition, "1st Edition")

    def test_21_uuid_strictly_v4_validation(self):
        """
        Obligatorio WHO-006B.1:
        - UUIDv4 válido → PASS
        - UUIDv5 → FAIL
        - UUID inválido → FAIL
        """
        import uuid
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        # UUIDv4 válidos
        valid_v4_str = str(uuid.uuid4())
        valid_v4_obj = uuid.uuid4()
        kwargs = self._make_valid_required_kwargs()
        kwargs["card_id"] = valid_v4_str
        kwargs["capture_id"] = valid_v4_obj
        card = Card(**kwargs)
        self.assertEqual(card.card_id, valid_v4_str)
        self.assertEqual(card.capture_id, str(valid_v4_obj))

        # UUIDv5 debe ser rechazado
        v5_card_id = str(uuid.uuid5(uuid.NAMESPACE_DNS, "whoanimal.org"))
        v5_capture_id = uuid.uuid5(uuid.NAMESPACE_URL, "https://whoanimal.org")

        with self.subTest(rejected="uuidv5_card_id"):
            kwargs_v5 = self._make_valid_required_kwargs()
            kwargs_v5["card_id"] = v5_card_id
            with self.assertRaises(ValidationError):
                Card(**kwargs_v5)

        with self.subTest(rejected="uuidv5_capture_id"):
            kwargs_v5 = self._make_valid_required_kwargs()
            kwargs_v5["capture_id"] = v5_capture_id
            with self.assertRaises(ValidationError):
                Card(**kwargs_v5)

        # UUIDv1 debe ser rechazado
        v1_id = str(uuid.uuid1())
        with self.subTest(rejected="uuidv1"):
            kwargs_v1 = self._make_valid_required_kwargs()
            kwargs_v1["card_id"] = v1_id
            with self.assertRaises(ValidationError):
                Card(**kwargs_v1)

        # UUID inválido debe ser rechazado
        invalid_uuids = ["not-a-uuid", "12345", "", "c84d7285-807e-468a-b5a8", None, True]
        for inv in invalid_uuids:
            with self.subTest(invalid_uuid=inv):
                kwargs_inv = self._make_valid_required_kwargs()
                kwargs_inv["card_id"] = inv
                with self.assertRaises(ValidationError):
                    Card(**kwargs_inv)

    def test_22_edition_contract_optional_non_nullable(self):
        """
        Obligatorio WHO-006B.1:
        - omitido → PASS
        - string válida → PASS
        - None explícito → FAIL
        - vacío → FAIL
        """
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        # Omitido → PASS
        kwargs = self._make_valid_required_kwargs()
        card_omitted = Card(**kwargs)
        self.assertFalse(card_omitted.has_edition)
        self.assertIsNone(card_omitted.edition)

        # String válida → PASS
        kwargs_valid = self._make_valid_required_kwargs()
        kwargs_valid["edition"] = "1st Edition"
        card_with_edition = Card(**kwargs_valid)
        self.assertTrue(card_with_edition.has_edition)
        self.assertEqual(card_with_edition.edition, "1st Edition")

        # None explícito → FAIL
        kwargs_none = self._make_valid_required_kwargs()
        kwargs_none["edition"] = None
        with self.assertRaises(ValidationError):
            Card(**kwargs_none)

        # Vacío o sólo espacios → FAIL
        for empty in ["", "   ", "\t\n"]:
            with self.subTest(empty_edition=empty):
                kwargs_empty = self._make_valid_required_kwargs()
                kwargs_empty["edition"] = empty
                with self.assertRaises(ValidationError):
                    Card(**kwargs_empty)

    def test_23_artwork_contract_open_type(self):
        """
        Obligatorio WHO-006B.1:
        - omitido → PASS
        - None → PASS
        - string → PASS
        - object → PASS
        """
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        # Omitido → PASS
        kwargs = self._make_valid_required_kwargs()
        card_omitted = Card(**kwargs)
        self.assertIsNone(card_omitted.artwork)

        # None → PASS
        kwargs_none = self._make_valid_required_kwargs()
        kwargs_none["artwork"] = None
        card_none = Card(**kwargs_none)
        self.assertIsNone(card_none.artwork)

        # String (URI o ruta) → PASS
        kwargs_str = self._make_valid_required_kwargs()
        kwargs_str["artwork"] = "https://assets.whoanimal.org/art/vulpes_oil.webp"
        card_str = Card(**kwargs_str)
        self.assertEqual(card_str.artwork, "https://assets.whoanimal.org/art/vulpes_oil.webp")

        # Object (dict o estructura de datos) → PASS
        artwork_dict = {
            "uri": "https://assets.whoanimal.org/art/vulpes_oil.webp",
            "illustrator_name": "Elena Rostova",
            "commission_year": 2026,
        }
        kwargs_obj = self._make_valid_required_kwargs()
        kwargs_obj["artwork"] = artwork_dict
        card_obj = Card(**kwargs_obj)
        self.assertEqual(card_obj.artwork, artwork_dict)

        # String vacía → FAIL
        kwargs_empty = self._make_valid_required_kwargs()
        kwargs_empty["artwork"] = "   "
        with self.assertRaises(ValidationError):
            Card(**kwargs_empty)

    def test_24_serialization_contract_details(self):
        """
        Obligatorio WHO-006B.1:
        - edition omitido → no aparece como null (se omite la clave)
        - edition=None en dict → FAIL en from_dict
        - artwork=None → permitido
        - visual_effects omitido → []
        - verification_status omitido → UNVERIFIED
        """
        from whoanimal.domain import Card, VerificationStatus
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()
        card = Card(**kwargs)
        data = card.to_dict()

        # edition omitido no aparece como null
        self.assertNotIn("edition", data)

        # edition=None en from_dict debe fallar
        dict_with_none_edition = dict(data)
        dict_with_none_edition["edition"] = None
        with self.assertRaises(ValidationError):
            Card.from_dict(dict_with_none_edition)

        # artwork=None permitido en serialización y deserialización
        self.assertIn("artwork", data)
        self.assertIsNone(data["artwork"])
        restored_artwork = Card.from_dict(data)
        self.assertIsNone(restored_artwork.artwork)

        # visual_effects omitido inicializa en []
        self.assertEqual(card.visual_effects, [])
        self.assertEqual(data["visual_effects"], [])
        self.assertEqual(restored_artwork.visual_effects, [])

        # verification_status omitido inicializa en UNVERIFIED
        self.assertEqual(card.verification_status, VerificationStatus.UNVERIFIED)
        self.assertEqual(data["verification_status"], "UNVERIFIED")
        self.assertEqual(restored_artwork.verification_status, VerificationStatus.UNVERIFIED)

        # verification_status=None en from_dict debe fallar
        dict_with_none_vs = dict(data)
        dict_with_none_vs["verification_status"] = None
        with self.assertRaises(ValidationError):
            Card.from_dict(dict_with_none_vs)

        # visual_effects=None en from_dict debe fallar
        dict_with_none_ve = dict(data)
        dict_with_none_ve["visual_effects"] = None
        with self.assertRaises(ValidationError):
            Card.from_dict(dict_with_none_ve)

    def test_25_rank_open_type_int_and_str(self):
        """Verifica que rank acepte tipo abierto (int y str) y rechace bool o None."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()

        # int válido
        kwargs["rank"] = 42
        c_int = Card(**kwargs)
        self.assertEqual(c_int.rank, 42)

        # str válido
        kwargs["rank"] = "Gran Maestro"
        c_str = Card(**kwargs)
        self.assertEqual(c_str.rank, "Gran Maestro")

        # None falla
        kwargs["rank"] = None
        with self.assertRaises(ValidationError):
            Card(**kwargs)

        # bool falla
        kwargs["rank"] = True
        with self.assertRaises(ValidationError):
            Card(**kwargs)

    def test_26_display_location_anti_gps_protection(self):
        """Verifica que display_location rechace coordenadas GPS exactas para salvaguardar privacidad."""
        from whoanimal.domain import Card
        from whoanimal.core.exceptions import ValidationError

        kwargs = self._make_valid_required_kwargs()

        # Ubicación general válida → PASS
        valid_locations = [
            "Parque Nacional de Doñana, España",
            "Selva Amazónica, Brasil",
            "Región Andina",
            None,
        ]
        for loc in valid_locations:
            with self.subTest(valid_loc=loc):
                kwargs["display_location"] = loc
                c = Card(**kwargs)
                self.assertEqual(c.display_location, loc)

        # Coordenadas exactas → FAIL
        forbidden_locations = [
            "40.416775, -3.703790",
            "-12.046374, -77.042793",
            "lat: 40.4168, lon: -3.7038",
            "latitude=51.5074, longitude=-0.1278",
        ]
        for forbidden in forbidden_locations:
            with self.subTest(forbidden_loc=forbidden):
                kwargs["display_location"] = forbidden
                with self.assertRaises(ValidationError):
                    Card(**kwargs)


if __name__ == "__main__":
    unittest.main()


