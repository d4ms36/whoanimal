import re
import uuid
from datetime import datetime, timezone
from typing import List, Dict, Any

class IdentificationResultError(Exception):
    pass

class IdentificationResult:
    """
    IdentificationResult represents the result produced by an identification engine on an Observation.
    It separates raw confidence from final acceptance.
    """
    
    def __init__(self,
                 identification_id: str,
                 observation_id: str,
                 candidate_species: List[Dict[str, Any]],
                 identification_method: str,
                 created_at: str):
                 
        self._validate_uuid4(identification_id, "identification_id")
        self._identification_id = identification_id
        
        self._validate_uuid4(observation_id, "observation_id")
        self._observation_id = observation_id
        
        self._validate_candidates(candidate_species)
        self.candidate_species = candidate_species
        
        if not isinstance(identification_method, str) or not identification_method.strip():
            raise IdentificationResultError("identification_method must be a non-empty string")
        self.identification_method = identification_method
        
        if not isinstance(created_at, str) or not created_at.strip():
            raise IdentificationResultError("created_at must be a non-empty string in UTC")
        self._created_at = created_at

    @property
    def identification_id(self) -> str:
        return self._identification_id
        
    @property
    def observation_id(self) -> str:
        return self._observation_id
        
    @property
    def created_at(self) -> str:
        return self._created_at

    @staticmethod
    def _validate_uuid4(uuid_str: str, field_name: str) -> None:
        if not isinstance(uuid_str, str):
            raise IdentificationResultError(f"{field_name} must be a string")
        uuid4_pattern = re.compile(
            r'^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$', 
            re.IGNORECASE
        )
        if not uuid4_pattern.match(uuid_str):
            raise IdentificationResultError(f"Invalid UUIDv4 format for {field_name}: {uuid_str}")

    @staticmethod
    def _validate_candidates(candidates: List[Dict[str, Any]]) -> None:
        if not isinstance(candidates, list):
            raise IdentificationResultError("candidate_species must be a list")
        if not candidates:
            raise IdentificationResultError("candidate_species list cannot be empty")
            
        seen_animal_ids = set()
        for i, candidate in enumerate(candidates):
            if not isinstance(candidate, dict):
                raise IdentificationResultError(f"Candidate at index {i} must be a dictionary")
                
            animal_id = candidate.get("animal_id")
            if not animal_id or not isinstance(animal_id, str):
                raise IdentificationResultError(f"Candidate at index {i} is missing a valid animal_id")
                
            if animal_id in seen_animal_ids:
                raise IdentificationResultError(f"Duplicate animal_id found in candidates: {animal_id}")
            seen_animal_ids.add(animal_id)
            
            confidence = candidate.get("confidence")
            if confidence is None:
                raise IdentificationResultError(f"Candidate at index {i} is missing confidence")
            if not isinstance(confidence, (int, float)) or isinstance(confidence, bool):
                raise IdentificationResultError(f"Candidate at index {i} confidence must be a float")
            if not (0.0 <= confidence <= 1.0):
                raise IdentificationResultError(f"Candidate at index {i} confidence must be between 0.0 and 1.0")

    @classmethod
    def create(cls, observation_id: str, candidate_species: List[Dict[str, Any]], identification_method: str) -> 'IdentificationResult':
        """Factory method to create a new IdentificationResult."""
        return cls(
            identification_id=str(uuid.uuid4()),
            observation_id=observation_id,
            candidate_species=candidate_species,
            identification_method=identification_method,
            created_at=datetime.now(timezone.utc).isoformat()
        )

    def to_dict(self) -> Dict[str, Any]:
        return {
            "identification_id": self._identification_id,
            "observation_id": self._observation_id,
            "candidate_species": self.candidate_species,
            "identification_method": self.identification_method,
            "created_at": self._created_at
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'IdentificationResult':
        try:
            return cls(
                identification_id=data["identification_id"],
                observation_id=data["observation_id"],
                candidate_species=data["candidate_species"],
                identification_method=data["identification_method"],
                created_at=data["created_at"]
            )
        except KeyError as e:
            raise IdentificationResultError(f"Missing required field: {e}")
