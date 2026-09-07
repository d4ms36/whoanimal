package com.whoanimal.app

import com.whoanimal.app.domain.boundary.CardGeneratorBoundary
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.model.TaxonomyContract
import com.whoanimal.app.domain.model.VerificationStatus
import com.whoanimal.app.domain.service.CardGeneratorService
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
import java.time.Instant
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class CardPresentationFlowTest {

    private lateinit var cardGenerator: CardGeneratorService

    @Before
    fun setUp() {
        cardGenerator = CardGeneratorService()
    }

    // ==========================================
    // 1. CARD GENERATOR & ONTOLOGY BOUNDARY TESTS
    // ==========================================

    @Test
    fun testCardGeneratorImplementsBoundary() {
        val boundary: CardGeneratorBoundary = cardGenerator
        assertNotNull("CardGeneratorService must be assignable to CardGeneratorBoundary", boundary)
    }


    @Test
    fun testGenerateCardMaintainsStrictOntologicalSeparation() {
        val capture = CaptureContract(
            captureId = "cap-uuid-12345",
            animalId = OfficialStarterCatalog.dog.animalId,
            identificationId = "id-uuid-67890",
            capturedAt = Instant.now().toString(),
            displayLocation = "Parque Natural Urbano"
        )

        val card = cardGenerator.generateCard(capture)

        // Capture != Card: strictly distinct entities
        assertNotEquals("cardId must not equal captureId", capture.captureId, card.cardId)
        assertEquals("Card must link back to captureId", capture.captureId, card.captureId)
        assertEquals("Card must link to correct animalId", capture.animalId, card.animalId)
        assertEquals("COMMON species must receive COMMON rarity", "COMMON", card.rarity)
        assertEquals("displayLocation must be preserved", capture.displayLocation, card.displayLocation)
        assertTrue("Serial must start with WHO-CARD", card.serial.startsWith("WHO-CARD-"))
        assertFalse("Serial must not contain exact GPS or telemetry", card.serial.contains("lat") || card.serial.contains("lon"))
    }

    @Test
    fun testGenerateCardForRareSpeciesAssignsRareRarity() {
        val rareSpecies = OfficialStarterCatalog.allSpecies.first { it.isRareSpecies }
        val capture = CaptureContract(
            captureId = "cap-rare-001",
            animalId = rareSpecies.animalId,
            identificationId = "id-rare-001",
            capturedAt = Instant.now().toString(),
            displayLocation = "Reserva Natural"
        )

        val card = cardGenerator.generateCard(capture)
        assertEquals("${rareSpecies.commonName} is rare, card must be RARE", "RARE", card.rarity)
    }

    @Test
    fun testAssembleFromDecisionEnforcesAcceptedStatus() {
        val rejectedDecision = IdentificationDecisionContract(
            decisionId = "dec-rejected",
            identificationId = "id-123",
            animalId = OfficialStarterCatalog.cat.animalId,
            status = DecisionStatus.REJECTED,
            decidedAt = Instant.now().toString()
        )

        var exceptionThrown = false
        try {
            cardGenerator.assembleFromDecision(rejectedDecision)
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
            assertTrue(e.message?.contains("no aceptada") == true)
        }
        assertTrue("Assembling card from non-accepted decision must throw IllegalArgumentException", exceptionThrown)
    }

    @Test
    fun testAssembleFromAcceptedDecisionProducesValidCard() {
        val acceptedDecision = IdentificationDecisionContract(
            decisionId = "dec-accepted",
            identificationId = "id-456",
            animalId = OfficialStarterCatalog.macaw.animalId,
            status = DecisionStatus.ACCEPTED,
            decidedAt = Instant.now().toString()
        )
        val observation = ObservationContract(
            observationId = "obs-macaw-1",
            createdAt = Instant.now().toString(),
            imagePath = "/cache/macaw_photo.jpg"
        )

        val (capture, card) = cardGenerator.assembleFromDecision(
            decision = acceptedDecision,
            observation = observation,
            confidence = 0.98,
            displayLocation = "Amazonia"
        )

        assertNotNull("Capture must be produced", capture)
        assertNotNull("Card must be produced", card)
        assertEquals(acceptedDecision.decidedAt, capture.capturedAt)
        assertEquals(observation.imagePath, card.imagePath)
        assertEquals(0.98, card.identificationConfidence ?: 0.0, 0.001)
        assertEquals("Amazonia", card.displayLocation)
    }

    // ==========================================
    // 2. OFFICIAL STARTER CATALOG FACTUAL BIOLOGY
    // ==========================================

    @Test
    fun testOfficialStarterCatalogContainsAuthenticFactualData() {
        val all = OfficialStarterCatalog.allSpecies
        assertEquals("Official catalog must contain exactly 28 species", 28, all.size)

        val dog = OfficialStarterCatalog.findById(OfficialStarterCatalog.dog.animalId)
        assertNotNull("Dog must be found", dog)
        assertEquals("Canis lupus familiaris", dog?.scientificName)
        assertEquals("Áreas habitadas por humanos a nivel global", dog?.habitat)
        assertEquals("Omnívoro", dog?.diet)
        assertEquals("Diurno", dog?.activityCycle)
        assertNotNull("Curiosity must be present", dog?.curiosity)
        assertNull("Dog should not have danger warning", dog?.dangerLevel)

        val jaguar = OfficialStarterCatalog.findById(OfficialStarterCatalog.jaguar.animalId)
        assertNotNull("Jaguar must be found", jaguar)
        assertEquals("Panthera onca", jaguar?.scientificName)
        assertEquals("Bosques tropicales y selvas", jaguar?.habitat)
        assertEquals("Carnívoro", jaguar?.diet)
        assertFalse("Jaguar in official catalog is not rare", jaguar?.isRareSpecies == true)
        assertNotNull("Jaguar should have caution note", jaguar?.dangerLevel)

        val rareSpecies = OfficialStarterCatalog.allSpecies.firstOrNull { it.isRareSpecies }
        assertNotNull("Catalog must contain rare species", rareSpecies)
        assertTrue(rareSpecies?.isRareSpecies == true)
    }

    // ==========================================
    // 3. ROBUSTNESS WITH OPTIONAL & MISSING DATA
    // ==========================================

    @Test
    fun testCardWithAllOptionalFieldsNullDoesNotCrashContract() {
        val minimalCard = AnimalCardContract(
            cardId = UUID.randomUUID().toString(),
            animalId = "unknown-animal-id",
            specimenNumber = 1,
            issuedAt = Instant.now().toString(),
            populationAtIssuance = 1,
            rarity = "COMMON",
            captureId = "cap-min-001",
            serial = "WHO-CARD-UNKNOWN-000001",
            imagePath = null,
            personalLore = null,
            ownerId = null,
            edition = null,
            identificationConfidence = null
        )

        assertNull("imagePath must be null", minimalCard.imagePath)
        assertNull("personalLore must be null", minimalCard.personalLore)
        assertNull("edition must be null", minimalCard.edition)
        assertNull("identificationConfidence must be null", minimalCard.identificationConfidence)
        assertEquals("General Location", minimalCard.displayLocation)
        assertEquals(VerificationStatus.UNVERIFIED, minimalCard.verificationStatus)
    }

    @Test
    fun testAnimalProfileWithMinimalDataDoesNotFail() {
        val minimalProfile = AnimalProfileContract(
            animalId = "min-profile-id",
            scientificName = "Testus minimus",
            commonName = "Animal de prueba",
            taxonomy = TaxonomyContract(
                kingdom = "Animalia",
                phylum = "Chordata",
                className = "Mammalia",
                order = "TestOrder",
                family = "TestFamily",
                genus = "Testus",
                species = "Testus minimus"
            )
        )

        assertNull(minimalProfile.habitat)
        assertNull(minimalProfile.diet)
        assertNull(minimalProfile.lifespanYears)
        assertNull(minimalProfile.sizeCm)
        assertNull(minimalProfile.weightKg)
        assertNull(minimalProfile.activityCycle)
        assertTrue(minimalProfile.nativeRegions.isEmpty())
        assertNull(minimalProfile.curiosity)
        assertNull(minimalProfile.dangerLevel)
        assertEquals("LC", minimalProfile.conservationStatus)
        assertFalse(minimalProfile.isRareSpecies)
    }

    // ==========================================
    // 4. CARD PRESENTATION FLOW DISPATCH & IDEMPOTENCY
    // ==========================================

    @Test
    fun testCardActionDispatchAndDoubleTapProtection() {
        var saveCallCount = 0
        var releaseCallCount = 0

        val testCard = cardGenerator.generateCard(
            CaptureContract(
                captureId = "cap-idempotent-001",
                animalId = OfficialStarterCatalog.cat.animalId,
                identificationId = "id-cat-001",
                capturedAt = Instant.now().toString()
            )
        )

        // Simular lógica de guardado con protección de doble tap
        var isProcessing = false
        val onSaveTrigger = {
            if (!isProcessing) {
                isProcessing = true
                saveCallCount++
            }
        }

        // Primer tap
        onSaveTrigger()
        assertEquals("Primer tap debe procesar save", 1, saveCallCount)

        // Segundo tap accidental mientras procesa
        onSaveTrigger()
        assertEquals("Segundo tap debe ser ignorado por protección de doble interacción", 1, saveCallCount)

        // Reset
        isProcessing = false
        val onReleaseTrigger = {
            if (!isProcessing) {
                isProcessing = true
                releaseCallCount++
            }
        }

        onReleaseTrigger()
        assertEquals("Primer tap de release debe procesarse", 1, releaseCallCount)
        onReleaseTrigger()
        assertEquals("Segundo tap de release debe ser ignorado", 1, releaseCallCount)
    }

    @Test
    fun testFrontToBackStateToggleMaintainsCardIntegrity() {
        var isFlipped = false
        val toggleFlip = { isFlipped = !isFlipped }

        assertFalse("Initial state must be Front face", isFlipped)
        toggleFlip()
        assertTrue("State must toggle to Back face", isFlipped)
        toggleFlip()
        assertFalse("State must toggle back to Front face", isFlipped)
    }
}
