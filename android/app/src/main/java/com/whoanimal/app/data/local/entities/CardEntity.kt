package com.whoanimal.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.VerificationStatus

/**
 * Entidad Room para la persistencia local de cartas zoológicas (AnimalCard).
 *
 * Reglas de integridad:
 * - cardId es la clave primaria.
 * - captureId tiene índice con restricción ÚNICA (1 captura -> <= 1 carta).
 * - Sincronía simétrica con los 19 campos del contrato canónico.
 */
@Entity(
    tableName = "cards",
    indices = [
        Index(value = ["captureId"], unique = true)
    ]
)
data class CardEntity(
    @PrimaryKey
    val cardId: String,
    @ColumnInfo(name = "captureId")
    val captureId: String,
    val animalId: String,
    val specimenNumber: Int,
    val schemaVersion: String = "1.0",
    val generation: String = "genesis",
    val issuedAt: String,
    val populationAtIssuance: Int,
    val rarity: String,
    val serial: String,
    val verificationStatus: String = "UNVERIFIED",
    val identificationMethod: String = "DETERMINISTIC_ALPHA",
    val identificationConfidence: Double? = null,
    val rank: Int = 1,
    val displayLocation: String = "General Location",
    val ownerId: String? = null,
    val edition: String? = null
) {
    fun toContract(): AnimalCardContract {
        val status = try {
            VerificationStatus.valueOf(verificationStatus)
        } catch (_: Exception) {
            VerificationStatus.UNVERIFIED
        }

        return AnimalCardContract(
            cardId = cardId,
            animalId = animalId,
            specimenNumber = specimenNumber,
            schemaVersion = schemaVersion,
            generation = generation,
            issuedAt = issuedAt,
            populationAtIssuance = populationAtIssuance,
            rarity = rarity,
            captureId = captureId,
            serial = serial,
            verificationStatus = status,
            identificationMethod = identificationMethod,
            identificationConfidence = identificationConfidence,
            rank = rank,
            displayLocation = displayLocation,
            ownerId = ownerId,
            edition = edition
        )
    }

    companion object {
        fun fromContract(contract: AnimalCardContract): CardEntity {
            return CardEntity(
                cardId = contract.cardId,
                captureId = contract.captureId,
                animalId = contract.animalId,
                specimenNumber = contract.specimenNumber,
                schemaVersion = contract.schemaVersion,
                generation = contract.generation,
                issuedAt = contract.issuedAt,
                populationAtIssuance = contract.populationAtIssuance,
                rarity = contract.rarity,
                serial = contract.serial,
                verificationStatus = contract.verificationStatus.name,
                identificationMethod = contract.identificationMethod,
                identificationConfidence = contract.identificationConfidence,
                rank = contract.rank,
                displayLocation = contract.displayLocation,
                ownerId = contract.ownerId,
                edition = contract.edition
            )
        }
    }
}
