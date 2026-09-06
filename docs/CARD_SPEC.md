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
* `population_at_issuance` (int, >= 1): Cantidad acumulada de Cards válidamente emitidas para esta especie (`animal_id`) en WHO Animal hasta el momento exacto de emisión de la carta (incluyendo a la carta actual como el ejemplar N). *(DEC-033; alias histórico: `population_at_issue`).*
* `auth_serial` (str): Serial alfanumérico visible discreto para trazabilidad y anclaje de autenticación.
* `schema_version` (str): Versión independiente del esquema de datos (ej. `"1.0"`).
* `is_collectible` (bool): Si la carta está desbloqueada y pertenece a la colección.
* `rarity_tier` (enum): Nivel de rareza de colección asignado (`COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY`).
* `owner_id` (str): Identificador del usuario propietario actual *(atributo mutable mediante intercambio o comercio futuro sin afectar los metadatos históricos de emisión)*.
* `verification_status` (enum, requerido con valor inicial `UNVERIFIED` en schema actual / `null` reservado a compatibilidad histórica): Estado actual de autenticación y validez certificado por la autoridad oficial (`UNVERIFIED`, `VERIFIED`, `FLAGGED`, `REVOKED`). Nombre canónico oficial que reemplaza a `verification`. Atributo mutable que no altera la identidad histórica de la carta *(DEC-034, DEC-035)*.
* `specimen_sex` (enum, opcional / proyectado de Capture, valores: `MALE`, `FEMALE`, `UNKNOWN`): Sexo biológico del individuo observado proyectado para exhibición desde el registro de `Capture` de origen (`DEC-036`). No forma parte de la entidad `AnimalProfile` (especie) y su fuente primaria de verdad reside en la captura.

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

* Las propiedades históricas de una carta (`card_id`, `specimen_number`, `generation`, `population_at_issuance`, `rarity_tier`, `auth_serial`, `edition` y `created_at`) quedan estrictamente congeladas tras su emisión.
* **El cambio de propietario mediante intercambio o comercio futuro NO altera estas propiedades.** El traspaso únicamente actualiza el campo de posesión (`owner_id`) e historial de custodia, manteniendo intacto el valor histórico de la pieza.

### 4.1. Semántica Formal y Alcance de `population_at_issuance` (DEC-033)

* **Definición Canónica:**  
  `population_at_issuance` es la **cantidad acumulada de Cards válidamente emitidas por WHO Animal para la especie específica (`animal_id`) hasta el momento exacto de emisión de la Card actual (incluyendo a dicha Card como el espécimen $N$)**.
* **Alcance Oficial:** **`ANIMAL/SPECIES`** (específico del taxón/perfil zoológico representado por `animal_id`).
* **Significado Unívoco:**  
  `population_at_issuance = N` ($N \ge 1$) indica que en el momento histórico de acuñación de la carta, existían exactamente $N$ cartas válidas emitidas de esa especie en el universo de WHO Animal, siendo esta pieza la $N$-ésima.
* **Exclusiones Terminantes (Qué NO significa):**
  1. ❌ **NO es población biológica mundial:** No representa censos de animales reales en la Tierra.
  2. ❌ **NO es población biológica regional:** No indica cuántos animales habitan en el área del avistamiento.
  3. ❌ **NO es conteo de animales físicos observados:** No censa individuos biológicos; avistamientos válidos independientes generan cartas separadas.
  4. ❌ **NO es número de usuarios:** No cuenta cuántos usuarios tienen la carta ni cuántos juegan en la app.
  5. ❌ **NO es número de capturas (`Capture`):** No cuenta eventos crudos de cámara ni escaneos fallidos o descartados.
  6. ❌ **NO es conteo global de cartas en la app:** No suma cartas de otras especies zoológicas.
  7. ❌ **NO es conteo de una tirada o edición física:** Su eje natural es el taxón zoológico en el juego.
  8. ❌ **NO es un contador dinámico ni mutable:** Permanece estático de por vida tras la emisión.
