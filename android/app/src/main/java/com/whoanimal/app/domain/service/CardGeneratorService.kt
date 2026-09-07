package com.whoanimal.app.domain.service

import com.whoanimal.app.domain.boundary.CardGeneratorBoundary
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.model.VerificationStatus
import java.time.Instant
import java.util.UUID

/**
 * Servicio de ensamblaje y generación de cartas coleccionables para WHO Animal Android.
 *
 * Sigue la estricta frontera ontológica de dominio:
 * Capture -> Card (Capture != Card, cardId != captureId).
 *
 * Consume contratos inmutables existentes sin inventar sistemas paralelos.
 */
class CardGeneratorService : CardGeneratorBoundary {

    override fun generateCard(capture: CaptureContract): AnimalCardContract {
        val profile = OfficialStarterCatalog.findById(capture.animalId)
        val isRare = profile?.isRareSpecies ?: false
        val rarity = if (isRare) "RARE" else "COMMON"
        val timestamp = Instant.now().toString()
        val shortId = UUID.randomUUID().toString().take(6).uppercase()
        val animalCode = capture.animalId.take(8).uppercase()
        val serial = "WHO-CARD-$animalCode-$shortId"

        return AnimalCardContract(
            cardId = UUID.randomUUID().toString(),
            animalId = capture.animalId,
            specimenNumber = 1,
            schemaVersion = "1.0",
            generation = "genesis",
            issuedAt = timestamp,
            populationAtIssuance = 1,
            rarity = rarity,
            captureId = capture.captureId,
            serial = serial,
            verificationStatus = VerificationStatus.VERIFIED,
            identificationMethod = "VISUAL_AI_ALPHA",
            identificationConfidence = 0.95,
            rank = 1,
            displayLocation = capture.displayLocation,
            ownerId = null,
            edition = "Alpha 0.1",
            imagePath = null,
            personalLore = "Avistamiento oficial documentado durante expedición de campo."
        )
    }

    /**
     * Construye de manera determinista el par (Capture, Card) a partir de una decisión aceptada.
     */
    fun assembleFromDecision(
        decision: IdentificationDecisionContract,
        observation: ObservationContract? = null,
        confidence: Double? = null,
        displayLocation: String = "Área Natural Protegida"
    ): Pair<CaptureContract, AnimalCardContract> {
        require(decision.status == DecisionStatus.ACCEPTED) {
            "No se puede emitir una carta desde una decisión no aceptada: ${decision.status}"
        }
        val targetAnimalId = decision.selectedAnimalId
            ?: throw IllegalArgumentException("La decisión aceptada no contiene animalId")

        val capture = CaptureContract(
            captureId = UUID.randomUUID().toString(),
            animalId = targetAnimalId,
            identificationId = decision.identificationId,
            capturedAt = decision.decidedAt,
            displayLocation = displayLocation
        )

        val card = generateCard(capture).copy(
            imagePath = observation?.imagePath,
            identificationConfidence = confidence ?: 0.95,
            displayLocation = displayLocation
        )

        return Pair(capture, card)
    }
}
