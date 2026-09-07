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

### DEC-034: Definición Canónica, Semántica y Modelo de `verification`
* **Tema:** Dominio de Cartas, Autenticación y Seguridad
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Problema:** Existía ambigüedad sobre si `verification` debía ser un identificador redundante, un objeto complejo de auditoría forense con firmas y timestamps, o un estado actual, con riesgo de acoplar prematuramente el contrato de `Card` a tecnologías de seguridad específicas o de inflar el payload de datos.
* **Alternativas Auditadas:**
  * **A (Estado de verificación simple):** Campo enum escalar (`UNVERIFIED`, `VERIFIED`, `FLAGGED`, `REVOKED`). Ligero, mutable, sin duplicidad.
  * **B (Evidencia histórica incrustada):** Estructura con `verified_at`, `verified_by`, `method`, `reference`. *Descartada:* Mezcla artefacto de colección con auditoría de infraestructura y fuerza componentes prematuros.
  * **C (Identificador/Token de verificación):** Token de validación. *Descartada:* Duplica innecesariamente las funciones ya cubiertas por `card_id` (identidad técnica) y `serial` (trazabilidad visible).
  * **D (Objeto completo de autenticación con firmas y PKI):** *Descartada:* Viola Clean Architecture y sobrecarga la app cliente en Fase 0.
  * **E (Referencia externa pura):** URI obligatorio a servidor externo. *Descartada:* Rompe la operación offline y acopla a infraestructura de red.
  * **F (Solución Integral Canónica — CURRENT STATE Desacoplado):** `verification` modelado formalmente como campo de estado actual de validez (`verification_status`), respaldado por un servicio de verificación externo sin incrustar logs pesados en la carta. *Seleccionada.*
* **Decisión:** Se aprueba formalmente la siguiente definición canónica:
  > **`verification`** (representado formalmente en el contrato como **`verification_status`**) es el **campo mutable de estado actual que expresa la condición de autenticidad y validez operativa de la Card certificada por la autoridad oficial de WHO Animal**, siendo estrictamente independiente de la identidad histórica inmutable de emisión anclada en `card_id` y `serial`.
* **Tipo Conceptual:** **`CURRENT STATE`** (Campo escalar de estado / Enum de dominio) con punto de extensión desacoplado hacia futuros servicios de auditoría.
* **Estados Conceptuales Aprobados:**
  1. `UNVERIFIED` (No verificada): Estado inicial por defecto tras la emisión; la carta existe legítimamente pero no ha sido certificada por la autoridad central.
  2. `VERIFIED` (Verificada): Certificada formalmente por el servicio de verificación oficial de WHO Animal como registro fidedigno y auténtico.
  3. `FLAGGED` (En revisión / Sospechosa): Marcada para auditoría por anomalías en telemetría, sospecha de foto de pantalla o reporte de abuso.
  4. `REVOKED` (Revocada / Invalidada): Declarada nula o fraudulenta tras auditoría. *Aclaración:* No elimina el registro físico de la base de datos (se preserva por trazabilidad forense), pero anula toda validez operativa, de colección o de juego de la carta.
* **Transiciones Permitidas:**
  * `UNVERIFIED` $\rightarrow$ `VERIFIED` (tras validación satisfactoria).
  * `UNVERIFIED` o `VERIFIED` $\rightarrow$ `FLAGGED` (detección de sospecha o reporte).
  * `FLAGGED` $\rightarrow$ `VERIFIED` (auditoría concluye legitimidad).
  * `FLAGGED` $\rightarrow$ `REVOKED` (auditoría confirma fraude).
  * `REVOKED` es un estado terminal de invalidación.
* **Obligatoriedad y Nullability:**
  ```text
  REQUIRED: no (opcional / nullable)
  OPTIONAL: sí
  NULLABLE: sí (admite null para reflejar ausencia de servicio de verificación, inicializándose conceptualmente en UNVERIFIED)
  ```
