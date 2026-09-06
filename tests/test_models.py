"""
Pruebas de modelos de dominio y validación de reglas de producto.
Compatible tanto con unittest (stdlib) como con pytest.
"""

import unittest
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


if __name__ == "__main__":
    unittest.main()
