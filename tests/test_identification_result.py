import unittest
import uuid
from datetime import datetime, timezone
from whoanimal.domain.models.identification_result import IdentificationResult, IdentificationResultError

class TestIdentificationResult(unittest.TestCase):

    def setUp(self):
        self.ident_id = str(uuid.uuid4())
        self.obs_id = str(uuid.uuid4())
        self.animal_1 = str(uuid.uuid4())
        self.animal_2 = str(uuid.uuid4())
        self.valid_candidates = [
            {"animal_id": self.animal_1, "confidence": 0.95},
            {"animal_id": self.animal_2, "confidence": 0.60}
        ]
        self.method = "mock"
        self.time = datetime.now(timezone.utc).isoformat()

    def test_01_valid_creation(self):
        result = IdentificationResult(
            identification_id=self.ident_id,
            observation_id=self.obs_id,
            candidate_species=self.valid_candidates,
            identification_method=self.method,
            created_at=self.time
        )
        self.assertEqual(result.identification_id, self.ident_id)
        self.assertEqual(result.observation_id, self.obs_id)
        self.assertEqual(len(result.candidate_species), 2)
        self.assertEqual(result.identification_method, self.method)
        self.assertEqual(result.created_at, self.time)

    def test_02_invalid_uuid_ident(self):
        with self.assertRaisesRegex(IdentificationResultError, "Invalid UUIDv4 format for identification_id"):
            IdentificationResult("invalid", self.obs_id, self.valid_candidates, self.method, self.time)

    def test_03_invalid_uuid_obs(self):
        with self.assertRaisesRegex(IdentificationResultError, "Invalid UUIDv4 format for observation_id"):
            IdentificationResult(self.ident_id, "invalid", self.valid_candidates, self.method, self.time)

    def test_04_empty_candidates(self):
        with self.assertRaisesRegex(IdentificationResultError, "candidate_species list cannot be empty"):
            IdentificationResult(self.ident_id, self.obs_id, [], self.method, self.time)

    def test_05_confidence_less_than_0(self):
        invalid_candidates = [{"animal_id": self.animal_1, "confidence": -0.1}]
        with self.assertRaisesRegex(IdentificationResultError, "confidence must be between 0.0 and 1.0"):
            IdentificationResult(self.ident_id, self.obs_id, invalid_candidates, self.method, self.time)

    def test_06_confidence_greater_than_1(self):
        invalid_candidates = [{"animal_id": self.animal_1, "confidence": 1.1}]
        with self.assertRaisesRegex(IdentificationResultError, "confidence must be between 0.0 and 1.0"):
            IdentificationResult(self.ident_id, self.obs_id, invalid_candidates, self.method, self.time)

    def test_07_candidate_without_animal_id(self):
        invalid_candidates = [{"confidence": 0.9}]
        with self.assertRaisesRegex(IdentificationResultError, "missing a valid animal_id"):
            IdentificationResult(self.ident_id, self.obs_id, invalid_candidates, self.method, self.time)

    def test_08_duplicate_candidate(self):
        invalid_candidates = [
            {"animal_id": self.animal_1, "confidence": 0.9},
            {"animal_id": self.animal_1, "confidence": 0.8}
        ]
        with self.assertRaisesRegex(IdentificationResultError, "Duplicate animal_id found"):
            IdentificationResult(self.ident_id, self.obs_id, invalid_candidates, self.method, self.time)

    def test_09_empty_identification_method(self):
        with self.assertRaisesRegex(IdentificationResultError, "must be a non-empty string"):
            IdentificationResult(self.ident_id, self.obs_id, self.valid_candidates, "   ", self.time)

    def test_10_serialization(self):
        result = IdentificationResult.create(self.obs_id, self.valid_candidates, self.method)
        data = result.to_dict()
        self.assertEqual(data["observation_id"], self.obs_id)
        self.assertEqual(data["identification_method"], self.method)
        self.assertEqual(data["candidate_species"][0]["animal_id"], self.animal_1)

    def test_11_deserialization(self):
        data = {
            "identification_id": self.ident_id,
            "observation_id": self.obs_id,
            "candidate_species": self.valid_candidates,
            "identification_method": "future_ai",
            "created_at": self.time
        }
        result = IdentificationResult.from_dict(data)
        self.assertEqual(result.identification_id, self.ident_id)
        self.assertEqual(result.identification_method, "future_ai")

    def test_12_immutability(self):
        result = IdentificationResult.create(self.obs_id, self.valid_candidates, self.method)
        with self.assertRaises(AttributeError):
            result.identification_id = str(uuid.uuid4())
        with self.assertRaises(AttributeError):
            result.observation_id = str(uuid.uuid4())
        with self.assertRaises(AttributeError):
            result.created_at = datetime.now(timezone.utc).isoformat()

    def test_13_conserves_order(self):
        # We supply out of order confidences intentionally
        candidates = [
            {"animal_id": self.animal_2, "confidence": 0.1},
            {"animal_id": self.animal_1, "confidence": 0.9}
        ]
        result = IdentificationResult.create(self.obs_id, candidates, self.method)
        self.assertEqual(result.candidate_species[0]["animal_id"], self.animal_2)
        self.assertEqual(result.candidate_species[1]["animal_id"], self.animal_1)

    def test_14_confidence_vs_acceptance(self):
        # The object should not have any accepted or rejected field.
        result = IdentificationResult.create(self.obs_id, self.valid_candidates, self.method)
        with self.assertRaises(AttributeError):
            _ = result.accepted
        with self.assertRaises(AttributeError):
            _ = result.verified

if __name__ == '__main__':
    unittest.main()