* **Relación con `rarity`:**
  * `population_at_issuance` es un **dato cuantitativo histórico de entrada** (input inmutable).
  * `rarity` (o `rarity_tier`) es la **categoría cualitativa de valor de colección** asignada en el instante de emisión en base a dicho input y las reglas/curvas de juego vigentes.
  * No son equivalentes ni se confunden: una carta con `population_at_issuance = 12` puede recibir `rarity_tier = EPIC` por pertenecer a la tirada temprana de la especie, mientras que una con `population_at_issuance = 20000` recibirá `rarity_tier = COMMON`.
* **Clasificación y Atributos:**
  ```text
  REQUIRED:        sí
  IMMUTABLE:       sí
  HISTORICAL:      sí
  BIOLOGICAL DATA: no
  COLLECTION DATA: sí
  ```

### 4.2. Semántica Formal y Modelo de `verification_status` (DEC-034, DEC-035)

* **Nombre Canónico Oficial:**  
  **`verification_status`** es el **único nombre canónico oficial** del campo en el contrato de `Card` y sus esquemas asociados. El término preliminar `verification` queda formalmente reemplazado y deprecado *(DEC-035)*.
* **Definición Canónica:**  
  `verification_status` es el **campo mutable de estado actual que expresa la condición de autenticidad y validez operativa de la Card certificada por la autoridad oficial de WHO Animal**, siendo estrictamente independiente de la identidad histórica inmutable de emisión anclada en `card_id` y `serial`.
* **Tipo Conceptual:** **`CURRENT STATE`** (Campo escalar de estado / Enum de dominio) con punto de extensión desacoplado hacia infraestructura externa de auditoría.
* **Semántica Canónica de Estados y Distinción Estricta `null ≠ UNVERIFIED` (DEC-035):**
  1. `UNVERIFIED` (No verificada): La Card posee un estado de verificación conocido y formal en su esquema, pero aún no ha sido certificada por la autoridad oficial. **Es el estado inicial obligatorio para toda Card nueva emitida bajo el esquema vigente**.
  2. `VERIFIED` (Verificada): Ratificada formalmente por el servicio de autenticación de WHO Animal como registro fidedigno y auténtico.
  3. `FLAGGED` (En revisión / Sospechosa): Marcada para auditoría por anomalías en telemetría o sospecha de fraude/duplicación.
  4. `REVOKED` (Revocada / Invalidada): Declarada nula o ilegítima tras auditoría. No borra el registro de la base de datos (se preserva por trazabilidad forense), pero anula toda validez operativa, de colección o de juego oficial.
  5. `null`: **Reservado exclusivamente para compatibilidad histórica** con Cards antiguas emitidas bajo versiones de esquema (`schema_version`) que no contemplaban todavía este campo.
* **Regla Innegociable para Nuevas Cards:**  
  Toda Card nueva bajo el esquema actual nace con `verification_status = UNVERIFIED`. **Queda terminantemente prohibido asignar `null` a una Card nueva como sustituto de `UNVERIFIED`**.
* **Transiciones de Estado:**
  * `UNVERIFIED` $\rightarrow$ `VERIFIED` (tras validación oficial).
  * `UNVERIFIED` o `VERIFIED` $\rightarrow$ `FLAGGED` (al detectar anomalías o reportes de abuso).
  * `FLAGGED` $\rightarrow$ `VERIFIED` (auditoría confirma legitimidad).
  * `FLAGGED` $\rightarrow$ `REVOKED` (auditoría confirma fraude). Estado terminal.
* **Obligatoriedad y Compatibilidad por Schema (`DEC-019`, `DEC-035`):**
  * **En Schema Actual (`schema_version >= 1.0`):** Requerido; no puede omitirse y se inicializa en `UNVERIFIED`.
  * **En Dominio / Schemas Históricos Previos:** Nullable exclusivamente para permitir deserialización de piezas históricas emitidas antes de la introducción del campo.
* **Mutabilidad y Clasificación del Atributo:**
  ```text
  REQUIRED EN SCHEMA ACTUAL: sí (toda Card nueva lo incluye con valor inicial)
  NULLABLE EN DOMINIO:       sí (exclusivamente para compatibilidad histórica)
  VALOR INICIAL NUEVA CARD:  UNVERIFIED
  IMMUTABLE:                 no (es mutable)
  HISTORICAL:                no (refleja el estado presente)
  CURRENT STATE:             sí
  BIOLOGICAL DATA:           no
  SECURITY/STATUS:           sí
  ```
