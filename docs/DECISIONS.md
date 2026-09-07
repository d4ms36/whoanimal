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
* **Estado:** `APPROVED` (Ampliación y rebaseline formal en `DEC-053`)
* **Decisión:** WHO Animal será un producto internacional y global 100% gratuito para el usuario en sus funcionalidades fundamentales (identificación, información zoológica, fichas, colección, descubrimiento y lore). La monetización principal inicial se basará en publicidad (como Google AdMob).
* **Motivo:** Asegurar accesibilidad sin crear barreras de pago en el núcleo del producto, definiendo una base de monetización viable que respete el principio de no alterar ni vender verdades biológicas.
* **Impacto:** La publicidad (`AdMob` o similar) se define estrictamente como **Infraestructura Externa** y NO debe formar parte del `Domain`. Entidades inmutables (`Animal`, `Capture`, `Card`, `Lore`) no deben depender de la publicidad. Queda prohibido implementar economía in-app (tienda, monedas, loot boxes, sistemas de pago) en la fase inicial de fundación. *(Nota evolutiva: DEC-053 rebaselina la visión de producto formalizando la arquitectura AdService desacoplada y una economía Free-to-Play ética sin pay-to-win, con moneda gratuita y tienda de cosméticos prevista a partir de Release 1.0)*.
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

---

### DEC-042: Enriquecimiento Científico del Catálogo
* **Tema:** Arquitectura de Datos y Dominio Biológico
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Las especies del catálogo zoológico se enriquecerán con información científica estable (`habitat`, `diet`, `lifespan_years`, `size_cm`, `weight_kg`, `activity_cycle`, `native_regions`). 
* **Justificación / Principios:**
  * Los atributos pertenecen exclusivamente a `AnimalProfile`.
  * No representan individuos concretos.
  * No contienen datos personales.
  * No contienen coordenadas de captura.
  * Permanecen compatibles con futuras integraciones científicas (ej. GBIF, IUCN).
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-043: Observation como entidad efímera
* **Tema:** Arquitectura de Dominio (Motor de Observaciones)
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Se introduce `Observation` como la entidad que representa una observación fotográfica pendiente de confirmación.
* **Justificación / Principios:**
  * Representa una observación pendiente.
  * Es efímera y puede descartarse o ser rechazada.
  * **No modifica** automáticamente `AnimalProfile`.
  * **No crea** automáticamente `Capture`.
  * **No crea** automáticamente `Card`.
  * Sólo una observación aceptada puede originar una `Capture` posteriormente en el flujo.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-044: Resultados de Identificación
* **Tema:** Arquitectura de Dominio (Motor de Observaciones)
* **Fecha:** 2026-09-07
* **Estado:** `APPROVED`
* **Decisión:** Se crea `IdentificationResult` para aislar el resultado de un motor de identificación sobre una `Observation`. Se separa explícitamente el valor de confianza (`confidence`) de la decisión de aceptación (acceptance).
* **Justificación / Principios:**
  * Un valor numérico de confianza no debe automatizar la verdad. La aceptación debe estar desacoplada del resultado.
  * Los resultados conservan el orden exacto devuelto por el motor (no se auto-ordenan).
  * No se incluyen conceptos de `accepted`, `verified`, ni conversiones a `Capture` en esta fase.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-045: Formalización de la Decisión Explícita de Identificación
