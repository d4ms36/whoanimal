package com.whoanimal.app.data.catalog

import android.content.Context
import android.content.res.AssetManager
import com.whoanimal.app.domain.model.AnimalProfileContract
import com.whoanimal.app.domain.repository.SpeciesCatalogRepository
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Implementación de [SpeciesCatalogRepository] respaldada por assets de Android.
 *
 * Características:
 * - Operación 100% offline (sin dependencias de red, APIs ni permisos externos).
 * - Carga en memoria única (in-memory caching thread-safe): evita reparsear JSON en recomposiciones UI.
 * - Resiliencia a errores: si un asset está corrupto o falta, se omite de forma segura sin crash.
 * - Soporta tanto [AssetManager] como [ClassLoader] y sistema de archivos para ejecución transparente.
 */
class AssetSpeciesCatalogRepository(
    private val assetManagerProvider: (() -> AssetManager?)? = null,
    private val classLoader: ClassLoader? = null
) : SpeciesCatalogRepository {

    private val lock = Any()
    @Volatile
    private var isLoaded: Boolean = false
    private val speciesList = mutableListOf<AnimalProfileContract>()
    private val speciesById = mutableMapOf<String, AnimalProfileContract>()
    private val speciesByScientificName = mutableMapOf<String, AnimalProfileContract>()

    constructor(assetManager: AssetManager) : this(assetManagerProvider = { assetManager })
    constructor(context: Context) : this(assetManagerProvider = { context.assets })

    private fun ensureLoaded() {
        if (isLoaded) return
        synchronized(lock) {
            if (isLoaded) return

            val loaded = loadFromAssets()
            speciesList.clear()
            speciesById.clear()
            speciesByScientificName.clear()

            for (species in loaded) {
                speciesList.add(species)
                speciesById[species.animalId] = species
                val normalizedSci = species.scientificName.lowercase().trim()
                speciesByScientificName[normalizedSci] = species
            }

            isLoaded = true
        }
    }

    private fun loadFromAssets(): List<AnimalProfileContract> {
        val assetManager = try {
            assetManagerProvider?.invoke()
        } catch (_: Exception) {
            null
        }

        // 1. Estrategia Principal: Leer catálogo consolidado species_catalog.json
        val consolidatedStream = openAssetStream("species_catalog.json", assetManager)
        if (consolidatedStream != null) {
            val parsed = try {
                consolidatedStream.use { SpeciesJsonParser.parseStream(it) }
            } catch (_: Exception) {
                emptyList()
            }
            if (parsed.isNotEmpty()) {
                return parsed
            }
        }

        // 2. Estrategia Secundaria: Listar y cargar archivos individuales de species/
        val individualSpecies = mutableListOf<AnimalProfileContract>()
        if (assetManager != null) {
            try {
                val files = assetManager.list("species") ?: emptyArray()
                for (file in files) {
                    if (file.endsWith(".json")) {
                        try {
                            assetManager.open("species/$file").use { stream ->
                                individualSpecies.addAll(SpeciesJsonParser.parseStream(stream))
                            }
                        } catch (_: Exception) {
                            // Omisión segura de archivo corrupto
                        }
                    }
                }
            } catch (_: Exception) {
                // Falla segura
            }
        }

        // 3. Fallback de sistema de archivos local para pruebas unitarias JVM
        if (individualSpecies.isEmpty()) {
            val localDirs = listOf(
                "src/main/assets/species",
                "android/app/src/main/assets/species",
                "../../data/species",
                "../data/species",
                "data/species"
            )
            for (dirPath in localDirs) {
                try {
                    val dir = File(dirPath)
                    if (dir.exists() && dir.isDirectory) {
                        dir.listFiles { f -> f.extension == "json" }?.forEach { f ->
                            try {
                                FileInputStream(f).use { stream ->
                                    individualSpecies.addAll(SpeciesJsonParser.parseStream(stream))
                                }
                            } catch (_: Exception) {}
                        }
                        if (individualSpecies.isNotEmpty()) break
                    }
                } catch (_: Exception) {}
            }
        }

        return individualSpecies
    }

    private fun openAssetStream(fileName: String, assetManager: AssetManager?): InputStream? {
        val cleanName = fileName.removePrefix("/")

        // 1. Vía AssetManager si está disponible
        if (assetManager != null) {
            try {
                return assetManager.open(cleanName)
            } catch (_: Exception) {}
        }

        // 2. Vía ClassLoaders
        val candidateLoaders = listOfNotNull(
            classLoader,
            Thread.currentThread().contextClassLoader,
            AssetSpeciesCatalogRepository::class.java.classLoader,
            ClassLoader.getSystemClassLoader()
        )
        for (cl in candidateLoaders) {
            try {
                val stream = cl.getResourceAsStream(cleanName)
                if (stream != null) return stream
            } catch (_: Exception) {}
            try {
                val stream = cl.getResourceAsStream("assets/$cleanName")
                if (stream != null) return stream
            } catch (_: Exception) {}
        }

        try {
            val stream = AssetSpeciesCatalogRepository::class.java.getResourceAsStream("/$cleanName")
            if (stream != null) return stream
        } catch (_: Exception) {}

        // 3. Vía sistema de archivos local (JVM tests)
        val fileCandidates = listOf(
            "src/main/assets/$cleanName",
            "android/app/src/main/assets/$cleanName",
            "../android/app/src/main/assets/$cleanName"
        )
        for (filePath in fileCandidates) {
            try {
                val file = File(filePath)
                if (file.exists() && file.isFile) {
                    return FileInputStream(file)
                }
            } catch (_: Exception) {}
        }

        return null
    }

    override fun getAllSpecies(): List<AnimalProfileContract> {
        ensureLoaded()
        return speciesList.toList()
    }

    override fun findById(animalId: String): AnimalProfileContract? {
        ensureLoaded()
        return speciesById[animalId]
    }

    override fun findByScientificName(scientificName: String): AnimalProfileContract? {
        ensureLoaded()
        val normalized = scientificName.lowercase().trim()
        return speciesByScientificName[normalized]
    }

    override fun search(query: String): List<AnimalProfileContract> {
        ensureLoaded()
        val q = query.lowercase().trim()
        if (q.isEmpty()) return speciesList.toList()

        return speciesList.filter {
            it.commonName.lowercase().contains(q) ||
            it.scientificName.lowercase().contains(q) ||
            it.animalId.lowercase().contains(q)
        }
    }
}

/**
 * Singleton accesible para acceso global al catálogo zoológico en Android.
 * Permite inyección o reemplazo en tests mediante [setInstance].
 */
object DefaultSpeciesCatalogRepository {
    @Volatile
    private var instance: SpeciesCatalogRepository? = null

    fun getInstance(): SpeciesCatalogRepository {
        return instance ?: synchronized(this) {
            instance ?: AssetSpeciesCatalogRepository().also { instance = it }
        }
    }

    fun init(context: Context) {
        synchronized(this) {
            instance = AssetSpeciesCatalogRepository(context)
        }
    }

    fun setInstance(repository: SpeciesCatalogRepository) {
        synchronized(this) {
            instance = repository
        }
    }

    fun reset() {
        synchronized(this) {
            instance = null
        }
    }
}
