package com.whoanimal.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.domain.identification.IdentificationService
import com.whoanimal.app.domain.model.ObservationContract
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import java.io.File
import java.time.Instant
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class CameraCaptureFlowTest {

    private lateinit var context: Context
    private lateinit var identificationService: IdentificationService
    private val createdFiles = mutableListOf<File>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        identificationService = IdentificationService()
    }

    @After
    fun tearDown() {
        createdFiles.forEach { file ->
            if (file.exists()) file.delete()
        }
        createdFiles.clear()
    }

    private fun createTemporaryPhoto(name: String = "test_photo_${UUID.randomUUID()}.jpg", sizeBytes: Int = 1024): File {
        val file = File(context.cacheDir, name)
        file.writeBytes(ByteArray(sizeBytes) { 0x55 })
        createdFiles.add(file)
        return file
    }

    // ==========================================
    // 1. OBSERVATION CREATION FROM REAL IMAGE
    // ==========================================

    @Test
    fun testObservationCreationWithImageFilePreservesCanonicalContract() {
        val photoFile = createTemporaryPhoto()
        assertTrue("Temporary photo file must exist", photoFile.exists())
        assertTrue("Temporary photo file must have size", photoFile.length() > 0)

        val observationId = UUID.randomUUID().toString()
        val timestamp = Instant.now().toString()

        val observation = ObservationContract(
            observationId = observationId,
            createdAt = timestamp,
            imagePath = photoFile.absolutePath
        )

        // Validar preservación de campos y ruta absoluta al archivo capturado
        assertEquals(observationId, observation.observationId)
        assertEquals(timestamp, observation.createdAt)
        assertEquals(photoFile.absolutePath, observation.imagePath)
        assertTrue(File(observation.imagePath).exists())
    }

    @Test
    fun testObservationFromRealImageFeedsIdentificationService() {
        val photoFile = createTemporaryPhoto("felis_catus_capture.jpg")

        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = Instant.now().toString(),
            imagePath = photoFile.absolutePath
        )

        val result = identificationService.identify(observation)

        assertNotNull(result)
        assertEquals(observation.observationId, result.observationId)
        assertTrue("Must return at least 1 candidate", result.candidateSpecies.isNotEmpty())
        assertTrue("Candidates must have confidence scores in [0.0, 1.0]", result.candidateSpecies.all { it.confidence in 0.0..1.0 })
        assertNotNull("Must select top candidate", result.topCandidate)
    }

    // ==========================================
    // 2. PERMISSION EVALUATION TESTS
    // ==========================================

    @Test
    fun testCameraPermissionGrantedEvaluation() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val shadowApp = Shadows.shadowOf(app)

        // Conceder permiso en entorno Shadow
        shadowApp.grantPermissions(Manifest.permission.CAMERA)

        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        assertEquals(PackageManager.PERMISSION_GRANTED, check)
    }

    @Test
    fun testCameraPermissionDeniedEvaluation() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val shadowApp = Shadows.shadowOf(app)

        // Denegar permiso en entorno Shadow
        shadowApp.denyPermissions(Manifest.permission.CAMERA)

        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        assertEquals(PackageManager.PERMISSION_DENIED, check)
    }

    // ==========================================
    // 3. SIMULATED CAPTURE FALLBACK
    // ==========================================

    @Test
    fun testSimulatedCaptureProducesValidObservationAndIdentifies() {
        val fallbackFile = File(context.cacheDir, "simulated_test_${UUID.randomUUID()}.jpg")
        fallbackFile.writeBytes(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte()))
        createdFiles.add(fallbackFile)

        assertTrue(fallbackFile.exists())
        assertTrue(fallbackFile.length() >= 4)

        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = Instant.now().toString(),
            imagePath = fallbackFile.absolutePath
        )

        val result = identificationService.identify(observation)
        assertNotNull(result)
        assertEquals(observation.observationId, result.observationId)
        assertTrue(result.candidateSpecies.isNotEmpty())
    }

    // ==========================================
    // 4. ONTOLOGY ISOLATION
    // ==========================================

    @Test
    fun testObservationAndResultNeverViolateOntology() {
        val photoFile = createTemporaryPhoto()

        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = Instant.now().toString(),
            imagePath = photoFile.absolutePath
        )

        val result = identificationService.identify(observation)

        // Observation no es un resultado
        assertTrue(observation.observationId.isNotBlank())
        assertTrue(result.identificationId.isNotBlank())
        assertEquals(observation.observationId, result.observationId)

        // Ni la foto ni la observación generan Card directamente
        assertFalse("Observation must not contain card_id", observation.toString().contains("cardId"))
        assertFalse("Result must not contain card_id", result.toString().contains("cardId"))
    }
}
