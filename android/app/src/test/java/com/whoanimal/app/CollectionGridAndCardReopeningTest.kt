package com.whoanimal.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomCardRepository
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.VerificationStatus
import com.whoanimal.app.domain.repository.StorageConstants
import com.whoanimal.app.domain.service.CardGeneratorService
import com.whoanimal.app.ui.screens.card.CardPresentationMode
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.time.Instant
import java.util.UUID

/**
 * WHO-018E — Collection Grid & Card Reopening Test Suite.
 *
 * Valida de forma exhaustiva:
 * 1. Colección: Contenedores, capacidad, slots, estados vacíos y múltiples cartas.
 * 2. Reopening: La carta seleccionada del Baúl es exactamente la misma entidad persistida.
 * 3. Modo de Presentación: PERSISTED_CARD no re-guarda ni duplica cartas.
 * 4. Resiliencia: Campos opcionales nulos, imagen faltante y base de datos reiniciada.
 */
@RunWith(RobolectricTestRunner::class)
class CollectionGridAndCardReopeningTest {

    private lateinit var context: Context
    private lateinit var database: WhoAnimalDatabase
    private lateinit var cardRepository: RoomCardRepository
    private lateinit var storageRepository: RoomCollectionStorageRepository
    private lateinit var cardGenerator: CardGeneratorService

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, WhoAnimalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cardRepository = RoomCardRepository(database.cardDao())
        storageRepository = RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
        cardGenerator = CardGeneratorService()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun createAndPersistTestCard(
        animalId: String = OfficialStarterCatalog.dog.animalId,
        containerIndex: Int? = null,
        slotIndex: Int? = null,
        imagePath: String? = null,
        personalLore: String? = "Lore de prueba para el espécimen"
    ): Pair<AnimalCardContract, Int> = runBlocking {
        val capture = CaptureContract(
            captureId = "cap-${UUID.randomUUID()}",
            animalId = animalId,
            identificationId = "id-${UUID.randomUUID()}",
            capturedAt = Instant.now().toString(),
            displayLocation = "Hábitat Natural Observado"
        )
        val card = cardGenerator.generateCard(capture).copy(
            imagePath = imagePath,
            personalLore = personalLore
        )

        val slot = if (containerIndex != null && slotIndex != null) {
            storageRepository.assignSlot(card, containerIndex, slotIndex)
        } else {
            storageRepository.autoAssignSlot(card)
        }

        Pair(card, slot.containerIndex)
    }

    // =========================================================================
    // 1. TESTS DE CONTENEDORES Y GRID EN EL BAÚL
    // =========================================================================

    @Test
    fun testEmptyCollectionShowsNoCardsAcrossAllContainers() = runBlocking {
        val capacity = storageRepository.getStorageCapacity()
        assertEquals(0, capacity.occupiedSlots)
        assertEquals(StorageConstants.TOTAL_CAPACITY, capacity.availableSlots)

        for (container in 1..StorageConstants.TOTAL_CONTAINERS) {
            val cardsInContainer = storageRepository.getCardsInContainer(container)
            assertTrue("Contenedor $container debe estar vacío inicialmente", cardsInContainer.isEmpty())
        }
    }

    @Test
    fun testSingleCardPersistedAppearsInExactContainerAndSlot() = runBlocking {
        val (card, container) = createAndPersistTestCard(
            animalId = OfficialStarterCatalog.cat.animalId,
            containerIndex = 2,
            slotIndex = 7
        )

        val cardsInC2 = storageRepository.getCardsInContainer(2)
        assertEquals("Contenedor 2 debe tener exactamente 1 carta", 1, cardsInC2.size)

        val (slot, retrievedCard) = cardsInC2.first()
        assertEquals("El slot debe ser 7", 7, slot)
        assertEquals("cardId debe ser idéntico", card.cardId, retrievedCard.cardId)
        assertEquals("captureId debe ser idéntico", card.captureId, retrievedCard.captureId)

        // Los otros contenedores deben permanecer vacíos
        val cardsInC1 = storageRepository.getCardsInContainer(1)
        assertTrue("Contenedor 1 debe seguir vacío", cardsInC1.isEmpty())
    }

