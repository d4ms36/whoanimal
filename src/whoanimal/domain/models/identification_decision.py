import re
import uuid
from datetime import datetime, timezone
from enum import Enum
from typing import Any, Dict, Iterable, List, Optional, Union

class DecisionState(str, Enum):
    ACCEPTED = "ACCEPTED"
    REJECTED = "REJECTED"
    CANCELLED = "CANCELLED"

class IdentificationDecisionError(Exception):
    pass

class IdentificationDecision:
    """
    IdentificationDecision represents an explicit decision made on an IdentificationResult.
    It separates raw confidence from explicit acceptance, rejection, or cancellation.
    """

    def __init__(self,
                 decision_id: str,
                 identification_id: str,
                 decision: Union[DecisionState, str],
                 decided_at: str,
                 selected_animal_id: Optional[str] = None,
                 candidates: Optional[Any] = None,
                 _bypass_candidate_check: bool = False,
                 **kwargs):

        self._validate_uuid4(decision_id, "decision_id")
        self._decision_id = decision_id

        # Support IdentificationResult passed directly as identification_id
        if hasattr(identification_id, "identification_id"):
            if candidates is None and hasattr(identification_id, "candidate_species"):
                candidates = getattr(identification_id, "candidate_species")
            identification_id = getattr(identification_id, "identification_id")

        self._validate_uuid4(identification_id, "identification_id")
        self._identification_id = identification_id

        self._decision = self._validate_decision(decision)

        if not isinstance(decided_at, str) or not decided_at.strip():
            raise IdentificationDecisionError("decided_at must be a non-empty string in UTC")
        self._validate_timestamp(decided_at)
        self._decided_at = decided_at

        # Consolidate candidate inputs
        if candidates is None:
            candidates = kwargs.get("candidate_species", kwargs.get("candidate_animal_ids"))

        self._selected_animal_id = self._validate_selection(
            decision=self._decision,
            selected_animal_id=selected_animal_id,
            candidates=candidates,
            bypass_candidate_check=_bypass_candidate_check
        )

    @property
    def decision_id(self) -> str:
        return self._decision_id

    @property
    def identification_id(self) -> str:
        return self._identification_id

    @property
    def decision(self) -> DecisionState:
        return self._decision

    @property
    def selected_animal_id(self) -> Optional[str]:
        return self._selected_animal_id

    @property
    def decided_at(self) -> str:
        return self._decided_at

    @staticmethod
    def _validate_uuid4(uuid_str: str, field_name: str) -> None:
        if not isinstance(uuid_str, str):
            raise IdentificationDecisionError(f"{field_name} must be a string")
        uuid4_pattern = re.compile(
            r'^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$',
            re.IGNORECASE
        )
        if not uuid4_pattern.match(uuid_str):
            raise IdentificationDecisionError(f"Invalid UUIDv4 format for {field_name}: {uuid_str}")

    @staticmethod
    def _validate_decision(decision: Union[DecisionState, str]) -> DecisionState:
        if isinstance(decision, DecisionState):
            return decision
        if isinstance(decision, str):
            try:
                return DecisionState(decision)
            except ValueError:
                raise IdentificationDecisionError(f"Invalid decision state: '{decision}'. Must be one of ACCEPTED, REJECTED, CANCELLED.")
        raise IdentificationDecisionError(f"decision must be DecisionState or str, got: {type(decision).__name__}")

    @staticmethod
    def _validate_timestamp(ts: str) -> None:
        try:
            parsed = datetime.fromisoformat(ts)
            if parsed.tzinfo is None:
                raise IdentificationDecisionError("decided_at must include timezone information (UTC)")
        except ValueError as e:
            raise IdentificationDecisionError(f"Invalid timestamp format for decided_at: {e}")

    @classmethod
    def _extract_candidate_ids(cls, candidates: Any) -> List[str]:
        if candidates is None:
            return []
        if hasattr(candidates, "candidate_species"):
            candidates = getattr(candidates, "candidate_species")

        extracted = []
        if isinstance(candidates, (list, tuple, set)):
            for item in candidates:
                if isinstance(item, str) and item.strip():
                    extracted.append(item.strip())
                elif isinstance(item, dict) and "animal_id" in item:
                    aid = item.get("animal_id")
                    if isinstance(aid, str) and aid.strip():
                        extracted.append(aid.strip())
        return extracted

    @classmethod
    def _validate_selection(cls,
                            decision: DecisionState,
                            selected_animal_id: Optional[str],
                            candidates: Optional[Any],
                            bypass_candidate_check: bool) -> Optional[str]:
        if decision == DecisionState.ACCEPTED:
            if not selected_animal_id or not isinstance(selected_animal_id, str) or not selected_animal_id.strip():
                raise IdentificationDecisionError("selected_animal_id is mandatory and must be a non-empty string for ACCEPTED decision")
            
            selected_animal_id = selected_animal_id.strip()

            if not bypass_candidate_check:
                if candidates is None:
                    raise IdentificationDecisionError(
                        "Candidates from IdentificationResult must be provided to validate selected_animal_id for ACCEPTED decision"
                    )
                candidate_ids = cls._extract_candidate_ids(candidates)
                if selected_animal_id not in candidate_ids:
                    raise IdentificationDecisionError(
                        f"selected_animal_id '{selected_animal_id}' does not exist among candidates: {candidate_ids}"
                    )
            return selected_animal_id
        else:
            # For REJECTED and CANCELLED, selected_animal_id is not mandatory
            if selected_animal_id is not None:
                if not isinstance(selected_animal_id, str):
                    raise IdentificationDecisionError("selected_animal_id must be a string or None")
                return selected_animal_id.strip() if selected_animal_id.strip() else None
            return None

    @classmethod
    def create(cls,
               identification: Union[str, Any],
               decision: Union[DecisionState, str],
               selected_animal_id: Optional[str] = None,
               candidates: Optional[Any] = None,
               **kwargs) -> 'IdentificationDecision':
        """Factory method to create a new IdentificationDecision with generated UUID and UTC timestamp."""
        return cls(
            decision_id=str(uuid.uuid4()),
            identification_id=identification,
            decision=decision,
            decided_at=datetime.now(timezone.utc).isoformat(),
            selected_animal_id=selected_animal_id,
            candidates=candidates,
            **kwargs
        )

    def to_dict(self) -> Dict[str, Any]:
        return {
            "decision_id": self._decision_id,
            "identification_id": self._identification_id,
            "selected_animal_id": self._selected_animal_id,
            "decision": self._decision.value,
            "decided_at": self._decided_at
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any], candidates: Optional[Any] = None) -> 'IdentificationDecision':
        try:
            return cls(
                decision_id=data["decision_id"],
                identification_id=data["identification_id"],
                decision=data["decision"],
                decided_at=data["decided_at"],
                selected_animal_id=data.get("selected_animal_id"),
                candidates=candidates,
                _bypass_candidate_check=(candidates is None)
            )
        except KeyError as e:
            raise IdentificationDecisionError(f"Missing required field: {e}")
