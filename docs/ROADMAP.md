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
* **`WHO-004` — Plan Maestro de Desarrollo y Alineación por Versión** `[IN_PROGRESS]`
  * Creación del tablero operativo central (`docs/PLANNING.md`) para sincronización entre Director, PM y Developer.
* **`WHO-005` — Validación de Esquemas y Serialización JSON (Planificado)** `[PLANNED]`
  * Definición de esquemas de exportación/importación JSON estandarizados para cartas y perfiles biológicos (con Pydantic / dataclasses).
* **`WHO-006` — Banco de Datos Inicial de Fauna (Semilla Educativa)** `[PROPOSED]`
  * Creación de un conjunto inicial de especímenes reales verificados (mamíferos, aves, reptiles) con información científica completa y Lore demarcado.
* **`WHO-007` — Prototipo del Servicio de Generación de Cartas** `[PROPOSED]`
  * Implementación del generador de cartas en base al protocolo `CardGeneratorService`.
* **`WHO-008` — Evaluación y Prototipo de Ingesta Taxonómica** `[PROPOSED]`
  * Conector experimental con APIs de biodiversidad abiertas (GBIF / iNaturalist) para validación de datos.
* **`WHO-009` — Prototipo del Motor de Identificación por Visión** `[PROPOSED]`
  * Implementación experimental de `IdentificationService` mediante modelos locales ligeros o API de visión.
* **`WHO-010` — Motor de Persistencia y Álbum de Colección** `[PROPOSED]`
  * Implementación de persistencia local (SQLite) para inventario y álbum de colección.
* **`WHO-011` — Interfaz Gráfica / Prototipo de Cliente Móvil** `[PROPOSED]`
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
