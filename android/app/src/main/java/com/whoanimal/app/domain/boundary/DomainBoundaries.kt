package com.whoanimal.app.domain.boundary

import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.model.ObservationContract

/**
 * Fronteras de integración arquitectónica entre la aplicación Android y los servicios del dominio.
 *
 * Estas interfaces preparan la integración sin implementar prematuramente:
 * - WHO-016: Servicio de identificación visual.
 * - WHO-017: Persistencia local de colección y almacenamiento (10 containers x 30 = 300 cartas).
 */

/**
 * Frontera para el servicio de identificación zoológica (a implementar en WHO-016).
 */
interface IdentificationServiceBoundary {
    suspend fun identifyObservation(observation: ObservationContract): IdentificationResultContract
    suspend fun recordDecision(decision: IdentificationDecisionContract): CaptureContract?
}

/**
 * Frontera para el motor de persistencia y álbum de colección (a implementar en WHO-017).
 * Storage: 10 contenedores x 30 cartas = 300 cartas máximo.
 */
interface CollectionStorageBoundary {
    suspend fun getCards(): List<AnimalCardContract>
    suspend fun getStorageCapacity(): StorageCapacityInfo
    suspend fun saveCard(card: AnimalCardContract): Boolean
}

data class StorageCapacityInfo(
    val totalContainers: Int = 10,
    val slotsPerContainer: Int = 30,
    val totalCapacity: Int = 300,
    val occupiedSlots: Int = 0
) {
    val availableSlots: Int get() = totalCapacity - occupiedSlots
}

/**
 * Frontera para la generación formal de cartas desde capturas válidas (asociada a WHO-014).
 */
interface CardGeneratorBoundary {
    fun generateCard(capture: CaptureContract): AnimalCardContract
}