* **Tema:** Arquitectura de Dominio (Motor de Observaciones)
* **Fecha:** 2026-09-07
* **Estado:** `APPROVED`
* **Decisión:** Se introduce `IdentificationDecision` para formalizar la decisión explícita tomada sobre un `IdentificationResult`. Se desacopla de forma absoluta la confianza (`confidence`) de la decisión (`decision`).
* **Justificación / Principios:**
  * La confianza cuantitativa nunca produce automáticamente una decisión de aceptación o rechazo (`confidence ≠ decision`). No existen umbrales automáticos en la entidad.
  * Los estados de decisión válidos son exclusivamente: `ACCEPTED`, `REJECTED` y `CANCELLED`.
  * La decisión `ACCEPTED` exige un `selected_animal_id` que debe pertenecer a los candidatos del `IdentificationResult`.
  * Las decisiones `REJECTED` y `CANCELLED` no requieren selección de animal y no originan `Capture` ni `Card`.
  * No se realiza inferencia automática del animal ni se asumen decisiones por defecto.
  * Esta entidad todavía no crea `Capture` ni `Card` (el puente hacia `Capture` se evaluará en un objetivo posterior).
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-046: Puente IdentificationDecision ACCEPTED → Capture
* **Tema:** Arquitectura de Dominio (Motor de Observaciones y Capturas)
* **Fecha:** 2026-09-07
* **Estado:** `APPROVED`
* **Decisión:** Una `Capture` únicamente puede crearse como consecuencia de una `IdentificationDecision` explícitamente en estado `ACCEPTED`. Se incorporan formalmente a `Capture` los campos obligatorios e inmutables `animal_id` (proveniente del `selected_animal_id` validado contra los candidatos) e `identification_id` (trazabilidad al `IdentificationResult`).
* **Justificación / Principios:**
  * Queda estrictamente prohibido crear `Capture` a partir de un umbral de confianza, directamente desde `IdentificationResult` o desde `Observation`.
  * La decisión debe ser explícita y pertenecer al `IdentificationResult` referenciado (`identification_id` coincidente).
  * Las decisiones `REJECTED` o `CANCELLED` nunca crean una `Capture`.
  * Se mantiene intacto el contrato de `sex` (DEC-036 / DEC-039): opcional, nullable, sin valor por defecto, sin inferencia visual.
  * Una `Capture` no genera automáticamente una `Card` (la emisión de cartas permanece como fase posterior).
  * Todos los atributos de `Capture` (`capture_id`, `animal_id`, `identification_id`, `sex`) son estrictamente inmutables tras su creación.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-047: Consolidación de Arquitectura, Definición del Mínimo Funcional y Roadmap por Fases
* **Tema:** Arquitectura de Producto y Planificación Estratégica (WHO-013)
* **Fecha:** 2026-09-07
* **Estado:** `APPROVED`
* **Decisión:** 
  1. Se define formalmente **WHO Animal** como una aplicación móvil centrada en el descubrimiento, identificación, aprendizaje y coleccionismo de fauna mediante cartas coleccionables. La identificación mediante cámara o imagen es la puerta de entrada, no el destino final.
  2. Se consagra el **Mínimo Funcional** sobre la plataforma Android como el flujo completo de 11 pasos:
     `Abrir App → Foto → Observation → Identificación → IdentificationResult → Mostrar al Usuario → Decisión (ACCEPT/REJECT) → Capture (si ACCEPTED) → Card → Mostrar Card → Colección Local`.
  3. Se reestructura oficialmente el Roadmap en 5 fases estancas:
     * `PHASE 0 — FOUNDATION` (Arquitectura, dominio, catálogo inicial, puente ontológico completo hasta Capture; completada con WHO-013).
     * `PHASE 1 — MINIMUM FUNCTIONAL` (Construcción del primer producto funcional Android con el core loop Foto → Colección).
     * `PHASE 2 — BETA` (Validación con usuarios reales, calibración de IA, rendimiento y Google Play Internal Testing).
     * `PHASE 3 — RELEASE` (Lanzamiento público comercial en Google Play Store).
     * `PHASE 4 — ECOSYSTEM` (Módulo avanzado de capacidades: comercio, PVP, cuentas avanzadas, cloud, marketplace, ilustradores, gamificación profunda, etc.).
  4. Se aíslan las 13 capacidades estratégicas futuras para que la arquitectura las admita sin que constituyan bloqueos o requisitos del Mínimo Funcional.
  5. Se adoptan como principios obligatorios de honestidad técnica:
     * *"Evolución continua del sistema de identificación mediante IA y mejora progresiva de precisión"* (prohibido prometer "IA perfecta").
     * *"Expansión progresiva del catálogo zoológico hacia una cobertura global objetivo"* (prohibido prometer "todas las especies" de forma inmediata).
* **Justificación / Principios:**
  * Garantizar trazabilidad y foco operativo absoluto, erradicando el desvío de alcance (*scope creep*).
  * Resolver colisiones de numeración histórica y fijar una secuencia estricta para la Fase 1.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-048: Formalización de Alpha 0.1, Golden Path y Horizontes Estratégicos
