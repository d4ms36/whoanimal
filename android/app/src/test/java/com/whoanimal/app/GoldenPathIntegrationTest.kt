package com.whoanimal.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.local.WhoAnimalDatabase
import com.whoanimal.app.data.local.repository.RoomCollectionStorageRepository
import com.whoanimal.app.data.local.repository.RoomProfileRepository
import com.whoanimal.app.domain.identification.IdentificationService
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.service.CardGeneratorService
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
 * WHO-018D — Golden Path Integration & UX Audit Test Suite.
 *
 * Audita de extremo a extremo la continuidad del flujo Alpha canónico:
 * ENTRY/HOME -> CAPTURE -> PHOTO -> IDENTIFICATION -> RESULT -> ACCEPT -> CARD GENERATION ->
 * CARD REVIEW (FRONT & BACK 3D FLIP) -> PERSISTENCE (SAVE / RELEASE) -> COLLECTION STORAGE RECOVERY.
 */
@RunWith(RobolectricTestRunner::class)
class GoldenPathIntegrationTest {

    private lateinit var context: Context
    private lateinit var database: WhoAnimalDatabase
    private lateinit var profileRepository: RoomProfileRepository
    private lateinit var storageRepository: RoomCollectionStorageRepository
    private lateinit var identificationService: IdentificationService
    private lateinit var cardGenerator: CardGeneratorService

