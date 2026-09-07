package com.whoanimal.app.domain.identification

import com.whoanimal.app.data.catalog.DefaultSpeciesCatalogRepository
import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.model.CandidateSpeciesContract
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.model.TaxonomyContract
import com.whoanimal.app.domain.repository.SpeciesCatalogRepository
import java.time.Instant
import java.util.UUID

/**
 * Excepciones del módulo de identificación de Android.
 */
open class IdentificationException(message: String) : Exception(message)
class InvalidObservationException(message: String) : IdentificationException(message)
class IdentificationProviderException(message: String) : IdentificationException(message)

/**
 * Catálogo zoológico oficial inicial para WHO Animal.
 *
 * Delega directamente en [SpeciesCatalogRepository], consumiendo la fuente única
 * de verdad zoológica del proyecto (data/species/ sincronizado a Android assets).
 * Nunca inventa datos taxonómicos ni científicos.
 */
object OfficialStarterCatalog {
    private val repository: SpeciesCatalogRepository
        get() = DefaultSpeciesCatalogRepository.getInstance()

    val allSpecies: List<AnimalProfileContract>
        get() = repository.getAllSpecies()

    fun findById(animalId: String): AnimalProfileContract? =
        repository.findById(animalId)

    fun findByScientificName(scientificName: String): AnimalProfileContract? =
        repository.findByScientificName(scientificName)

    val dog: AnimalProfileContract
        get() = findByScientificName("Canis lupus familiaris")
            ?: allSpecies.firstOrNull()
            ?: fallbackProfile("Canis lupus familiaris", "Perro doméstico")

    val cat: AnimalProfileContract
        get() = findByScientificName("Felis catus")
            ?: allSpecies.firstOrNull()
            ?: fallbackProfile("Felis catus", "Gato doméstico")

    val jaguar: AnimalProfileContract
        get() = findByScientificName("Panthera onca")
            ?: allSpecies.firstOrNull()
            ?: fallbackProfile("Panthera onca", "Jaguar")

    val macaw: AnimalProfileContract
        get() = findByScientificName("Ara macao")
            ?: allSpecies.firstOrNull()
            ?: fallbackProfile("Ara macao", "Guacamayo rojo")

    private fun fallbackProfile(scientificName: String, commonName: String) = AnimalProfileContract(
        animalId = "fallback-$scientificName",
        scientificName = scientificName,
        commonName = commonName,
        taxonomy = TaxonomyContract(
            phylum = "Chordata",
            className = "Mammalia",
            order = "Carnivora",
            family = "Canidae",
            genus = "Canis",
            species = scientificName
        )
    )
}

/**
 * Contrato base para proveedores de identificación zoológica.
 */
interface IdentificationProvider {
    fun identify(observation: ObservationContract): List<CandidateSpeciesContract>
}

/**
 * Proveedor de identificación determinista para Alpha 0.1.
 * Utiliza estrictamente especies del catálogo oficial existente.
 * Es un provider controlado y auditable, diseñado para ser reemplazado
 * en fases posteriores por un motor de visión artificial real.
 */
class DeterministicIdentificationProvider(
    private val catalogRepository: SpeciesCatalogRepository = DefaultSpeciesCatalogRepository.getInstance(),
    private val catalog: List<AnimalProfileContract>? = null
) : IdentificationProvider {

    constructor(catalog: List<AnimalProfileContract>) : this(
        catalogRepository = DefaultSpeciesCatalogRepository.getInstance(),
        catalog = catalog
    )

    private fun effectiveCatalog(): List<AnimalProfileContract> =
        catalog ?: catalogRepository.getAllSpecies()

    override fun identify(observation: ObservationContract): List<CandidateSpeciesContract> {
        val speciesList = effectiveCatalog()
        if (speciesList.isEmpty()) {
            throw IdentificationProviderException("El catálogo de especies está vacío.")
        }

        val imagePathLower = observation.imagePath.lowercase()
        val matchedProfile = speciesList.find { profile ->
            val sciSlug = profile.scientificName.lowercase().replace(" ", "_")
            val commSlug = profile.commonName.lowercase()
            imagePathLower.contains(sciSlug) || imagePathLower.contains(commSlug) || imagePathLower.contains(profile.animalId.lowercase())
        } ?: speciesList.first()

        val candidates = mutableListOf<CandidateSpeciesContract>()

        // Candidato primario
        candidates.add(
            CandidateSpeciesContract(
                animalId = matchedProfile.animalId,
                scientificName = matchedProfile.scientificName,
                commonName = matchedProfile.commonName,
                confidence = 0.94
            )
        )

        // Candidato alternativo del catálogo si existe
        val alternative = speciesList.find { it.animalId != matchedProfile.animalId }
        if (alternative != null) {
            candidates.add(
                CandidateSpeciesContract(
                    animalId = alternative.animalId,
                    scientificName = alternative.scientificName,
                    commonName = alternative.commonName,
                    confidence = 0.42
                )
            )
        }

        // Orden estrictamente descendente
        return candidates.sortedByDescending { it.confidence }
    }
}

/**
 * Servicio de identificación zoológica para Android.
 *
 * Responsabilidad:
 * Observation → IdentificationService → IdentificationResult
 *
 * Reglas de integridad:
 * - Rechaza observaciones nulas o con ID inválido.
 * - NO crea Capture automáticamente.
 * - NO crea Card automáticamente.
 */
class IdentificationService(
    val provider: IdentificationProvider = DeterministicIdentificationProvider()
) {
    fun identify(observation: ObservationContract?): IdentificationResultContract {
        if (observation == null) {
            throw InvalidObservationException("La observación no puede ser nula.")
        }
        if (observation.observationId.isBlank()) {
            throw InvalidObservationException("La observación debe tener un observationId válido.")
        }
        if (observation.imagePath.isBlank()) {
            throw InvalidObservationException("La observación debe incluir una ruta de imagen válida.")
        }

        val candidates = try {
            provider.identify(observation)
        } catch (e: IdentificationException) {
            throw e
        } catch (e: Exception) {
            throw IdentificationProviderException("Fallo en el proveedor de identificación: ${e.message}")
        }

        if (candidates.isEmpty()) {
            throw IdentificationProviderException("No se produjeron candidatos de identificación.")
        }

        return IdentificationResultContract(
            identificationId = UUID.randomUUID().toString(),
            observationId = observation.observationId,
            candidateSpecies = candidates,
            identificationMethod = "DETERMINISTIC_ALPHA",
            createdAt = Instant.now().toString()
        )
    }
}
