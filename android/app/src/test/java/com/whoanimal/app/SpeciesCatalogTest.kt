package com.whoanimal.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.whoanimal.app.data.catalog.AssetSpeciesCatalogRepository
import com.whoanimal.app.data.catalog.DefaultSpeciesCatalogRepository
import com.whoanimal.app.data.catalog.SpeciesJsonParser
import com.whoanimal.app.domain.identification.DeterministicIdentificationProvider
import com.whoanimal.app.domain.identification.IdentificationProviderException
import com.whoanimal.app.domain.identification.IdentificationService
import com.whoanimal.app.domain.identification.OfficialStarterCatalog
import com.whoanimal.app.domain.model.ObservationContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SpeciesCatalogTest {

    private lateinit var context: Context
    private lateinit var repository: AssetSpeciesCatalogRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = AssetSpeciesCatalogRepository(context)
        DefaultSpeciesCatalogRepository.init(context)
    }

    @Test
    fun testCatalogContainsExpectedNumberOfSpecies() {
        val species = repository.getAllSpecies()
        assertEquals("El catálogo oficial sincronizado debe contener exactamente 28 especies", 28, species.size)
    }

    @Test
    fun testAllSpeciesHaveMandatoryFields() {
        val species = repository.getAllSpecies()
        assertTrue("El catálogo no debe estar vacío", species.isNotEmpty())

        for (item in species) {
            assertTrue("animalId no debe estar en blanco para ${item.commonName}", item.animalId.isNotBlank())
            assertTrue("scientificName no debe estar en blanco para ${item.animalId}", item.scientificName.isNotBlank())
            assertTrue("commonName no debe estar en blanco para ${item.animalId}", item.commonName.isNotBlank())

            // Taxonomía estricta obligatoria
            assertNotNull("Taxonomía debe existir", item.taxonomy)
            assertEquals("Reino debe ser Animalia", "Animalia", item.taxonomy.kingdom)
            assertTrue("Phylum no debe estar vacío", item.taxonomy.phylum.isNotBlank())
            assertTrue("Clase no debe estar vacía", item.taxonomy.className.isNotBlank())
            assertTrue("Orden no debe estar vacío", item.taxonomy.order.isNotBlank())
            assertTrue("Familia no debe estar vacía", item.taxonomy.family.isNotBlank())
            assertTrue("Género no debe estar vacío", item.taxonomy.genus.isNotBlank())
            assertTrue("Especie no debe estar vacía", item.taxonomy.species.isNotBlank())

            // Campos biológicos enriquecidos
            assertTrue("Estado de conservación debe ser válido", item.conservationStatus.isNotBlank())
            assertNotNull("Hábitat debe estar presente", item.habitat)
            assertNotNull("Dieta debe estar presente", item.diet)
        }
    }

    @Test
    fun testSpeciesIdsAndScientificNamesAreUnique() {
        val species = repository.getAllSpecies()
        val ids = mutableSetOf<String>()
        val scientificNames = mutableSetOf<String>()

        for (item in species) {
            val isIdUnique = ids.add(item.animalId)
            assertTrue("animal_id duplicado detectado: ${item.animalId}", isIdUnique)

            val isSciUnique = scientificNames.add(item.scientificName.lowercase())
            assertTrue("scientific_name duplicado detectado: ${item.scientificName}", isSciUnique)
        }

        assertEquals(28, ids.size)
        assertEquals(28, scientificNames.size)
    }

    @Test
    fun testFindByIdAndScientificName() {
        val species = repository.getAllSpecies()
        val first = species.first()

        val foundById = repository.findById(first.animalId)
        assertNotNull("Debe encontrar la especie por ID", foundById)
        assertEquals(first.scientificName, foundById?.scientificName)

        val foundBySci = repository.findByScientificName(first.scientificName)
        assertNotNull("Debe encontrar la especie por nombre científico", foundBySci)
        assertEquals(first.animalId, foundBySci?.animalId)

        val notFound = repository.findById("non-existent-uuid")
        assertNull("Debe retornar null para ID inexistente", notFound)
    }

    @Test
    fun testOfficialStarterCatalogDelegation() {
        val all = OfficialStarterCatalog.allSpecies
        assertEquals("OfficialStarterCatalog debe exponer las 28 especies del repositorio", 28, all.size)

        // Verificar acceso a especies clave
        val dog = OfficialStarterCatalog.dog
        assertEquals("Canis lupus familiaris", dog.scientificName)
        assertEquals("99fcbd5c-911d-4cee-af0c-9b5d4fb1e53f", dog.animalId)
        assertNotNull("Curiosidad de perro debe existir", dog.curiosity)
        assertNull("Perro no debe tener aviso de peligro", dog.dangerLevel)

        val jaguar = OfficialStarterCatalog.jaguar
        assertEquals("Panthera onca", jaguar.scientificName)
        assertEquals("d1930818-e880-4181-81e9-3bb96b13dcdb", jaguar.animalId)
        assertNotNull("Curiosidad de jaguar debe existir", jaguar.curiosity)
        assertNotNull("Jaguar debe tener aviso de precaución", jaguar.dangerLevel)

        val cat = OfficialStarterCatalog.cat
        assertEquals("Felis catus", cat.scientificName)
        assertEquals("32b02707-5b6d-45d5-83d3-8f7e8be95a76", cat.animalId)

        val macaw = OfficialStarterCatalog.macaw
        assertEquals("Ara macao", macaw.scientificName)
        assertEquals("5c5d17d4-1e67-48c5-9e55-406ab73baa65", macaw.animalId)
    }

    @Test
    fun testIdentificationServiceWithLoadedCatalogMatchesKnownSpecies() {
        val provider = DeterministicIdentificationProvider(catalogRepository = repository)
        val service = IdentificationService(provider = provider)

        // Observación con pista en ruta de imagen
        val obs = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T12:00:00Z",
            imagePath = "captures/field_panthera_onca_sample.jpg"
        )
        val result = service.identify(obs)
        assertNotNull(result)
        val top = result.topCandidate
        assertNotNull(top)
        assertEquals("Panthera onca", top?.scientificName)
        assertEquals("Jaguar", top?.commonName)
        assertEquals("d1930818-e880-4181-81e9-3bb96b13dcdb", top?.animalId)
        assertTrue((top?.confidence ?: 0.0) >= 0.90)
    }

    @Test
    fun testParserResilienceWithCorruptOrEmptyInput() {
        val emptyResult = SpeciesJsonParser.parseString("")
        assertTrue("Input vacío debe retornar lista vacía", emptyResult.isEmpty())

        val corruptResult = SpeciesJsonParser.parseString("{ corrupt json invalid [")
        assertTrue("JSON corrupto no debe crashear y debe retornar lista vacía", corruptResult.isEmpty())

        val streamResult = SpeciesJsonParser.parseStream(ByteArrayInputStream("not a json".toByteArray()))
        assertTrue("Stream corrupto no debe crashear", streamResult.isEmpty())

        val missingFieldsObj = """[{"animal_id": "123"}]"""
        val missingResult = SpeciesJsonParser.parseString(missingFieldsObj)
        assertTrue("Objeto sin campos requeridos debe omitirse sin crasheo", missingResult.isEmpty())
    }

    @Test(expected = IdentificationProviderException::class)
    fun testIdentificationServiceThrowsSafelyOnEmptyCatalog() {
        val emptyProvider = DeterministicIdentificationProvider(catalog = emptyList())
        val service = IdentificationService(provider = emptyProvider)

        val obs = ObservationContract(
            observationId = UUID.randomUUID().toString(),
            createdAt = "2026-09-07T12:00:00Z",
            imagePath = "captures/sample.jpg"
        )
        service.identify(obs)
    }
}
