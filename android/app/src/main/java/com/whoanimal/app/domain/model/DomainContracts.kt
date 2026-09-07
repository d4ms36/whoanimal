package com.whoanimal.app.domain.model

/**
 * Contratos y DTOs de dominio inmutables para WHO Animal Android.
 *
 * Reflejan estrictamente la ontología y separación conceptual establecida en el proyecto:
 * Observation -> IdentificationResult -> IdentificationDecision (ACCEPTED) -> Capture -> Card
 *
 * Reglas fundamentales:
 * - Capture != Card (separación ontológica estricta).
 * - Observation != IdentificationResult != IdentificationDecision.
 * - Capture.sex in { MALE, FEMALE, UNKNOWN } (sex no pertenece a Animal).
 * - Serial sin telemetría GPS exacta ni datos privados.
 */

enum class DecisionStatus {
    ACCEPTED,
    REJECTED,
    CANCELLED
}

enum class BiologicalSex {
    MALE,
    FEMALE,
    UNKNOWN
}

enum class VerificationStatus {
    UNVERIFIED,
    VERIFIED,
    FLAGGED,
    REVOKED
}

data class TaxonomyContract(
    val kingdom: String = "Animalia",
    val phylum: String,
    val className: String,
    val order: String,
    val family: String,
    val genus: String,
    val species: String
)

data class AnimalProfileContract(
    val animalId: String,
    val scientificName: String,
    val commonName: String,
    val taxonomy: TaxonomyContract,
    val conservationStatus: String = "LC",
    val isRareSpecies: Boolean = false
)

data class ObservationContract(
    val observationId: String,
    val capturedAt: String,
    val imageUri: String,
    val notes: String? = null
)

data class IdentificationResultContract(
    val identificationId: String,
    val observationId: String,
    val candidateAnimalId: String,
    val candidateScientificName: String,
    val confidence: Double
)

data class IdentificationDecisionContract(
    val decisionId: String,
    val identificationId: String,
    val animalId: String,
    val status: DecisionStatus,
    val decidedAt: String
)

data class CaptureContract(
    val captureId: String,
    val animalId: String,
    val identificationId: String,
    val sex: BiologicalSex = BiologicalSex.UNKNOWN,
    val capturedAt: String,
    val displayLocation: String = "General Location"
)

data class AnimalCardContract(
    val cardId: String,
    val animalId: String,
    val specimenNumber: Int,
    val schemaVersion: String = "1.0",
    val generation: String = "genesis",
    val issuedAt: String,
    val populationAtIssuance: Int,
    val rarity: String,
    val captureId: String,
    val serial: String,
    val verificationStatus: VerificationStatus = VerificationStatus.UNVERIFIED,
    val identificationMethod: String = "VISUAL_AI",
    val identificationConfidence: Double? = null,
    val rank: Int = 1,
    val displayLocation: String = "General Location",
    val ownerId: String? = null,
    val edition: String? = null
)