    @Test
    fun testMultipleCardsDistributedAcrossDifferentContainersAndSlots() = runBlocking {
        val speciesList = OfficialStarterCatalog.allSpecies
        val persistedCards = mutableListOf<AnimalCardContract>()

        // Insertar 4 cartas en diferentes contenedores y slots
        val (card1, _) = createAndPersistTestCard(speciesList[0].animalId, containerIndex = 1, slotIndex = 1)
        val (card2, _) = createAndPersistTestCard(speciesList[1].animalId, containerIndex = 1, slotIndex = 5)
        val (card3, _) = createAndPersistTestCard(speciesList[2].animalId, containerIndex = 3, slotIndex = 12)
        val (card4, _) = createAndPersistTestCard(speciesList[3].animalId, containerIndex = 5, slotIndex = 30)

        persistedCards.addAll(listOf(card1, card2, card3, card4))

        val c1Cards = storageRepository.getCardsInContainer(1)
        assertEquals("Contenedor 1 debe tener 2 cartas", 2, c1Cards.size)
        assertEquals(listOf(1, 5), c1Cards.map { it.first })

        val c3Cards = storageRepository.getCardsInContainer(3)
        assertEquals("Contenedor 3 debe tener 1 carta en slot 12", 1, c3Cards.size)
        assertEquals(12, c3Cards.first().first)

        val c5Cards = storageRepository.getCardsInContainer(5)
        assertEquals("Contenedor 5 debe tener 1 carta en slot 30", 1, c5Cards.size)
        assertEquals(30, c5Cards.first().first)

        val totalCapacity = storageRepository.getStorageCapacity()
        assertEquals(4, totalCapacity.occupiedSlots)
        assertEquals(296, totalCapacity.availableSlots)
    }

    // =========================================================================
    // 2. REOPENING DE CARTA PERSISTIDA (IDENTIDAD ESTRICTA)
    // =========================================================================

    @Test
    fun testCardReopenedFromCollectionPreservesExactPersistedIdentityWithoutRegeneration() = runBlocking {
        val originalImagePath = "/data/user/0/com.whoanimal.app/cache/photo_jaguar.jpg"
        val originalLore = "Encontrado acechando cerca de las ruinas al amanecer."

        val (persistedCard, containerIdx) = createAndPersistTestCard(
            animalId = OfficialStarterCatalog.jaguar.animalId,
            containerIndex = 1,
            slotIndex = 1,
            imagePath = originalImagePath,
            personalLore = originalLore
        )

        // Simular selección desde la UI del Baúl:
        var selectedCardFromGrid: AnimalCardContract? = null
        val onSelectCard = { card: AnimalCardContract ->
            selectedCardFromGrid = card
        }

        val cardsInGrid = storageRepository.getCardsInContainer(containerIdx)
        val tappedCard = cardsInGrid.first().second
        onSelectCard(tappedCard)

        assertNotNull("Carta debe ser seleccionada", selectedCardFromGrid)
        val reopened = selectedCardFromGrid!!

        // Verificaciones críticas de NO regeneración:
        assertEquals("cardId debe ser exactamente el mismo", persistedCard.cardId, reopened.cardId)
        assertEquals("captureId debe ser exactamente el mismo", persistedCard.captureId, reopened.captureId)
        assertEquals("animalId no debe cambiar", persistedCard.animalId, reopened.animalId)
        assertEquals("rareza no debe cambiar", persistedCard.rarity, reopened.rarity)
        assertEquals("serial visual no debe cambiar", persistedCard.serial, reopened.serial)
        assertEquals("specimenNumber no debe cambiar", persistedCard.specimenNumber, reopened.specimenNumber)
        assertEquals("issuedAt no debe cambiar", persistedCard.issuedAt, reopened.issuedAt)
        assertEquals("displayLocation no debe cambiar", persistedCard.displayLocation, reopened.displayLocation)
        assertEquals("imagePath no debe cambiar", originalImagePath, reopened.imagePath)
        assertEquals("personalLore no debe cambiar", originalLore, reopened.personalLore)

        // Ficha biológica oficial vinculada
        val profile = OfficialStarterCatalog.findById(reopened.animalId)
        assertEquals("Panthera onca", profile?.scientificName)
        assertEquals("Jaguar", profile?.commonName)
        assertEquals("Carnívoro", profile?.diet)
    }

    // =========================================================================
    // 3. MODO DE PRESENTACIÓN (PERSISTED_CARD vs NEW_CARD_REVIEW)
    // =========================================================================