* **Tema:** Definición de Producto Alpha 0.1 y Horizontes Estratégicos (WHO-013.1)
* **Fecha:** 2026-09-07
* **Estado:** `APPROVED`
* **Decisión:**
  1. Se formaliza como **definición y objetivo rector de Alpha 0.1**:
     > *"Un usuario nuevo debe poder entrar a WHO Animal, fotografiar un animal, obtener una identificación, generar una carta, revisarla, guardarla en su colección, cerrar la aplicación, volver a abrirla y encontrar la carta nuevamente."*
  2. Se establece el **Golden Path oficial de Alpha 0.1**:
     ```text
     LOGIN → HOME → CAPTURE → CAMERA → OBSERVATION → IDENTIFICATION → RESULT → DECISION → CAPTURE → CARD → REVIEW / EDIT → SAVE → STORAGE
     ```
     y su flujo alternativo de consulta y gestión de colección:
     ```text
     HOME → STORAGE → CONTAINER → CARD → VIEW / EDIT / DELETE
     ```
  3. Se define el **alcance funcional obligatorio de Alpha 0.1**:
     * **Cuenta:** Registro, Login, Logout locales básicos.
     * **Home:** Menú simple con accesos principales directos a Capture y Storage.
     * **Capture:** Gestión de permisos de cámara, disparo fotográfico, instanciación de `Observation`, ejecución de identificación, presentación de resultado, aceptación/descarte y manejo amigable de errores.
     * **Card:** Generación automática tras decisión válida, renderizado frontal, animación de flip, reverso informativo (datos científicos del catálogo + metadatos de captura), edición restringida de campos personales (Lore/Historia Personal según reglas de dominio) y bloqueo estricto de campos de dominio inmutables, con opción de descartar o guardar.
     * **Location:** Representación generalizada (`display_location`) para la experiencia de usuario y protección de fauna silvestre (sin almacenar dirección residencial exacta), dejando preparada la arquitectura para aislar `display_location` de `precise_location` ante futuras políticas de privacidad.
     * **Storage:** Persistencia local robusta de la colección dividida en 10 containers de 30 espacios cada uno (capacidad de 300 cartas), selector de container, indicador visual de ocupación, visualización, apertura individual y eliminación confirmada de cartas, garantizando persistencia íntegra tras cerrar y reabrir la app.
     * **UX mínima:** Splash/entrada sencilla, estados de carga, visualización de estado vacío (*empty state*), diálogo de confirmación antes de eliminar, animación de flip, navegación Back/Home consistente y diseño visual cohesivo.
  4. Se establecen explícitamente las **exclusiones fuera de alcance de Alpha**: Trading, PVP, Marketplace, economía in-app completa, infraestructura Cloud/backend, sincronización multidispositivo, cuentas avanzadas, red social, red de ilustradores, rarezas dinámicas con curvas matemáticas, gamificación profunda, cobertura zoológica mundial completa, IA de precisión perfecta, publicación en Google Play y monetización operativa.
  5. Se reestructura el horizonte de desarrollo en 4 planos:
     * **CORTO PLAZO — Alpha:** Core loop funcional demostrable en Android (`WHO-014` a `WHO-018`).
     * **MEDIANO PLAZO — Beta / Release:** Pruebas con usuarios reales, calibración de IA, estabilidad, rendimiento, catálogo inicial ampliado, testing Android y despliegue en Google Play Store.
     * **LARGO PLAZO — Ecosystem:** Las 13 capacidades estratégicas avanzadas que enriquecen el ecosistema.
     * **FUTURO ABIERTO:** Espacio conceptual para iniciativas e ideas sin alcance, versión, dependencia ni criterios asignados, sin convertirlas prematuramente en objetivos ejecutables.
  6. Se consagra la línea temporal única del proyecto:
     ```text
     FOUNDATION = COMPLETADA
     ALPHA = PRÓXIMO PRODUCTO FUNCIONAL (WHO-014 → WHO-018)
     BETA = SIGUIENTE HORIZONTE
     RELEASE = LANZAMIENTO PÚBLICO
     ECOSYSTEM = CAPACIDADES AVANZADAS
     ```
