import unittest
import uuid
from datetime import datetime, timezone
from whoanimal.domain.models.observation import Observation, ObservationStatus, ObservationError

class TestObservation(unittest.TestCase):

    def setUp(self):
        self.valid_id = str(uuid.uuid4())
        self.valid_time = datetime.now(timezone.utc).isoformat()
        self.valid_path = "/path/to/image.jpg"

    def test_create_valid_observation(self):
        obs = Observation(
            observation_id=self.valid_id,
            created_at=self.valid_time,
            image_path=self.valid_path,
            status=ObservationStatus.PENDING
        )
        self.assertEqual(obs.observation_id, self.valid_id)
        self.assertEqual(obs.created_at, self.valid_time)
        self.assertEqual(obs.image_path, self.valid_path)
        self.assertEqual(obs.status, ObservationStatus.PENDING)
        self.assertEqual(obs.candidate_species, [])
        self.assertIsNone(obs.confidence)
        
    def test_factory_create(self):
        obs = Observation.create(self.valid_path)
        self.assertEqual(obs.image_path, self.valid_path)
        self.assertEqual(obs.status, ObservationStatus.PENDING)
        self.assertTrue(isinstance(obs.observation_id, str))

    def test_invalid_uuid(self):
        with self.assertRaisesRegex(ObservationError, "Invalid UUIDv4 format"):
            Observation(
                observation_id="invalid-uuid",
                created_at=self.valid_time,
                image_path=self.valid_path,
                status=ObservationStatus.PENDING
            )

    def test_confidence_out_of_range(self):
        with self.assertRaisesRegex(ObservationError, "must be between 0.0 and 1.0"):
            Observation(
                observation_id=self.valid_id,
                created_at=self.valid_time,
                image_path=self.valid_path,
                status=ObservationStatus.PENDING,
                confidence=1.5
            )
            
        with self.assertRaisesRegex(ObservationError, "must be between 0.0 and 1.0"):
            Observation(
                observation_id=self.valid_id,
                created_at=self.valid_time,
                image_path=self.valid_path,
                status=ObservationStatus.PENDING,
                confidence=-0.1
            )

    def test_invalid_candidate_species_list(self):
        with self.assertRaisesRegex(ObservationError, "must be a list"):
            Observation(
                observation_id=self.valid_id,
                created_at=self.valid_time,
                image_path=self.valid_path,
                status=ObservationStatus.PENDING,
                candidate_species="Panthera onca"
            )

    def test_serialization(self):
        obs = Observation(
            observation_id=self.valid_id,
            created_at=self.valid_time,
            image_path=self.valid_path,
            status=ObservationStatus.IDENTIFIED,
            candidate_species=["Panthera onca"],
            confidence=0.95
        )
        data = obs.to_dict()
        self.assertEqual(data["observation_id"], self.valid_id)
        self.assertEqual(data["status"], "IDENTIFIED")
        self.assertEqual(data["confidence"], 0.95)
        
        # Test missing confidence is omitted
        obs_none = Observation.create(self.valid_path)
        data_none = obs_none.to_dict()
        self.assertNotIn("confidence", data_none)

    def test_deserialization(self):
        data = {
            "observation_id": self.valid_id,
            "created_at": self.valid_time,
            "image_path": self.valid_path,
            "status": "PROCESSING",
            "candidate_species": ["Panthera onca", "Felis catus"],
            "confidence": 0.88
        }
        obs = Observation.from_dict(data)
        self.assertEqual(obs.observation_id, self.valid_id)
        self.assertEqual(obs.status, ObservationStatus.PROCESSING)
        self.assertEqual(obs.confidence, 0.88)
        self.assertEqual(len(obs.candidate_species), 2)
        
        # Test deserialization with missing confidence
        data_missing = data.copy()
        del data_missing["confidence"]
        obs_missing = Observation.from_dict(data_missing)
        self.assertIsNone(obs_missing.confidence)

    def test_immutability(self):
        obs = Observation.create(self.valid_path)
        with self.assertRaises(AttributeError):
            obs.observation_id = str(uuid.uuid4())
        with self.assertRaises(AttributeError):
            obs.created_at = datetime.now(timezone.utc).isoformat()

if __name__ == '__main__':
    unittest.main()
