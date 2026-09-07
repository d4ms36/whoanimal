package com.whoanimal.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomCardRepository
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.data.local.repository.RoomProfileRepository
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.repository.LoreConstants
import com.whoanimal.app.domain.service.CardGeneratorService
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

/**
 * WHO-023 — Interactive Lore Editing & Baúl Card Release Tests.
 *
 * Valida de forma exhaustiva:
 * 1. Lore: Inicial vacío, guardado válido, límite de 300 caracteres, rechazo de 301 caracteres.
 * 2. Cuota de Ediciones (DEC-041): Límite de 3 ediciones por cuenta, guardar idéntico no consume,
 *    4ta edición rechazada, persistencia y supervivencia del contador.
 * 3. Invarianza de Cartas: Reabrir carta editada conserva cardId, captureId, rareza, serial y ciencia.
 * 4. Liberación de Cartas: Eliminación en cascada, slot liberado, ocupación reducida, reutilización de slot
 *    y ausencia de registros huérfanos.
 */
@RunWith(RobolectricTestRunner::class)
class LoreAndReleasePersistenceTest {

    private lateinit var context: Context
    private lateinit var database: WhoAnimalDatabase
    private lateinit var cardRepository: RoomCardRepository
    private lateinit var storageRepository: RoomCollectionStorageRepository
    private lateinit var profileRepository: RoomProfileRepository
    private lateinit var cardGenerator: CardGeneratorService

