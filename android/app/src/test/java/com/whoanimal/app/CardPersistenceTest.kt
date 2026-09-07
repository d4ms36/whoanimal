package com.whoanimal.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.entities.CardEntity
import com.whoanimal.app.data.local.repository.RoomCardRepository
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.VerificationStatus
import com.whoanimal.app.domain.repository.DuplicateCardException
import com.whoanimal.app.domain.repository.DuplicateSlotException
import com.whoanimal.app.domain.repository.InvalidSlotException
import com.whoanimal.app.domain.repository.StorageConstants
import com.whoanimal.app.domain.repository.StorageFullException
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
import java.io.File
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class CardPersistenceTest {

    private lateinit var context: Context
    private lateinit var db: WhoAnimalDatabase
    private lateinit var cardRepository: RoomCardRepository
    private lateinit var storageRepository: RoomCollectionStorageRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        db = WhoAnimalDatabase.buildInMemory(context)
        cardRepository = RoomCardRepository(db.cardDao())
        storageRepository = RoomCollectionStorageRepository(db.cardDao(), db.storageSlotDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun createSampleCard(
        cardId: String = UUID.randomUUID().toString(),
        captureId: String = UUID.randomUUID().toString(),
        animalId: String = "canis_lupus_familiaris",
        specimenNumber: Int = 1,
        ownerId: String? = "user_alpha_1"
    ): AnimalCardContract {
        return AnimalCardContract(
            cardId = cardId,
            animalId = animalId,
            specimenNumber = specimenNumber,
            schemaVersion = "1.0",
            generation = "genesis",
            issuedAt = "2026-09-07T03:00:00Z",
            populationAtIssuance = 1,
            rarity = "COMMON",
            captureId = captureId,
            serial = "WA-CAN-001",
            verificationStatus = VerificationStatus.UNVERIFIED,
            identificationMethod = "DETERMINISTIC_ALPHA",
            identificationConfidence = 0.94,
            rank = 1,
            displayLocation = "Madrid, ES",
            ownerId = ownerId,
            edition = "Standard"
        )
    }

    // ==========================================
    // 1. REPOSITORY CRUD TESTS
    // ==========================================

    @Test
    fun testCardRepositorySaveGetGetAllDeleteExists(): Unit = runBlocking {
        val card = createSampleCard()

        // exists before save
        assertFalse(cardRepository.exists(card.cardId))
        assertFalse(cardRepository.existsByCaptureId(card.captureId))

        // save
        val saved = cardRepository.saveCard(card)
        assertTrue(saved)

        // exists after save
        assertTrue(cardRepository.exists(card.cardId))
        assertTrue(cardRepository.existsByCaptureId(card.captureId))

        // get
        val retrieved = cardRepository.getCard(card.cardId)
        assertNotNull(retrieved)
        assertEquals(card.cardId, retrieved?.cardId)
        assertEquals(card.captureId, retrieved?.captureId)
        assertEquals(card.animalId, retrieved?.animalId)

        // getAll
        val all = cardRepository.getAllCards()
        assertEquals(1, all.size)
        assertEquals(card.cardId, all.first().cardId)

        // delete
        val deleted = cardRepository.deleteCard(card.cardId)
        assertTrue(deleted)
        assertFalse(cardRepository.exists(card.cardId))
        assertNull(cardRepository.getCard(card.cardId))
    }

    // ==========================================
    // 2. SERIALIZATION FIDELITY
    // ==========================================

    @Test
    fun testSerializationFidelityPreservesAllCanonicalFields(): Unit = runBlocking {
        val original = createSampleCard(
            ownerId = null // Test nullable field
        )

        val entity = CardEntity.fromContract(original)
        val reconstructed = entity.toContract()

        // Symmetrical roundtrip
        assertEquals(original.cardId, reconstructed.cardId)
        assertEquals(original.captureId, reconstructed.captureId)
        assertEquals(original.animalId, reconstructed.animalId)
        assertEquals(original.specimenNumber, reconstructed.specimenNumber)
        assertEquals(original.schemaVersion, reconstructed.schemaVersion)
        assertEquals(original.generation, reconstructed.generation)
        assertEquals(original.issuedAt, reconstructed.issuedAt)
        assertEquals(original.populationAtIssuance, reconstructed.populationAtIssuance)
        assertEquals(original.rarity, reconstructed.rarity)
        assertEquals(original.serial, reconstructed.serial)
        assertEquals(original.verificationStatus, reconstructed.verificationStatus)
        assertEquals(original.identificationMethod, reconstructed.identificationMethod)
        assertEquals(original.identificationConfidence, reconstructed.identificationConfidence)
        assertEquals(original.rank, reconstructed.rank)
        assertEquals(original.displayLocation, reconstructed.displayLocation)
        assertEquals(original.ownerId, reconstructed.ownerId) // nullability preserved
        assertEquals(original.edition, reconstructed.edition)
    }

    // ==========================================
    // 3. INTEGRITY CONSTRAINTS
    // ==========================================

    @Test
    fun testCardIdDistinctFromCaptureIdEnforced(): Unit = runBlocking {
        val sameId = UUID.randomUUID().toString()
        val corruptCard = createSampleCard(cardId = sameId, captureId = sameId)

        var errorThrown = false
        try {
            cardRepository.saveCard(corruptCard)
        } catch (_: IllegalArgumentException) {
            errorThrown = true
        }
        assertTrue("card_id must be distinct from capture_id", errorThrown)
    }

    @Test
    fun testDuplicateCaptureIdRejectedAtDataLayer(): Unit = runBlocking {
        val captureId = UUID.randomUUID().toString()
        val card1 = createSampleCard(cardId = UUID.randomUUID().toString(), captureId = captureId)
        val card2 = createSampleCard(cardId = UUID.randomUUID().toString(), captureId = captureId)

        cardRepository.saveCard(card1)

        var duplicateRejected = false
        try {
            cardRepository.saveCard(card2)
        } catch (_: DuplicateCardException) {
            duplicateRejected = true
        }
        assertTrue("1 Capture -> <= 1 Card: duplicate capture_id must be rejected", duplicateRejected)
    }

    // ==========================================
    // 4. COLLECTION / STORAGE CONSTRAINTS (10x30=300)
    // ==========================================

    @Test
    fun testCollectionStorageInvariantsAndCapacity(): Unit = runBlocking {
        val capacity = storageRepository.getStorageCapacity()
        assertEquals(10, capacity.totalContainers)
        assertEquals(30, capacity.slotsPerContainer)
        assertEquals(300, capacity.totalCapacity)
        assertEquals(0, capacity.occupiedSlots)
        assertEquals(300, capacity.availableSlots)
    }

    @Test
    fun testAssignSlotAndQueryContainer(): Unit = runBlocking {
        val card = createSampleCard()
        val slotRecord = storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 1)

        assertEquals(card.cardId, slotRecord.cardId)
        assertEquals(1, slotRecord.containerIndex)
        assertEquals(1, slotRecord.slotIndex)

        val inContainer1 = storageRepository.getCardsInContainer(1)
        assertEquals(1, inContainer1.size)
        assertEquals(1, inContainer1.first().first) // slot 1
        assertEquals(card.cardId, inContainer1.first().second.cardId)

        val capacity = storageRepository.getStorageCapacity()
        assertEquals(1, capacity.occupiedSlots)
        assertEquals(299, capacity.availableSlots)
    }

    @Test
    fun testInvalidContainerAndSlotOutOfRangeRejected(): Unit = runBlocking {
        val card = createSampleCard()

        // Container 0 (out of range 1..10)
        var invalidContainer = false
        try {
            storageRepository.assignSlot(card, containerIndex = 0, slotIndex = 1)
        } catch (_: InvalidSlotException) {
            invalidContainer = true
        }
        assertTrue("Container 0 must be rejected", invalidContainer)

        // Container 11 (out of range 1..10)
        var invalidContainerHigh = false
        try {
            storageRepository.assignSlot(card, containerIndex = 11, slotIndex = 1)
        } catch (_: InvalidSlotException) {
            invalidContainerHigh = true
        }
        assertTrue("Container 11 must be rejected", invalidContainerHigh)

        // Slot 0 (out of range 1..30)
        var invalidSlot = false
        try {
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 0)
        } catch (_: InvalidSlotException) {
            invalidSlot = true
        }
        assertTrue("Slot 0 must be rejected", invalidSlot)

        // Slot 31 (out of range 1..30)
        var invalidSlotHigh = false
        try {
            storageRepository.assignSlot(card, containerIndex = 1, slotIndex = 31)
        } catch (_: InvalidSlotException) {
            invalidSlotHigh = true
        }
        assertTrue("Slot 31 must be rejected", invalidSlotHigh)
    }

    @Test
    fun testDuplicateSlotOccupancyRejected(): Unit = runBlocking {
        val card1 = createSampleCard()
        val card2 = createSampleCard()

        storageRepository.assignSlot(card1, containerIndex = 2, slotIndex = 5)

        var duplicateSlotRejected = false
        try {
            storageRepository.assignSlot(card2, containerIndex = 2, slotIndex = 5)
        } catch (_: DuplicateSlotException) {
            duplicateSlotRejected = true
        }
        assertTrue("Two cards cannot occupy the same slot", duplicateSlotRejected)
    }

    @Test
    fun testDeleteCardFreesSlotImmediatelyWithoutOrphan(): Unit = runBlocking {
        val card = createSampleCard()
        storageRepository.assignSlot(card, containerIndex = 3, slotIndex = 10)

        assertEquals(1, storageRepository.getStorageCapacity().occupiedSlots)
        assertNotNull(storageRepository.getStorageSlot(card.cardId))

        // Delete card and free slot
        val freed = storageRepository.deleteCardAndFreeSlot(card.cardId)
        assertTrue(freed)

        // Slot is freed and capacity reduced
        assertEquals(0, storageRepository.getStorageCapacity().occupiedSlots)
        assertNull(storageRepository.getStorageSlot(card.cardId))
        assertNull(cardRepository.getCard(card.cardId))

        // Reusing the same slot is now allowed
        val newCard = createSampleCard()
        val newSlot = storageRepository.assignSlot(newCard, containerIndex = 3, slotIndex = 10)
        assertEquals(10, newSlot.slotIndex)
        assertEquals(1, storageRepository.getStorageCapacity().occupiedSlots)
    }

    @Test
    fun testAutoAssignSlotFillsSequentially(): Unit = runBlocking {
        val card1 = createSampleCard()
        val card2 = createSampleCard()

        val slot1 = storageRepository.autoAssignSlot(card1)
        val slot2 = storageRepository.autoAssignSlot(card2)

        assertEquals(1, slot1.containerIndex)
        assertEquals(1, slot1.slotIndex)

        assertEquals(1, slot2.containerIndex)
        assertEquals(2, slot2.slotIndex)
    }

    @Test
    fun testStorageFullThrowsException(): Unit = runBlocking {
        // Mock or fill to capacity check:
        // We verify that if occupiedSlots == 300, StorageFullException is thrown.
        // Let's populate 300 slots or test autoAssign / assignSlot when capacity reached.
        val cards = (1..300).map { createSampleCard() }
        for (i in 0 until 300) {
            val cIdx = (i / 30) + 1
            val sIdx = (i % 30) + 1
            storageRepository.assignSlot(cards[i], cIdx, sIdx)
        }

        val cap = storageRepository.getStorageCapacity()
        assertEquals(300, cap.occupiedSlots)
        assertEquals(0, cap.availableSlots)

        var overflowThrown = false
        try {
            val extraCard = createSampleCard()
            storageRepository.autoAssignSlot(extraCard)
        } catch (_: StorageFullException) {
            overflowThrown = true
        }
        assertTrue("StorageFullException must be thrown when capacity is reached", overflowThrown)
    }

    // ==========================================
    // 5. APP RESTART SIMULATION TEST
    // ==========================================

    @Test
    fun testAppRestartSimulationPreservesDataAcrossDatabaseRecreation(): Unit = runBlocking {
        val dbName = "test_restart_simulation.db"
        context.deleteDatabase(dbName)

        val cardId = UUID.randomUUID().toString()
        val captureId = UUID.randomUUID().toString()
        val card = createSampleCard(cardId = cardId, captureId = captureId)

        // 1. Instancia A: Guarda carta y slot
        val dbA = WhoAnimalDatabase.buildPersistent(context, dbName)
        val cardRepoA = RoomCardRepository(dbA.cardDao())
        val storageRepoA = RoomCollectionStorageRepository(dbA.cardDao(), dbA.storageSlotDao())

        storageRepoA.assignSlot(card, containerIndex = 4, slotIndex = 15)
        assertEquals(1, storageRepoA.getStorageCapacity().occupiedSlots)

        // Cerrar aplicación / base de datos
        dbA.close()

        // 2. Instancia B (App Restart): Recrea base de datos sobre el mismo archivo
        val dbB = WhoAnimalDatabase.buildPersistent(context, dbName)
        val cardRepoB = RoomCardRepository(dbB.cardDao())
        val storageRepoB = RoomCollectionStorageRepository(dbB.cardDao(), dbB.storageSlotDao())

        // Verificar supervivencia de la carta y su slot
        val loadedCard = cardRepoB.getCard(cardId)
        assertNotNull("Card must survive app restart", loadedCard)
        assertEquals(cardId, loadedCard?.cardId)
        assertEquals(captureId, loadedCard?.captureId)
        assertEquals("canis_lupus_familiaris", loadedCard?.animalId)

        val loadedSlot = storageRepoB.getStorageSlot(cardId)
        assertNotNull("Storage slot must survive app restart", loadedSlot)
        assertEquals(4, loadedSlot?.containerIndex)
        assertEquals(15, loadedSlot?.slotIndex)

        val capacityB = storageRepoB.getStorageCapacity()
        assertEquals(1, capacityB.occupiedSlots)

        dbB.close()
        context.deleteDatabase(dbName)
        Unit
    }
}
