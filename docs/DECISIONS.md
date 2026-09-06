# Registro Oficial de Decisiones de Arquitectura y Producto (ADR)

**Documento:** `docs/DECISIONS.md`  
**Propósito:** Registrar las decisiones formales tomadas a lo largo de la vida del proyecto para garantizar la trazabilidad del diseño y evitar la reapertura arbitraria de debates zanjados.

---

## Estructura Estándar de una Decisión

```text
[ID]: [Título Breve]
Tema: [Área de impacto]
Fecha: [AAAA-MM-DD]
Estado: [APPROVED | PENDING | DEPRECATED | SUPERSEDED]
Decisión: [Qué se decidió de forma concisa]
Motivo: [Por qué se tomó esta decisión / alternativas descartadas]
Impacto: [Consecuencias en arquitectura, producto o procesos]
Aprobado por: [Director Creativo / Project Manager / Consenso]
```

---

## Decisiones Aprobadas (Approved)

### DEC-001: WHO Animal como Experiencia Integral y no solo Identificador
* **Tema:** Visión de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** La identificación visual de fauna mediante IA no es el objetivo final, sino la puerta de entrada a una experiencia completa de descubrimiento, cartas coleccionables, aprendizaje científico y Lore.
* **Motivo:** Diferenciar el producto de utilidades genéricas de cámara, fomentando el compromiso a largo plazo y la apreciación de la naturaleza.
* **Impacto:** Todas las pantallas, interfaces y modelos de datos deben subordinarse a la narrativa de colección y descubrimiento.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-002: Anatomía de la Carta con Doble Cara (Frente y Reverso)
* **Tema:** Diseño de Cartas y UX
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Cada carta tendrá un Frente visualmente atractivo (imagen de calidad, nombre común, nombre científico secundario, categoría y códigos) y un Reverso estructurado con datos zoológicos, indicadores secundarios, curiosidades y Lore.
* **Motivo:** Evitar la sobrecarga cognitiva en la primera impresión visual manteniendo a su vez la profundidad formativa accesible al voltear.
* **Impacto:** Los modelos de datos desacoplan explícitamente `CardFront` y `CardBack`.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-003: Separación Estricta entre Información Real, Experiencia y Lore
* **Tema:** Reglas de Contenido y Ética Pedagógica
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los datos biológicos reales y la capa de narrativa mitológica/fantasía (Lore) deben vivir en contenedores independientes y portar etiquetas explícitas. Jamás se presentará el Lore como un hecho zoológico.
* **Motivo:** Preservar la función educativa y científica sin renunciar a la creatividad lúdica.
* **Impacto:** Modelos `ScientificInfo` y `LoreProfile` separados en el dominio. El Lore incorpora obligatoriamente un disclaimer de ficción.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-004: Indicadores de Protección y Rareza como Elementos Secundarios y Discretos
* **Tema:** UX y Formato de Datos
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los indicadores de si una especie está protegida o si es biológicamente rara se mostrarán mediante pequeños sellos discretos (`Sí/No`), sin transformar la carta en un formulario burocrático de conservación.
* **Motivo:** No convertir la aplicación en un catálogo sombrío o sobrecargado de leyes ambientales, manteniendo la experiencia estimulante y accesible.
* **Impacto:** Inclusión de booleanos en `ConservationIndicators` diseñados para insignias minimalistas.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-005: Avisos de Precaución y Peligro Exclusivamente Cuando Corresponda y Sin Exageración
* **Tema:** Seguridad Ciudadana y Ética de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los avisos de ⚠️ *Precaución* o ⚠️ *Peligro* se activarán únicamente para especies con riesgos reales (veneno activo, mordedura, territorialismo salvaje). Queda prohibido el sensacionalismo o exagerar peligros para generar morbo.
* **Motivo:** Fomentar una convivencia responsable y prudente con la fauna sin estigmatizar a los animales.
* **Impacto:** Modelo `DangerAssessment` condicional que solo se renderiza cuando `has_warning` es verdadero.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-006: Arquitectura Limpia y Desacoplada (Clean Architecture / Domain-Driven Design)
* **Tema:** Arquitectura de Software
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** El núcleo de la aplicación se estructurará en `domain` (modelos inmutables), `services` (interfaces/protocolos abstractos) y `core` (configuración y excepciones), desacoplado de interfaces gráficas o motores de IA concretos.
* **Motivo:** Permitir que el sistema evolucione sin rehacer código de negocio al conectar modelos locales de ML, APIs remotas o clientes móviles (Flutter/Web).
* **Impacto:** Implementación de protocolos `typing.Protocol` y aislamiento absoluto de las dependencias externas.
* **Aprobado por:** Project Manager / Developer

