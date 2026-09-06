# Especificación Conceptual de la Carta (Card Specification)

**Documento:** `CARD_SPEC.md`  
**Estado:** Especificación Base v0.3 (Alineada con directrices WHO-005B-A)  
**Objetivo:** Definir el modelo conceptual, campos de datos, inmutabilidad, privacidad y estructura de visualización de las cartas coleccionables de Who Animal.

---

## 1. Principios de Diseño de la Carta

1. **Jerarquía Visual Clara:** El usuario debe identificar de un vistazo el animal en el frente y desear conservarlo en su colección.
2. **Separación Realidad / Ficción:** Los datos biológicos y el Lore narrativo habitan en contenedores aislados.
3. **No Sobrecarga:** Los datos técnicos de conservación y taxonomía se presentan con síntesis y elegancia.
4. **Inmutabilidad Post-Emisión:** Las propiedades históricas de una carta quedan congeladas una vez emitida, incluso ante transferencias de dueño.
5. **Privacidad y Pet Friendly:** La carta expone ubicación generalizada para proteger los hábitats de fauna vulnerable.
6. **Independencia de Rareza y Rank:** La rareza de colección es cosmética/estética (marcos, efectos, texturas) y no representa abundancia biológica ni se confunde con el rango de progreso.

---

## 2. Anatomía de la Carta

```text
┌───────────────────────────────┐        ┌───────────────────────────────┐
│         FRENTE (FRONT)        │        │        REVERSO (BACK)         │
│ ┌───────────────────────────┐ │        │ ┌───────────────────────────┐ │
│ │                           │ │        │ │ [Nombre Común + Científico│ │
│ │                           │ │        │ ├───────────────────────────┤ │
│ │      IMAGEN PRINCIPAL     │ │        │ │ • Hábitat / Distribución  │ │
│ │   (FOTO O ARTWORK ÚNICO)  │ │        │ │ • Ubicación: Costa Rica   │ │
│ │                           │ │        │ │ • Dieta / Comportamiento  │ │
│ │                           │ │        │ │ • Tamaño / Peso           │ │
│ └───────────────────────────┘ │        │ ├───────────────────────────┤ │
│  Nombre Común                 │  <──>  │ │ Protegido: Sí | Rara: No  │ │
│  (Nombre científico)          │        │ ├───────────────────────────┤ │
│  [Categoría / Tipo]           │        │ │ ⚠️ Precaución (si aplica) │ │
│  Specimen #0042      [Icono]  │        │ ├───────────────────────────┤ │
│  [SN: 9A8F-42] (Serial)       │        │ │ 💡 Curiosidades           │ │
│  [Ilustrador: @artista]*      │        │ ├───────────────────────────┤ │
│                               │        │ │ ✨ LORE (Narrativa)       │ │
└───────────────────────────────┘        └───────────────────────────────┘
```
*\*El distintivo de ilustrador solo aparece si la carta posee una capa de artwork único personalizado.*

---

## 3. Estructura de Campos y Metadatos

### 3.1. Metadatos de Sistema e Identidad Histórica (`CardMetadata`)
* `card_id` (str): Identificador universal inmutable de sistema (ej. UUIDv4 `550e8400-e29b-41d4-a716-446655440000`).
* `specimen_number` (int / str): Número de espécimen visible y legible para el coleccionista (ej. `#0042`).
* `animal_id` (str): Identificador único del perfil biológico del animal asociado (ej. `animal_panthera_onca`).
* `card_code` (str): Código amigable de exhibición (ej. `WA-MAM-0042`).
* `created_at` (datetime / ISO 8601): Fecha y hora exacta de emisión original.
* `edition` (str, opcional): Edición de la tirada (ej. `"1st Edition"`, `"Standard"`).
* `generation` (str): Generación histórica de emisión (ej. `"genesis"`, `"gen_1"`).
* `population_at_issue` (int): Cantidad de registros válidos acumulados de la especie en el instante exacto de emisión.
* `auth_serial` (str): Serial alfanumérico visible discreto para trazabilidad y anclaje de autenticación.
* `schema_version` (str): Versión independiente del esquema de datos (ej. `"1.0"`).
* `is_collectible` (bool): Si la carta está desbloqueada y pertenece a la colección.
* `rarity_tier` (enum): Nivel de rareza de colección asignado (`COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY`).
* `owner_id` (str): Identificador del usuario propietario actual *(atributo mutable mediante intercambio o comercio futuro sin afectar los metadatos históricos de emisión)*.

---

