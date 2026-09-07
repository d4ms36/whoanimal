import unittest
import uuid
from datetime import datetime, timezone

from whoanimal.domain.models.observation import Observation, ObservationStatus
from whoanimal.domain.models.identification_result import IdentificationResult
from whoanimal.domain.models.identification_decision import IdentificationDecision, DecisionState
from whoanimal.domain.models.capture import Capture
from whoanimal.domain.models.card import AnimalCard, Card
from whoanimal.services.identification import (
    IdentificationService,
    IdentificationProvider,
    DeterministicIdentificationProvider,
    IdentificationServiceError,
    InvalidObservationError,
    IdentificationProviderError,
)


class TestIdentificationService(unittest.TestCase):
    """
    Tests obligatorios para WHO-016: Pipeline de Identificación Zoológica
    Observation → IdentificationService → IdentificationResult
    """

    def setUp(self):
        self.provider = DeterministicIdentificationProvider()
        self.service = IdentificationService(provider=self.provider)

    def test_case_01_valid_observation_produces_valid_result(self):
        """Caso 1: Observation válida → IdentificationResult válido."""
        obs = Observation.create(image_path="photos/canis_lupus_familiaris.jpg")
        result = self.service.identify(obs)

        self.assertIsInstance(result, IdentificationResult)
        self.assertTrue(result.identification_id)
        self.assertEqual(result.observation_id, obs.observation_id)
        self.assertEqual(result.identification_method, "DETERMINISTIC_ALPHA")
        self.assertIsInstance(result.candidate_species, list)
        self.assertGreater(len(result.candidate_species), 0)
        self.assertTrue(result.created_at)

    def test_case_02_candidates_only_contain_existing_catalog_species(self):
        """Caso 2: Resultado contiene únicamente candidatos existentes en el catálogo oficial."""
        obs = Observation.create(image_path="camera/shot_001.jpg")
        result = self.service.identify(obs)

        catalog_animal_ids = {
            entry["animal_id"] for entry in self.provider._catalog.values()
        }

        for candidate in result.candidate_species:
            candidate_id = candidate["animal_id"]
            self.assertIn(
                candidate_id,
                catalog_animal_ids,
                f"Candidate {candidate_id} must exist in official catalog",
            )
            confidence = candidate["confidence"]
            self.assertIsInstance(confidence, float)
            self.assertGreaterEqual(confidence, 0.0)
            self.assertLessEqual(confidence, 1.0)

    def test_case_03_candidate_ranking_is_deterministic_and_descending(self):
        """Caso 3: Orden de candidatos estrictamente determinista (confidence descendente)."""
        obs = Observation.create(image_path="photos/felis_catus.jpg")
        result_1 = self.service.identify(obs)
        result_2 = self.service.identify(obs)

        # Mismo input produce mismos candidatos en el mismo orden
        self.assertEqual(
            [c["animal_id"] for c in result_1.candidate_species],
            [c["animal_id"] for c in result_2.candidate_species],
        )
        self.assertEqual(
            [c["confidence"] for c in result_1.candidate_species],
            [c["confidence"] for c in result_2.candidate_species],
        )

        # Orden estrictamente decreciente
        confidences = [c["confidence"] for c in result_1.candidate_species]
        self.assertEqual(confidences, sorted(confidences, reverse=True))

    def test_case_04_invalid_observation_raises_controlled_error(self):
        """Caso 4: Observation inválida (None, tipo erróneo, id corrupto) → error controlado."""
        # None
        with self.assertRaises(InvalidObservationError):
            self.service.identify(None)

        # Tipo incorrecto
        with self.assertRaises(InvalidObservationError):
            self.service.identify("not_an_observation")

        # Objeto sin observation_id válido
        class MockCorruptObservation:
            observation_id = ""
        with self.assertRaises(InvalidObservationError):
            self.service.identify(MockCorruptObservation())

    def test_case_05_provider_without_result_raises_controlled_error(self):
        """Caso 5: Provider sin resultado / catálogo vacío → estado/error correcto."""
        empty_provider = DeterministicIdentificationProvider(custom_catalog={})
        service_empty = IdentificationService(provider=empty_provider)

        obs = Observation.create(image_path="test.jpg")
        with self.assertRaises(IdentificationProviderError):
            service_empty.identify(obs)

    def test_case_06_no_capture_created_automatically(self):
        """Caso 6: No se crea Capture automáticamente como efecto secundario."""
        obs = Observation.create(image_path="photos/ara_macao.jpg")
        result = self.service.identify(obs)

        # El resultado no es Capture
        self.assertFalse(isinstance(result, Capture))
        self.assertFalse(hasattr(result, "capture_id"))
        self.assertFalse(hasattr(result, "captured_at"))

    def test_case_07_no_card_created_automatically(self):
        """Caso 7: No se crea Card automáticamente."""
        obs = Observation.create(image_path="photos/panthera_onca.jpg")
        result = self.service.identify(obs)

        # El resultado no es Card ni AnimalCard
        self.assertFalse(isinstance(result, AnimalCard))
        self.assertFalse(isinstance(result, Card))
        self.assertFalse(hasattr(result, "card_id"))
        self.assertFalse(hasattr(result, "auth_serial"))
        self.assertFalse(hasattr(result, "generation"))

    def test_case_08_serialization_and_deserialization_fidelity(self):
        """Caso 8: El resultado puede serializarse y deserializarse conservando su semántica."""
        obs = Observation.create(image_path="photos/canis_lupus_familiaris.jpg")
        result = self.service.identify(obs)

        as_dict = result.to_dict()
        self.assertIn("identification_id", as_dict)
        self.assertIn("observation_id", as_dict)
        self.assertIn("candidate_species", as_dict)
        self.assertIn("identification_method", as_dict)
        self.assertIn("created_at", as_dict)

        restored = IdentificationResult.from_dict(as_dict)
        self.assertEqual(restored.identification_id, result.identification_id)
        self.assertEqual(restored.observation_id, result.observation_id)
        self.assertEqual(restored.candidate_species, result.candidate_species)
        self.assertEqual(restored.identification_method, result.identification_method)

    def test_case_09_result_prepares_decision_boundary(self):
        """Verifica que IdentificationResult se integra limpiamente con IdentificationDecision."""
        obs = Observation.create(image_path="photos/canis_lupus_familiaris.jpg")
        result = self.service.identify(obs)

        top_candidate_id = result.candidate_species[0]["animal_id"]

        # Crear decisión explícita ACCEPTED sobre el resultado sin crear Capture automáticamente
        decision = IdentificationDecision(
            decision_id=str(uuid.uuid4()),
            identification_id=result.identification_id,
            decision=DecisionState.ACCEPTED,
            decided_at=datetime.now(timezone.utc).isoformat(),
            selected_animal_id=top_candidate_id,
            candidates=result.candidate_species,
        )

        self.assertEqual(decision.decision, DecisionState.ACCEPTED)
        self.assertEqual(decision.selected_animal_id, top_candidate_id)
        self.assertEqual(decision.identification_id, result.identification_id)


if __name__ == "__main__":
    unittest.main()
