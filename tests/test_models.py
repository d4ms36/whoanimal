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
            id="animal_vulpes_vulpes",
            category=AnimalCategory.MAMMAL,
            scientific_info=sci_info,
            conservation=indicators,
            danger=danger,
            curiosities=[curiosity],
        )

        self.assertEqual(profile.scientific_info.common_name, "Zorro Rojo")
        self.assertEqual(profile.scientific_info.scientific_name, "Vulpes vulpes")
        self.assertFalse(profile.conservation.is_protected)
        self.assertTrue(profile.danger.has_warning)
        self.assertEqual(len(profile.curiosities), 1)

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
            id="animal_bubo_bubo",
            category=AnimalCategory.BIRD,
            scientific_info=sci_info,
            conservation=ConservationIndicators(is_protected=True, is_rare_species=False),
            danger=DangerAssessment(level=DangerLevel.NONE),
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
        self.assertEqual(card.back.profile.scientific_info.scientific_name, "Bubo bubo")
        self.assertTrue(card.back.profile.conservation.is_protected)
        self.assertFalse(card.back.profile.danger.has_warning)
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

        card = Card(**self._make_valid_required_kwargs())
        data = card.to_dict()
        self.assertIsInstance(data, dict)
        self.assertEqual(len(data), 19)
        self.assertEqual(data["card_id"], "c84d7285-807e-468a-b5a8-ef075a3e1201")
        self.assertEqual(data["verification_status"], "UNVERIFIED")

        # Reconstrucción mediante from_dict
        restored = Card.from_dict(data)
        self.assertEqual(restored.card_id, card.card_id)
        self.assertEqual(restored.verification_status, VerificationStatus.UNVERIFIED)


if __name__ == "__main__":
    unittest.main()