* **Mutabilidad y Clasificación:**
  ```text
  REQUIRED:        no
  IMMUTABLE:       no (es mutable)
  HISTORICAL:      no (refleja el estado presente)
  CURRENT STATE:   sí
  BIOLOGICAL DATA: no
  SECURITY/STATUS: sí
  ```
* **Fuente de Verdad:**
  El **Verification Service / Authority** de WHO Animal. La carta no es su propia autoridad y el cliente móvil local no puede auto-certificarse como `VERIFIED` unilateralmente.
* **Relación con otros Identificadores:**
  * **Con `card_id`:** `card_id` es la identidad técnica universal inmutable; `verification_status` es un estado mutable que califica a dicha identidad.
  * **Con `serial`:** `serial` (`auth_serial`) es el ancla visible inmutable acuñada en la carta. El usuario o sistema consulta el `serial` ante el servicio para obtener el `verification_status` actual. Son roles complementarios: `serial` es la llave/ancla de consulta; `verification_status` es el estado actual devuelto.
  * **Con `owner_id`:** La transferencia o cambio de dueño actualiza `owner_id` pero **no altera la identidad histórica ni revoca el estado de verificación** si la transacción fue legítima. La verificación avala la autenticidad del artefacto zoológico, no la persona que lo custodia.
  * **Con `capture_id`:** `capture_id` es la telemetría del evento de origen; `verification_status` es el estado de validez de la carta resultante.
* **Qué NO Significa (Exclusiones Terminantes):**
  1. ❌ **NO es la identidad de la carta:** La carta conserva su identidad histórica inmutable aunque su estado sea `UNVERIFIED` o `REVOKED`.
  2. ❌ **NO es el `serial` ni lo reemplaza:** El serial es un ancla alfanumérica fija; `verification` es una condición mutable.
  3. ❌ **NO almacena credenciales ni claves privadas:** Prohibido guardar secretos o claves de firma en la carta.
  4. ❌ **NO almacena datos personales (PII):** No contiene nombres, correos ni perfiles de usuarios.
  5. ❌ **NO almacena telemetría GPS privada:** La privacidad del usuario y de la fauna silvestre se mantiene según `DEC-030`.
  6. ❌ **NO es un log histórico acumulativo incrustado:** No infla el payload de la carta con listas de auditorías.
  7. ❌ **NO es un mecanismo propietario rígido:** Es agnóstico a la tecnología de autenticación subyacente.
* **Neutralidad hacia Tecnologías Futuras:**
  Cualquier mecanismo futuro (códigos QR, chips NFC, APIs REST, firmas criptográficas PKI o anclajes en blockchain) interactúa con la infraestructura de seguridad externa y se proyecta limpiamente en la carta como una actualización de su `verification_status`, sin romper el contrato base ni corromper los datos históricos inmutables.
* **Aprobado por:** Director Creativo / Project Manager / Developer

---

### DEC-035: Canonicalización de `verification_status` y Semántica de `null` vs `UNVERIFIED`
* **Tema:** Contratos de Dominio de Card, Normalización de Esquema y Compatibilidad Histórica
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Problema:** En `DEC-034` persistía una ambigüedad contractual entre la cualidad de opcional/nullable y la inicialización conceptual en `UNVERIFIED`. Se requería además formalizar `verification_status` como nombre canónico oficial en sustitución definitiva del término preliminar `verification`.
* **Decisión A — Nombre Canónico Oficial:**
  Se aprueba formalmente que **`verification_status`** es el **único nombre canónico oficial** del campo en el contrato y esquemas de la entidad `Card`. El término provisional `verification` queda formalmente reemplazado y deprecado.
