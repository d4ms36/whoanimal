package com.whoanimal.app.data.local.repository

import android.database.sqlite.SQLiteConstraintException
import com.whoanimal.app.data.local.dao.CardDao
import com.whoanimal.app.data.local.entities.CardEntity
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.repository.CardRepository
import com.whoanimal.app.domain.repository.DuplicateCardException

class RoomCardRepository(
    private val cardDao: CardDao
) : CardRepository {

    override suspend fun saveCard(card: AnimalCardContract): Boolean {
        // Regla ontológica: card_id != capture_id
        require(card.cardId != card.captureId) {
            "card_id must be distinct from capture_id"
        }

        // Regla de unicidad contractual: 1 captura -> <= 1 carta
        if (cardDao.countByCaptureId(card.captureId) > 0) {
            throw DuplicateCardException("Ya existe una carta para la captura '${card.captureId}'.")
        }

        val entity = CardEntity.fromContract(card)
        return try {
            cardDao.insert(entity)
            true
        } catch (_: SQLiteConstraintException) {
            throw DuplicateCardException("Violación de restricción única en persistencia de carta.")
        }
    }

    override suspend fun getCard(cardId: String): AnimalCardContract? {
        if (cardId.isBlank()) return null
        return cardDao.getById(cardId)?.toContract()
    }

    override suspend fun getAllCards(): List<AnimalCardContract> {
        return cardDao.getAll().map { it.toContract() }
    }

    override suspend fun deleteCard(cardId: String): Boolean {
        if (cardId.isBlank()) return false
        return cardDao.deleteById(cardId) > 0
    }

    override suspend fun exists(cardId: String): Boolean {
        if (cardId.isBlank()) return false
        return cardDao.countById(cardId) > 0
    }

    override suspend fun existsByCaptureId(captureId: String): Boolean {
        if (captureId.isBlank()) return false
        return cardDao.countByCaptureId(captureId) > 0
    }
}
