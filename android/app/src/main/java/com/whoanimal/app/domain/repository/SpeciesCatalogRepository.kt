package com.whoanimal.app.domain.repository

import com.whoanimal.app.domain.model.AnimalProfileContract

/**
 * Contrato de repositorio de dominio para acceso al catalogo zoologico oficial.
 * Fuente unica de verdad: data/species/ a Android assets a SpeciesCatalogRepository
 */
interface SpeciesCatalogRepository {
    fun getAllSpecies(): List<AnimalProfileContract>
    fun findById(animalId: String): AnimalProfileContract?
    fun findByScientificName(scientificName: String): AnimalProfileContract?
    fun search(query: String): List<AnimalProfileContract>
}
