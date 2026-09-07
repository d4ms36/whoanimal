package com.whoanimal.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * WHO-022 — Localization & UI String Externalization Tests.
 *
 * Valida la existencia, simetría 100% (ES/EN), integridad de placeholders/parámetros
 * y respeto a la frontera ontológica (datos zoológicos y Lore NO se externalizan como strings).
 */
class LocalizationResourcesTest {

    private val projectRoot: File = File(".").canonicalFile
    private val resDir: File = File(projectRoot, "src/main/res")
    private val esStringsFile: File = File(resDir, "values/strings.xml")
    private val enStringsFile: File = File(resDir, "values-en/strings.xml")

    private fun loadStringMap(file: File): Map<String, String> {
        assertTrue("El archivo de recursos debe existir: ${file.path}", file.exists())
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(file)
        val nodeList = doc.getElementsByTagName("string")
        val map = mutableMapOf<String, String>()
        for (i in 0 until nodeList.length) {
            val element = nodeList.item(i) as Element
            val name = element.getAttribute("name")
            val text = element.textContent
            map[name] = text
        }
        return map
    }

    @Test
    fun testResourceFilesExist() {
        assertTrue("El archivo de base en español debe existir", esStringsFile.exists())
        assertTrue("El archivo de base en inglés debe existir", enStringsFile.exists())
    }

    @Test
    fun testResourceSymmetryBetweenSpanishAndEnglish() {
        val esStrings = loadStringMap(esStringsFile)
        val enStrings = loadStringMap(enStringsFile)

        assertTrue("Deben existir al menos 100 recursos externalizados", esStrings.size >= 100)
        assertEquals(
            "Ambos archivos de recursos deben contener la misma cantidad exacta de claves",
            esStrings.size,
            enStrings.size
        )

        val missingInEn = esStrings.keys - enStrings.keys
        val missingInEs = enStrings.keys - esStrings.keys

        assertTrue("Todas las claves en español deben estar en inglés: $missingInEn", missingInEn.isEmpty())
        assertTrue("Todas las claves en inglés deben estar en español: $missingInEs", missingInEs.isEmpty())
    }

    @Test
    fun testKeyCategoriesPresent() {
        val esStrings = loadStringMap(esStringsFile)

        // General / Brand
        assertNotNull(esStrings["app_name"])
        assertNotNull(esStrings["tagline"])
        assertNotNull(esStrings["action_back"])
        assertNotNull(esStrings["action_save"])

        // Screens
        assertNotNull(esStrings["splash_loading"])
        assertNotNull(esStrings["welcome_start_action"])
        assertNotNull(esStrings["profile_title"])
        assertNotNull(esStrings["home_greeting"])
        assertNotNull(esStrings["home_start_capture_action"])
        assertNotNull(esStrings["camera_live_view"])
        assertNotNull(esStrings["capture_screen_title"])
        assertNotNull(esStrings["identification_screen_title"])
        assertNotNull(esStrings["card_mode_review_title"])
        assertNotNull(esStrings["collection_title"])

        // WHO-023: Lore Editor & Release
        assertNotNull(esStrings["lore_editor_title"])
        assertNotNull(esStrings["lore_editor_hint"])
        assertNotNull(esStrings["lore_char_counter"])
        assertNotNull(esStrings["lore_edits_remaining"])
        assertNotNull(esStrings["lore_limit_reached"])
        assertNotNull(esStrings["action_edit_lore"])
        assertNotNull(esStrings["card_persisted_release_dialog_title"])
        assertNotNull(esStrings["card_persisted_release_dialog_body"])

        // WHO-024: Accessibility & CameraX Hardening
        assertNotNull(esStrings["camera_unavailable_title"])
        assertNotNull(esStrings["camera_unavailable_body"])
        assertNotNull(esStrings["camera_simulated_capture_action"])
        assertNotNull(esStrings["camera_capturing_in_progress"])
        assertNotNull(esStrings["card_flip_hint_front"])
        assertNotNull(esStrings["card_flip_hint_back"])
        assertNotNull(esStrings["collection_card_item_description"])
        assertNotNull(esStrings["collection_container_chip_description"])
        assertNotNull(esStrings["identification_confidence_description"])
        assertNotNull(esStrings["identification_top_candidate_label"])
    }

    @Test
    fun testParameterIntegrityAcrossLocales() {
        val esStrings = loadStringMap(esStringsFile)
        val enStrings = loadStringMap(enStringsFile)

        val regex = Regex("%(\\d+\\$)?[-#+ 0,(]*\\d*(\\.\\d+)?[a-zA-Z%]")

        esStrings.forEach { (key, esValue) ->
            val enValue = enStrings[key] ?: error("Key $key missing in EN")
            val esPlaceholders = regex.findAll(esValue).map { it.value }.toList()
            val enPlaceholders = regex.findAll(enValue).map { it.value }.toList()

            assertEquals(
                "La cantidad de parámetros en '$key' debe ser idéntica en ES y EN ($esValue vs $enValue)",
                esPlaceholders.size,
                enPlaceholders.size
            )
        }
    }

    @Test
    fun testOntologicalSeparationScientificDataNotExternalizedAsUIStrings() {
        val esStrings = loadStringMap(esStringsFile)

        // Comprobación de que nombres científicos concretos o códigos de especies no son claves UI
        val forbiddenScientificKeys = listOf(
            "panthera_onca",
            "harpia_harpyja",
            "tremarctos_ornatus",
            "podocnemis_expansa",
            "vultur_gryphus",
            "morpho_helenor"
        )
        forbiddenScientificKeys.forEach { key ->
            assertFalse(
                "Nombres de especies taxonómicas no deben ser claves de recursos de UI: $key",
                esStrings.containsKey(key)
            )
        }
    }
}