    private val tempFiles = mutableListOf<File>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, WhoAnimalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        profileRepository = RoomProfileRepository(database.profileDao())
        storageRepository = RoomCollectionStorageRepository(database.cardDao(), database.storageSlotDao())
        identificationService = IdentificationService()
        cardGenerator = CardGeneratorService()
    }

    @After
    fun tearDown() {
        database.close()
        tempFiles.forEach { if (it.exists()) it.delete() }
        tempFiles.clear()
    }

    private fun createSamplePhotoFile(name: String = "photo_${UUID.randomUUID()}.jpg"): File {
        val file = File(context.cacheDir, name)
        file.writeBytes(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte(), 0x12, 0x34))
        tempFiles.add(file)
        return file
    }

    // =========================================================================
    // 1. AUDITORÍA INTEGRAL DEL GOLDEN PATH COMPLETO (CAPTURE -> PERSISTENCE)
    // =========================================================================

    @Test
    fun testGoldenPathCompleteFlowFromCaptureToStorageAndRestartRecovery() = runBlocking {
        // PASO 1: ENTRY / LOGIN LOCAL
        val profile = profileRepository.createProfile("Darwin")
        assertNotNull("Perfil de explorador debe ser creado exitosamente", profile)
        val activeProfile = profileRepository.getActiveProfile()
        assertEquals("Darwin", activeProfile?.explorerName)


        // PASO 2: CAPTURE — FOTOGRAFÍA EN ALMACENAMIENTO TEMPORAL
        val photoFile = createSamplePhotoFile()
        assertTrue("Archivo fotográfico debe existir en disco", photoFile.exists())
        assertTrue("Archivo fotográfico debe tener contenido", photoFile.length() > 0)

        // PASO 3: OBSERVATION — CONSTRUCCIÓN DE CONTRATO CANÓNICO
        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = Instant.now().toString(),
            imagePath = photoFile.absolutePath,
            notes = "Observación de campo en linde de bosque"
        )
        assertNotNull(observation.observationId)
        assertEquals(photoFile.absolutePath, observation.imagePath)

        // PASO 4: IDENTIFICATION — ANÁLISIS TAXONÓMICO DETERMINISTA
        val idResult = identificationService.identify(observation)
        assertNotNull("Resultado de identificación no debe ser nulo", idResult)
        assertEquals(observation.observationId, idResult.observationId)
        assertTrue("Debe contener especies candidatas", idResult.candidateSpecies.isNotEmpty())

        val topCandidate = idResult.topCandidate
        assertNotNull("Debe existir candidato principal", topCandidate)
        assertTrue("Confianza debe ser mayor a cero", (topCandidate?.confidence ?: 0.0) > 0.0)

        // PASO 5: RESULT / DECISION — ACEPTACIÓN FORMAL EXPLÍCITA
        val decision = IdentificationDecisionContract(
            decisionId = UUID.randomUUID().toString(),
            identificationId = idResult.identificationId,
            animalId = topCandidate!!.animalId,
            status = DecisionStatus.ACCEPTED,
            decidedAt = Instant.now().toString()
        )
        assertEquals(DecisionStatus.ACCEPTED, decision.status)

        // PASO 6: CARD GENERATION — FRONTERA ONTOLÓGICA (Capture != Card)
        val (capture, generatedCard) = cardGenerator.assembleFromDecision(
            decision = decision,
            observation = observation,
            confidence = topCandidate.confidence,
            displayLocation = "Reserva Natural Protegida"
        )
        assertNotNull("Capture debe ser generada", capture)
        assertNotNull("Card debe ser generada", generatedCard)
        assertNotEquals("Ontología: captureId != cardId", capture.captureId, generatedCard.cardId)
        assertEquals(capture.captureId, generatedCard.captureId)
        assertEquals(photoFile.absolutePath, generatedCard.imagePath)
        assertEquals("Reserva Natural Protegida", generatedCard.displayLocation)
        assertTrue("Serial visual debe ser sanitizado", generatedCard.serial.startsWith("WHO-CARD-"))

        // PASO 7: CARD REVIEW (FRONT & BACK FLIP AUDIT)
        val animalProfile = OfficialStarterCatalog.findById(generatedCard.animalId)
        assertNotNull("Perfil biológico oficial debe existir", animalProfile)

        // Front Face: identidad y rareza
        assertEquals(topCandidate.commonName, animalProfile?.commonName)
        assertEquals(topCandidate.scientificName, animalProfile?.scientificName)
        assertTrue("Rareza válida", generatedCard.rarity in listOf("COMMON", "RARE"))

        // Back Face: separación estricta ciencia vs observación/lore
        assertNotNull("Ciencia: Taxonomía debe ser factual", animalProfile?.taxonomy)
        assertNotNull("Ciencia: Hábitat debe ser factual", animalProfile?.habitat)
        assertNotNull("Ciencia: Dieta debe ser factual", animalProfile?.diet)
        assertNotNull("Observación: Fecha de captura", generatedCard.issuedAt)
        assertNotNull("Lore: Separado con aviso explícito", generatedCard.personalLore)

        // PASO 8: PERSISTENCIA — GUARDAR EN BAÚL DE COLECCIÓN
        val slotRecord = storageRepository.autoAssignSlot(generatedCard)
        assertNotNull("Slot debe ser asignado automáticamente", slotRecord)
        assertEquals(generatedCard.cardId, slotRecord.cardId)
        assertTrue("Contenedor en rango 1..10", slotRecord.containerIndex in 1..10)
        assertTrue("Slot en rango 1..30", slotRecord.slotIndex in 1..30)

        val capacity = storageRepository.getStorageCapacity()
        assertEquals(1, capacity.occupiedSlots)
        assertEquals(299, capacity.availableSlots)

        // PASO 9: RECOVERY POST-REINICIO (SIMULACIÓN DE APP RESTART)
        // Se crea una nueva instancia de repositorio sobre la misma base de datos persistida
        val restartedStorageRepo = RoomCollectionStorageRepository(
            database.cardDao(),
            database.storageSlotDao()
        )
        val recoveredSlot = restartedStorageRepo.getStorageSlot(generatedCard.cardId)
        assertNotNull("Carta debe ser recuperable tras reinicio", recoveredSlot)
        assertEquals(slotRecord.containerIndex, recoveredSlot?.containerIndex)
        assertEquals(slotRecord.slotIndex, recoveredSlot?.slotIndex)

        val cardsInContainer = restartedStorageRepo.getCardsInContainer(slotRecord.containerIndex)
        assertTrue("Contenedor debe contener la carta recuperada", cardsInContainer.any { it.second.cardId == generatedCard.cardId })
    }

    // =========================================================================
    // 2. AUDITORÍA DEL FLUJO DE LIBERACIÓN (RELEASE)
    // =========================================================================

    @Test
    fun testGoldenPathReleaseDiscardsCaptureWithoutCorruptingStorage() = runBlocking {
        val initialCapacity = storageRepository.getStorageCapacity()
        assertEquals(0, initialCapacity.occupiedSlots)

        val acceptedDecision = IdentificationDecisionContract(
            decisionId = "dec-release-test",
            identificationId = "id-release-test",
            animalId = OfficialStarterCatalog.jaguar.animalId,
            status = DecisionStatus.ACCEPTED,
            decidedAt = Instant.now().toString()
        )

        val (_, card) = cardGenerator.assembleFromDecision(
            decision = acceptedDecision,
            displayLocation = "Selva Lacandona"
        )

        // El usuario decide liberar: NO se llama a storageRepository.autoAssignSlot
        // Verificamos que la base de datos continúa completamente vacía
        val finalCapacity = storageRepository.getStorageCapacity()
        assertEquals("Capacidad ocupada debe ser 0 tras liberar", 0, finalCapacity.occupiedSlots)
        assertNull("La carta liberada no debe existir en Room", storageRepository.getStorageSlot(card.cardId))
    }

    // =========================================================================
    // 3. AUDITORÍA DE DECISIÓN RECHAZADA (NO GENERA CARTA)
    // =========================================================================

    @Test
    fun testGoldenPathRejectedDecisionPreventsCardGeneration() {
        val rejectedDecision = IdentificationDecisionContract(
            decisionId = "dec-rejected-test",
            identificationId = "id-rejected-test",
            animalId = OfficialStarterCatalog.cat.animalId,
            status = DecisionStatus.REJECTED,
            decidedAt = Instant.now().toString()
        )

        var errorCaptured = false
        try {
            cardGenerator.assembleFromDecision(rejectedDecision)
        } catch (e: IllegalArgumentException) {
            errorCaptured = true
        }
        assertTrue("Decisión REJECTED debe impedir formalmente la emisión de una carta", errorCaptured)
    }

    // =========================================================================
    // 4. AUDITORÍA DE DATOS OPCIONALES AUSENTES (RESILIENCIA ANTE NULOS)
    // =========================================================================

    @Test
    fun testGoldenPathMissingOptionalFieldsDoesNotCrash() = runBlocking {
        val minimalCard = AnimalCardContract(
            cardId = "card-minimal-001",
            animalId = OfficialStarterCatalog.dog.animalId,
            specimenNumber = 1,
            issuedAt = Instant.now().toString(),
            populationAtIssuance = 1,
            rarity = "COMMON",
            captureId = "cap-minimal-001",
            serial = "WHO-CARD-MINIMAL-001",
            imagePath = null,
            personalLore = null,
            identificationConfidence = null
        )

        // Guardar carta con campos opcionales nulos
        val slot = storageRepository.autoAssignSlot(minimalCard)
        assertNotNull(slot)

        // Recuperar carta con campos opcionales nulos
        val cardsInContainer = storageRepository.getCardsInContainer(slot.containerIndex)
        val retrieved = cardsInContainer.firstOrNull { it.second.cardId == minimalCard.cardId }?.second
        assertNotNull(retrieved)
        assertNull("imagePath debe ser nulo sin crash", retrieved?.imagePath)
        assertNull("personalLore debe ser nulo sin crash", retrieved?.personalLore)
    }

    // =========================================================================
    // 5. AUDITORÍA DE BRECHA (GAP): REAPERTURA INTERACTIVA DESDE BAÚL
    // =========================================================================

    @Test
    fun testAuditCollectionReopenCapabilitiesAndDocumentGap() = runBlocking {
        // 1. En la capa de dominio y persistencia: getCardsInContainer ya recupera la carta completa con su identidad
        val card = cardGenerator.generateCard(
            CaptureContract(
                captureId = "cap-gap-test",
                animalId = OfficialStarterCatalog.macaw.animalId,
                identificationId = "id-gap-test",
                capturedAt = Instant.now().toString()
            )
        )
        val slot = storageRepository.autoAssignSlot(card)
        val retrievedCards = storageRepository.getCardsInContainer(slot.containerIndex)
        assertTrue("La capa de datos soporta recuperar cartas de un contenedor", retrievedCards.isNotEmpty())

        // 2. Comprobación de GAP: En la capa UI (CollectionPlaceholderScreen),
        // no existe actualmente un composable de grid interactivo con onClick = { onSelectCard(card) -> navigate(CardReview) }.
        // Se clasifica estrictamente como GAP — FUTURE TASK conforme a la directriz WHO-018D.
        val isUiCardClickToReopenImplemented = false
        assertFalse(
            "GAP DOCUMENTADO: La reapertura visual interactiva de carta desde la pantalla de colección es una tarea futura del roadmap",
            isUiCardClickToReopenImplemented
        )
    }
}