* **Justificación / Principios:** Evitar tanto el *scope creep* como una planificación excesivamente rígida, manteniendo trazabilidad absoluta entre el core loop demostrable y las visiones de largo alcance.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-049: Fundación de la Aplicación Android Nativa (Kotlin + Jetpack Compose)
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-015`
* **Tema:** Arquitectura Móvil / Frontend
* **Resolución:**
  1. Se adopta oficialmente **Android Nativo** con **Kotlin (2.0+)**, **Jetpack Compose**, **Material 3** y **Navigation Compose** como la tecnología cliente definitiva para Alpha 0.1 de WHO Animal.
  2. Se resuelve formalmente la propuesta abierta en `DEC-012-PENDING`, sustituyéndola por una implementación nativa moderna para garantizar rendimiento óptimo en la renderización de cartas, micro-interacciones de dos caras (flip), e integración directa con las APIs de cámara e inferencia local de visión.
  3. Se establece una arquitectura desacoplada y limpia:
     * `UI`: Activity, Jetpack Compose, componentes visuales, temas (Material 3) y navegación centralizada (`AppNavHost`, `NavDestination`: `Splash`, `Home`, `Capture`, `Collection`).
     * `Domain Boundary`: Contratos inmutables de datos (`AnimalProfileContract`, `ObservationContract`, `IdentificationResultContract`, `IdentificationDecisionContract`, `CaptureContract`, `AnimalCardContract`) e interfaces de servicio (`IdentificationServiceBoundary`, `CollectionStorageBoundary`, `CardGeneratorBoundary`).
  4. Se preserva rigurosamente la ontología estricta del proyecto:
     * `Observation ≠ IdentificationResult ≠ IdentificationDecision ≠ Capture ≠ AnimalCard`.
     * `Capture != Card`: La captura es inmutable y no se contamina con campos de carta.
     * `Capture.sex` restringido a `{ MALE, FEMALE, UNKNOWN }` (excluido de `Animal`).
     * `auth_serial` / `serial` alfanumérico limpio sin coordenadas GPS ni datos privados.
  5. Las capacidades de cámara real (CameraX), motor visual (IA on-device), persistencia local (Room/SQLite) y generación desde Android quedan estrictamente delimitadas para sus respectivos objetivos posteriores (`WHO-016`, `WHO-017` y `WHO-018`).
* **Justificación / Principios:** Máximo rendimiento visual, facilidad de integración con librerías nativas de machine learning y hardware de cámara, adherencia al diseño declarativo moderno de Android, y estricto respeto a las fronteras arquitectónicas de dominio.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-050: Persistencia Local Alpha con Room y Arquitectura de Storage (10x30=300)
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-017`
* **Tema:** Persistencia Local / Almacenamiento / Arquitectura de Datos
* **Resolución:**
  1. Se adopta oficialmente **Room + SQLite** (Room 2.6.1 + KSP 2.0.0-1.0.21) como la solución de persistencia local offline para Android Alpha 0.1 de WHO Animal.
  2. Queda terminantemente prohibido introducir base de datos remota, Firebase, nube, autenticación externa o sincronización multidispositivo en Alpha 0.1.
  3. Se mantiene una rigurosa separación de capas arquitectónicas:
     * `UI`: Consume exclusivamente casos de uso o repositorios de dominio. Sin acceso directo a DAOs ni entidades de base de datos.
     * `Domain`: Define interfaces agnósticas `CardRepository` y `CollectionStorageRepository`, así como las invariantes de almacenamiento físico (`StorageConstants`) y excepciones específicas (`StorageFullException`, `InvalidSlotException`, `DuplicateSlotException`, `DuplicateCardException`).
     * `Data Layer`: Implementa las interfaces mediante `RoomCardRepository` y `RoomCollectionStorageRepository`, operando sobre entidades Room (`CardEntity`, `StorageSlotEntity`), DAOs transaccionales (`CardDao`, `StorageSlotDao`) y la base de datos `WhoAnimalDatabase`.
  4. Invariantes de almacenamiento físico de colección Alpha:
     * Estructura fija: 10 contenedores × 30 espacios = 300 ranuras de capacidad total (`1..10` y `1..30`).
     * Cada ranura solo puede alojar una carta. Inserciones fuera de rango, duplicadas o cuando la capacidad esté completa son rechazadas a nivel de dominio y base de datos con índice único compuesto `(container_index, slot_index)`.
  5. Integridad relacional y ontológica estricta:
     * `1 Capture → <= 1 Card`: Protegido por índice único en `cards(capture_id)`.
     * `card_id != capture_id`: Validado rigurosamente en la capa de repositorio antes de persistir.
     * Integridad transaccional en borrado: La eliminación de una carta libera inmediatamente su ranura de almacenamiento en la misma transacción (`CASCADE`), garantizando que no existan registros huérfanos.
     * Serialización simétrica de los 19 campos canónicos de `AnimalCardContract` preservando valores nulos, enums y tipos canónicos.