### 3.2. Frente de la Carta (`CardFront`)
* `image_uri` (str): Ruta local o remota de la fotografía de avistamiento o del arte representativo.
* `common_name` (str): Nombre popular principal en el idioma del usuario (ej. *"Jaguar"*).
* `scientific_name_secondary` (str): Nombre binomial en latín en formato secundario/cursiva (ej. *"Panthera onca"*).
* `category` (enum): Categoría zoológica elemental (`MAMMAL`, `BIRD`, `REPTILE`, `AMPHIBIAN`, `FISH`, `INVERTEBRATE`).
* `visual_theme` (str): Tema estético del marco influenciado por la rareza (ej. `"emerald_foil"`, `"mythic_gold"`).
* `specimen_display` (str): Representación visible del número de espécimen (ej. `"Specimen #0042"`).
* `auth_serial_display` (str): Representación tipográfica reducida del serial en el borde inferior.
* `custom_artwork` (opcional): Capa visual adicional de ilustración personalizada (ver sección 6).

---

### 3.3. Reverso de la Carta (`CardBack`)

#### A. Información Científica Factual (`ScientificData`)
* `description` (str): Resumen conciso, accesible y educativo del animal (máx. 200 caracteres recomendados).
* `habitat` (str): Ecosistema característico (ej. *"Selvas tropicales y humedales"*).
* `distribution` (str): Región geográfica natural amplia (ej. *"América del Sur y Central"*).
* `display_location` (str): Ubicación geográfica pública generalizada del avistamiento (ej. *"Costa Rica"* o *"Península Ibérica"*).
* `diet` (str): Clasificación trófica y presas usuales (ej. *"Carnívoro oportunista"*).
* `behavior` (str): Patrones notables de actividad (ej. *"Solitario, crepuscular, excelente nadador"*).
* `size` (str): Medidas típicas de longitud / altura (ej. *"1.1 a 1.8 m de longitud corporal"*).
* `weight` (str, opcional): Rango de masa corporal cuando sea relevante (ej. *"55 a 100 kg"*).
* `sources` (list[str]): Referencias de fuentes biológicas (ej. `["UICN Red List", "GBIF"]`).

#### B. Indicadores Secundarios (`ConservationIndicators`)
* `is_protected` (bool): `True` si la especie cuenta con estatus de protección legal/ambiental.
* `is_rare_species` (bool): `True` si es de avistamiento biológicamente inusual o baja densidad en la naturaleza.

#### C. Sección de Seguridad (`DangerAssessment` - Condicional)
* `has_notice` (bool): Determina si se renderiza el bloque de seguridad.
* `level` (enum): `NONE`, `PRECAUTION` (⚠️ Precaución), `DANGER` (⚠️ Peligro).
* `notice_text` (str): Mensaje preventivo, educativo y ponderado (ej. *"⚠️ Precaución: Depredador de gran fuerza. Mantener estricta distancia de seguridad en áreas silvestres."*).
* `risk_factors` (list[str]): Etiquetas descriptivas (`venomous`, `territorial`, `bite_risk`, etc.).

#### D. Curiosidades (`Curiosities`)
* Colección de 1 a 3 micro-datos sorprendentes y verídicos sobre la adaptación o biología de la especie.

#### E. Lore Narrativo (`LoreProfile` - Capa Independiente)
* `title` (str): Título de fábula, mito o arquetipo (ej. *"La Sombra de los Cenotes"*).
* `narrative` (str): Micro-relato fantástico contextualizado en el universo de Who Animal.
* `disclaimer_tag` (str): Marca de agua o etiqueta fija: *"Contenido narrativo ficticio / Who Animal Lore"*.

---

## 4. Principio de Inmutabilidad Post-Emisión y Transferencia

* Las propiedades históricas de una carta (`card_id`, `specimen_number`, `generation`, `population_at_issue`, `rarity_tier`, `auth_serial`, `edition` y `created_at`) quedan estrictamente congeladas tras su emisión.
* **El cambio de propietario mediante intercambio o comercio futuro NO altera estas propiedades.** El traspaso únicamente actualiza el campo de posesión (`owner_id`) e historial de custodia, manteniendo intacto el valor histórico de la pieza.

---

## 5. Privacidad en Telemetría de Captura

* **Telemetría Interna (Privada):** Coordenadas GPS exactas (latitud/longitud) retenidas para verificación antifraude y bitácora personal.
* **Exposición Pública (`display_location`):** Ubicación generalizada a nivel regional/país para salvaguardar nidos, colonias y fauna protegida de actos de perturbación o furtivismo.

---

## 6. Capa de Artwork Único / Cartas Ilustradas (Extensión Futura)

Las cartas admiten conceptualmente la incorporación de una capa de arte exclusivo elaborado por ilustradores colaboradores:
* **Estructura de la Capa (`CustomArtworkOverlay`):**
  * `artwork_id`: Identificador del arte generado.
  * `illustrator_name` / `illustrator_id`: Identidad artística del creador.
  * `artwork_image_uri`: Ruta del renderizado artístico exclusivo.
  * `commission_date`: Fecha de aprobación del encargo.
* **Regla de No Sustitución:** El arte personalizado complementa o embellece el frente de la carta, pero **bajo ninguna circunstancia altera la identidad del animal, la información científica ni los metadatos históricos inmutables de la carta**.
