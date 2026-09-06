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

## 3. Estructura Conceptual de Campos (19 Campos Oficiales)

La entidad `Card` se estructura conceptualmente en 6 módulos funcionales que comprenden exactamente 19 campos canónicos:

```text
CARD
│
├── identity
│   ├── card_id
│   ├── animal_id
│   ├── specimen_number
│   └── schema_version
│
├── issuance
│   ├── edition
│   ├── generation
│   ├── issued_at
│   ├── population_at_issuance
│   └── rarity
│
├── provenance
│   ├── capture_id
│   ├── identification_method
│   └── identification_confidence
│
├── presentation
│   ├── rank
│   ├── display_location
│   ├── visual_effects
│   └── artwork
│
├── ownership
│   └── owner_id
│
└── authentication
    ├── serial
    └── verification_status
```

### 3.1. Detalle de Campos por Módulo

#### A. Identidad (`identity`)
* `card_id` (str, UUIDv4): Identificador técnico universal e inmutable de sistema para persistencia e integridad relacional.
* `animal_id` (str): Llave foránea inmutable hacia el perfil zoológico/taxonómico universal de la especie (`AnimalProfile`). Respeta `Animal ≠ Card`.
* `specimen_number` (int / str): Número visible de espécimen legible para el coleccionista (ej. `#0042`, *DEC-031*).
* `schema_version` (str): Versión del contrato de datos de la carta (ej. `"1.0"`, *DEC-019*), totalmente desacoplada de la versión de la app móvil.

#### B. Emisión (`issuance`) — Inmutables Históricos
* `edition` (str, opcional): Edición o serie de colección de la tirada (ej. `"1st Edition"`, `"Standard"`, `"Fundadores"`).
* `generation` (str): Generación histórica del sistema (ej. `"genesis"`, `"gen_1"`, *DEC-017*).
* `issued_at` (datetime / ISO 8601 UTC): Marca temporal exacta de acuñación/emisión oficial de la carta. *(Nombre canónico oficial; alias histórico: `created_at`)*.
* `population_at_issuance` (int, $\ge 1$): Conteo acumulado de cartas emitidas válidamente para esa especie (`animal_id`) al momento exacto de emisión (*DEC-033*).
* `rarity` (enum / str): Categoría cualitativa de valor de colección asignada al momento de emisión e históricamente inmutable (*DEC-016*, *DEC-033*). Representa exclusivamente *collection rarity*, totalmente desacoplada de la rareza biológica (`is_rare_species`) y de `rank`. *(Nota contractual vinculante: Denominaciones como `COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY` constituyen EJEMPLOS NO CONTRACTUALES; la taxonomía formal definitiva de tiers y sus curvas probabilísticas continúan pendientes de aprobación en DEC-022-PENDING. Alias de tipo: `rarity_tier`)*.

#### C. Procedencia (`provenance`) — Inmutables Históricos
* `capture_id` (str): Llave foránea hacia el evento de observación de campo (`Capture`) que originó la carta.
* `identification_method` (str): Metodología o modelo de visión empleado para clasificar la especie (ej. `"onnx_local_v1"`, `"cloud_vision"`, `"manual_curator"`).
* `identification_confidence` (float, 0.0 - 1.0, opcional/nullable): Nivel cuantitativo de confianza probabilística devuelto por el método de identificación. Nullable si el método no produce score. Prohibido inventar datos.

#### D. Presentación y Experiencia (`presentation`)
* `rank` (int / str, mutable): Nivel o clasificación dinámica de progresión y maestría de la experiencia del usuario con la carta o espécimen (*DEC-032*). Pertenece al sistema externo de progresión/gamificación, residiendo en `Card` exclusivamente como referencia o proyección de presentación visual. Es mutable y refleja el avance en el descubrimiento y aprendizaje. Desacoplado de `rarity` y de datos biológicos (no representa rareza, calidad zoológica, nivel taxonómico, edad, tamaño ni fuerza del animal). Sin fórmulas, XP, niveles específicos ni combate aprobados en esta fase (quedan diferidos como decisiones futuras).
* `display_location` (str): Ubicación geográfica pública generalizada (país, región, bioma) para salvaguardar la privacidad del usuario y la fauna protegida (*DEC-030*). Coherente con `Capture` pero sin exponer GPS exacto.
* `visual_effects` (str / dict, mutable): Efectos cosméticos especiales (foil, marcos holográficos, texturas). Desacoplados de datos biológicos (*DEC-032*).
* `artwork` (str / URI / objeto, opcional / mutable): Capa de ilustración artística única de encargo con ilustradores (*DEC-028*). No sustituye información zoológica ni identidad histórica.

#### E. Posesión (`ownership`)
* `owner_id` (str, mutable): Identificador del usuario custodio o propietario actual. Se actualiza en transferencias/intercambios sin alterar la identidad histórica (*DEC-027*).

#### F. Autenticación (`authentication`)
* `serial` (str, inmutable): Serial visible acuñado en la carta para trazabilidad visual y anclaje ante el servicio oficial (*DEC-018*). *(Alias descriptivo: `auth_serial`)*.
* `verification_status` (enum, mutable): Estado actual de autenticación (`UNVERIFIED`, `VERIFIED`, `FLAGGED`, `REVOKED`, `null` reservado a schemas previos) respaldado por la autoridad oficial de WHO Animal (*DEC-034, DEC-035*).

#### Campo Proyectado Adicional (Límite Ontológico)
* `specimen_sex` (enum, opcional / proyectado de `Capture`, valores: `MALE`, `FEMALE`, `UNKNOWN`): Sexo biológico del individuo observado proyectado para exhibición desde el registro de `Capture` de origen (*DEC-036*). Reside formalmente en `Capture` (`sex ∈ Capture`, `sex ∉ Animal`) y la Card actúa únicamente como capa de proyección visual sin ser la fuente primaria de verdad.

