# Hoja de Ruta del Proyecto (Roadmap) — WHO Animal

**Documento:** `docs/ROADMAP.md`  
**Propósito:** Planificación estratégica y técnica por fases y objetivos atómicos.  
**Nota:** Las funcionalidades de fases avanzadas son propuestas conceptuales y no compromisos cerrados; se irán aprobando progresivamente por el Director Creativo.

---

## Fases Principales

```text
┌─────────────────────────┐     ┌─────────────────────────┐     ┌─────────────────────────┐
│     PHASE 0: ALPHA      │ ──> │      PHASE 1: BETA      │ ──> │    PHASE 2: RELEASE     │
│   Construcción Núcleo   │     │   Validación Usuarios   │     │   Lanzamiento Público   │
│       (v0.x.x)          │     │        (v1.x.x)         │     │        (v2.x.x)         │
└─────────────────────────┘     └─────────────────────────┘     └─────────────────────────┘
```

---

## 1. PHASE 0 — ALPHA (En Curso)

**Objetivo Central:** Construir, estructurar y validar el núcleo conceptual, arquitectónico y lógico de WHO Animal.

### Objetivos Atómicos de la Fase Alpha

* **`WHO-001` — Evaluación de Requisitos y Estado Cero** `[APPROVED_COMPLETE]`
  * Inspección del espacio de trabajo, verificación de no-código y alineación inicial.
* **`WHO-002` — Arquitectura Base y Especificación de Cartas** `[APPROVED_COMPLETE]`
  * Creación de modelos de dominio inmutables en Python (`ScientificInfo`, `AnimalProfile`, `LoreProfile`, `AnimalCard`), separación de capas y primera suite de pruebas.
* **`WHO-003` — Gobernanza, Memoria del Proyecto y Versionado** `[APPROVED_COMPLETE]`
  * Creación de manual de agentes (`AGENTS.md`), memoria oficial (`PROJECT_CONTEXT.md`), gobernanza de 3 roles, esquema de versionado semántico/Android y registro de decisiones.
* **`WHO-004` — Plan Maestro de Desarrollo y Alineación por Versión** `[APPROVED_COMPLETE]`
  * Creación del tablero operativo central (`docs/PLANNING.md`) para sincronización entre Director, PM y Developer.
* **`WHO-005A` — Incorporación de Nuevas Decisiones al Contexto** `[APPROVED_COMPLETE]`
  * Consolidación en la memoria y gobernanza de las decisiones sobre entidad Animal vs. Carta, inmutabilidad, rareza dinámica y autenticación.
* **`WHO-005B-A` — Actualización Documental de Capacidades Futuras** `[APPROVED_COMPLETE]`
  * Registro y blindaje arquitectónico de capacidades futuras aprobadas: PVP, intercambio/comercio, artwork único/ilustradores y privacidad de ubicación.
* **`WHO-005B-C` — Resolución A: Semántica de `population_at_issuance`** `[APPROVED_COMPLETE]`
  * Definición formal, alcance (`ANIMAL/SPECIES`), inmutabilidad y desacoplamiento estricto de censos biológicos reales (DEC-033).
* **`WHO-005B-D` — Auditoría y Resolución de `verification`** `[APPROVED_COMPLETE]`
  * Formalización canónica de `verification_status` como estado actual mutable, desacoplado de `serial` e identidad histórica (DEC-034).
* **`WHO-005B-D.1` — Canonicalización de `verification_status` y Semántica `null` vs `UNVERIFIED`** `[APPROVED_COMPLETE]`
  * Formalización definitiva del nombre canónico, estado inicial `UNVERIFIED` y reserva de `null` para compatibilidad histórica (DEC-035).
* **`WHO-005C.1` — Definición Conceptual de `sex` en Animal/Capture** `[APPROVED_COMPLETE]`
  * Delimitación ontológica del sexo biológico en Capture/Specimen y desacoplamiento del modelo taxonómico Animal (DEC-036).
* **`WHO-005B-E` — Auditoría Semántica Final de la Estructura `Card`** `[APPROVED_COMPLETE]`
  * Auditoría y cierre conceptual de los 19 campos y 6 módulos funcionales previa a la serialización formal de esquemas.
* **`WHO-005B-E.1` — Cierre Semántico de `rank` y `rarity`** `[APPROVED_COMPLETE]`
  * Delimitación estricta de `rarity` (inmutable/emisión, ejemplos de tiers no contractuales) y `rank` (mutable/progresión, sin fórmulas ni implicaciones zoológicas).
* **`WHO-006A` — Definición Formal de Tipos y Obligatoriedad de los 19 Campos de Card** `[APPROVED_COMPLETE]`
  * Formalización contractual de tipos técnicos, requiredness, nullability, defaults e inmutabilidad previa a la implementación de modelos.
* **`WHO-006A.1` — Corrección del Contrato de Obligatoriedad, Nullability y Defaults de Card** `[APPROVED_COMPLETE]`
  * Eliminación de defaults no aprobados, distinción estricta entre Optional y Nullable, y protección histórica de `display_location`.
* **`WHO-006B` — Implementación Formal del Modelo Card** `[APPROVED_COMPLETE]`
  * Implementación del modelo de dominio `Card` con los 19 campos canónicos, validación técnica estricta, inmutabilidad post-emisión y cobertura de pruebas.
* **`WHO-006B.1` — Corrección de Contrato Técnico del Modelo Card** `[APPROVED_COMPLETE]`
  * Alineación estricta con el contrato: UUIDv4 exclusivo, distinción ausencia vs. null en `edition`, `artwork` como tipo abierto, `rank` int/str, protección anti-GPS y 30 tests unitarios.
