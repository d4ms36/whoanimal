package com.whoanimal.app

import com.whoanimal.app.domain.boundary.StorageCapacityInfo
import com.whoanimal.app.domain.model.AnimalCardContract
import com.whoanimal.app.domain.model.BiologicalSex
import com.whoanimal.app.domain.model.CaptureContract
import com.whoanimal.app.domain.model.DecisionStatus
import com.whoanimal.app.domain.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainBoundaryTest {

    @Test
    fun testCaptureContractIntegrityAndImmutability() {
        val capture = CaptureContract(
            captureId = "cap-uuid-1234",
            animalId = "canis_lupus_familiaris",
            identificationId = "id-result-5678",
            sex = BiologicalSex.FEMALE,
            capturedAt = "2026-09-07T00:00:00Z",
            displayLocation = "Madrid, ES"
        )

        assertEquals("cap-uuid-1234", capture.captureId)
        assertEquals("canis_lupus_familiaris", capture.animalId)
        assertEquals("id-result-5678", capture.identificationId)
        assertEquals(BiologicalSex.FEMALE, capture.sex)
        assertEquals("2026-09-07T00:00:00Z", capture.capturedAt)
        assertEquals("Madrid, ES", capture.displayLocation)
    }

    @Test
    fun testAnimalCardContractOntologySeparation() {
        val capture = CaptureContract(
            captureId = "cap-9999",
            animalId = "felis_catus",
            identificationId = "id-1111",
            capturedAt = "2026-09-07T01:00:00Z"
        )

        val card = AnimalCardContract(
            cardId = "card-unique-4444",
            animalId = capture.animalId,
            specimenNumber = 1,
            generation = "genesis",
            issuedAt = "2026-09-07T01:05:00Z",
            populationAtIssuance = 12,
            rarity = "COMMON",
            captureId = capture.captureId,
            serial = "WA-FEL-4444",
            verificationStatus = VerificationStatus.UNVERIFIED
        )

        // Ontological rule: Capture != Card
        assertNotEquals("Card ID must be distinct from Capture ID", capture.captureId, card.cardId)
        assertEquals(capture.captureId, card.captureId)
        assertEquals(capture.animalId, card.animalId)
        assertEquals(VerificationStatus.UNVERIFIED, card.verificationStatus)
        assertTrue("Serial must not contain exact GPS data", !card.serial.contains(","))
    }

    @Test
    fun testStorageCapacityInfoBoundary() {
        val storage = StorageCapacityInfo(
            totalContainers = 10,
            slotsPerContainer = 30,
            totalCapacity = 300,
            occupiedSlots = 42
        )

        assertEquals(10, storage.totalContainers)
        assertEquals(30, storage.slotsPerContainer)
        assertEquals(300, storage.totalCapacity)
        assertEquals(42, storage.occupiedSlots)
        assertEquals(258, storage.availableSlots)
    }

    @Test
    fun testDecisionStatuses() {
        val accepted = DecisionStatus.ACCEPTED
        val rejected = DecisionStatus.REJECTED
        val cancelled = DecisionStatus.CANCELLED

        assertNotNull(accepted)
        assertNotNull(rejected)
        assertNotNull(cancelled)
    }
}