* **Decisión B — Semántica Canónica de Estados y Distinción Estricta `null ≠ UNVERIFIED`:**
  1. **`UNVERIFIED`:** La Card posee un estado de verificación conocido y formal en su esquema, pero todavía no ha sido certificada por la autoridad oficial de WHO Animal. Es el **estado inicial obligatorio para toda Card nueva** emitida bajo un esquema que contemple este campo.
  2. **`VERIFIED`:** La Card ha sido formalmente verificada y autenticada por la autoridad oficial de WHO Animal como fidedigna y legítima.
  3. **`FLAGGED`:** La Card está bajo revisión o auditoría técnica por presunta anomalía en telemetría, sospecha de fraude o reporte de abuso.
  4. **`REVOKED`:** La Card ha sido invalidada oficialmente tras auditoría y carece de validez operativa, de colección, de intercambio o de juego en la plataforma (preservando el registro en base de datos para trazabilidad forense).
  5. **`null`:** **Reservado exclusivamente para compatibilidad histórica** con Cards antiguas emitidas bajo versiones de esquema (`schema_version`) que no contemplaban todavía el campo `verification_status`.
* **Regla Innegociable para Nuevas Cards:**
  * Toda Card nueva emitida bajo un esquema vigente nace obligatoriamente con:
    $$\text{verification\_status} = \text{UNVERIFIED}$$
  * Queda **estrictamente prohibido** emitir una Card nueva con `verification_status = null` como sustituto semántico de `UNVERIFIED`.
  * `null` denota estrictamente: *"campo no presente / no soportado en la versión de esquema de emisión original de esta pieza histórica"*.
* **Compatibilidad Histórica por `schema_version` (`DEC-019`):**
  * `schema_version` anterior al campo $\rightarrow$ `verification_status = null` (o campo omitido/opcional en el parser de lectura).
  * `schema_version` canónico actual $\rightarrow$ `verification_status` es campo formal con valor inicial obligatorio `UNVERIFIED`.
* **Relación con `serial` y `card_id`:**
  * `card_id` (identidad técnica universal) y `serial` (`auth_serial`, ancla visible de trazabilidad) son **estrictamente inmutables e históricos**.
  * `verification_status` es un **estado actual mutable** que califica la validez operativa de la carta sin alterar su identidad histórica.
* **Mutabilidad y Clasificación del Campo:**
  ```text
  REQUIRED EN SCHEMA ACTUAL: sí (toda Card nueva bajo schema actual lo incluye con valor inicial)
  NULLABLE EN DOMINIO:       sí (únicamente por compatibilidad histórica con schemas previos)
  VALOR INICIAL NUEVA CARD:  UNVERIFIED
  IMMUTABLE:                 no (es mutable)
  HISTORICAL:                no (refleja el estado presente)
  CURRENT STATE:             sí
  BIOLOGICAL DATA:           no
  SECURITY/STATUS:           sí
  ```
* **Aprobado por:** Director Creativo / Project Manager / Developer

---

### DEC-036: Definición Conceptual y Atribución de `sex` en Capture/Specimen (`sex ∉ Animal`)
* **Tema:** Modelo Conceptual de Dominio, Separación de Entidades y Datos Biológicos
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Problema:** Existía el riesgo de modelar el sexo biológico (`sex`) dentro de la entidad `Animal`. Sin embargo, `Animal` representa conocimiento zoológico y taxonómico universal de una especie/taxón (la especie en sí no posee un sexo individual). El sexo biológico pertenece intrínsecamente al individuo o espécimen concreto registrado durante una observación/captura en campo.
* **Decisión:** Se aprueba formalmente la delimitación conceptual:
  $$\text{sex} \in \text{Capture / Specimen}$$
  $$\text{sex} \notin \text{Animal}$$
  * **`Animal`:** Modela el saber biológico abstracto de la especie (`animal_id`, taxonomía, hábitat, dieta, descripción general, etc.).
  * **`Capture / Specimen`:** Modela el evento de observación y registro del individuo físico (`capture_id`, `animal_id`, telemetría, timestamp y `sex` del individuo observado).
