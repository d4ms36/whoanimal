package com.whoanimal.app.data.catalog

/**
 * Anotaciones pedagógicas y avisos preventivos para especies del catálogo zoológico.
 *
 * Principios:
 * - Toda la información es factual, contrastable y de divulgación científica.
 * - Los avisos de precaución y peligro son objetivos y preventivos, nunca sensacionalistas (DEC-005, Regla 11 AGENTS.md).
 * - No introduce Lore: el Lore es personal del usuario, la ciencia es del producto.
 */
object SpeciesPedagogicalAnnotations {

    data class Annotation(
        val curiosity: String? = null,
        val dangerLevel: String? = null
    )

    private val annotations = mapOf(
        "Canis lupus familiaris" to Annotation(
            curiosity = "Posee un sentido del olfato hasta 100.000 veces más sensible que el humano.",
            dangerLevel = null
        ),
        "Felis catus" to Annotation(
            curiosity = "Pasan aproximadamente el 70% de su vida durmiendo y acicalándose.",
            dangerLevel = null
        ),
        "Panthera onca" to Annotation(
            curiosity = "Posee la mordida más potente de todos los grandes felinos en relación a su tamaño.",
            dangerLevel = "Precaución: Gran depredador carnívoro. Observar a distancia segura en hábitats naturales."
        ),
        "Ara macao" to Annotation(
            curiosity = "Forma parejas monógamas de por vida y sus llamadas pueden escucharse a kilómetros.",
            dangerLevel = null
        ),
        "Crocodylus niloticus" to Annotation(
            curiosity = "Su fuerza mandibular es una de las más elevadas documentadas en el reino animal.",
            dangerLevel = "Precaución: Depredador ápice semiacuático. Mantener distancia prudencial en riberas naturales."
        ),
        "Carcharodon carcharias" to Annotation(
            curiosity = "Puede detectar cantidades mínimas de sangre dispersas en millones de litros de agua.",
            dangerLevel = "Precaución: Superdepredador marino. Observación restringida a expediciones oficiales protegidas."
        ),
        "Ophiophagus hannah" to Annotation(
            curiosity = "Es la serpiente venenosa más larga del mundo y se alimenta principalmente de otros ofidios.",
            dangerLevel = "Precaución: Especie con veneno neurotóxico. No manipular ni aproximarse en campo silvestre."
        ),
        "Dendrobates tinctorius" to Annotation(
            curiosity = "Sus colores aposemáticos advierten visualmente a potenciales depredadores de su toxicidad.",
            dangerLevel = "Precaución: Secreción cutánea alcaloide defensiva. Evitar contacto dérmico directo."
        ),
        "Theraphosa blondi" to Annotation(
            curiosity = "Es la araña más voluminosa y pesada registrada en el planeta.",
            dangerLevel = "Precaución: Pelos urticantes defensivos y quelíceros prominentes. Manipulación desaconsejada."
        ),
        "Balaenoptera musculus" to Annotation(
            curiosity = "Es el animal más grande conocido que haya habitado la Tierra, alcanzando hasta 30 metros.",
            dangerLevel = null
        ),
        "Ailuropoda melanoleuca" to Annotation(
            curiosity = "Pasa entre 10 y 16 horas diarias alimentándose principalmente de diversas especies de bambú.",
            dangerLevel = null
        ),
        "Ambystoma mexicanum" to Annotation(
            curiosity = "Conserva rasgos larvarios en estado adulto y posee notable capacidad de regeneración celular.",
            dangerLevel = null
        ),
        "Apis mellifera" to Annotation(
            curiosity = "Realiza danzas complejas en el panal para comunicar la orientación y distancia de fuentes florales.",
            dangerLevel = null
        ),
        "Danaus plexippus" to Annotation(
            curiosity = "Realiza migraciones multigeneracionales de miles de kilómetros a través del continente americano.",
            dangerLevel = null
        ),
        "Elephas maximus" to Annotation(
            curiosity = "Su trompa contiene más de 40.000 músculos independientes y permite enorme destreza prensil.",
            dangerLevel = null
        ),
        "Loxodonta africana" to Annotation(
            curiosity = "Sus grandes orejas actúan como radiadores térmicos altamente eficientes disipando calor.",
            dangerLevel = null
        ),
        "Gorilla beringei" to Annotation(
            curiosity = "Comparte más del 98% de su genoma con el ser humano y exhibe una compleja estructura social.",
            dangerLevel = null
        ),
        "Haliaeetus leucocephalus" to Annotation(
            curiosity = "Posee una agudeza visual cuatro veces superior a la visión promedio humana.",
            dangerLevel = null
        ),
        "Spheniscus magellanicus" to Annotation(
            curiosity = "Forma colonias reproductivas densas y permanece fiel a la misma pareja reproductiva.",
            dangerLevel = null
        ),
        "Octopus vulgaris" to Annotation(
            curiosity = "Cuenta con un sistema nervioso descentralizado donde sus brazos operan con cierta autonomía motora.",
            dangerLevel = null
        ),
        "Hippocampus kuda" to Annotation(
            curiosity = "Es una de las pocas especies donde el macho gesta directamente los huevos en su bolsa incubadora.",
            dangerLevel = null
        ),
        "Chelonia mydas" to Annotation(
            curiosity = "Navega miles de kilómetros en mar abierto orientándose mediante campos geomagnéticos terrestres.",
            dangerLevel = null
        ),
        "Bubo bubo" to Annotation(
            curiosity = "Posee plumas adaptadas con bordes flecados para un vuelo prácticamente insonoro al cazar.",
            dangerLevel = null
        ),
        "Struthio camelus" to Annotation(
            curiosity = "Es el ave viviente más grande y veloz en tierra firme, capaz de superar los 70 km/h en carrera.",
            dangerLevel = null
        ),
        "Amphiprion ocellaris" to Annotation(
            curiosity = "Vive en relación simbiótica mutualista con anémonas marinas gracias a una mucosa protectora.",
            dangerLevel = null
        ),
        "Iguana iguana" to Annotation(
            curiosity = "Posee un órgano fotorreceptor parietal en la parte superior del cráneo conocido como ojo parietal.",
            dangerLevel = null
        ),
        "Phoenicopterus roseus" to Annotation(
            curiosity = "Su plumaje rosado proviene de los pigmentos carotenoides asimilados a través de su dieta.",
            dangerLevel = null
        ),
        "Agalychnis callidryas" to Annotation(
            curiosity = "Sus llamativos ojos rojos inducen sobresalto en depredadores cuando los abre súbitamente.",
            dangerLevel = null
        )
    )

    fun getAnnotation(scientificName: String): Annotation? =
        annotations[scientificName]
}