---

### 3.2. Proyección en Interfaz: Frente y Reverso (`CardFront` / `CardBack`)

Los 19 campos conceptuales se proyectan visualmente en las dos caras de la carta:

#### Frente de la Carta (`CardFront`)
* Visual y de atracción: `artwork` / `image_uri`, `specimen_number`, `serial` (visible en borde inferior), `visual_effects`, `rank` y proyección taxonómica desde `AnimalProfile` (`common_name`, `scientific_name_secondary`, `category`).

#### Reverso de la Carta (`CardBack`)
* Educativo y formativo: `display_location`, información científica factual de `AnimalProfile` (hábitat, dieta, comportamiento, tamaño, conservación, advertencias ponderadas) y la capa independiente de `LoreProfile` con marca de ficción.

---

## 4. Principio de Inmutabilidad Post-Emisión y Transferencia

* Las propiedades históricas de una carta (`card_id`, `animal_id`, `specimen_number`, `schema_version`, `edition`, `generation`, `issued_at`, `population_at_issuance`, `rarity`, `capture_id`, `identification_method`, `identification_confidence` y `serial`) quedan estrictamente congeladas tras su emisión.
* **El cambio de propietario mediante intercambio o comercio futuro NO altera estas propiedades.** El traspaso únicamente actualiza el campo de posesión (`owner_id`) e historial de custodia, manteniendo intacto el valor histórico de la pieza (*DEC-027*).

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
  * No son equivalentes ni se confunden: a modo de ejemplo puramente ilustrativo y no contractual, una carta con `population_at_issuance = 12` podría recibir una rareza alta (ej. `EPIC`) por pertenecer a la tirada temprana de la especie, mientras que una con `population_at_issuance = 20000` recibiría una rareza base (ej. `COMMON`). Las categorías formales definitivas continúan pendientes de aprobación.
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

### 4.4. Delimitación Semántica y Ortogonalidad de `rank` y `rarity` (DEC-016, DEC-032)

* **Principio Innegociable de Ortogonalidad Absoluta:**
  $$\text{rank} \neq \text{rarity}$$
  $$\text{rank alto} \quad \not\Rightarrow \quad \text{rarity alta}$$
  $$\text{rarity alta} \quad \not\Rightarrow \quad \text{rank alto}$$
  `rank` y `rarity` pertenecen a dimensiones conceptuales completamente distintas e independientes dentro del ecosistema de WHO Animal:
  * **`rarity` $\longrightarrow$ Dimensión de Colección y Emisión (Histórica e Inmutable):**
    * Califica exclusivamente el artefacto coleccionable en el momento exacto de su acuñación/emisión en base a `population_at_issuance` y las reglas del ecosistema (*DEC-016*, *DEC-033*).
    * Una vez emitida la carta, su `rarity` queda **estrictamente congelada e inmutable de por vida**.
    * Es totalmente independiente de la rareza biológica (`is_rare_species`) y no representa censos de animales reales en la Tierra (*DEC-016*).
    * **Ejemplos No Contractuales:** Enumeraciones como `COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY` constituyen **ejemplos ilustrativos no vinculantes**. La escala definitiva de tiers, sus nombres oficiales, porcentajes, fórmulas y umbrales matemáticos continúan pendientes de aprobación en *DEC-022-PENDING*.
    * **Restricción de Alcance:** Queda prohibido diseñar en esta etapa fórmulas matemáticas, probabilidades de drop, curvas económicas o monetización.
  * **`rank` $\longrightarrow$ Dimensión de Progresión y Experiencia (Dinámica y Mutable):**
    * Califica el avance, maestría, estudio y experiencia del usuario con la carta o espécimen en su viaje continuo de descubrimiento.
    * Es un atributo **mutable** que evoluciona con la interacción formativa y lúdica del usuario.
    * Pertenece al sistema externo de progresión/gamificación; reside en `Card` exclusivamente como **referencia o proyección de presentación visual**.
    * Si la carta es transferida (`owner_id`), el `rank` interactúa según las reglas del sistema de progresión del usuario, pero **jamás altera los metadatos históricos inmutables de emisión de la carta** (*DEC-027*).
    * **Exclusiones Terminantes (Qué NO representa `rank`):**
      1. ❌ **NO es rareza:** Una carta de rareza común puede alcanzar el rango más alto mediante dedicación, mientras que una carta recién emitida de máxima rareza nace en rango inicial.
      2. ❌ **NO es calidad zoológica ni valor biológico:** Ninguna especie ni ejemplar es "superior" o "inferior" biológicamente a otro.
      3. ❌ **NO es nivel taxonómico:** No representa familia, género, orden ni clado.
      4. ❌ **NO es la edad del animal físico:** No indica si el animal observado era cría, juvenil o adulto.
      5. ❌ **NO es tamaño ni peso:** No refleja envergadura, masa corporal ni dimensiones físicas.
      6. ❌ **NO es fuerza, agresividad ni poder real:** No mide letalidad, potencia física ni estatus de combate.
    * **Elementos Pendientes de Aprobación Futura:** Queda terminantemente prohibido inventar en esta etapa fórmulas de progresión, curvas de puntos de experiencia (XP), niveles concretos (ej. 1 a 5, estrellas), estadísticas numéricas de enfrentamiento o combate (ataque, defensa, salud) o tablas de recompensas. Todos estos aspectos permanecen explícitamente como decisiones futuras no aprobadas.

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