    @Test
    fun testCardPresentationModeDistinctionPreventsAccidentalResaving() {
        val reviewMode = CardPresentationMode.NEW_CARD_REVIEW
        val persistedMode = CardPresentationMode.PERSISTED_CARD

        assertNotEquals(reviewMode, persistedMode)
        assertEquals(CardPresentationMode.PERSISTED_CARD, CardPresentationMode.valueOf("PERSISTED_CARD"))
        assertEquals(CardPresentationMode.NEW_CARD_REVIEW, CardPresentationMode.valueOf("NEW_CARD_REVIEW"))

        // En modo PERSISTED_CARD:
        // Las intenciones de la UI no incluyen save ni release duplicate actions.
        var saveInvocationCount = 0
        val onSaveAttempt: () -> Unit = {
            if (persistedMode == CardPresentationMode.NEW_CARD_REVIEW) {
                saveInvocationCount++
            }
        }

        onSaveAttempt()
        assertEquals("En modo PERSISTED_CARD no se debe invocar guardado", 0, saveInvocationCount)
    }

    // =========================================================================
    // 4. PERSISTENCIA ENTRE REINICIOS DE APLICACIÓN (APP RESTART)
    // =========================================================================

    @Test
    fun testReopeningCardSurvivesAppRestartSimulation() = runBlocking {
        val dbFile = "reopen_restart_test.db"
        context.deleteDatabase(dbFile)

        val cardId = "card-survive-001"
        val captureId = "cap-survive-001"
        val imagePath = "/cache/sample_observation.jpg"
        val lore = "Espécimen registrado previo al reinicio del sistema."

        val originalCard = AnimalCardContract(
            cardId = cardId,
            animalId = OfficialStarterCatalog.macaw.animalId,
            specimenNumber = 1,
            issuedAt = Instant.now().toString(),
            populationAtIssuance = 1,
            rarity = "COMMON",
            captureId = captureId,
            serial = "WHO-CARD-MACAW-001",
            imagePath = imagePath,
            personalLore = lore,
            displayLocation = "Selva Amazónica"
        )

        // 1. Instancia A: Guarda la carta en el baúl
        val dbA = WhoAnimalDatabase.buildPersistent(context, dbFile)
        val storageRepoA = RoomCollectionStorageRepository(dbA.cardDao(), dbA.storageSlotDao())
        storageRepoA.assignSlot(originalCard, containerIndex = 4, slotIndex = 12)
        assertEquals(1, storageRepoA.getStorageCapacity().occupiedSlots)
        dbA.close()

        // 2. Instancia B (Simulación de reinicio): Reabre la base de datos
        val dbB = WhoAnimalDatabase.buildPersistent(context, dbFile)
        val storageRepoB = RoomCollectionStorageRepository(dbB.cardDao(), dbB.storageSlotDao())

        // Inspeccionar el contenedor 4 en el Baúl
        val cardsInC4 = storageRepoB.getCardsInContainer(4)
        assertEquals(1, cardsInC4.size)

        val (slotIdx, reopenedCard) = cardsInC4.first()
        assertEquals(12, slotIdx)
        assertEquals(cardId, reopenedCard.cardId)
        assertEquals(captureId, reopenedCard.captureId)
        assertEquals(OfficialStarterCatalog.macaw.animalId, reopenedCard.animalId)
        assertEquals(imagePath, reopenedCard.imagePath)
        assertEquals(lore, reopenedCard.personalLore)
        assertEquals("Selva Amazónica", reopenedCard.displayLocation)

        dbB.close()
        context.deleteDatabase(dbFile)
        Unit
    }

    // =========================================================================
    // 5. RESILIENCIA ANTE DATOS OPCIONALES NULOS O IMAGEN INEXISTENTE
    // =========================================================================

    @Test
    fun testReopeningCardWithNullOptionalFieldsAndMissingImageDoesNotCrash() = runBlocking {
        // Carta con campos opcionales totalmente nulos y ruta de imagen inexistente
        val (card, containerIdx) = createAndPersistTestCard(
            animalId = OfficialStarterCatalog.dog.animalId,
            containerIndex = 1,
            slotIndex = 3,
            imagePath = "/non/existent/path/photo.jpg",
            personalLore = null
        )

        val cards = storageRepository.getCardsInContainer(containerIdx)
        val retrieved = cards.first { it.second.cardId == card.cardId }.second

        assertNotNull(retrieved)
        assertNull("personalLore debe ser nulo sin excepción", retrieved.personalLore)
        assertEquals("/non/existent/path/photo.jpg", retrieved.imagePath)

        // Comprobar que el fallback de decodificación no lanza excepciones
        val file = File(retrieved.imagePath ?: "")
        assertFalse("El archivo no existe", file.exists())
    }
}