* **Justificación / Principios:** Asegura la supervivencia del estado de la colección tras el cierre y reapertura de la app, garantiza robustez offline en campo sin depender de red, permite tests unitarios deterministas sobre JVM con Robolectric, y previene cualquier acoplamiento entre el dominio biológico y librerías de persistencia.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-051: Perfil de Explorador Local y Sesión Offline sin Backend
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-018A`
* **Tema:** Identidad Local / Sesión de Usuario / Arquitectura Android
* **Resolución:**
  1. Se establece el modelo `ExplorerProfile` como la representación canónica de identidad del usuario en Alpha 0.1 (`profile_id`, `explorer_name`, `created_at`, `last_opened_at`, `is_active`).
  2. La sesión y perfil son 100% locales y residen en la base de datos Room (`ProfileEntity`, `ProfileDao`, `RoomProfileRepository`). Queda prohibida la introducción de autenticación remota, correos, contraseñas, OAuth (Google/Apple) o backend en Alpha.
  3. Regla de sesión única en Alpha: Solo puede existir un único perfil activo en el dispositivo (`is_active = 1`). La activación de un perfil desactiva de forma atómica y transaccional cualquier registro previo.
  4. Flujo de navegación condicional automático:
     * Primera apertura: `Splash → Welcome → CreateProfile → Home`.
     * Reaperturas subsiguientes: `Splash → Home` (detecta el perfil activo y actualiza `last_opened_at` de forma no bloqueante).
     * El backstack de navegación elimina las pantallas de bienvenida y creación de perfil tras la entrada a `Home` para evitar navegación inversa redundante.
  5. Reglas de validación de identidad en campo: El nombre de explorador debe ser no vacío, no componerse únicamente de espacios en blanco y tener una longitud estrictamente entre 2 y 30 caracteres.
* **Justificación / Principios:** Permite una experiencia de usuario fluida y persistente desde la primera apertura, garantiza privacidad total (sin recolección ni transmisión de datos personales) y prepara la arquitectura para futura edición o exportación sin acoplarse a servicios externos.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-052: Captura Visual Real con CameraX y Almacenamiento Efímero de Observaciones
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-018B`
* **Tema:** Cámara / Captura Visual / Flujo de Observación
* **Resolución:**
  1. Se adopta oficialmente **AndroidX CameraX (1.3.4)** con `PreviewView` e `ImageCapture` como el subsistema de captura visual nativa para Alpha 0.1 de WHO Animal.
  2. La pantalla de captura (`CameraCaptureScreen`) sustituye el selector de catálogo previo por un visor de cámara en vivo con retícula de enfoque, botón de disparo circular de alto contraste y control de ciclo de vida atado a `LocalLifecycleOwner`.
  3. Gestión estricta de permisos en tiempo de ejecución: Requiere `android.permission.CAMERA` mediante `rememberLauncherForActivityResult`. En caso de denegación, presenta explicaciones educativas sobre observación respetuosa y opción de reintento.
  4. Almacenamiento efímero de fotografías de campo: Las capturas se guardan temporalmente en la caché local de la aplicación (`context.cacheDir/observation_<UUID>.jpg`), sirviendo exclusivamente para referenciar el campo inmutable `imagePath` de la entidad `ObservationContract`.
  5. Preservación ontológica innegociable:
     * La fotografía y la observación pertenecen al dominio sensorial efímero.
     * `Observation ≠ IdentificationResult ≠ IdentificationDecision ≠ Capture ≠ Card`.
     * La imagen capturada solo alimenta la entrada del `IdentificationService`; no se almacena en base de datos permanente ni crea cartas de manera directa ni prematura.
  6. Resiliencia en entornos emulados o sin hardware de cámara: Se provee un mecanismo de captura simulada de respaldo que previene bloqueos o caídas imprevistas de la aplicación.