---

### DEC-007: Cero Dependencias Innecesarias en Fase de Fundación
* **Tema:** Gestión de Dependencias
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Mantener cero dependencias externas de producción durante la Fase 0, utilizando exclusivamente la biblioteca estándar de Python (`dataclasses`, `enum`, `typing`).
* **Motivo:** Evitar la instalación prematura de librerías pesadas (PyTorch, TensorFlow, frameworks web) sin justificación técnica inmediata, garantizando builds ligeras y reproducibles.
* **Impacto:** `requirements.txt` limpio; dependencias categorizadas en `pyproject.toml` como previstas y futuras.
* **Aprobado por:** Project Manager / Developer

---

### DEC-008: Principio Fundamental 100% Pet Friendly
* **Tema:** Filosofía de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** El bienestar, la protección y la preservación animal tienen prioridad absoluta sobre cualquier mecánica lúdica o esquema de monetización. Prohibida toda mecánica que premie la persecución, captura física o daño a animales.
* **Motivo:** Compromiso ético inquebrantable con la conservación y el respeto ambiental.
* **Impacto:** Criterio rector para la aprobación de cualquier futura propuesta de juego o monetización.
* **Aprobado por:** Director Creativo

---

### DEC-013: Diferenciación Ontológica entre Entidad Animal y Entidad Carta
* **Tema:** Arquitectura de Dominio
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** `Animal` y `Card` son entidades de datos ontológicamente distintas e independientes. Un espécimen animal puede residir en la base zoológica sin haber sido emitido en una carta para ningún usuario.
* **Motivo:** Evitar acoplamiento entre la realidad biológica objetiva y los artefactos de juego/colección de los usuarios.
* **Impacto:** Separación clara entre modelos `AnimalProfile` y `AnimalCard` en el dominio y en la futura serialización JSON.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-014: Inmutabilidad Histórica de las Cartas tras su Emisión
* **Tema:** Economía de Colección y Reglas de Dominio
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Una vez emitida una carta, sus atributos históricos fundamentales (`card_id`, generación, población registrada al momento de emisión, rareza asignada, serial de autenticación y edición) quedan congelados e inmutables.
* **Motivo:** Garantizar que los objetos coleccionables de los pioneros retengan permanentemente su valor y autenticidad histórica, aunque la especie se registre millones de veces en el futuro.
* **Impacto:** Prohibición estricta de recálculos destructivos o retroactivos de rareza en cartas ya emitidas.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-015: Criterio Funcional para Campos Obligatorios y Clasificación de Errores
* **Tema:** Integridad y Validación de Datos
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Se abandona la regla de "obligatorio es lo que no puede calcularse". Campo obligatorio es aquel sin el cual la entidad no puede considerarse válida ni operar. Si falta un dato crítico esencial, se genera ERROR (prohibido crear entidades inválidas). Si falta un dato complementario, se representa como nulo/desconocido sin inventar información.
* **Motivo:** Proporcionar una regla inequívoca para los esquemas de validación y erradicar conjeturas falsas en la información real.
* **Impacto:** Diseños de validadores que rechazan instancias incompletas críticas y admiten `None`/`null` en atributos biológicos accesorios.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-016: Desacoplamiento Estricto entre Rareza Biológica Natural y Rareza de Colección
* **Tema:** Gamificación y Rigor Pedagógico
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** La rareza biológica (escasez o densidad natural de una especie) y la rareza de colección (dificultad lúdica de obtención de una carta en el juego) son propiedades totalmente desacopladas.
* **Motivo:** Evitar confundir a los usuarios sobre el verdadero estatus ecológico de las especies (e.g., una carta de perro doméstico puede ser Ultra Rare por su generación, sin que el perro sea biológicamente escaso).
* **Impacto:** Los modelos diferencian `ConservationIndicators.is_rare_species` de `CardMetadata.rarity_tier`.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-017: Incorporación Conceptual de Rareza Dinámica y Generaciones Históricas (Genesis/Gen 1)
* **Tema:** Diseño de Coleccionismo
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Se aprueba conceptualmente que la rareza de colección se determine en el instante de emisión de la carta tomando en cuenta la población acumulada de la especie, introduciendo generaciones históricas (Genesis/Gen 1) y mayor probabilidad de rarezas superiores para los primeros registros.
* **Motivo:** Estimular el descubrimiento temprano de fauna y recompensar a la comunidad pionera con piezas de gran valor histórico.
* **Impacto:** La arquitectura de emisión requerirá registrar la población al momento del avistamiento. Las fórmulas matemáticas exactas quedan diferidas a simulación posterior.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-018: Incorporación de Serial Visual Discreto de Autenticación
* **Tema:** Estética y Seguridad de Cartas
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Las cartas incluirán en el diseño visual de su cara un serial discreto inferior. Se asume que el serial por sí solo no garantiza seguridad, pero servirá de anclaje de interfaz para futuros sistemas de verificación criptográfica o centralizada.
* **Motivo:** Proporcionar sensación táctil de autenticidad coleccionable preparando la arquitectura para validación antifraude.
* **Impacto:** Reserva de atributo `auth_serial` en los metadatos de la carta y espacio reservado en la maqueta visual.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-019: Aislamiento e Independencia de Versionado para el Esquema de Datos JSON
* **Tema:** Gobernanza Técnica y Compatibilidad
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** El esquema de serialización JSON (`schema_version`) se versiona de forma completamente independiente del `versionName` de la aplicación y del `versionCode` de Android. Las evoluciones del esquema deben asegurar retrocompatibilidad sin invalidar cartas históricas.
* **Motivo:** Evitar acoplar el formato de datos e intercambio a los ciclos de lanzamiento o despliegue en tiendas de aplicaciones móviles.
* **Impacto:** Los metadatos de las cartas portan su propio `schema_version` (ej. `"1.0"`).
* **Aprobado por:** Project Manager / Developer

