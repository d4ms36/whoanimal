package com.whoanimal.app.data.catalog

import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.model.TaxonomyContract
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 * Parser de dominio zoológico para archivos JSON de especies.
 *
 * Transforma mecánicamente los datos del esquema canónico oficial (docs/DATASET_SPEC.md)
 * a los contratos inmutables de dominio [AnimalProfileContract].
 *
 * Características:
 * - Sin dependencias externas runtime (utiliza org.json integrado en Android).
 * - Soporta arrays JSON `[{...}]` o documentos unitarios `{...}`.
 * - Tolerante a fallos: omite registros sintácticamente inválidos sin interrumpir el proceso global.
 */
object SpeciesJsonParser {

    /**
     * Parsea un InputStream que contiene JSON UTF-8 de especies.
     */
    fun parseStream(inputStream: InputStream): List<AnimalProfileContract> {
        val jsonText = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
        return parseString(jsonText)
    }

    /**
     * Parsea una cadena JSON de especies.
     */
    fun parseString(jsonString: String): List<AnimalProfileContract> {
        val trimmed = jsonString.trim()
        if (trimmed.isEmpty()) return emptyList()

        val results = mutableListOf<AnimalProfileContract>()

        try {
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i) ?: continue
                    parseSingleObject(obj)?.let { results.add(it) }
                }
            } else if (trimmed.startsWith("{")) {
                val obj = JSONObject(trimmed)
                parseSingleObject(obj)?.let { results.add(it) }
            }
        } catch (e: Exception) {
            // Error de parseo seguro: retorna lo que haya podido parsear o lista vacía
        }

        return results
    }

    /**
     * Parsea un [JSONObject] individual mapeando sus campos canónicos y anotaciones pedagógicas.
     */
    fun parseSingleObject(obj: JSONObject): AnimalProfileContract? {
        val animalId = obj.optString("animal_id", "").trim()
        val scientificName = obj.optString("scientific_name", "").trim()
        val commonName = obj.optString("common_name", "").trim()

        if (animalId.isEmpty() || scientificName.isEmpty() || commonName.isEmpty()) {
            return null
        }

        val taxObj = obj.optJSONObject("taxonomy") ?: JSONObject()
        val taxonomy = TaxonomyContract(
            kingdom = taxObj.optString("kingdom", "Animalia"),
            phylum = taxObj.optString("phylum", ""),
            className = taxObj.optString("class", ""),
            order = taxObj.optString("order", ""),
            family = taxObj.optString("family", ""),
            genus = taxObj.optString("genus", ""),
            species = taxObj.optString("species", scientificName)
        )

        val conservationStatus = obj.optString("conservation_status", "LC").ifBlank { "LC" }
        val isRareSpecies = obj.optBoolean("is_rare_species", false)
        val habitat = obj.optString("habitat", "").takeIf { it.isNotBlank() }
        val diet = obj.optString("diet", "").takeIf { it.isNotBlank() }

        val lifespanYears = if (obj.has("lifespan_years") && !obj.isNull("lifespan_years")) {
            obj.optInt("lifespan_years")
        } else null

        val sizeCm = if (obj.has("size_cm") && !obj.isNull("size_cm")) {
            obj.optInt("size_cm")
        } else null

        val weightKg = if (obj.has("weight_kg") && !obj.isNull("weight_kg")) {
            obj.optDouble("weight_kg")
        } else null

        val activityCycle = obj.optString("activity_cycle", "").takeIf { it.isNotBlank() }

        val nativeRegions = mutableListOf<String>()
        val regionsArray = obj.optJSONArray("native_regions")
        if (regionsArray != null) {
            for (i in 0 until regionsArray.length()) {
                val region = regionsArray.optString(i, "").trim()
                if (region.isNotEmpty()) {
                    nativeRegions.add(region)
                }
            }
        }

        // Anotaciones pedagógicas (curiosidades científicas y avisos preventivos)
        val annotation = SpeciesPedagogicalAnnotations.getAnnotation(scientificName)
        val curiosity = if (obj.has("curiosity") && !obj.isNull("curiosity")) {
            obj.optString("curiosity")
        } else {
            annotation?.curiosity
        }

        val dangerLevel = if (obj.has("danger_level") && !obj.isNull("danger_level")) {
            obj.optString("danger_level")
        } else {
            annotation?.dangerLevel
        }

        return AnimalProfileContract(
            animalId = animalId,
            scientificName = scientificName,
            commonName = commonName,
            taxonomy = taxonomy,
            conservationStatus = conservationStatus,
            isRareSpecies = isRareSpecies,
            habitat = habitat,
            diet = diet,
            lifespanYears = lifespanYears,
            sizeCm = sizeCm,
            weightKg = weightKg,
            activityCycle = activityCycle,
            nativeRegions = nativeRegions,
            curiosity = curiosity,
            dangerLevel = dangerLevel
        )
    }
}