* **Valores Conceptuales Aprobados:**
  1. `MALE`: El sexo biológico del espécimen observado fue determinado de manera suficientemente confiable.
  2. `FEMALE`: El sexo biológico del espécimen observado fue determinado de manera suficientemente confiable.
  3. `UNKNOWN`: No existe evidencia suficiente para determinar el sexo del individuo a partir de la observación/captura disponible.
* **Semántica de `UNKNOWN` y Regla Anti-Inferencia:**
  * `UNKNOWN` no significa que el animal carezca de sexo biológico ni representa una condición biológica anómala; describe honestidad epistémica ante la falta de evidencia diagnóstica en la observación.
  * Queda **estrictamente prohibido inferir o adivinar** `MALE` o `FEMALE` por mera apariencia superficial sin evidencia biológica diagnóstica sólida (dimorfismo sexual claro, caracteres sexuales primarios/secundarios contrastados). Ante cualquier incertidumbre, el valor obligatorio es `UNKNOWN`.
* **Reglas de Negocio Vinculantes:**
  1. `Animal` representa conocimiento zoológico de la especie; `sex` representa un atributo del individuo observado.
  2. Un mismo `animal_id` puede vincularse a múltiples Captures/Specimens con valores diversos de `sex` (`MALE`, `FEMALE`, `UNKNOWN`).
  3. El dato conserva trazabilidad directa e inmutable hacia `capture_id`.
  4. Principio de veracidad zoológica: Prohibido inventar el sexo biológico de un individuo.
  5. La ausencia de información de sexo no contamina el modelo de `Animal` ni invalida la observación.
  6. La `Card` puede proyectar este dato para presentación visual cuando esté disponible, pero la fuente primaria de verdad es siempre `Capture / Specimen`.
  7. Separación de pilares: El sexo biológico pertenece exclusivamente a **INFORMACIÓN REAL**, nunca a narrativa de Lore ni a invención de Experiencia.
* **Aprobado por:** Director Creativo / Project Manager / Developer

---

