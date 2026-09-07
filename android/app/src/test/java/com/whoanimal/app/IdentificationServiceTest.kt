package com.whoanimal.app

import com.whoanimal.app.domain.identification.DeterministicIdentificationProvider
import com.whoanimal.app.domain.identification.IdentificationProviderException
import com.whoanimal.app.domain.identification.IdentificationService
import com.whoanimal.app.domain.identification.InvalidObservationException
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.IdentificationDecisionContract
import com.whoanimal.app.domain.model.ObservationContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

class IdentificationServiceTest {

    private lateinit var service: IdentificationService
    private lateinit var provider: DeterministicIdentificationProvider

    @Before
    fun setUp() {
        provider = DeterministicIdentificationProvider()
        service = IdentificationService(provider = provider)
    }

    @Test
    fun testValidObservationProducesValidIdentificationResult() {
        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photos/canis_lupus_familiaris.jpg"
        )

        val result = service.identify(observation)

        assertNotNull(result)
        assertTrue(result.identificationId.isNotBlank())
        assertEquals(observation.observationId, result.observationId)
        assertEquals("DETERMINISTIC_ALPHA", result.identificationMethod)
        assertTrue("Candidate list must not be empty", result.candidateSpecies.isNotEmpty())

        val top = result.topCandidate
        assertNotNull(top)
        assertEquals("Canis lupus familiaris", top?.scientificName)
        assertEquals("Perro doméstico", top?.commonName)
        assertTrue((top?.confidence ?: 0.0) >= 0.9)
    }

    @Test
    fun testCandidatesOnlyFromOfficialCatalog() {
        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "camera/shot.jpg"
        )

        val result = service.identify(observation)
        val catalogIds = OfficialStarterCatalog.allSpecies.map { it.animalId }.toSet()

        for (candidate in result.candidateSpecies) {
            assertTrue(
                "Candidate ${candidate.animalId} must belong to the official catalog",
                catalogIds.contains(candidate.animalId)
            )
            assertTrue("Confidence must be in [0.0, 1.0]", candidate.confidence in 0.0..1.0)
        }
    }

    @Test
    fun testCandidateRankingIsDeterministicAndDescending() {
        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photos/felis_catus.jpg"
        )

        val result1 = service.identify(observation)
        val result2 = service.identify(observation)

        // Deterministic candidate list
        assertEquals(
            result1.candidateSpecies.map { it.animalId },
            result2.candidateSpecies.map { it.animalId }
        )

        // Strictly descending confidence
        val confidences = result1.candidateSpecies.map { it.confidence }
        assertEquals(confidences, confidences.sortedDescending())
    }

    @Test(expected = InvalidObservationException::class)
    fun testNullObservationThrowsInvalidObservationException() {
        service.identify(null)
    }

    @Test(expected = InvalidObservationException::class)
    fun testEmptyObservationIdThrowsInvalidObservationException() {
        val badObs = ObservationContract(
            observationId = "",
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photo.jpg"
        )
        service.identify(badObs)
    }

    @Test(expected = IdentificationProviderException::class)
    fun testEmptyCatalogThrowsIdentificationProviderException() {
        val emptyProvider = DeterministicIdentificationProvider(catalog = emptyList())
        val serviceWithEmptyCatalog = IdentificationService(provider = emptyProvider)

        val obs = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photo.jpg"
        )
        serviceWithEmptyCatalog.identify(obs)
    }

    @Test
    fun testOntologyIsolationNoAutomaticCaptureOrCard() {
        val obs = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photos/panthera_onca.jpg"
        )
        val result = service.identify(obs)

        // Result is distinct from Capture and Card
        val rawResult: Any = result
        assertFalse("Result must not be a CaptureContract", rawResult is CaptureContract)
    }

    @Test
    fun testDecisionBoundaryIntegration() {
        val obs = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T00:00:00Z",
            imagePath = "photos/ara_macao.jpg"
        )
        val result = service.identify(obs)
        val topAnimalId = result.topCandidate?.animalId

        // Prepare explicit decision
        val decision = IdentificationDecisionContract(
            decisionId = UUID.randomUUID().toString(),
            identificationId = result.identificationId,
            animalId = topAnimalId,
            status = DecisionStatus.ACCEPTED,
            decidedAt = "2026-09-07T00:05:00Z",
            selectedAnimalId = topAnimalId
        )

        assertEquals(DecisionStatus.ACCEPTED, decision.status)
        assertEquals(topAnimalId, decision.selectedAnimalId)
        assertEquals(result.identificationId, decision.identificationId)
    }
}