* **Justificación / Principios:** Conexión del núcleo visual interactivo con el pipeline de dominio existente sin romper contratos ontológicos, asegurando fluidez en dispositivos reales y robustez en pruebas unitarias y de integración.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-053: Rebaseline de Visión de Producto, Capas Core/Game/Social y Roadmap Estratégico
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-DOC-001`
* **Tema:** Visión de Producto / Arquitectura de Capas / Roadmap / Free-to-Play
* **Resolución:**
  1. **Definición Oficial de la Visión:** Se establece formalmente la fórmula rectora del producto:
     $$\text{WHO Animal} = \text{Identificación} + \text{Cartas Coleccionables} + \text{Enciclopedia Personal} + \text{Juego Ligero} + \text{Capa Social}$$
  2. **Arquitectura Tripartita de Producto:**
     * **CORE (Irrenunciable):** $\text{Capture} \rightarrow \text{Observation} \rightarrow \text{IdentificationResult} \rightarrow \text{IdentificationDecision} \rightarrow \text{Capture} \rightarrow \text{Card} \rightarrow \text{Persistence} \rightarrow \text{Collection}$. Funciona de forma autónoma sin Game ni Social.
     * **GAME (Juego Ligero):** Rareza de colección, estadísticas ficticias de combate (HP/ATK/DEF/SPD/TYPE/SPECIAL), duelos PvP con equipos de 10 cartas configurables semanalmente, progresión, monedas gratuitas, tienda cosmética, temporadas y logros.
     * **SOCIAL (Comunidad):** Perfiles, amigos, regalos, intercambio (*trading* con metadatos históricos congelados), colecciones públicas, UGC, moderación, reporte y bloqueo.
  3. **Filosofía Free-to-Play por Diseño y Cero Pay-to-Win:**
     * El núcleo de identificación, aprendizaje y colección es y será siempre 100% gratuito.
     * Prohibición absoluta de comercializar ventajas zoológicas ficticias, mejor identificación, alteración de datos biológicos o superioridad competitiva derivada de pagar.
  4. **Arquitectura Publicitaria Desacoplada (`AdService`):**
     * Desacoplamiento estricto entre dominio zoológico y proveedores de anuncios.
     * Separación de placements y políticas (`BannerPlacement`, `InterstitialPolicy`, `RewardedAdService`, `FrequencyPolicy`).
     * Zonas libres de publicidad intrusiva: cámara, captura, identificación, carga crítica, revelación de carta, lectura científica y combates PvP.
     * Anuncios recompensados voluntarios (*opt-in*) con opción explícita "No, gracias".
  5. **Economía Ética y Tienda:**
     * Moneda gratuita obtenida mediante el juego y recompensas de descubrimiento.
     * Tienda centrada en personalización cosmética (marcos, temas, fondos, avatares, efectos visuales y utilidades no competitivas).
  6. **Rebaseline del Roadmap en 7 Fases:**
     * `FOUNDATION` $\rightarrow$ `ALPHA` (Core Loop) $\rightarrow$ `BETA` (Estabilidad/UX) $\rightarrow$ `RELEASE 1.0` (Público con Core, Baúl, Enciclopedia, Shop cosmética, Ads desacoplados) $\rightarrow$ `RELEASE 1.x (GAME)` (PvP, equipos de 10, temporadas) $\rightarrow$ `RELEASE 2.x (SOCIAL)` (Amigos, trading, UGC) $\rightarrow$ `RELEASE 3.x (WORLD)` (Biomas globales, cloud/multidispositivo).
  7. **Capacidad de Almacenamiento:**
     * La persistencia de 10 containers × 30 espacios = 300 cartas se ratifica como la capacidad oficial de la fase *Alpha / Foundation*, no como una limitación conceptual permanente del producto.
  8. **Internacionalización Transversal:**
     * Aislamiento estricto: $\text{Traducciones UI} \neq \text{Contenido Científico} \neq \text{Lore Personal}$.
  9. **Realidad Tecnológica:**
     * Reconocimiento explícito de la sinergia entre Python (dominio, servicios taxonómicos, tooling) y Android + Kotlin (aplicación nativa, UI con Jetpack Compose, CameraX, Room).
* **Justificación / Principios:** Unificar la visión de producto hacia el mercado masivo y el largo plazo sin comprometer la ética pedagógica, el rigor científico ni el foco operativo de las fases tempranas.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-054: Presentación Visual Interactiva de Carta y Volteo Tridimensional (Card Flip)
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-018C`
* **Tema:** UX / Animación 3D / Doble Cara de Carta / Presentación de Dominio
* **Resolución:**
  1. Se implementa la pantalla unificada `CardPresentationScreen` con renderizado de doble cara desacoplado (`CardFrontView` y `CardBackView`).
  2. Animación de giro tridimensional táctil mediante Compose `graphicsLayer(rotationY = animatedRotation)` y `cameraDistance = 12f * density`, con cambio de cara exacto a los 90 grados para evitar reflejos especulares de texto.
  3. Visualización estricta de la separación ontológica tripartita:
     * **Frente:** Fotografía real de la especie (o fallback artístico), categoría zoológica, nombre común y científico, rareza visual y serial único.
     * **Reverso:** Ficha científica contrastada (taxonomía, hábitat, dieta, peso, estado de conservación) y sección de Lore (Historia Personal) con advertencia explícita de narrativa personal.
  4. Flujo interactivo de toma de decisión de colección: Opciones de Guardar en Baúl (con asignación atómica a slot disponible) o Descartar/Liberar (con confirmación modal de seguridad).
