from enum import Enum
import uuid
import re
from typing import List, Optional, Any, Dict
from datetime import datetime, timezone

class ObservationStatus(Enum):
    PENDING = "PENDING"
    PROCESSING = "PROCESSING"
    IDENTIFIED = "IDENTIFIED"
    REJECTED = "REJECTED"

class ObservationError(Exception):
    pass

class Observation:
    """
    Observation represents a pending observation bridging a photograph and a Capture.
    It is an ephemeral entity that doesn't create a Capture or AnimalProfile automatically.
    """

    def __init__(self, 
                 observation_id: str, 
                 created_at: str, 
                 image_path: str,
                 status: ObservationStatus,
                 candidate_species: List[str] = None,
                 confidence: Optional[float] = None):
        
        self._validate_uuid4(observation_id)
        self._observation_id = observation_id
        
        if not isinstance(created_at, str) or not created_at.strip():
            raise ObservationError("created_at must be a non-empty string in UTC")
        self._created_at = created_at
        
        if not isinstance(image_path, str) or not image_path.strip():
            raise ObservationError("image_path must be a non-empty string")
        self.image_path = image_path
        
        if not isinstance(status, ObservationStatus):
            raise ObservationError("status must be an instance of ObservationStatus")
        self.status = status
        
        if candidate_species is None:
            self.candidate_species = []
        elif isinstance(candidate_species, list):
            self.candidate_species = candidate_species
        else:
            raise ObservationError("candidate_species must be a list")
            
        if confidence is not None:
            if not isinstance(confidence, (int, float)) or isinstance(confidence, bool):
                raise ObservationError("confidence must be a float")
            if not (0.0 <= confidence <= 1.0):
                raise ObservationError("confidence must be between 0.0 and 1.0")
        self.confidence = float(confidence) if confidence is not None else None

    @property
    def observation_id(self) -> str:
        return self._observation_id

    @property
    def created_at(self) -> str:
        return self._created_at

    @staticmethod
    def _validate_uuid4(uuid_str: str) -> None:
        if not isinstance(uuid_str, str):
            raise ObservationError("UUID must be a string")
        uuid4_pattern = re.compile(
            r'^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$', 
            re.IGNORECASE
        )
        if not uuid4_pattern.match(uuid_str):
            raise ObservationError(f"Invalid UUIDv4 format: {uuid_str}")

    @classmethod
    def create(cls, image_path: str) -> 'Observation':
        """Factory method to create a new PENDING Observation."""
        return cls(
            observation_id=str(uuid.uuid4()),
            created_at=datetime.now(timezone.utc).isoformat(),
            image_path=image_path,
            status=ObservationStatus.PENDING,
            candidate_species=[],
            confidence=None
        )

    def to_dict(self) -> Dict[str, Any]:
        result = {
            "observation_id": self._observation_id,
            "created_at": self._created_at,
            "image_path": self.image_path,
            "status": self.status.value,
            "candidate_species": self.candidate_species
        }
        # Only include confidence if it is not None
        if self.confidence is not None:
            result["confidence"] = self.confidence
        return result

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Observation':
        try:
            return cls(
                observation_id=data["observation_id"],
                created_at=data["created_at"],
                image_path=data["image_path"],
                status=ObservationStatus(data["status"]),
                candidate_species=data.get("candidate_species", []),
                confidence=data.get("confidence")
            )
        except ValueError as e:
             raise ObservationError(f"Invalid status value: {e}")
        except KeyError as e:
            raise ObservationError(f"Missing required field: {e}")
