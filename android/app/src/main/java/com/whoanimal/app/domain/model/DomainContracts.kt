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
    val isRareSpecies: Boolean = false,
    val habitat: String? = null,
    val diet: String? = null,
    val lifespanYears: Int? = null,
    val sizeCm: Int? = null,
    val weightKg: Double? = null,
    val activityCycle: String? = null,
    val nativeRegions: List<String> = emptyList(),
    val curiosity: String? = null,
    val dangerLevel: String? = null
)

data class ObservationContract(
    val observationId: String,
    val createdAt: String,
    val imagePath: String,
    val notes: String? = null
) {
    val imageUri: String get() = imagePath
    val capturedAt: String get() = createdAt
}

data class CandidateSpeciesContract(
    val animalId: String,
    val scientificName: String,
    val commonName: String,
    val confidence: Double
)

data class IdentificationResultContract(
    val identificationId: String,
    val observationId: String,
    val candidateSpecies: List<CandidateSpeciesContract>,
    val identificationMethod: String = "DETERMINISTIC_ALPHA",
    val createdAt: String
) {
    val topCandidate: CandidateSpeciesContract? get() = candidateSpecies.firstOrNull()
    val candidateAnimalId: String get() = topCandidate?.animalId ?: ""
    val candidateScientificName: String get() = topCandidate?.scientificName ?: ""
    val confidence: Double get() = topCandidate?.confidence ?: 0.0
}

data class IdentificationDecisionContract(
    val decisionId: String,
    val identificationId: String,
    val animalId: String? = null,
    val status: DecisionStatus = DecisionStatus.ACCEPTED,
    val decidedAt: String,
    val selectedAnimalId: String? = animalId
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
    val edition: String? = null,
    val imagePath: String? = null,
    val personalLore: String? = null
)