* **`WHO-006C` — Formalizar Dominio Capture/Specimen** `[APPROVED_COMPLETE]`
  * Formalización de la entidad `Capture` (`capture_id` UUIDv4, `sex ∈ {MALE, FEMALE, UNKNOWN}`) delimitando estrictamente que el sexo biológico pertenece al espécimen observado y no al conocimiento taxonómico de la especie (`Animal`) ni es canónico de `Card` (DEC-036).
  * **`WHO-006C.2`:** Corrección técnica para establecer `Capture.sex` como `OPTIONAL`, `NULLABLE` y sin valor default (DEC-039).
* **`WHO-006D` — Formalizar Modelo de Monetización Gratuito + Publicidad** `[APPROVED_COMPLETE]`
  * Documentar el principio de producto 100% gratuito, uso de publicidad (ej. AdMob) como infraestructura externa desacoplada y prioridad conceptual de Rewarded Ads sin implementar economía in-app (DEC-038).

* **`WHO-007` — Banco de Datos Inicial de Fauna (Semilla Educativa)** `[APPROVED_COMPLETE]`
  * Creación e implementación formal del modelo de dominio `AnimalProfile` como fuente única de verdad para la identidad zoológica, estableciendo la frontera ontológica inquebrantable frente a `Capture` y `Card`.
* **`WHO-008A` — Formalizar Historia Personal de la Carta (Personal Lore)** `[APPROVED_COMPLETE]`
  * Formalización documental del concepto de "Lore" como Historia Personal escrita por el usuario, limitada a 300 caracteres, asociada a la carta.
* **`WHO-008B` — Definir reglas de edición de la Historia Personal (Lore)** `[APPROVED_COMPLETE]`
  * Documentar reglas de edición: límite global de 3 ediciones por cuenta y Solicitud Oficial excepcional.
* **`WHO-010` — Conexión y publicación inicial del repositorio** `[APPROVED_COMPLETE]`
  * Configuración de infraestructura Git/GitHub y publicación del código inicial.
* **`WHO-010A` — Auditoría y Sincronización Integral de Documentación** `[APPROVED_COMPLETE]`
  * Revisión exhaustiva y consolidación de documentación técnica y directrices del proyecto.
* **`WHO-011A` — Infraestructura del Banco de Datos Zoológico** `[APPROVED_COMPLETE]`
  * Infraestructura base para almacenar y validar el dataset estático de especies en JSON.
* **`WHO-011B` — Implementación del Índice Taxonómico Oficial** `[APPROVED_COMPLETE]`
  * Servicio de dominio y archivo JSON para validar y buscar rutas taxonómicas de especies.
* **`WHO-011C` — Implementación del Catálogo Zoológico Oficial Inicial** `[APPROVED_COMPLETE]`
  * Primera ingesta formal de 28 especies JSON, validadas estrictamente sin motor de base de datos.
* **`WHO-011D` — Enriquecimiento Científico del Catálogo Zoológico** `[APPROVED_COMPLETE]`
  * Incorporación de información científica estable (hábitat, dieta, peso, etc.) a las especies iniciales respetando Clean Architecture.
* **`WHO-011` — Evaluación y Prototipo de Ingesta Taxonómica** `[PROPOSED]`
  * Conector experimental con APIs de biodiversidad abiertas (GBIF / iNaturalist) para validación de datos.
* **`WHO-012` — Prototipo del Servicio de Generación de Cartas** `[PROPOSED]`
  * Implementación del generador de cartas en base al protocolo `CardGeneratorService`.
* **`WHO-013` — Prototipo del Motor de Identificación por Visión** `[PROPOSED]`
  * Implementación experimental de `IdentificationService` mediante modelos locales ligeros o API de visión.
* **`WHO-014` — Motor de Persistencia y Álbum de Colección** `[PROPOSED]`
  * Implementación de persistencia local (SQLite) para inventario y álbum de colección.
* **`WHO-015` — Interfaz Gráfica / Prototipo de Cliente Móvil** `[PROPOSED]`
  * Configuración del frontend cliente (Flutter) para visualización e interacción de cartas con volteo.

---

## 2. PHASE 1 — BETA (Planificada)

**Objetivo Central:** Preparar el producto para evaluadores reales, estabilizar la experiencia visual y calibrar la precisión del sistema.

* Pruebas de usabilidad del core loop: *Identificar → Descubrir → Carta → Aprender → Lore → Coleccionar*.
* Calibración y pruebas de campo del modelo de identificación en distintas condiciones lumínicas.
* Optimización de rendimiento, animaciones de volteo de cartas y fluidez en dispositivos móviles.
* Pruebas internas de distribución mediante Google Play Internal Testing (`versionCode` 2 en adelante).
* Revisión legal y formal del `DISCLAIMER.md` y términos de servicio con asesores profesionales.

---

## 3. PHASE 2 — RELEASE (Futura)

**Objetivo Central:** Preparar y desplegar la versión pública y comercialmente estable.

* Publicación en tiendas oficiales (Google Play Store).
* Sistema ampliado de álbumes, logros éticos y exploración por biomas.
* Infraestructura de sincronización en la nube (si es aprobada por el Director).
* Soporte continuo y ampliación del catálogo de especies y Lore.

---

## 4. EXTENSIONES FUTURAS APROBADAS (Post-Lanzamiento)

Capacidades de evolución aprobadas conceptualmente que se implementarán en fases avanzadas:
* **Módulo de Intercambio y Comercio de Cartas:** Transferencia controlada de propiedad entre coleccionistas preservando la inmutabilidad histórica original.
* **Sistema de Artwork Único y Red de Ilustradores:** Encargos artísticos personalizados gestionados in-app asociados a cartas específicas.
* **Módulo de Enfrentamientos PVP:** Sistema de duelos entre cartas desacoplado del conocimiento zoológico y del core loop.

