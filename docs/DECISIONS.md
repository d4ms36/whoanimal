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
