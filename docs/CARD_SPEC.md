# Especificación Conceptual de la Carta (Card Specification)

**Documento:** `CARD_SPEC.md`  
**Estado:** Especificación Base v0.1  
**Objetivo:** Definir el modelo conceptual, campos de datos y estructura de visualización de las cartas coleccionables de Who Animal.

---

## 1. Principios de Diseño de la Carta

1. **Jerarquía Visual Clara:** El usuario debe identificar de un vistazo el animal en el frente y desear conservarlo en su colección.
2. **Separación Realidad / Ficción:** Los datos biológicos y el Lore narrativo habitan en contenedores aislados.
3. **No Sobrecarga:** Los datos técnicos de conservación y taxonomía se presentan con síntesis y elegancia.

---

## 2. Anatomía de la Carta

```text
┌───────────────────────────────┐        ┌───────────────────────────────┐
│         FRENTE (FRONT)        │        │        REVERSO (BACK)         │
│ ┌───────────────────────────┐ │        │ ┌───────────────────────────┐ │
│ │                           │ │        │ │ [Nombre Común + Científico│ │
│ │                           │ │        │ ├───────────────────────────┤ │
│ │      IMAGEN PRINCIPAL     │ │        │ │ • Hábitat / Distribución  │ │
│ │          (FOTO/ARTE)      │ │        │ │ • Dieta / Comportamiento  │ │
│ │                           │ │        │ │ • Tamaño / Peso           │ │
│ │                           │ │        │ ├───────────────────────────┤ │
│ └───────────────────────────┘ │        │ │ Protegido: Sí | Rara: No  │ │
│  Nombre Común                 │  <──>  │ ├───────────────────────────┤ │
│  (Nombre científico)          │        │ │ ⚠️ Precaución (si aplica) │ │
│  [Categoría / Tipo]           │        │ ├───────────────────────────┤ │
│  #WA-0001-COL        [Icono]  │        │ │ 💡 Curiosidades           │ │
│                               │        │ ├───────────────────────────┤ │
│                               │        │ │ ✨ LORE (Narrativa)       │ │
└───────────────────────────────┘        └───────────────────────────────┘
```

---

## 3. Estructura de Campos y Metadatos

### 3.1. Metadatos de Sistema (`CardMetadata`)
* `card_id` (str): Identificador universal de la carta (ej. `card_550e8400-e29b-41d4-a716-446655440000`).
* `animal_id` (str): Identificador único del perfil biológico del animal (ej. `animal_panthera_onca`).
* `card_code` (str): Código amigable para coleccionistas (ej. `WA-MAM-0042`).
* `created_at` (datetime / ISO 8601): Fecha y hora de creación o emisión.
* `schema_version` (str): Versión del esquema de datos (ej. `1.0.0`).
* `is_collectible` (bool): Si la carta está desbloqueada y pertenece al inventario del usuario.
* `rarity_tier` (opcional/futuro): Nivel de rareza coleccionable en la aplicación, si llega a aprobarse.

---

### 3.2. Frente de la Carta (`CardFront`)
* `image_uri` (str): Ruta local o URL remota de la fotografía/arte representativo.
* `common_name` (str): Nombre popular principal en el idioma del usuario (ej. *"Jaguar"*).
* `scientific_name_secondary` (str): Nombre binomial en latín en formato secundario/cursiva (ej. *"Panthera onca"*).
* `category` (enum): Categoría zoológica elemental (`MAMMAL`, `BIRD`, `REPTILE`, `AMPHIBIAN`, `FISH`, `INVERTEBRATE`).
* `visual_theme` (str): Clave para el estilo estético del marco (ej. `"jungle_emerald"`, `"savanna_gold"`).

---

### 3.3. Reverso de la Carta (`CardBack`)

#### A. Información Científica Factual (`ScientificData`)
* `description` (str): Resumen conciso, accesible y educativo del animal (máx. 200 caracteres recomendados).
* `habitat` (str): Ecosistema característico (ej. *"Selvas tropicales y humedales"*).
* `distribution` (str): Región geográfica natural (ej. *"América del Sur y Central"*).
* `diet` (str): Clasificación trófica y presas usuales (ej. *"Carnívoro oportunista"*).
* `behavior` (str): Patrones notables de actividad (ej. *"Solitario, crepuscular, excelente nadador"*).
* `size` (str): Medidas típicas de longitud / altura (ej. *"1.1 a 1.8 m de longitud corporal"*).
* `weight` (str, opcional): Rango de masa corporal cuando sea relevante (ej. *"55 a 100 kg"*).
* `sources` (list[str]): Referencias de fuentes biológicas (ej. `["UICN Red List", "GBIF"]`).

#### B. Indicadores Secundarios (`ConservationIndicators`)
* `is_protected` (bool): `True` si la especie cuenta con estatus de protección legal/ambiental.
* `is_rare_species` (bool): `True` si es de avistamiento biológicamente inusual o baja densidad en la naturaleza.
* *(Nota: Estos campos se presentan como pequeños sellos discretos; nunca dominan la interfaz).*

#### C. Sección de Seguridad (`DangerAssessment` - Condicional)
* `has_notice` (bool): Determina si se renderiza el bloque de seguridad.
* `level` (enum): `NONE`, `PRECAUTION` (⚠️ Precaución), `DANGER` (⚠️ Peligro).
* `notice_text` (str): Mensaje preventivo, educativo y ponderado (ej. *"⚠️ Precaución: Depredador de gran fuerza. Mantener estricta distancia de seguridad en áreas silvestres."*).
* `risk_factors` (list[str]): Etiquetas descriptivas (`venomous`, `territorial`, `bite_risk`, etc.).

#### D. Curiosidades (`Curiosities`)
* Colección de 1 a 3 micro-datos sorprendentes y verídicos sobre la adaptación o biología de la especie.
* Ej: *"Posee la mordida más potente de los grandes felinos en relación a su tamaño, capaz de perforar caparazones de tortugas."*

#### E. Lore Narrativo (`LoreProfile` - Capa Independiente)
* `title` (str): Título de fábula, mito o arquetipo (ej. *"La Sombra de los Cenotes"*).
* `narrative` (str): Micro-relato fantástico contextualizado en el universo de Who Animal.
* `disclaimer_tag` (str): Marca de agua o etiqueta fija: *"Contenido narrativo ficticio / Who Animal Lore"*.