* **Justificación / Principios:** Consolida la experiencia central de la carta física en un formato digital elegante, respetando la regla innegociable de separación entre ciencia y Lore sin acoplar la UI con la lógica de generación.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-055: Auditoría de Integración del Golden Path Alpha y Cierre de Gaps de Persistencia
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-018D`
* **Tema:** Integración de Sistemas / Persistencia Room / Auditoría E2E
* **Resolución:**
  1. Se audita y verifica el flujo end-to-end continuo desde la apertura de app hasta la persistencia en el Baúl:
     `Login → Home → Capture → Camera → Observation → Identification → Decision → Capture → Card → Review/Flip → Save → Storage`.
  2. Identificación de la brecha funcional de Alpha: Visualización en grid y reapertura de la carta persistida desde el Baúl (`WHO-018E`).
  3. Fortalecimiento de la persistencia Room: Se audita la retención de campos de imagen y Lore personal para garantizar supervivencia total tras reinicio.
* **Justificación / Principios:** Asegura la continuidad de la experiencia del usuario y detecta discrepancias de integración antes de declarar la madurez de la versión Alpha.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-056: Grid de Colección en Baúl y Reapertura Segura de Cartas Persistidas sin Regeneración
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-018E`
* **Tema:** Colección / Baúl / Reopening Inmutable / Persistencia Room V2
* **Resolución:**
  1. Evolución de `CollectionPlaceholderScreen` a `CollectionScreen` con navegación por contenedores (`C-1` a `C-10`), indicador de ocupación en tiempo real (baseline 10×30=300 slots) y grid de cartas de 2 columnas con soporte de miniaturas y fallback.
  2. Regla Absoluta de Reapertura Inmutable: Al seleccionar una carta del Baúl, se recupera exactamente la entidad almacenada desde `CollectionStorageRepository`. Queda terminantemente prohibido regenerar la carta, invocar de nuevo `CardGeneratorService`, alterar `card_id`, `capture_id`, rareza o serial.
  3. Modos de Presentación de Carta (`CardPresentationMode`):
     * `NEW_CARD_REVIEW`: Modo de revisión inicial tras captura con acciones de "Guardar en Baúl" y "Descartar".
     * `PERSISTED_CARD`: Modo de inspección de carta guardada; deshabilita botones de guardado/descarte para evitar duplicaciones o pérdida accidental de datos, ofreciendo navegación limpia de regreso al Baúl.
  4. Migración de Esquema Room a Versión 2: Incorporación canónica de `imagePath` y `personalLore` en `CardEntity` con mapeo simétrico bidireccional, garantizando la supervivencia integral de la carta tras el reinicio completo de la aplicación.
