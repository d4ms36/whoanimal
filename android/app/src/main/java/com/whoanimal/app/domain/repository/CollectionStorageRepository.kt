package com.whoanimal.app.domain.repository

import com.whoanimal.app.domain.boundary.StorageCapacityInfo
import com.whoanimal.app.domain.model.AnimalCardContract

/**
 * Excepciones de dominio para el sistema de almacenamiento y colección.
 */
open class StorageException(message: String) : Exception(message)
class StorageFullException(message: String) : StorageException(message)
class InvalidSlotException(message: String) : StorageException(message)
class DuplicateSlotException(message: String) : StorageException(message)
class DuplicateCardException(message: String) : StorageException(message)

/**
 * Registro de ubicación de una carta dentro de la colección.
 */
data class StorageSlotRecord(
    val cardId: String,
    val containerIndex: Int,
    val slotIndex: Int,
    val assignedAt: String
)

/**
 * Invariantes contractuales de Storage Alpha 0.1:
 * - 10 contenedores.
 * - 30 slots por contenedor.
 * - 300 cartas de capacidad máxima total.
 */
object StorageConstants {
    const val TOTAL_CONTAINERS = 10
    const val SLOTS_PER_CONTAINER = 30
    const val TOTAL_CAPACITY = TOTAL_CONTAINERS * SLOTS_PER_CONTAINER // 300
}

/**
 * Repositorio de dominio para la gestión y persistencia del álbum de colección.
 */
interface CollectionStorageRepository {
    suspend fun getStorageSlot(cardId: String): StorageSlotRecord?
    suspend fun getCardsInContainer(containerIndex: Int): List<Pair<Int, AnimalCardContract>>
    suspend fun assignSlot(card: AnimalCardContract, containerIndex: Int, slotIndex: Int): StorageSlotRecord
    suspend fun autoAssignSlot(card: AnimalCardContract): StorageSlotRecord
    suspend fun getStorageCapacity(): StorageCapacityInfo
    suspend fun deleteCardAndFreeSlot(cardId: String): Boolean
}
