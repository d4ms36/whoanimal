import unittest
import uuid
from datetime import datetime, timezone
from whoanimal.domain.models.identification_result import IdentificationResult
from whoanimal.domain.models.identification_decision import (
    IdentificationDecision,
    DecisionState,
    IdentificationDecisionError
)

class TestIdentificationDecision(unittest.TestCase):

    def setUp(self):
        self.decision_id = str(uuid.uuid4())
        self.identification_id = str(uuid.uuid4())
        self.animal_1 = "canis_lupus_familiaris"
        self.animal_2 = "felis_catus"
        self.valid_candidates = [
            {"animal_id": self.animal_1, "confidence": 0.95},
            {"animal_id": self.animal_2, "confidence": 0.05}
        ]
        self.time = datetime.now(timezone.utc).isoformat()
        self.result = IdentificationResult(
            identification_id=self.identification_id,
            observation_id=str(uuid.uuid4()),
            candidate_species=self.valid_candidates,
            identification_method="mock_classifier",
            created_at=self.time
        )

    # 1. Identidad
    def test_01_valid_creation(self):
        decision = IdentificationDecision(
            decision_id=self.decision_id,
            identification_id=self.identification_id,
            decision=DecisionState.ACCEPTED,
            decided_at=self.time,
            selected_animal_id=self.animal_1,
            candidates=self.valid_candidates
        )
        self.assertEqual(decision.decision_id, self.decision_id)
        self.assertEqual(decision.identification_id, self.identification_id)
        self.assertEqual(decision.decision, DecisionState.ACCEPTED)
        self.assertEqual(decision.selected_animal_id, self.animal_1)
        self.assertEqual(decision.decided_at, self.time)

    def test_02_invalid_uuid_decision_id(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "Invalid UUIDv4 format for decision_id"):
            IdentificationDecision(
                decision_id="not-a-valid-uuid",
                identification_id=self.identification_id,
                decision=DecisionState.REJECTED,
                decided_at=self.time
            )

    def test_03_invalid_uuid_identification_id(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "Invalid UUIDv4 format for identification_id"):
            IdentificationDecision(
                decision_id=self.decision_id,
                identification_id="bad-uuid",
                decision=DecisionState.REJECTED,
                decided_at=self.time
            )

    def test_04_immutability(self):
        decision = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_1
        )
        with self.assertRaises(AttributeError):
            decision.decision_id = str(uuid.uuid4())
        with self.assertRaises(AttributeError):
            decision.identification_id = str(uuid.uuid4())
        with self.assertRaises(AttributeError):
            decision.decided_at = datetime.now(timezone.utc).isoformat()
        with self.assertRaises(AttributeError):
            decision.decision = DecisionState.REJECTED
        with self.assertRaises(AttributeError):
            decision.selected_animal_id = self.animal_2

    # 2. Decision states
    def test_05_decision_states_accepted(self):
        d = IdentificationDecision.create(
            identification=self.identification_id,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_1,
            candidates=self.valid_candidates
        )
        self.assertEqual(d.decision, DecisionState.ACCEPTED)
        self.assertEqual(d.decision.value, "ACCEPTED")

    def test_06_decision_states_rejected(self):
        d = IdentificationDecision.create(
            identification=self.identification_id,
            decision="REJECTED"
        )
        self.assertEqual(d.decision, DecisionState.REJECTED)
        self.assertIsNone(d.selected_animal_id)

    def test_07_decision_states_cancelled(self):
        d = IdentificationDecision.create(
            identification=self.identification_id,
            decision=DecisionState.CANCELLED
        )
        self.assertEqual(d.decision, DecisionState.CANCELLED)
        self.assertIsNone(d.selected_animal_id)

    def test_08_unknown_decision_state_rejected(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "Invalid decision state"):
            IdentificationDecision.create(
                identification=self.identification_id,
                decision="CONFIRMED"
            )

    # 3. ACCEPTED requirements and candidate verification
    def test_09_accepted_requires_selected_animal_id(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "selected_animal_id is mandatory"):
            IdentificationDecision.create(
                identification=self.result,
                decision=DecisionState.ACCEPTED,
                selected_animal_id=None
            )
        with self.assertRaisesRegex(IdentificationDecisionError, "selected_animal_id is mandatory"):
            IdentificationDecision.create(
                identification=self.result,
                decision=DecisionState.ACCEPTED,
                selected_animal_id="   "
            )

    def test_10_accepted_with_candidate_present(self):
        decision = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_2
        )
        self.assertEqual(decision.selected_animal_id, self.animal_2)

    def test_11_accepted_with_animal_not_in_candidates(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "does not exist among candidates"):
            IdentificationDecision.create(
                identification=self.result,
                decision=DecisionState.ACCEPTED,
                selected_animal_id="panthera_onca"
            )

    def test_12_accepted_without_candidates_fails(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "Candidates from IdentificationResult must be provided"):
            IdentificationDecision.create(
                identification=self.identification_id,
                decision=DecisionState.ACCEPTED,
                selected_animal_id=self.animal_1,
                candidates=None
            )

    # 4. REJECTED behavior
    def test_13_rejected_without_selected_animal(self):
        d = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.REJECTED
        )
        self.assertIsNone(d.selected_animal_id)
        # Verify no Capture or Card properties exist
        self.assertFalse(hasattr(d, "capture"))
        self.assertFalse(hasattr(d, "card"))
        self.assertFalse(hasattr(d, "capture_id"))
        self.assertFalse(hasattr(d, "card_id"))

    # 5. CANCELLED behavior
    def test_14_cancelled_without_selected_animal(self):
        d = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.CANCELLED
        )
        self.assertIsNone(d.selected_animal_id)
        # Verify no Capture or Card properties exist
        self.assertFalse(hasattr(d, "capture"))
        self.assertFalse(hasattr(d, "card"))
        self.assertFalse(hasattr(d, "capture_id"))
        self.assertFalse(hasattr(d, "card_id"))

    # 6. Independence of Confidence
    def test_15_high_confidence_does_not_auto_accept(self):
        # Even with 0.999 confidence, creating an IdentificationResult never creates a decision
        high_conf_result = IdentificationResult.create(
            observation_id=str(uuid.uuid4()),
            candidate_species=[{"animal_id": self.animal_1, "confidence": 0.999}],
            identification_method="mock"
        )
        # Decision must be explicitly created by the caller
        self.assertFalse(hasattr(high_conf_result, "decision"))
        # An explicit decision can still REJECT a high confidence result
        decision = IdentificationDecision.create(
            identification=high_conf_result,
            decision=DecisionState.REJECTED
        )
        self.assertEqual(decision.decision, DecisionState.REJECTED)

    def test_16_low_confidence_does_not_auto_reject(self):
        # Even with very low confidence, caller can explicitly ACCEPT if confirmed
        low_conf_result = IdentificationResult.create(
            observation_id=str(uuid.uuid4()),
            candidate_species=[{"animal_id": self.animal_1, "confidence": 0.05}],
            identification_method="mock"
        )
        decision = IdentificationDecision.create(
            identification=low_conf_result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_1
        )
        self.assertEqual(decision.decision, DecisionState.ACCEPTED)
        self.assertEqual(decision.selected_animal_id, self.animal_1)

    def test_17_no_acceptance_threshold(self):
        decision = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_1
        )
        self.assertFalse(hasattr(decision, "threshold"))
        self.assertFalse(hasattr(decision, "confidence"))

    # 7. No inference
    def test_18_no_default_or_inferred_candidate(self):
        # If ACCEPTED is requested without passing selected_animal_id, it must NOT pick the first or highest candidate
        with self.assertRaises(IdentificationDecisionError):
            IdentificationDecision.create(
                identification=self.result,
                decision=DecisionState.ACCEPTED
            )

    # 8. Timestamp
    def test_19_timestamp_required_and_utc(self):
        with self.assertRaisesRegex(IdentificationDecisionError, "decided_at must be a non-empty string in UTC"):
            IdentificationDecision(
                decision_id=self.decision_id,
                identification_id=self.identification_id,
                decision=DecisionState.REJECTED,
                decided_at="   "
            )
        with self.assertRaisesRegex(IdentificationDecisionError, "decided_at must include timezone information"):
            IdentificationDecision(
                decision_id=self.decision_id,
                identification_id=self.identification_id,
                decision=DecisionState.REJECTED,
                decided_at="2026-09-07T12:00:00"  # Missing timezone
            )

    # 9. Serialization
    def test_20_serialization_to_dict(self):
        decision = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_1
        )
        data = decision.to_dict()
        self.assertEqual(data["decision_id"], decision.decision_id)
        self.assertEqual(data["identification_id"], self.result.identification_id)
        self.assertEqual(data["decision"], "ACCEPTED")
        self.assertEqual(data["selected_animal_id"], self.animal_1)
        self.assertEqual(data["decided_at"], decision.decided_at)
        # Ensure no unexpected keys
        self.assertEqual(set(data.keys()), {"decision_id", "identification_id", "selected_animal_id", "decision", "decided_at"})

    def test_21_deserialization_from_dict(self):
        data = {
            "decision_id": self.decision_id,
            "identification_id": self.identification_id,
            "decision": "ACCEPTED",
            "selected_animal_id": self.animal_1,
            "decided_at": self.time
        }
        decision = IdentificationDecision.from_dict(data)
        self.assertEqual(decision.decision_id, self.decision_id)
        self.assertEqual(decision.identification_id, self.identification_id)
        self.assertEqual(decision.decision, DecisionState.ACCEPTED)
        self.assertEqual(decision.selected_animal_id, self.animal_1)

    def test_22_round_trip_serialization(self):
        original = IdentificationDecision.create(
            identification=self.result,
            decision=DecisionState.ACCEPTED,
            selected_animal_id=self.animal_2
        )
        serialized = original.to_dict()
        restored = IdentificationDecision.from_dict(serialized)
        self.assertEqual(original.decision_id, restored.decision_id)
        self.assertEqual(original.identification_id, restored.identification_id)
        self.assertEqual(original.decision, restored.decision)
        self.assertEqual(original.selected_animal_id, restored.selected_animal_id)
        self.assertEqual(original.decided_at, restored.decided_at)

    def test_23_from_dict_missing_fields(self):
        incomplete_data = {
            "decision_id": self.decision_id,
            "identification_id": self.identification_id
        }
        with self.assertRaisesRegex(IdentificationDecisionError, "Missing required field"):
            IdentificationDecision.from_dict(incomplete_data)

    # 10. String candidates support in validation
    def test_24_candidates_as_list_of_strings(self):
        decision = IdentificationDecision.create(
            identification=self.identification_id,
            decision="ACCEPTED",
            selected_animal_id="panthera_leo",
            candidates=["canis_lupus", "panthera_leo"]
        )
        self.assertEqual(decision.selected_animal_id, "panthera_leo")

if __name__ == '__main__':
    unittest.main()