---

## Decisiones Aprobadas — Extensiones Futuras (Approved — Future Extensions)

> ⚠️ **Nota:** Estas decisiones están formalmente aprobadas como parte del diseño conceptual a largo plazo, pero **NO están autorizadas para implementación en la fase actual**. Sirven como restricciones arquitectónicas para no cerrar opciones en el diseño del dominio.

### DEC-026: Arquitectura Desacoplada para Futuro Sistema de Enfrentamientos PVP
* **Tema:** Mecánicas de Juego Futuras
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** WHO Animal podrá incorporar en el futuro un sistema de enfrentamientos PVP entre usuarios/cartas. Este sistema debe concebirse como un módulo independiente desacoplado de: `Animal`, Identificación por visión, Información científica factual, Lore y el motor básico de emisión de cartas.
* **Motivo:** Asegurar que la capa de enfrentamiento lúdico no contamine la pureza de los datos científicos ni el flujo principal de descubrimiento y colección.
* **Impacto:** Ningún modelo del dominio biológico ni de cartas básicas debe depender de lógica o estadísticas de combate en esta etapa.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-027: Transferencia de Propiedad e Intercambio de Cartas sin Pérdida de Identidad Histórica
* **Tema:** Economía de Colección y Transacciones
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** Las cartas podrán cambiar de propietario en el futuro mediante intercambio, comercio o transferencia controlada. Regla innegociable: **El cambio de dueño NO modifica la identidad histórica de la carta** (`card_id`, `specimen_number`, generación, rareza, población al momento de emisión, edición, serial de autenticación e historial de eventos relevantes).
* **Motivo:** Preservar la inmutabilidad y el valor de colección histórico de cada pieza acuñada ante eventos de mercado o traspaso.
* **Impacto:** El propietario actual es un atributo de posesión/inventario mutable, mientras que los metadatos de acuñación de la carta son perpetuamente inmutables.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-028: Capa de Artwork Único / Cartas Ilustradas y Red de Ilustradores
* **Tema:** Arte y Personalización
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** En el futuro, WHO Animal podrá ofrecer diseños artísticos únicos asociados a una carta específica mediante colaboración con ilustradores. El artwork personalizado es una **capa visual/creativa asociada a la carta** y **NO sustituye**: la identidad del Animal, la información científica, la identidad histórica de la Card, la rareza, la generación ni el serial/specimen.
* **Motivo:** Permitir personalización estética de alto valor coleccionable sin corromper la veracidad biológica ni los registros históricos de la carta.
* **Impacto:** Los contratos de la carta deben prever soporte para asociar metadatos de artwork personalizado sin sobreescribir la información canónica de la especie.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-029: Solicitud y Gestión de Encargos Artísticos Directamente desde la Aplicación
* **Tema:** UX y Servicios Integrados
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** Se aprueba que el usuario pueda solicitar un diseño único y establecer contacto con el ecosistema de ilustradores colaboradores directamente desde la aplicación (Flujo: Solicitud del usuario → Gestión interna → Contacto con ilustrador → Propuesta/Diseño → Aprobación → Vinculación del artwork a la carta).
* **Motivo:** Facilitar la interacción directa y una experiencia integrada de personalización para coleccionistas.
* **Impacto:** La arquitectura futura de servicios contemplará la gestión de solicitudes artísticas de forma completamente separada de los datos zoológicos.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-030: Privacidad en Captura: Separación entre GPS Exacto y Ubicación Pública Generalizada
* **Tema:** Seguridad, Privacidad y Preservación de Fauna
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** Al capturar/registrar un espécimen, el sistema puede almacenar internamente las coordenadas GPS exactas para fines de validación científica o personal; sin embargo, **la carta expone públicamente únicamente una ubicación generalizada** (país, región, provincia o bioma).
* **Motivo:** Proteger la privacidad del usuario y, fundamentalmente, **proteger a las especies silvestres o en peligro contra la caza furtiva o la perturbación**, en estricto cumplimiento del principio 100% Pet Friendly.
* **Impacto:** Separación clara en los modelos de captura entre telemetría privada y datos públicos de la carta.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-031: Doble Identificador de Carta: `card_id` Técnico y `Specimen #` Visible
* **Tema:** Identificadores de Colección
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** Cada carta poseerá tanto un identificador técnico universal (`card_id`, ej. UUID) para integridad de base de datos, como un `Specimen #` (número de espécimen visible y legible, ej. `#0042`) para la experiencia del coleccionista.
* **Motivo:** Combinar rigor técnico en la persistencia con una experiencia amigable y reconocible en la interfaz de usuario.
* **Impacto:** Los esquemas y modelos contemplan ambos campos de manera complementaria.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-032: Independencia Conceptual entre Rareza de Colección y Rank con Efectos Visuales
* **Tema:** Gamificación y Diseño Visual
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED — FUTURE EXTENSION`
* **Decisión:** La Rareza de Colección y el Rank son conceptos independientes. La rareza de colección puede inducir modificaciones cosméticas y estéticas en la carta (marcos especiales, paletas cromáticas, texturas holográficas o efectos visuales dinámicos), sin representar abundancia biológica ni vincularse obligatoriamente al rango de progresión.
* **Motivo:** Enriquecer la dimensión visual de coleccionismo preservando la claridad conceptual entre dificultad de obtención y nivel alcanzado.
* **Impacto:** Soporte en la capa de vista para temas visuales condicionales basados en rareza.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-033: Definición Formal, Semántica y Alcance de `population_at_issuance`
* **Tema:** Dominio de Cartas, Coleccionismo y Rareza Dinámica
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Problema:** Existía ambigüedad conceptual sobre el significado exacto de la "población al momento de emisión", con riesgo crítico de confundir el término con censos biológicos de fauna real, eventos efímeros de captura, inventario global de la app o cuentas de usuarios.
* **Alternativas Consideradas:**
  * **A (Global):** Total de Cards emitidas por WHO Animal en todas las especies. *Descartada:* Diluye la novedad de especies raras descubiertas tardíamente; no refleja la abundancia dentro del taxón.
  * **B (Capturas brutas):** Total de eventos de telemetría registrados. *Descartada:* Confunde `Capture` con `Card`, introduce ruido de intentos fallidos/descartados y sugiere erróneamente un censo de animales físicos.
  * **C (Especie / Taxón):** Total de Cards emitidas dentro de la misma especie (`animal_id`). *Evaluada positivamente como base fundacional.*
  * **D (Edición):** Total de Cards emitidas dentro de un lote o tirada cerrada. *Descartada como alcance primario:* WHO Animal opera con avistamiento orgánico continuo, no con tiradas preimpresas.
  * **E (Generación):** Total de Cards emitidas en una ventana generacional. *Descartada como alcance primario:* Sin discriminar especie, replica los defectos de la alternativa global.
  * **F (Solución Integral Especie-Colección):** Definición formal rigurosa con alcance primario `ANIMAL/SPECIES` compatible con generaciones y reglas de rareza. *Seleccionada.*
* **Decisión:** Se aprueba formalmente la siguiente definición canónica:
  > **`population_at_issuance`** es la **cantidad acumulada de Cards válidamente emitidas por WHO Animal para la especie específica (`animal_id`) hasta el momento exacto de emisión de la Card actual (incluyendo a dicha Card como el espécimen $N$)**.
* **Alcance Oficial:** **`ANIMAL/SPECIES`** (Especie / Taxón representado por `animal_id`).
* **Significado Exacto:**  
  `population_at_issuance = N` significa de forma inequívoca:  
  *"Al momento exacto de emisión de esta Card, existen exactamente $N$ Cards válidamente emitidas por WHO Animal para esta especie (`animal_id`), siendo esta Card la $N$-ésima emitida dentro del catálogo de colección del juego."*
* **Qué NO Significa (Exclusiones Estrictas):**
  1. ❌ **NO es la población biológica mundial:** No representa cuántos ejemplares vivos de la especie existen en la naturaleza en el planeta Tierra.
  2. ❌ **NO es la población biológica regional o local:** No representa cuántos animales habitan en el país, región o bioma de la observación.
  3. ❌ **NO es el número de animales reales observados:** No realiza censos de individuos físicos; avistamientos separados válidos generan cartas independientes.
  4. ❌ **NO es el número de usuarios:** No representa cuántos jugadores han visto la especie ni cuántos poseen la carta.
  5. ❌ **NO es el número de capturas (`Capture`):** No contabiliza intentos de escaneo de cámara, fotos borrosas o eventos descartados por filtros antifraude.
  6. ❌ **NO es el número total global de Cards en la app:** No suma cartas de otras especies zoológicas.
  7. ❌ **NO es el número de Cards de una edición o lote:** Su alcance natural e intransferible es la especie (`animal_id`).
  8. ❌ **NO es un contador dinámico ni mutable:** No aumenta cuando se emiten nuevas cartas de esa especie en el futuro.
* **Relación con `rarity`:**
  * `population_at_issuance` es un **dato histórico cuantitativo de entrada** (input inmutable, $N \in \mathbb{N}_{\ge 1}$).
  * `rarity` (o `rarity_tier`) es la **categoría cualitativa de valor de colección** (`COMMON`, `UNCOMMON`, `RARE`, `EPIC`, `LEGENDARY`), determinada por el motor de emisión en el momento de acuñar la carta mediante reglas o curvas probabilísticas que evalúan dicho input.
  * *Ejemplo:* Si para *Panthera onca* una carta se emite con `population_at_issuance = 42`, las reglas de emisión le asignan `rarity_tier = EPIC` por pertenecer a los primeros 100 ejemplares (Genesis). Si otra carta de la misma especie se emite años después con `population_at_issuance = 25000`, las reglas le asignarán `rarity_tier = COMMON`.
* **Inmutabilidad y Clasificación del Atributo:**
  ```text
  REQUIRED: sí
  IMMUTABLE: sí
  HISTORICAL: sí
  BIOLOGICAL DATA: no
  COLLECTION DATA: sí
  ```
* **Consecuencias Futuras:** Permite formular de manera limpia algoritmos de rareza dinámica (`DEC-022`), validar observaciones sin mezclar capas (`DEC-023`), implementar esquemas JSON sin ambigüedades (`DEC-020`) y preservar la inmutabilidad histórica absoluta ante intercambios futuros (`DEC-027`).
* **Aprobado por:** Director Creativo / Project Manager / Developer

---

## Decisiones Pendientes de Aprobación (Pending)

### DEC-009-PENDING: Motor Definitivo de Identificación Visual
* **Tema:** Tecnología de Visión / IA
* **Estado:** `PENDING`
* **Opciones en evaluación:**
  1. Modelo ligero local on-device (MobileNet / EfficientNet optimizado con ONNX).
  2. Integración híbrida con APIs públicas de taxonomía (iNaturalist / GBIF).
  3. Modelo multimodal de visión en la nube vía backend propio.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-010-PENDING: Estilo y Universo Mitológico del Lore
* **Tema:** Diseño Narrativo
* **Estado:** `PENDING`
* **Opciones en evaluación:**
  1. Universo mitológico unificado de fantasía (ej. "Los Guardianes del Equilibrio").
  2. Adaptación de fábulas y leyendas folclóricas reales según el bioma y región del animal.
* **Decisión requerida de:** Director Creativo

---

### DEC-011-PENDING: Convención Definitiva de Identificadores `card_id` y Códigos de Colección
* **Tema:** Identificadores de Colección
* **Estado:** `PENDING`
* **Propuesta actual:** Formato amigable de exhibición `WA-[CATEGORÍA]-[NÚMERO]` complementado con identificador universal inmutable.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-012-PENDING: Tecnología Cliente Definitiva para la App de Usuario
* **Tema:** Frontend Móvil
* **Estado:** `PENDING`
* **Propuesta inicial:** Flutter para aplicación móvil multiplataforma (Android / iOS) consumiendo los servicios y modelos definidos en Python.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-020-PENDING: Diseño Definitivo del Esquema JSON de Datos
* **Tema:** Formato de Intercambio y Persistencia
* **Estado:** `PENDING`
* **Descripción:** Definición formal de los contratos de esquema JSON (campos, tipos, anidamiento y validación) para `Animal` y `Card`.
* **Decisión requerida de:** Project Manager / Developer (durante WHO-005)

---

### DEC-021-PENDING: Umbrales Definitivos de Confianza en la Identificación por Visión/Cámara
* **Tema:** Visión Computacional / UX
* **Estado:** `PENDING`
* **Descripción:** Determinación de valores mínimos de confianza para aceptar una identificación, solicitar re-escaneo o advertir incertidumbre taxonómica al usuario.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-022-PENDING: Algoritmo Matemático Definitivo y Curvas de Probabilidad para Rareza Dinámica
* **Tema:** Economía de Juego y Progresión
* **Estado:** `PENDING`
* **Descripción:** Formulación de la función matemática que mapea la población de registros acumulados a las probabilidades de rareza (Común, Poco común, Rara, Épica, Legendaria / Génesis).
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-023-PENDING: Definición Exacta y Reglas Anti-Abuso para Validación de Observaciones y Conteo de Población
* **Tema:** Integridad de Datos y Prevención de Fraude
* **Estado:** `PENDING`
* **Descripción:** Reglas técnicas (cooldowns, geolocalización, análisis de similitud fotográfica) para asegurar que fotos repetidas del mismo animal no inflen artificialmente la población.
* **Decisión requerida de:** Project Manager / Developer

---

### DEC-024-PENDING: Sistema Definitivo de Autenticación y Verificación de Cartas
* **Tema:** Seguridad e Infraestructura
* **Estado:** `PENDING`
* **Descripción:** Mecanismo técnico para corroborar la autenticidad del serial visual (firmas criptográficas, hash de integridad, o validación remota contra base de datos oficial).
* **Decisión requerida de:** Project Manager / Developer

---

### DEC-025-PENDING: Estrategia Definitiva de Compatibilidad y Migración entre Versiones del Esquema JSON
* **Tema:** Evolución de Esquemas y Persistencia
* **Estado:** `PENDING`
* **Descripción:** Protocolo para que los parsers lean cartas generadas en esquemas antiguos (`schema_version: 1.0`) sin corrupción ni pérdida de datos cuando el esquema evolucione a versiones superiores.
* **Decisión requerida de:** Project Manager / Developer (durante WHO-005)