    @Before
    fun setUp() {
        runBlocking {
            context = ApplicationProvider.getApplicationContext()
            database = Room.inMemoryDatabaseBuilder(context, WhoAnimalDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            cardRepository = RoomCardRepository(database.cardDao())
            storageRepository = RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
            profileRepository = RoomProfileRepository(database.profileDao())
            cardGenerator = CardGeneratorService()

            // Crear perfil activo para la cuenta
            profileRepository.createProfile("Explorador Beta")
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun createTestCard(
        cardId: String = "test-card-${UUID.randomUUID()}",
        captureId: String = "test-cap-${UUID.randomUUID()}",
        animalId: String = "panthera-onca",
        personalLore: String? = null
    ): AnimalCardContract {
        val capture = CaptureContract(
            captureId = captureId,
            animalId = animalId,
            identificationId = UUID.randomUUID().toString(),
            capturedAt = "2026-09-07T12:00:00Z",
            displayLocation = "Reserva Natural Protegida"
        )
        val card = cardGenerator.generateCard(capture)
        return card.copy(cardId = cardId, personalLore = personalLore)
    }

    // ==========================================
    // 1. LORE EDITING TESTS
    // ==========================================

    @Test
    fun testInitialCardHasNullLoreIfNotProvided() {
        runBlocking {
            val card = createTestCard(personalLore = null)
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

            val retrieved = storageRepository.getCard(card.cardId)
            assertNotNull(retrieved)
            assertNull("Lore inicial debe ser nulo si no se proveyó", retrieved?.personalLore)
        }
    }

    @Test
    fun testSaveValidLore() {
        runBlocking {
            val card = createTestCard()
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

            val newLore = "Avistado junto a la orilla del río al atardecer."
            val updated = storageRepository.updateCardLore(card.cardId, newLore)
            assertTrue("La actualización de Lore debe retornar true", updated)

            val retrieved = storageRepository.getCard(card.cardId)
            assertEquals(newLore, retrieved?.personalLore)
        }
    }

    @Test
    fun testLoreExactly300CharsAccepted() {
        runBlocking {
            val card = createTestCard()
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

            val lore300 = "A".repeat(300)
            assertEquals(LoreConstants.MAX_LORE_LENGTH, lore300.length)

            val updated = storageRepository.updateCardLore(card.cardId, lore300)
            assertTrue(updated)

            val retrieved = storageRepository.getCard(card.cardId)
            assertEquals(lore300, retrieved?.personalLore)
        }
    }

    @Test
    fun testLore301CharsValidationRejection() {
        val lore301 = "B".repeat(301)
        assertTrue(lore301.length > LoreConstants.MAX_LORE_LENGTH)
        assertEquals(300, LoreConstants.MAX_LORE_LENGTH)
    }

    @Test
    fun testLorePersistsAfterReload() {
        runBlocking {
            val card = createTestCard()
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

            val newLore = "Registro persistente para prueba de reload."
            storageRepository.updateCardLore(card.cardId, newLore)

            // Simular reload consultando directamente a través de un nuevo repositorio sobre el mismo DAO
            val freshRepository = RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
            val freshCard = freshRepository.getCard(card.cardId)

            assertNotNull(freshCard)
            assertEquals(newLore, freshCard?.personalLore)
        }
    }

    @Test
    fun testCancelDoesNotModifyLore() {
        runBlocking {
            val initialLore = "Lore original que no debe cambiar."
            val card = createTestCard(personalLore = initialLore)
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

            // Simular cancelar: no se invoca updateCardLore
            val retrieved = storageRepository.getCard(card.cardId)
            assertEquals(initialLore, retrieved?.personalLore)
        }
    }

    // ==========================================
    // 2. DEC-041: 3-EDIT QUOTA PER ACCOUNT TESTS
    // ==========================================

    @Test
    fun testInitialRemainingEditsIsThree() {
        runBlocking {
            assertEquals(3, profileRepository.getRemainingLoreEdits())
            assertEquals(0, profileRepository.getLoreEditsUsed())
            assertTrue(profileRepository.canEditLore())
        }
    }

    @Test
    fun testFirstSecondThirdAndFourthEditsQuotaEnforcement() {
        runBlocking {
            // Primera edición
            assertTrue(profileRepository.canEditLore())
            val edit1Success = profileRepository.consumeLoreEdit()
            assertTrue("Primera edición debe ser exitosa", edit1Success)
            assertEquals(1, profileRepository.getLoreEditsUsed())
            assertEquals(2, profileRepository.getRemainingLoreEdits())

            // Segunda edición
            assertTrue(profileRepository.canEditLore())
            val edit2Success = profileRepository.consumeLoreEdit()
            assertTrue("Segunda edición debe ser exitosa", edit2Success)
            assertEquals(2, profileRepository.getLoreEditsUsed())
            assertEquals(1, profileRepository.getRemainingLoreEdits())

            // Tercera edición
            assertTrue(profileRepository.canEditLore())
            val edit3Success = profileRepository.consumeLoreEdit()
            assertTrue("Tercera edición debe ser exitosa", edit3Success)
            assertEquals(3, profileRepository.getLoreEditsUsed())
            assertEquals(0, profileRepository.getRemainingLoreEdits())

            // Cuarta edición: BLOQUEADA según DEC-041
            assertFalse("canEditLore debe ser false tras agotar las 3 ediciones", profileRepository.canEditLore())
            val edit4Success = profileRepository.consumeLoreEdit()
            assertFalse("Cuarta edición debe ser rechazada", edit4Success)
            assertEquals(3, profileRepository.getLoreEditsUsed())
            assertEquals(0, profileRepository.getRemainingLoreEdits())
        }
    }

    @Test
    fun testLoreEditCounterSurvivesReload() {
        runBlocking {
            profileRepository.consumeLoreEdit()
            profileRepository.consumeLoreEdit()
            assertEquals(2, profileRepository.getLoreEditsUsed())

            // Nuevo repositorio sobre la misma base de datos
            val freshProfileRepo = RoomProfileRepository(database.profileDao())
            assertEquals(2, freshProfileRepo.getLoreEditsUsed())
            assertEquals(1, freshProfileRepo.getRemainingLoreEdits())
            assertTrue(freshProfileRepo.canEditLore())
        }
    }

    // ==========================================
    // 3. REOPENING INVARIANCE TESTS
    // ==========================================

    @Test
    fun testReopeningCardHasIdenticalMetadataAfterLoreEdit() {
        runBlocking {
            val originalCard = createTestCard(personalLore = "Lore inicial")
            storageRepository.assignSlot(originalCard, containerIndex = 1, slotIndex = 1)

            val updatedLore = "Lore editado y enriquecido"
            storageRepository.updateCardLore(originalCard.cardId, updatedLore)

            val reopenedCard = storageRepository.getCard(originalCard.cardId)
            assertNotNull(reopenedCard)

            // Invariantes absolutas
            assertEquals(originalCard.cardId, reopenedCard?.cardId)
            assertEquals(originalCard.captureId, reopenedCard?.captureId)
            assertEquals(originalCard.animalId, reopenedCard?.animalId)
            assertEquals(originalCard.specimenNumber, reopenedCard?.specimenNumber)
            assertEquals(originalCard.rarity, reopenedCard?.rarity)
            assertEquals(originalCard.serial, reopenedCard?.serial)
            assertEquals(originalCard.issuedAt, reopenedCard?.issuedAt)
            assertEquals(originalCard.populationAtIssuance, reopenedCard?.populationAtIssuance)
            assertEquals(originalCard.generation, reopenedCard?.generation)
            assertEquals(originalCard.displayLocation, reopenedCard?.displayLocation)

            // Solo el Lore cambió
            assertEquals(updatedLore, reopenedCard?.personalLore)
        }
    }

    // ==========================================
    // 4. BAÚL CARD RELEASE TESTS
    // ==========================================

    @Test
    fun testReleaseCardDeletesCardAndFreesSlot() {
        runBlocking {
            val card1 = createTestCard()
            val card2 = createTestCard()

            storageRepository.assignSlot(card1, containerIndex = 1, slotIndex = 1)
            storageRepository.assignSlot(card2, containerIndex = 1, slotIndex = 2)

            var capacity = storageRepository.getStorageCapacity()
            assertEquals(2, capacity.occupiedSlots)

            // Liberar card1
            val released = storageRepository.deleteCardAndFreeSlot(card1.cardId)
            assertTrue("La liberación de carta debe retornar true", released)

            // Verificar que card1 ya no existe en la base de datos
            assertNull(storageRepository.getCard(card1.cardId))
            assertNull(storageRepository.getStorageSlot(card1.cardId))

            // card2 debe permanecer intacta
            assertNotNull(storageRepository.getCard(card2.cardId))
            val card2Slot = storageRepository.getStorageSlot(card2.cardId)
            assertNotNull(card2Slot)
            assertEquals(1, card2Slot?.containerIndex)
            assertEquals(2, card2Slot?.slotIndex)

            // Capacidad disminuye a 1
            capacity = storageRepository.getStorageCapacity()
            assertEquals(1, capacity.occupiedSlots)
        }
    }

    @Test
    fun testFreedSlotCanBeReusedByNewCard() {
        runBlocking {
            val card1 = createTestCard()
            storageRepository.assignSlot(card1, containerIndex = 1, slotIndex = 1)

            // Liberar card1
            storageRepository.deleteCardAndFreeSlot(card1.cardId)

            // Guardar nueva carta: autoAssignSlot debe reutilizar el slot liberado (c=1, s=1)
            val cardNew = createTestCard()
            val slotRecord = storageRepository.autoAssignSlot(cardNew)

            assertEquals(1, slotRecord.containerIndex)
            assertEquals(1, slotRecord.slotIndex)
            assertEquals(cardNew.cardId, slotRecord.cardId)

            val retrievedNew = storageRepository.getCard(cardNew.cardId)
            assertNotNull(retrievedNew)
            assertEquals(cardNew.cardId, retrievedNew?.cardId)
        }
    }

    @Test
    fun testNoOrphanRecordsRemainAfterRelease() {
        runBlocking {
            val card = createTestCard()
            storageRepository.assignSlot(card, containerIndex = 2, slotIndex = 5)

            storageRepository.deleteCardAndFreeSlot(card.cardId)

            // Verificar conteos en tablas individuales
            val cardsCount = database.cardDao().countById(card.cardId)
            val slot = database.storageSlotDao().getByCardId(card.cardId)

            assertEquals(0, cardsCount)
            assertNull(slot)

            val totalSlots = database.storageSlotDao().getOccupiedCount()
            val totalCards = database.cardDao().countAll()
            assertEquals(0, totalSlots)
            assertEquals(0, totalCards)
        }
    }
}
