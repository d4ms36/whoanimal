package com.whoanimal.app.domain.identification

import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.model.CandidateSpeciesContract
import com.whoanimal.app.domain.model.IdentificationResultContract
import com.whoanimal.app.domain.model.ObservationContract
import com.whoanimal.app.domain.model.TaxonomyContract
import java.time.Instant
import java.util.UUID

/**
 * Excepciones del módulo de identificación de Android.
 */
open class IdentificationException(message: String) : Exception(message)
class InvalidObservationException(message: String) : IdentificationException(message)
class IdentificationProviderException(message: String) : IdentificationException(message)

/**
 * Catálogo zoológico oficial inicial embebido para Alpha 0.1.
 * Contiene especies reales verificadas procedentes de data/species/.
 * Nunca se inventan datos taxonómicos ni científicos.
 */
object OfficialStarterCatalog {
    val dog = AnimalProfileContract(
        animalId = "99fcbd5c-911d-4cee-af0c-9b5d4fb1e53f",
        scientificName = "Canis lupus familiaris",
        commonName = "Perro doméstico",
        taxonomy = TaxonomyContract(
            kingdom = "Animalia",
            phylum = "Chordata",
            className = "Mammalia",
            order = "Carnivora",
            family = "Canidae",
            genus = "Canis",
            species = "Canis lupus familiaris"
        ),
        conservationStatus = "NE",
        isRareSpecies = false,
        habitat = "Áreas habitadas por humanos a nivel global",
        diet = "Omnívoro",
        lifespanYears = 13,
        sizeCm = 60,
        weightKg = 20.0,
        activityCycle = "Diurno",
        nativeRegions = listOf("Global"),
        curiosity = "Posee un sentido del olfato hasta 100.000 veces más sensible que el humano."
    )

    val cat = AnimalProfileContract(
        animalId = "a29bebb6-c73e-4b24-a7fc-14ff4c000101",
        scientificName = "Felis catus",
        commonName = "Gato doméstico",
        taxonomy = TaxonomyContract(
            kingdom = "Animalia",
            phylum = "Chordata",
            className = "Mammalia",
            order = "Carnivora",
            family = "Felidae",
            genus = "Felis",
            species = "Felis catus"
        ),
        conservationStatus = "NE",
        isRareSpecies = false,
        habitat = "Áreas habitadas por humanos a nivel global",
        diet = "Carnívoro",
        lifespanYears = 15,
        sizeCm = 46,
        weightKg = 4.5,
        activityCycle = "Crepuscular",
        nativeRegions = listOf("Global"),
        curiosity = "Pasan aproximadamente el 70% de su vida durmiendo y acicalándose."
    )

    val jaguar = AnimalProfileContract(
        animalId = "b38ceaa5-b82d-4c13-a6eb-25ee3b111202",
        scientificName = "Panthera onca",
        commonName = "Jaguar",
        taxonomy = TaxonomyContract(
            kingdom = "Animalia",
            phylum = "Chordata",
            className = "Mammalia",
            order = "Carnivora",
            family = "Felidae",
            genus = "Panthera",
            species = "Panthera onca"
        ),
        conservationStatus = "NT",
        isRareSpecies = true,
        habitat = "Bosques tropicales y selvas",
        diet = "Carnívoro",
        lifespanYears = 15,
        sizeCm = 170,
        weightKg = 95.5,
        activityCycle = "Diurno",
        nativeRegions = listOf("América del Sur", "América Central"),
        curiosity = "Posee la mordida más potente de todos los grandes felinos en relación a su tamaño.",
        dangerLevel = "Precaución: Gran depredador carnívoro. Observar a distancia segura en hábitats naturales."
    )

    val macaw = AnimalProfileContract(
        animalId = "c47bfaa4-a93e-4d24-b7ec-36ff2c222303",
        scientificName = "Ara macao",
        commonName = "Guacamayo rojo",
        taxonomy = TaxonomyContract(
            kingdom = "Animalia",
            phylum = "Chordata",
            className = "Aves",
            order = "Psittaciformes",
            family = "Psittacidae",
            genus = "Ara",
            species = "Ara macao"
        ),
        conservationStatus = "LC",
        isRareSpecies = false,
        habitat = "Selvas tropicales",
        diet = "Herbívoro",
        lifespanYears = 50,
        sizeCm = 85,
        weightKg = 1.0,
        activityCycle = "Diurno",
        nativeRegions = listOf("América Central", "América del Sur"),
        curiosity = "Forma parejas monógamas de por vida y sus llamadas pueden escucharse a kilómetros."
    )

    val allSpecies = listOf(dog, cat, jaguar, macaw)

    fun findById(animalId: String): AnimalProfileContract? =
        allSpecies.firstOrNull { it.animalId == animalId }
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
    private val catalog: List<AnimalProfileContract> = OfficialStarterCatalog.allSpecies
) : IdentificationProvider {

    override fun identify(observation: ObservationContract): List<CandidateSpeciesContract> {
        if (catalog.isEmpty()) {
            throw IdentificationProviderException("El catálogo de especies está vacío.")
        }

        val imagePathLower = observation.imagePath.lowercase()
        val matchedProfile = catalog.find { profile ->
            val sciSlug = profile.scientificName.lowercase().replace(" ", "_")
            val commSlug = profile.commonName.lowercase()
            imagePathLower.contains(sciSlug) || imagePathLower.contains(commSlug) || imagePathLower.contains(profile.animalId.lowercase())
        } ?: catalog.first()

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
        val alternative = catalog.find { it.animalId != matchedProfile.animalId }
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