* **Justificación / Principios:** Cierra formalmente la última brecha funcional del Golden Path Alpha 0.1, garantizando que el usuario pueda contemplar y voltear sus cartas guardadas cuantas veces desee sin corromper la inmutabilidad histórica del objeto.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-057: Cierre Oficial de Alpha 0.1, Auditoría de Madurez y Hoja de Ruta de Transición a Beta
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-020`
* **Tema:** Auditoría Post-Release / Definición de Beta / Cierre de Deuda Técnica
* **Resolución:**
  1. **Ratificación de Alpha 0.1 (`v0.1.0-alpha`):** Se valida el primer release técnico ejecutable (Build 2, hash SHA-256 verificado en `docs/RELEASES.md`), con el Golden Path completo y probado (150/150 tests Python, 63/63 tests Android).
  2. **Definición Canónica de Fase Beta para WHO Animal:**
     * Beta **NO** adelantará mecánicas de la Capa Game (PvP, estadísticas de combate, economía in-app) ni de la Capa Social (amigos, trading, regalos), las cuales pertenecen a Release 1.x y 2.x respectivamente.
     * La fase Beta se focaliza estrictamente en seis pilares de estabilización y preparación para uso en el mundo real:
       a) **Cierre de deuda Alpha:** Edición interactiva de Lore con límite de 3 ediciones (`DEC-041`) y liberación segura de cartas desde el Baúl con modal de confirmación.
       b) **Sincronización de Catálogo Único:** Ingesta de `data/species/` como Android assets para unificar el dominio zoológico y erradicar duplicaciones hardcodeadas en código.
       c) **Internacionalización (i18n):** Extracción sistemática de cadenas Compose a `res/values/strings.xml`, respetando la regla $\text{UI} \neq \text{Ciencia} \neq \text{Lore}$.
       d) **Accesibilidad (a11y) y Hardware CameraX:** Soporte TalkBack, target táctiles mínimos de 48dp y compatibilidad robusta en múltiples sensores y resoluciones.
       e) **Infraestructura Publicitaria Desacoplada (`AdService`):** Contrato limpio y stubs locales de políticas de frecuencia sin SDKs intrusivos en runtime (`DEC-053`).
       f) **Pipeline de Distribución Interna:** Keystore de release y configuración para Google Play Internal Testing.
  3. **Hoja de Ruta de Ejecución Beta:** Se aprueba la secuencia atómica de objetivos `WHO-021` a `WHO-026` para guiar la construcción y certificación de la Beta.
* **Justificación / Principios:** Asegura que el salto a Beta responda a la visión oficial aprobada, manteniendo el rigor arquitectónico, la separación de capas y evitando el desvío del alcance hacia features comerciales tempranas.
* **Aprobado por:** Director Creativo / Project Manager

### DEC-058: Unificación del Catálogo Zoológico Oficial como Android Assets y Repositorio Desacoplado
* **Fecha:** 2026-09-07
* **Estado:** `APROBADA`
* **Objetivo:** `WHO-021`
* **Tema:** Datos Zoológicos / Sincronización Assets / Clean Architecture / Fuente Única de Verdad
* **Resolución:**
  1. **Fuente Única de Verdad (`data/species/*.json`):** Se establece formalmente la cadena de datos `data/species/*.json` $\rightarrow$ `src/main/assets/species/` + `species_catalog.json` $\rightarrow$ `SpeciesCatalogRepository` $\rightarrow$ `IdentificationService` / `CardGeneratorService` / UI.
  2. **Erradicación de Duplicación:** Se elimina la lista hardcodeada de 4 especies fijas en `OfficialStarterCatalog`. `OfficialStarterCatalog` delega de forma dinámica y transparente en `DefaultSpeciesCatalogRepository.getInstance()`, garantizando acceso a las 28 especies zoológicas canónicas con sus `animal_id` UUID oficiales.
  3. **Frontera de Dominio y Parser Dedicado:** Creación de `SpeciesCatalogRepository` en la capa de dominio y `AssetSpeciesCatalogRepository` + `SpeciesJsonParser` en la capa de datos. Utiliza `org.json` integrado (cero dependencias runtime añadidas), caching thread-safe en memoria y tolerancia a fallos/omisión segura ante datos ausentes o corruptos.
  4. **Automatización Gradle:** Tarea `syncSpeciesAssets` registrada en `build.gradle.kts` que sincroniza automáticamente `data/species/*.json` hacia `src/main/assets/species/` durante `preBuild`.
  5. **Compatibilidad Plena:** Preservación de compatibilidad con `CardPresentationScreen` integrando anotaciones pedagógicas objetivas (curiosidades científicas y advertencias preventivas responsables según `DEC-005` y Regla 11 de `AGENTS.md`).
* **Justificación / Principios:** Erradica el riesgo de divergencia biológica entre plataformas, garantiza una experiencia 100% offline sin infraestructura compleja de base de datos y mantiene la separación estricta entre Ciencia, Experiencia y Lore.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-010: Estilo y Universo Mitológico del Lore (SUPERSEDED)
* **Tema:** Diseño Narrativo
* **Estado:** `SUPERSEDED`
* **Motivo:** Sustituido por `DEC-040` (El Lore ya no es un universo mitológico ficticio, sino una Historia Personal escrita por el usuario).

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

### DEC-011-PENDING: Convención Definitiva de Identificadores `card_id` y Códigos de Colección
* **Tema:** Identificadores de Colección
* **Estado:** `PENDING`
* **Propuesta actual:** Formato amigable de exhibición `WA-[CATEGORÍA]-[NÚMERO]` complementado con identificador universal inmutable.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-012: Tecnología Cliente Definitiva para la App de Usuario (RESOLVED)
* **Tema:** Frontend Móvil
* **Estado:** `RESOLVED` (Resuelto por `DEC-049`)
* **Resolución:** Sustituida formalmente por `DEC-049`. Se adopta Android Nativo con Kotlin (2.0+), Jetpack Compose, Material 3 y Navigation Compose para el cliente oficial de WHO Animal.

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
