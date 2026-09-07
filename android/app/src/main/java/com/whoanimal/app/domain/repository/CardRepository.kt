package com.whoanimal.app.domain.repository

import com.whoanimal.app.domain.model.AnimalCardContract

/**
 * Contrato de persistencia para cartas zoológicas individuales (AnimalCard).
 *
 * Mantiene separación estricta:
 * Capture != Card (no se mezclan en la misma entidad).
 * card_id != capture_id.
 */
interface CardRepository {
    suspend fun saveCard(card: AnimalCardContract): Boolean
    suspend fun getCard(cardId: String): AnimalCardContract?
    suspend fun getAllCards(): List<AnimalCardContract>
    suspend fun deleteCard(cardId: String): Boolean
    suspend fun exists(cardId: String): Boolean
    suspend fun existsByCaptureId(captureId: String): Boolean
}
