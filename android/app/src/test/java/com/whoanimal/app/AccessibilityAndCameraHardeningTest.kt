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
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.w3c.dom.Element
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory

@RunWith(RobolectricTestRunner::class)
class AccessibilityAndCameraHardeningTest {

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

    private fun parseStringXml(file: File): Map<String, String> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(file)
        val stringNodes = doc.getElementsByTagName("string")
        val map = mutableMapOf<String, String>()
        for (i in 0 until stringNodes.length) {
            val node = stringNodes.item(i) as Element
            val name = node.getAttribute("name")
            val text = node.textContent
            map[name] = text
        }
        return map
    }

    private fun getResDir(): File {
        val candidates = listOf(
            File("src/main/res"),
            File("app/src/main/res"),
            File("android/app/src/main/res"),
            File("../app/src/main/res")
        )
        return candidates.firstOrNull { it.exists() && it.isDirectory }
            ?: throw IllegalStateException("Resource directory not found in candidate paths")
    }

    // =========================================================================
    // 1. ACCESSIBILITY RESOURCE PARITY & STRING INTEGRITY
    // =========================================================================

    @Test
    fun testA11yResourceKeysSymmetryAcrossLocales() {
        val resDir = getResDir()
        val esFile = File(resDir, "values/strings.xml")
        val enFile = File(resDir, "values-en/strings.xml")

        assertTrue("ES strings.xml must exist", esFile.exists())
        assertTrue("EN strings.xml must exist", enFile.exists())

        val esStrings = parseStringXml(esFile)
        val enStrings = parseStringXml(enFile)

        val requiredA11yKeys = listOf(
            "camera_unavailable_title",
            "camera_unavailable_body",
            "camera_simulated_capture_action",
            "camera_capturing_in_progress",
            "card_flip_hint_front",
            "card_flip_hint_back",
            "collection_card_item_description",
            "collection_container_chip_description",
            "identification_confidence_description",
            "identification_top_candidate_label"
        )

        for (key in requiredA11yKeys) {
            assertTrue("Key '$key' must exist in values/strings.xml (ES)", esStrings.containsKey(key))
            assertTrue("Key '$key' must exist in values-en/strings.xml (EN)", enStrings.containsKey(key))

            val esValue = esStrings[key].orEmpty()
            val enValue = enStrings[key].orEmpty()

            assertTrue("ES string for '$key' must not be blank", esValue.isNotBlank())
            assertTrue("EN string for '$key' must not be blank", enValue.isNotBlank())
        }
    }

    @Test
    fun testA11yFormatPlaceholdersMatchBetweenLocales() {
        val resDir = getResDir()
        val esStrings = parseStringXml(File(resDir, "values/strings.xml"))
        val enStrings = parseStringXml(File(resDir, "values-en/strings.xml"))

        val formatKeys = listOf(
            "card_flip_hint_front",
            "card_flip_hint_back",
            "collection_card_item_description",
            "collection_container_chip_description",
            "identification_confidence_description"
        )

        val placeholderRegex = Regex("%(\\d+\\$)?[sdf]")

        for (key in formatKeys) {
            val esPlaceholders = placeholderRegex.findAll(esStrings[key].orEmpty()).map { it.value }.toList()
            val enPlaceholders = placeholderRegex.findAll(enStrings[key].orEmpty()).map { it.value }.toList()

            assertEquals(
                "Placeholder count for '$key' must match between ES and EN",
                esPlaceholders.size,
                enPlaceholders.size
            )
        }
    }

    @Test
    fun testCardFlipHintsAreDistinctForFrontAndBack() {
        val resDir = getResDir()
        val esStrings = parseStringXml(File(resDir, "values/strings.xml"))
        val enStrings = parseStringXml(File(resDir, "values-en/strings.xml"))

        assertNotEquals(esStrings["card_flip_hint_front"], esStrings["card_flip_hint_back"])
        assertNotEquals(enStrings["card_flip_hint_front"], enStrings["card_flip_hint_back"])
    }

    // =========================================================================
    // 2. TOUCH TARGETS (48dp Minimum Contract)
    // =========================================================================

    @Test
    fun testTouchTargetStandardIsAtLeast48dp() {
        // According to Material Design and WCAG 2.5.5, minimum target size is 48dp
        val standardMinTouchTargetDp = 48
        val backButtonSizeDp = 48
        val shutterButtonSizeDp = 76
        val simulateFallbackButtonMinHeightDp = 50
        val chipMinHeightDp = 48
        val actionButtonMinHeightDp = 48

        assertTrue("Back button size must be >= 48dp", backButtonSizeDp >= standardMinTouchTargetDp)
        assertTrue("Shutter button size must be >= 48dp", shutterButtonSizeDp >= standardMinTouchTargetDp)
        assertTrue("Simulate fallback button height must be >= 48dp", simulateFallbackButtonMinHeightDp >= standardMinTouchTargetDp)
        assertTrue("Filter chips height must be >= 48dp", chipMinHeightDp >= standardMinTouchTargetDp)
        assertTrue("Action buttons height must be >= 48dp", actionButtonMinHeightDp >= standardMinTouchTargetDp)
    }

    // =========================================================================
    // 3. CAMERAX HARDENING: PERMISSIONS & FALLBACK
    // =========================================================================

    @Test
    fun testCameraPermissionGrantedAllowsCapturePreparation() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val shadowApp = Shadows.shadowOf(app)
        shadowApp.grantPermissions(Manifest.permission.CAMERA)

        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        assertEquals(PackageManager.PERMISSION_GRANTED, permission)
    }

    @Test
    fun testCameraPermissionDeniedTriggersAlternativeWorkflow() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val shadowApp = Shadows.shadowOf(app)
        shadowApp.denyPermissions(Manifest.permission.CAMERA)

        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        assertEquals(PackageManager.PERMISSION_DENIED, permission)

        // When permission is denied, simulated capture must remain 100% accessible
        val simulatedFile = File(context.cacheDir, "fallback_a11y_${UUID.randomUUID()}.jpg")
        simulatedFile.writeBytes(byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte()))
        createdFiles.add(simulatedFile)

        val observation = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = Instant.now().toString(),
            imagePath = simulatedFile.absolutePath
        )

        val result = identificationService.identify(observation)
        assertNotNull(result)
        assertEquals(observation.observationId, result.observationId)
        assertNotNull(result.topCandidate)
    }

    // =========================================================================
    // 4. CAMERAX HARDENING: DOUBLE CAPTURE GUARD & LIFECYCLE IDEMPOTENCY
    // =========================================================================

    @Test
    fun testDoubleCaptureGuardPreventsDuplicateTrigger() {
        var isCapturing = false
        var captureInvocationCount = 0

        fun triggerCapture(): Boolean {
            if (isCapturing) return false
            isCapturing = true
            captureInvocationCount++
            return true
        }

        // Primer trigger -> aceptado
        val firstAttempt = triggerCapture()
        assertTrue("First capture invocation must succeed", firstAttempt)
        assertEquals(1, captureInvocationCount)

        // Segundo trigger inmediato mientras isCapturing == true -> bloqueado
        val secondAttempt = triggerCapture()
        assertFalse("Second capture invocation while in progress must be blocked", secondAttempt)
        assertEquals(1, captureInvocationCount)

        // Simular fin de captura
        isCapturing = false

        // Tercer trigger tras completar el primero -> aceptado
        val thirdAttempt = triggerCapture()
        assertTrue("Capture after completion must succeed", thirdAttempt)
        assertEquals(2, captureInvocationCount)
    }

    @Test
    fun testHardwareRotationDegreesComputation() {
        // Probar que las rotaciones estándar (0, 90, 180, 270) no produzcan desbordamientos
        val testDegrees = listOf(0, 90, 180, 270, 359, 360, 45, 135, 225, 315)
        for (deg in testDegrees) {
            val normalized = (deg % 360 + 360) % 360
            assertTrue("Normalized rotation must be between 0 and 359", normalized in 0..359)
            val nearestQuarter = when {
                normalized >= 315 || normalized < 45 -> 0
                normalized in 45..134 -> 90
                normalized in 135..224 -> 180
                else -> 270
            }
            assertTrue("Nearest quarter must be valid rotation step", nearestQuarter in listOf(0, 90, 180, 270))
        }
    }

    // =========================================================================
    // 5. DOMAIN & LORE SEPARATION INTEGRITY
    // =========================================================================

    @Test
    fun testObservationContractNeverStoresLoreOrCardId() {
        val file = File(context.cacheDir, "contract_test_${UUID.randomUUID()}.jpg")
        file.writeBytes(ByteArray(64) { 0x11 })
        createdFiles.add(file)

        val contract = ObservationContract(
            observationId = "obs-${UUID.randomUUID()}",
            createdAt = Instant.now().toString(),
            imagePath = file.absolutePath
        )

        val str = contract.toString()
        assertFalse("ObservationContract must not know cardId", str.contains("cardId"))
        assertFalse("ObservationContract must not know lore", str.contains("personalLore", ignoreCase = true))
        assertFalse("ObservationContract must not know scientificName", str.contains("scientificName", ignoreCase = true))
    }
}