* **Fuente de Verdad:**
  El **Verification Service / Authority** de WHO Animal. La carta no es su propia autoridad y el cliente móvil no puede auto-certificarse como `VERIFIED` unilateralmente.
* **Relación con otros Identificadores:**
  * **Con `card_id`:** `card_id` es la identidad técnica universal inmutable; `verification_status` es una propiedad mutable que califica su estado de validez.
  * **Con `serial`:** `serial` (`auth_serial`) es el ancla visible inmutable acuñada en la carta. El usuario o sistema consulta el `serial` ante el servicio para obtener el `verification_status` actual. El `serial` es la llave/ancla de consulta; `verification_status` es el estado actual retornado.
  * **Con `owner_id`:** El traspaso de propiedad actualiza `owner_id` pero **no altera la identidad histórica ni revoca el estado de verificación** si la transferencia es legítima. La verificación certifica la autenticidad del artefacto zoológico, no la persona que lo custodia.
* **Exclusiones Terminantes (Qué NO significa):**
  1. ❌ **NO es la identidad de la carta:** La carta conserva su identidad histórica inmutable aunque su estado sea `UNVERIFIED` o `REVOKED`.
  2. ❌ **NO es el `serial` ni lo sustituye:** El serial es un ancla alfanumérica fija; `verification` es una condición mutable.
  3. ❌ **NO almacena credenciales ni claves privadas:** Prohibido guardar secretos o claves de firma en la carta.
  4. ❌ **NO almacena datos personales (PII):** No contiene nombres, correos ni perfiles de usuarios.
  5. ❌ **NO almacena telemetría GPS privada:** La privacidad del usuario y de la fauna silvestre se mantiene según `DEC-030`.
  6. ❌ **NO es un log histórico acumulativo incrustado:** No infla el payload de la carta con listas de auditorías.
  7. ❌ **NO es un mecanismo propietario rígido:** Es agnóstico a la tecnología de autenticación subyacente.
* **Neutralidad hacia Tecnologías Futuras:**
  Cualquier mecanismo futuro (códigos QR, chips NFC, APIs REST, firmas criptográficas PKI o anclajes en blockchain) interactúa con la infraestructura de seguridad externa y se proyecta limpiamente en la carta como una actualización de su `verification_status`, sin romper el contrato base ni corromper los datos históricos inmutables.

### 4.3. Atribución de `sex` en Capture/Specimen vs. Animal (DEC-036)

* **Principio de Delimitación Ontológica:**  
  $$\text{sex} \in \text{Capture / Specimen} \quad \land \quad \text{sex} \notin \text{Animal}$$
  * `AnimalProfile` modela el conocimiento científico abstracto y universal de la especie; la especie como taxón no posee un único sexo biológico individual.
  * `Capture / Specimen` registra el individuo físico concreto avistado en campo, donde reside el atributo biológico `sex`.
* **Valores Conceptuales Aprobados:**
  * `MALE`: Sexo biológico determinado con fiabilidad diagnóstica suficiente.
  * `FEMALE`: Sexo biológico determinado con fiabilidad diagnóstica suficiente.
  * `UNKNOWN`: Evidencia insuficiente para determinar el sexo del individuo a partir de la captura disponible.
* **Semántica de `UNKNOWN` y Regla Anti-Inferencia:**
  * `UNKNOWN` no denota ausencia de sexo biológico ni anomalía reproductiva; expresa rigor epistémico ante la falta de evidencia diagnóstica visible.
  * Queda **estrictamente prohibido inferir o adivinar** `MALE` o `FEMALE` sin evidencia concluyente (dimorfismo sexual claro, caracteres sexuales contrastados). Ante cualquier duda, el valor obligatorio es `UNKNOWN`.
* **Proyección en Card:**
  * La `Card` puede proyectar este dato (`specimen_sex`) para enriquecer la experiencia visual del coleccionista, pero **la fuente primaria inmutable de verdad reside siempre en el registro de captura (`capture_id`)**, sin que la carta sustituya al registro de origen.
* **Separación de Pilares:**
  * El sexo biológico pertenece estrictamente a **INFORMACIÓN REAL**, quedando prohibida su invención o asignación mediante elementos narrativos de Lore o Experiencia.

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