### DEC-038: Modelo Inicial de Monetización Gratuito y Basado en Publicidad
* **Tema:** Modelo de Negocio, Producto y Arquitectura
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** WHO Animal será un producto internacional y global 100% gratuito para el usuario en sus funcionalidades fundamentales (identificación, información zoológica, fichas, colección, descubrimiento y lore). La monetización principal inicial se basará en publicidad (como Google AdMob).
* **Motivo:** Asegurar accesibilidad sin crear barreras de pago en el núcleo del producto, definiendo una base de monetización viable que respete el principio de no alterar ni vender verdades biológicas.
* **Impacto:** La publicidad (`AdMob` o similar) se define estrictamente como **Infraestructura Externa** y NO debe formar parte del `Domain`. Entidades inmutables (`Animal`, `Capture`, `Card`, `Lore`) no deben depender de la publicidad. Queda prohibido implementar economía in-app (tienda, monedas, loot boxes, sistemas de pago) en la fase actual.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-039: Resolución Contractual de Capture.sex (Cardinalidad y Nullability)
* **Tema:** Modelo Conceptual de Dominio y Validaciones
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Se aprueba formalmente que el atributo `sex` en `Capture / Specimen` es **OPTIONAL** (puede omitirse al instanciar), **NULLABLE** (puede recibir explícitamente el valor `null`), y **NO TIENE VALOR DEFAULT** (no se asume `UNKNOWN` por defecto si falta o es null). Los valores permitidos, cuando existen, siguen siendo `MALE`, `FEMALE`, y `UNKNOWN` (conforme a DEC-036).
* **Motivo:** Evitar la inferencia arbitraria de datos. `UNKNOWN` denota que el sexo se intentó identificar pero faltó evidencia. La ausencia o `null` denota que el dato todavía no está disponible en la observación.
* **Impacto:** El contrato del modelo `Capture` y su serialización/deserialización debe soportar la diferencia semántica entre un campo ausente, un campo explícitamente nulo y un campo con valor.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-040: Formalización de la Historia Personal (Lore)
* **Tema:** Modelo Conceptual de Coleccionismo y Experiencia
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** La Historia Personal/Lore es un contenido personal escrito por el usuario y asociado a una Card específica (1 Card -> 0..1 Personal Story). Tiene un límite inicial de 300 caracteres (longitud real, sin truncamiento silencioso). No forma parte del conocimiento zoológico del `AnimalProfile` ni de la información científica del animal. La historia podrá evolucionar únicamente mediante reglas de edición controlada que serán definidas posteriormente.
* **Motivo:** Asegurar que la experiencia narrativa y personal del usuario no contamine la verdad zoológica inmutable de la especie ni los datos históricos de autenticación de la carta (`issued_at`, `display_location`, `serial`).
* **Impacto:** Modela la Historia Personal como una capa evolutiva, separada de la identidad histórica inmutable de la `Card`. Las mecánicas exactas de edición (límite por cuenta/carta, validación de historial, solicitudes y persistencia) quedan pendientes de resolución. Ningún campo canónico de la `Card` es alterado.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-041: Reglas de Edición de la Historia Personal (Lore)
* **Tema:** Modelo Conceptual de Coleccionismo y Experiencia
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** La edición de la Historia Personal de una Card está gobernada por las siguientes reglas limitadas:
  1. El límite máximo de longitud es **300 caracteres** por historia (se rechazan sobrepasos sin truncamiento).
  2. La creación inicial de la historia por el propietario no consume ninguna edición.
  3. Se establece un **máximo de 3 ediciones acumulativas por cuenta** (límite global compartido entre todas las Cards de la cuenta).
  4. Al agotar las 3 ediciones, se bloquea la edición normal de cualquier Historia Personal, pero la Card sigue funcionando normalmente.
  5. Existe el mecanismo de **Solicitud Oficial (Official Lore Change Request)** para modificaciones excepcionales una vez agotadas las ediciones. Las solicitudes aprobadas **NO consumen** una de las 3 ediciones normales.
  6. En caso de transferencia de la Card a un nuevo propietario (`owner_id`), la Historia Personal permanece asociada a la pieza. Sin embargo, el nuevo dueño no hereda automáticamente las ediciones del dueño anterior.
  7. El mantenimiento de un historial/versionado completo queda fuera del alcance inicial.
* **Motivo:** Mantener la simplicidad técnica al mismo tiempo que se permite evolucionar la experiencia de usuario de manera controlada y evitar el spam de contenido, preservando intactos los 19 campos inmutables de la Card.
* **Impacto:** Define la lógica de negocio para la futura API de edición y establece el límite a nivel de cuenta (global) y no por Card. No requiere implementar versionado complejo inicialmente.
* **Aprobado por:** Director Creativo / Project Manager

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

### DEC-022-PENDING: Taxonomía Definitiva de Tiers, Algoritmo Matemático y Curvas de Rareza Dinámica
* **Tema:** Economía de Colección y Progresión
* **Estado:** `PENDING`
* **Descripción:** Aprobación de la escala nominal cerrada de tiers de rareza de colección (las denominaciones como Común, Rara, Épica, etc., permanecen como ejemplos no contractuales) y formulación de la función matemática que mapea la población acumulada (`population_at_issuance`) a probabilidades de asignación al momento de emisión.
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

---

### DEC-037-PENDING: Sistema Definitivo de Progresión, Niveles y Mecánicas de `rank`
* **Tema:** Gamificación y Progresión de Experiencia
* **Estado:** `PENDING`
* **Descripción:** Definición del sistema de progresión y maestría del usuario asociado al campo mutable `rank` de la carta (escalas de nivel, puntos de avance, interacción formativa y aprendizaje), asegurando que no represente atributos biológicos, edad, tamaño ni estadísticas de combate.
* **Decisión requerida de:** Director Creativo / Project Manager
