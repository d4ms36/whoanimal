package com.whoanimal.app.data.local.repository

import com.whoanimal.app.data.local.dao.CardDao
import com.whoanimal.app.data.local.dao.StorageSlotDao
import com.whoanimal.app.data.local.entities.CardEntity
import com.whoanimal.app.data.local.entities.StorageSlotEntity
import com.whoanimal.app.domain.boundary.StorageCapacityInfo
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.repository.CollectionStorageRepository
import com.whoanimal.app.domain.repository.DuplicateCardException
import com.whoanimal.app.domain.repository.DuplicateSlotException
import com.whoanimal.app.domain.repository.InvalidSlotException
import com.whoanimal.app.domain.repository.StorageConstants
import com.whoanimal.app.domain.repository.StorageFullException
import com.whoanimal.app.domain.repository.StorageSlotRecord
import java.time.Instant

class RoomCollectionStorageRepository(
    private val cardDao: CardDao,
    private val storageSlotDao: StorageSlotDao
) : CollectionStorageRepository {

    override suspend fun getStorageSlot(cardId: String): StorageSlotRecord? {
        if (cardId.isBlank()) return null
        return storageSlotDao.getByCardId(cardId)?.toRecord()
    }

    override suspend fun getCardsInContainer(containerIndex: Int): List<Pair<Int, AnimalCardContract>> {
        if (containerIndex !in 1..StorageConstants.TOTAL_CONTAINERS) {
            throw InvalidSlotException(
                "Contenedor $containerIndex fuera de rango permitido (1..${StorageConstants.TOTAL_CONTAINERS})"
            )
        }

        val slots = storageSlotDao.getByContainer(containerIndex)
        val result = mutableListOf<Pair<Int, AnimalCardContract>>()

        for (slot in slots) {
            val cardEntity = cardDao.getById(slot.cardId)
            if (cardEntity != null) {
                result.add(Pair(slot.slotIndex, cardEntity.toContract()))
            }
        }

        return result.sortedBy { it.first }
    }

    override suspend fun assignSlot(
        card: AnimalCardContract,
        containerIndex: Int,
        slotIndex: Int
    ): StorageSlotRecord {
        // 1. Validar rangos de contenedor y slot
        if (containerIndex !in 1..StorageConstants.TOTAL_CONTAINERS) {
            throw InvalidSlotException(
                "Contenedor $containerIndex fuera de rango (1..${StorageConstants.TOTAL_CONTAINERS})"
            )
        }
        if (slotIndex !in 1..StorageConstants.SLOTS_PER_CONTAINER) {
            throw InvalidSlotException(
                "Slot $slotIndex fuera de rango (1..${StorageConstants.SLOTS_PER_CONTAINER})"
            )
        }

        // 2. Comprobar límite de capacidad total (300 cartas)
        val currentOccupied = storageSlotDao.getOccupiedCount()
        if (currentOccupied >= StorageConstants.TOTAL_CAPACITY) {
            throw StorageFullException(
                "El almacenamiento está lleno (capacidad máxima: ${StorageConstants.TOTAL_CAPACITY} cartas)."
            )
        }

        // 3. Comprobar si el slot solicitado ya está ocupado por otra carta
        val occupiedBy = storageSlotDao.getByLocation(containerIndex, slotIndex)
        if (occupiedBy != null && occupiedBy.cardId != card.cardId) {
            throw DuplicateSlotException(
                "El slot $slotIndex en contenedor $containerIndex ya está ocupado por la carta '${occupiedBy.cardId}'."
            )
        }

        // 4. Comprobar si la carta ya tiene asignado otro slot en el almacenamiento
        val existingSlotForCard = storageSlotDao.getByCardId(card.cardId)
        if (existingSlotForCard != null) {
            throw DuplicateSlotException(
                "La carta '${card.cardId}' ya tiene asignado el slot ${existingSlotForCard.slotIndex} en contenedor ${existingSlotForCard.containerIndex}."
            )
        }

        // 5. Comprobar unicidad de capture_id en cartas
        val cardWithSameCapture = cardDao.getByCaptureId(card.captureId)
        if (cardWithSameCapture != null && cardWithSameCapture.cardId != card.cardId) {
            throw DuplicateCardException("Ya existe una carta asociada a la captura '${card.captureId}'.")
        }

        // 6. Asegurar inserción de la entidad Card
        if (cardDao.getById(card.cardId) == null) {
            cardDao.insert(CardEntity.fromContract(card))
        }

        // 7. Persistir asignación de slot
        val now = Instant.now().toString()
        val slotEntity = StorageSlotEntity(
            cardId = card.cardId,
            containerIndex = containerIndex,
            slotIndex = slotIndex,
            assignedAt = now
        )
        storageSlotDao.insert(slotEntity)

        return slotEntity.toRecord()
    }

    override suspend fun autoAssignSlot(card: AnimalCardContract): StorageSlotRecord {
        val currentOccupied = storageSlotDao.getOccupiedCount()
        if (currentOccupied >= StorageConstants.TOTAL_CAPACITY) {
            throw StorageFullException(
                "El almacenamiento está lleno (capacidad máxima: ${StorageConstants.TOTAL_CAPACITY} cartas)."
            )
        }

        val occupiedLocations = storageSlotDao.getAll()
            .map { Pair(it.containerIndex, it.slotIndex) }
            .toSet()

        var foundContainer = -1
        var foundSlot = -1

        outer@ for (c in 1..StorageConstants.TOTAL_CONTAINERS) {
            for (s in 1..StorageConstants.SLOTS_PER_CONTAINER) {
                if (!occupiedLocations.contains(Pair(c, s))) {
                    foundContainer = c
                    foundSlot = s
                    break@outer
                }
            }
        }

        if (foundContainer == -1 || foundSlot == -1) {
            throw StorageFullException("No se encontró ningún slot libre disponible.")
        }

        return assignSlot(card, foundContainer, foundSlot)
    }

    override suspend fun getStorageCapacity(): StorageCapacityInfo {
        val occupied = storageSlotDao.getOccupiedCount()
        return StorageCapacityInfo(
            totalContainers = StorageConstants.TOTAL_CONTAINERS,
            slotsPerContainer = StorageConstants.SLOTS_PER_CONTAINER,
            totalCapacity = StorageConstants.TOTAL_CAPACITY,
            occupiedSlots = occupied
        )
    }

    override suspend fun deleteCardAndFreeSlot(cardId: String): Boolean {
        if (cardId.isBlank()) return false
        // Eliminar slot explícitamente para asegurar liberación inmediata de slot
        storageSlotDao.deleteByCardId(cardId)
        val deletedRows = cardDao.deleteById(cardId)
        return deletedRows > 0
    }

    override suspend fun updateCardLore(cardId: String, newLore: String?): Boolean {
        if (cardId.isBlank()) return false
        return cardDao.updatePersonalLore(cardId, newLore) > 0
    }

    override suspend fun getCard(cardId: String): AnimalCardContract? {
        if (cardId.isBlank()) return null
        return cardDao.getById(cardId)?.toContract()
    }
}
