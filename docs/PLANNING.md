# Plan Maestro de Desarrollo (Master Development Planning) — WHO Animal

**Documento:** `docs/PLANNING.md`  
**Propósito:** Tablero operativo central de alineación entre Director Creativo, Project Manager y Developer Principal.  
**Estado:** Activo y dinámico  
**Última actualización:** 2026-09-06 (Actualización de ciclo WHO-006B)

---

## 1. Estado Actual del Proyecto

| Campo | Estado |
| :--- | :--- |
| **Proyecto** | WHO Animal |
| **Fase actual** | Transición Alpha $\rightarrow$ Beta (Auditoría Alpha 0.1 Completada) |
| **Versión actual** | `0.1.0-alpha` (Build 2) |
| **Estado** | Auditoría Post-Release Alpha 0.1 y Hoja de Ruta Beta Consolidadas |
| **Objetivo activo** | Ninguno (Ciclo WHO-027 completado) |
| **Último objetivo completado** | `WHO-024` — Accesibilidad (a11y) y Robustecimiento de Compatibilidad CameraX (`DEC-061`) |
| **Próximo objetivo propuesto** | Pendiente de definición |
| **Bloqueos** | Ninguno |
| **Decisiones pendientes** | `DEC-009`, `DEC-011`, `DEC-021` a `DEC-025`, `DEC-037-PENDING` |
| **Última actualización** | 2026-09-07 |

---

## 2. Versión Actualmente en Desarrollo

```text
=====================================================
          VERSIÓN ACTUAL EN DESARROLLO: 0.0.1
                      Fase: ALPHA
=====================================================
```

### Significado de la Versión según WHO Animal:
La versión no sigue la semántica convencional de SemVer de librerías, sino el esquema propio definido en [docs/VERSIONING.md](VERSIONING.md):

$$\text{FASE} . \text{CORRECCIONES} . \text{ITERACIÓN}$$

* **Primer número (`0` = FASE):** Representa la madurez global del producto (`0 = Alpha`, `1 = Beta`, `2 = Release`).
* **Segundo número (`0` = CORRECCIONES):** Nivel acumulado de parches, correcciones y ajustes internos en el ciclo.
* **Tercer número (`1` = ITERACIÓN):** Primera entrega o iteración funcional del producto dentro de la fase correspondiente (Fundación arquitectónica y documental).

---

## 3. Objetivos de la Versión (`0.0.1`)

| ID | Objetivo | Estado | Prioridad | Versión | Aprobado |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **WHO-001** | Exploración Inicial y Estado Cero | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-002** | Fundación de Arquitectura y Especificación de Cartas | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-003** | Gobernanza, Memoria del Proyecto y Versionado | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-004** | Plan Maestro de Desarrollo y Alineación por Versión | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005A**| Incorporación de Nuevas Decisiones al Contexto | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-A**| Actualización Documental de Capacidades Futuras | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-C**| Resolución A: Semántica de `population_at_issuance` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-D**| Auditoría y resolución de `verification` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-D.1**| Canonicalización de `verification_status` y `null` vs `UNVERIFIED` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005C.1**| Definición conceptual de `sex` en Animal/Capture | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-E**| Auditoría semántica final de la estructura `Card` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-E.1**| Cierre semántico de `rank` y `rarity` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006A**| Definición formal de tipos y obligatoriedad de los 19 campos de Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006A.1**| Corrección del contrato de obligatoriedad, nullability y defaults de Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006B**| Implementación formal del modelo Card (19 campos canónicos) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006B.1**| Corrección de contrato técnico del modelo Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006C**| Formalizar dominio Capture/Specimen (DEC-036) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006C.2**| Cierre técnico de Capture/Specimen y sex (DEC-039) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006D**| Formalizar Modelo de Monetización Gratuito + Publicidad | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-007** | Banco de Datos Inicial de Fauna (Semilla Educativa) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-008A** | Formalizar Historia Personal de la Carta (Personal Lore) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-008B** | Definir reglas de edición de la Historia Personal (Lore) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-010** | Conexión y publicación inicial del repositorio | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-010A** | Auditoría y Sincronización Integral de Documentación | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011A** | Infraestructura del Banco de Datos Zoológico | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011B** | Implementación del Índice Taxonómico Oficial | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011C** | Implementación del Catálogo Zoológico Oficial Inicial | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011D** | Enriquecimiento Científico del Catálogo Zoológico | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-012A** | Implementación del Motor de Observaciones | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-012B** | Implementación del Resultado de Identificación Zoológica | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-012C** | Formalizar la decisión explícita sobre un IdentificationResult | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-012D** | Implementación del puente IdentificationDecision → Capture | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-013** | Consolidación de Arquitectura, Roadmap y Mínimo Funcional | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-013.1** | Formalización de Alpha y Horizontes Estratégicos | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-014** | Servicio de Ensamblaje y Generación de Cartas | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-015** | Fundación del Cliente Android y Decisión Tecnológica Móvil | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-016** | Servicio de Identificación de Especies (Observation → Result) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-017** | Motor de Persistencia Local y Colección (Room SQLite) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-018A**| Login Alpha Local (Perfil y Sesión Offline) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-018B**| Captura y Cámara Alpha (CameraX / Visual Input) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-DOC-001**| Product Vision & Roadmap Rebaseline | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-018C**| Visualización y Giro 3D de Carta (Card View & Flip) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-018D**| Auditoría de Integración del Golden Path y UX | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-018E**| Grid de Colección y Reapertura de Cartas Persistidas | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-018** | Integración del Mínimo Funcional Android (Core Loop) | `COMPLETADO` | Alta | 0.0.1 | Sí |

---

### Tabla Maestra de Auditoría de Objetivos WHO (Fase 0 y Propuestos Históricos)

Auditoría integral oficial realizada en **WHO-013** y consolidada en **WHO-013.1** para resolver colisiones de numeración y definir la situación de cada identificador:

| ID | Nombre Oficial | Estado | Fase | Dependencias | Situación / Resolución |
| :--- | :--- | :---: | :---: | :--- | :--- |
| **WHO-001** | Evaluación de Requisitos y Estado Cero | `COMPLETADO` | Fase 0 | Ninguna | Base cero inicial verificada |
| **WHO-002** | Fundación de Arquitectura y Especificación de Cartas | `COMPLETADO` | Fase 0 | WHO-001 | Modelos base inmutables y Clean Architecture |
| **WHO-003** | Gobernanza, Memoria del Proyecto y Versionado | `COMPLETADO` | Fase 0 | WHO-002 | Manual AGENTS.md, gobernanza y versionado |
| **WHO-004** | Plan Maestro de Desarrollo y Alineación por Versión | `COMPLETADO` | Fase 0 | WHO-003 | Creación de PLANNING.md |
| **WHO-005A** | Incorporación de Nuevas Decisiones al Contexto | `COMPLETADO` | Fase 0 | WHO-004 | Memoria de producto y decisiones clave |
| **WHO-005B-A** | Actualización Documental de Capacidades Futuras | `COMPLETADO` | Fase 0 | WHO-005A | Blindaje documental de capacidades futuras |
| **WHO-005B-B** | Validación de Esquemas y Serialización JSON | `ABSORBIDO` | Fase 0 | WHO-002, WHO-005B-A | Absorbido en modelos de dominio (`to_dict`/`from_dict`) y esquemas JSON del dataset |
| **WHO-005B-C** | Semántica de population_at_issuance | `COMPLETADO` | Fase 0 | WHO-005B-A | Definición formal e inmutabilidad (DEC-033) |
| **WHO-005B-D** | Auditoría y Resolución de verification | `COMPLETADO` | Fase 0 | WHO-005B-C | verification_status como estado mutable (DEC-034) |
| **WHO-005B-D.1** | Canonicalización de verification_status y null vs UNVERIFIED | `COMPLETADO` | Fase 0 | WHO-005B-D | Estado inicial UNVERIFIED (DEC-035) |
| **WHO-005C.1** | Definición Conceptual de sex en Animal/Capture | `COMPLETADO` | Fase 0 | WHO-005B-D.1 | Sexo biológico atribuido a Capture (DEC-036) |
| **WHO-005B-E** | Auditoría Semántica Final de la Estructura Card | `COMPLETADO` | Fase 0 | WHO-005C.1 | Cierre de 19 campos y 6 módulos funcionales |
| **WHO-005B-E.1** | Cierre Semántico de rank y rarity | `COMPLETADO` | Fase 0 | WHO-005B-E | Ortogonalidad absoluta entre rank y rarity (DEC-032) |
| **WHO-006A** | Definición Formal de Tipos y Obligatoriedad de Card | `COMPLETADO` | Fase 0 | WHO-005B-E.1 | Contrato formal de tipos de Card |
| **WHO-006A.1** | Corrección del Contrato de Obligatoriedad y Defaults | `COMPLETADO` | Fase 0 | WHO-006A | Eliminación de defaults y protección anti-GPS |
| **WHO-006B** | Implementación Formal del Modelo Card | `COMPLETADO` | Fase 0 | WHO-006A.1 | Subsumido por WHO-006B.1 |
| **WHO-006B.1** | Corrección de Contrato Técnico del Modelo Card | `COMPLETADO` | Fase 0 | WHO-006B | UUIDv4 estricto, 30 tests unitarios pasando |
| **WHO-006C** | Formalizar Dominio Capture/Specimen | `COMPLETADO` | Fase 0 | WHO-006B.1 | Subsumido por WHO-006C.2 |
| **WHO-006C.2** | Cierre Técnico de Capture/Specimen y sex | `COMPLETADO` | Fase 0 | WHO-006C | sex como opcional, nullable, sin default (DEC-039) |
| **WHO-006D** | Formalizar Modelo de Monetización Gratuito + Publicidad | `COMPLETADO` | Fase 0 | WHO-006C.2 | Producto 100% gratuito y Rewarded Ads (DEC-038) |
| **WHO-007** | Banco de Datos Inicial de Fauna (Semilla Educativa) | `COMPLETADO` | Fase 0 | WHO-006D | Modelo AnimalProfile como fuente única de verdad |
| **WHO-008A** | Formalizar Historia Personal de la Carta (Lore) | `COMPLETADO` | Fase 0 | WHO-007 | Lore como historia personal de 300 caracteres |
| **WHO-008B** | Definir reglas de edición de la Historia Personal | `COMPLETADO` | Fase 0 | WHO-008A | Límite de 3 ediciones por cuenta (DEC-041) |
| **WHO-010** | Conexión y publicación inicial del repositorio | `COMPLETADO` | Fase 0 | WHO-008B | Infraestructura Git y GitHub configurada |
| **WHO-010A** | Auditoría y Sincronización Integral de Documentación | `COMPLETADO` | Fase 0 | WHO-010 | Sincronización y resolución de duplicados |
| **WHO-011A** | Infraestructura del Banco de Datos Zoológico | `COMPLETADO` | Fase 0 | WHO-010A | Esquemas JSON y validador de catálogo |
| **WHO-011B** | Implementación del Índice Taxonómico Oficial | `COMPLETADO` | Fase 0 | WHO-011A | Servicio TaxonomyIndex en memoria sin BD |
| **WHO-011C** | Implementación del Catálogo Zoológico Oficial Inicial | `COMPLETADO` | Fase 0 | WHO-011B | 28 especies zoológicas validadas |
| **WHO-011D** | Enriquecimiento Científico del Catálogo Zoológico | `COMPLETADO` | Fase 0 | WHO-011C | Campos científicos agregados (DEC-042) |
| **WHO-012A** | Implementación del Motor de Observaciones | `COMPLETADO` | Fase 0 | WHO-011D | Modelo Observation efímero (DEC-043) |
| **WHO-012B** | Implementación del Resultado de Identificación Zoológica | `COMPLETADO` | Fase 0 | WHO-012A | Modelo IdentificationResult (DEC-044) |
| **WHO-012C** | Formalizar la decisión explícita sobre un IdentificationResult | `COMPLETADO` | Fase 0 | WHO-012B | Modelo IdentificationDecision (DEC-045) |
| **WHO-012D** | Implementación del puente IdentificationDecision → Capture | `COMPLETADO` | Fase 0 | WHO-012C | Creación de Capture a partir de ACCEPTED (DEC-046) |
| **WHO-013** | Consolidación de Arquitectura, Roadmap y Mínimo Funcional | `COMPLETADO` | Fase 0 | WHO-012D | Auditoría, cierre de Fase 0 y Roadmap de 5 fases (DEC-047) |
| **WHO-013.1** | Formalización de Alpha y Horizontes Estratégicos | `COMPLETADO` | Fase 0 | WHO-013 | Formalización canónica de Alpha 0.1, Golden Path, delimitación de Storage (10x30) y 4 horizontes estratégicos (DEC-048) |
| **WHO-011 (Antiguo)** | Evaluación y Prototipo de Ingesta Taxonómica API | `REUBICADO` | Fase 4 | WHO-018 | Reubicado a Fase 4 (Ecosystem) para ingesta masiva externa |
| **WHO-012 (Antiguo)** | Prototipo del Servicio de Generación de Cartas | `REUBICADO` | Fase 1 | WHO-013 | Reubicado y resecuenciado como WHO-014 en Fase 1 |
| **WHO-013 (Antiguo)** | Prototipo del Motor de Identificación por Visión | `REUBICADO` | Fase 1 | WHO-015 | Reubicado y resecuenciado como WHO-016 en Fase 1 |
| **WHO-014 (Antiguo)** | Motor de Persistencia y Álbum de Colección | `REUBICADO` | Fase 1 | WHO-016 | Reubicado y resecuenciado como WHO-017 en Fase 1 |
| **WHO-015 (Antiguo)** | Interfaz Gráfica / Prototipo Cliente Móvil | `REUBICADO` | Fase 1 | WHO-014 | Reubicado y resecuenciado como WHO-015 en Fase 1 |

---

## 4. Objetivo Activo

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│                           OBJETIVO ACTIVO ACTUAL                            │
├──────────────────┬──────────────────────────────────────────────────────────┤
│ ID               │ Ninguno                                                  │
│ Nombre           │ N/A                                                      │
│ Propósito        │ N/A                                                      │
│ Estado           │ N/A                                                      │
│ Versión Asociada │ N/A                                                      │
│ Requisitos       │ N/A                                                      │
│ Responsable      │ N/A                                                      │
└──────────────────┴──────────────────────────────────────────────────────────┘
```

* **Criterios de Aceptación:** N/A
* **Archivos Afectados:** N/A
* **Dependencias:** N/A
* **Bloqueos:** Ninguno.
* **Resultado Esperado:** Esperando aprobación formal del Director o Project Manager para iniciar un nuevo objetivo.

---

## 5. Próximos Objetivos — Fase 1: Alpha Funcional 0.1 (Construcción de Alpha)

**Definición Oficial de Alpha 0.1 (DEC-048):**
> *"Un usuario nuevo debe poder entrar a WHO Animal, fotografiar un animal, obtener una identificación, generar una carta, revisarla, guardarla en su colección, cerrar la aplicación, volver a abrirla y encontrar la carta nuevamente."*

**Golden Path Oficial (Completado):**
```text
LOGIN → HOME → CAPTURE → CAMERA → OBSERVATION → IDENTIFICATION → RESULT → DECISION → CAPTURE → CARD → REVIEW / FLIP → SAVE → STORAGE → REOPEN
```

**Flujo Alternativo (Gestión de Colección):**
```text
HOME → STORAGE → CONTAINER → CARD → VIEW / FLIP / BACK
```
*Capacidad de Storage: 10 containers x 30 espacios (300 cartas de capacidad de la fase Alpha).*

### Secuencia Técnica de Alpha (WHO-014 a WHO-019):

| ID | Nombre | Propósito | Prioridad | Dependencias | Estado | Req. Aprobación Director |
| :--- | :--- | :--- | :---: | :--- | :---: | :---: |
| **WHO-014** | Servicio de Ensamblaje y Generación de Cartas (`Capture → Card`) | Implementación del servicio de dominio `CardGeneratorService` que emite una `Card` formal desde una `Capture` validada | Alta | WHO-013.1 | `COMPLETADO` | **Sí** |
| **WHO-015** | Fundación del Cliente Android y Decisión Tecnológica Móvil | Configuración del proyecto base Android y formalización de la tecnología de interfaz de usuario | Alta | WHO-014, DEC-012 | `COMPLETADO` | **Sí** |
| **WHO-016** | Servicio de Identificación de Especies (`Observation → IdentificationResult`) | Implementación de `IdentificationService` mediante motor local ligero/on-device | Alta | WHO-015, DEC-009 | `COMPLETADO` | **Sí** |
| **WHO-017** | Motor de Persistencia Local y Colección (`Collection Album`) | Almacenamiento local para inventario, capturas y visualización de cartas en el álbum del usuario (10 containers x 30 espacios) | Media | WHO-016 | `COMPLETADO` | **Sí** |
| **WHO-018A**| Login Alpha Local (Perfil y Sesión Offline) | Pantalla de bienvenida, creación de perfil de explorador local, persistencia con Room y navegación automática condicional | Alta | WHO-017 | `COMPLETADO` | **Sí** |
| **WHO-018B**| Captura y Cámara Alpha (CameraX / Visual Input) | Integración de CameraX y toma de fotografía zoológica con gestión de permisos | Alta | WHO-018A | `COMPLETADO` | **Sí** |
| **WHO-DOC-001**| Product Vision & Roadmap Rebaseline | Rebaseline integral de visión (Core/Game/Social), F2P por diseño, AdService, economía y roadmap oficial en 7 fases (DEC-053) | Alta | WHO-018B | `COMPLETADO` | **Sí** |
| **WHO-018C**| Visualización y Giro 3D de Carta (Card View & Flip) | Visualización frontal/trasera de carta, animación de volteo tridimensional táctil y persistencia (DEC-054) | Alta | WHO-DOC-001 | `COMPLETADO` | **Sí** |
| **WHO-018D**| Auditoría de Integración del Golden Path y UX | Auditoría funcional end-to-end de 11 pasos y detección de gaps (DEC-055) | Alta | WHO-018C | `COMPLETADO` | **Sí** |
| **WHO-018E**| Grid de Colección y Reapertura de Cartas Persistidas | Selector de contenedores, grid visual en Baúl, reapertura inmutable sin regeneración, modo PERSISTED_CARD y Room v2 (DEC-056) | Alta | WHO-018D | `COMPLETADO` | **Sí** |
| **WHO-018** | Integración del Mínimo Funcional Android (End-to-End Core Loop) | Cierre integral del flujo de 11 pasos; formalmente completado y subsumido por la serie `WHO-018A` a `WHO-018E` | Alta | WHO-018E | `COMPLETADO` | **Sí** |
| **WHO-019** | Alpha 0.1 Release Packaging & Tagging (`v0.1.0-alpha`) | Sincronización de versión Android (versionCode 2, versionName "0.1.0-alpha"), verificación de assembleRelease, registro en RELEASES.md y creación del tag Git v0.1.0-alpha | Alta | WHO-018E | `COMPLETADO` | **Sí** |
| **WHO-020** | Auditoría Post-Release Alpha 0.1 y Madurez Beta (Beta Readiness) | Auditoría integral por capas, comprobación de evidencia en código, plan de cierre de deuda y definición del roadmap Beta (DEC-057) | Alta | WHO-019 | `COMPLETADO` | **Sí** |
| **WHO-021** | Ingesta y Sincronización del Catálogo JSON como Android Assets | Unificación de la fuente única de verdad biológica (`data/species/`), eliminando duplicación de especies en código Kotlin | Alta | WHO-020 | `COMPLETADO` | **Sí** |
| **WHO-022** | Extracción y Externalización de Cadenas de UI (i18n Foundation) | Migración de cadenas hardcodeadas de Compose a `strings.xml`, preparando la internacionalización ($\text{UI} \neq \text{Ciencia} \neq \text{Lore}$) | Alta | WHO-021 | `COMPLETADO` | **Sí** |
| **WHO-023** | Edición Interactiva de Lore (DEC-041) y Liberación en Baúl | UI de edición de Historia Personal con control de cuota de 3 ediciones y acción segura de liberar carta desde Baúl con modal | Alta | WHO-022 | `COMPLETADO` | **Sí** |
| **WHO-024** | Accesibilidad (a11y) y Robustecimiento de Compatibilidad CameraX | Soporte TalkBack, target táctiles mínimos de 48dp y compatibilidad CameraX ante variantes de sensor y rotación | Media | WHO-023 | `COMPLETADO` | **Sí** |
| **WHO-025** | Contrato Desacoplado `AdService` y Stubs de Infraestructura | Definición de interfaces limpias de publicidad desacoplada (`DEC-053`) y stubs locales de políticas de frecuencia | Media | WHO-024 | `COMPLETADO` | **Sí** |
| **WHO-026** | Pipeline de Firma de Release y Google Play Internal Testing | Keystore de release, optimización R8/ProGuard y preparación del paquete de distribución interna `v0.2.0-beta.1` (versionCode 3) | Alta | WHO-025 | `COMPLETADO` | **Sí** |
| **WHO-027** | Beta Validation Protocol & Field Testing | Protocolo de test Beta (Casos manuales, compatibilidad, reporte de bugs, smoke test) para validar en dispositivos físicos la estabilidad de `v0.2.0-beta.1` | Alta | WHO-026 | `COMPLETADO` | **Sí** |

### 5.1 Especificación Formal del Próximo Objetivo: Pendiente

* **ID:** `Pendiente`
* **Nombre Oficial:** `Decoupled AdService Contract & Infrastructure Stubs`
* **Objetivo:** Definir e implementar el contrato desacoplado de infraestructura publicitaria (`DEC-053`) mediante interfaces puras y stubs locales con simulación de políticas de frecuencia y rewarded ads para recarga ética de slots o aceleración opcional, sin introducir dependencias invasivas de SDKs de terceros ni violar la separación de capas de Clean Architecture.
* **Alcance:**
  1. Contrato `AdService` y modelos de políticas (`PlacementType`, `AdPolicy`, `AdRewardResult`).
  2. Implementación de `LocalStubAdService` con simulación determinista para pruebas y desarrollo.
  3. Pruebas unitarias de límites de frecuencia y entrega de recompensas.
* **Fuera de Alcance:**
  - Integración de Google Mobile Ads SDK o redes comerciales en runtime.
  - Pay-to-win, loot boxes o micropagos con dinero real.
* **Dependencias:** `WHO-024` (Completado).
* **Criterios de Aceptación:**
  1. El dominio de WHO Animal no tiene dependencias hacia librerías de anuncios.
  2. Políticas de frecuencia y recompensas operan de forma desacoplada y predecible.
  3. Tests pasan al 100%.

> ⚠️ **Aviso de Gobernanza:**  
> Que un objetivo aparezca en esta tabla **NO constituye autorización para su desarrollo**.  
> Solo los objetivos marcados como `APROBADO` en la sección de control de aprobaciones pueden ser ejecutados por el Developer.

---

## 6. Control de Aprobación (Aprobaciones)

| Objetivo | Director Creativo | Project Manager | Developer | Estado Global |
| :--- | :---: | :---: | :---: | :--- |
| **WHO-001** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-002** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-003** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-004** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-C**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-D**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-D.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005C.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-E**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-E.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006A.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006B**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006B.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006C**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006C.2**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006D**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-007** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-008A** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-008B** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-010** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-010A** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011A** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011B** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011C** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011D** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-012A** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-012B** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-012C** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-012D** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-013** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-013.1** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-014** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-015** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-016** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-017** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018A**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018B**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-DOC-001**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018C**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018D**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018E**| `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-018** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-019** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-020** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-021** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-022** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-023** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-024** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-025** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |

> **Regla:** El Developer no puede auto-aprobar objetivos. La autorización debe ser explícita por parte del Director Creativo y estructurada por el Project Manager.

---

## 7. Decisiones que Necesitamos Tomar

Vista operativa de las decisiones pendientes documentadas oficialmente en [docs/DECISIONS.md](DECISIONS.md):

| ID | Decisión | Impacto | ¿Bloquea desarrollo inmediato? | Estado Oficial |
| :--- | :--- | :---: | :---: | :---: |
| **DEC-009** | Motor definitivo de identificación visual (on-device vs. API vs. nube) | Alto | Sí (bloquea WHO-016) | `PENDING` |
| **DEC-011** | Convención definitiva de identificadores `card_id` y códigos de colección | Medio | No (temporal en v0.1) | `PENDING` |
| **DEC-012** | Tecnología cliente definitiva para la app de usuario (Android nativo / Flutter) | Alto | No (resuelto por DEC-049) | `RESOLVED` |
| **DEC-020** | Diseño definitivo del esquema JSON de datos (`Animal` y `Card`) | Alto | No (absorbido en dominio) | `PENDING` |
| **DEC-021** | Umbrales definitivos de confianza en la identificación por visión/cámara | Medio | Sí (para WHO-016) | `PENDING` |
| **DEC-022** | Algoritmo matemático y curvas de probabilidad para rareza dinámica | Alto | No (Fase 4 Ecosystem) | `PENDING` |
| **DEC-023** | Reglas anti-abuso para validación de observaciones y población | Alto | No (Fase 4 Ecosystem) | `PENDING` |
| **DEC-024** | Sistema definitivo de autenticación y verificación de cartas | Medio | No (Fase 4 Ecosystem) | `PENDING` |
| **DEC-025** | Estrategia de compatibilidad y migración entre versiones del esquema JSON | Medio | No (en fase documental) | `PENDING` |
| **DEC-037** | Sistema definitivo de progresión, niveles y mecánicas de `rank` | Medio | No (Fase 4 Ecosystem) | `PENDING` |

---

## 8. Bloqueos (Blockers)

| Bloqueo | Causa | Objetivo Afectado | Responsable | Acción Necesaria | Estado |
| :--- | :--- | :---: | :---: | :--- | :---: |
| **Ninguno.** | N/A | N/A | N/A | N/A | `CLEAR` |

*Actualmente el flujo de trabajo opera sin impedimentos técnicos ni bloqueos externos.*

---

## 9. Registro Resumido de Versiones

| Versión | Fase | Estado | Objetivos Asociados | VersionCode | Tipo |
| :--- | :--- | :--- | :--- | :---: | :--- |
| **0.0.1** | Alpha | Foundation | WHO-001 a WHO-005B-A | **1\*** | Internal / Foundation |
| **0.1.0-alpha** | Alpha | Released | WHO-001 a WHO-019 | **2** | Internal Alpha Release |

*\*Nota: El `versionCode` 1 fue de carácter lógico en la fundación del repositorio. El `versionCode` 2 corresponde al primer binario físico APK release generado (`app-release-unsigned.apk`, SHA-256 verificado en [docs/RELEASES.md](RELEASES.md)).*  
*La fuente histórica y vinculante completa reside en [docs/RELEASES.md](RELEASES.md).*

---

## 10. Regla APK / AAB de Distribución

Recordatorio mandatorio del protocolo establecido en [docs/VERSIONING.md](VERSIONING.md):

Antes de compilar y distribuir cualquier paquete Android (APK/AAB):
1. Comprobar la versión actual en [docs/RELEASES.md](RELEASES.md).
2. Determinar el nuevo `versionName`.
3. Incrementar obligatoriamente `versionCode` (entero estrictamente mayor, único).
4. No reutilizar jamás un `versionCode` ya empleado.
5. Registrar el cambio en [docs/RELEASES.md](RELEASES.md).
6. Generar el binario físico.
7. Ejecutar validaciones de integridad y testing de instalación.
8. Registrar el artefacto con su hash SHA-256 oficial.

---

## 11. Fuera del Alcance de Alpha (Qué NO Estamos Haciendo en Alpha)

Para prevenir el desvío del alcance (*scope creep*) y asegurar la entrega de un producto Android funcional y demostrable en el menor tiempo razonable, las siguientes capacidades quedan **explícitamente fuera del alcance de Alpha**:

* ❌ **Comercio de cartas (Trading):** Capacidad futura de Fase 4.
* ❌ **Sistema de duelos (PVP):** Capacidad futura de Fase 4 desacoplada del core loop.
* ❌ **Mercado interno (Marketplace):** No contemplado para Alpha.
* ❌ **Economía in-app completa:** Sin monedas virtuales, gemas, tiendas ni loot boxes.
* ❌ **Infraestructura Cloud / Backend distribuido:** Alpha opera con persistencia local robusta.
* ❌ **Sincronización multidispositivo en la nube:** Prevista para fases posteriores.
* ❌ **Cuentas avanzadas / autenticación remota:** Registro y login básicos locales en Alpha.
* ❌ **Red social o interacciones comunitarias:** Fuera de alcance.
* ❌ **Red de ilustradores colaboradores:** Encargos artísticos reservados a Fase 4.
* ❌ **Sistema completo de rarezas dinámicas:** Curvas matemáticas complejas reservadas a Fase 4 (DEC-022-PENDING).
* ❌ **Gamificación profunda:** Progresión avanzada de `rank`, niveles y medallas reservada a Fase 4.
* ❌ **Cobertura mundial completa de especies:** Catálogo inicial de 28 especies validadas suficiente para validar el flujo.
* ❌ **IA perfecta de visión:** Evolución continua y progresiva de precisión, sin exigencias imposibles.
* ❌ **Publicación comercial en Google Play Store:** Reservada a Fase 3 (Release).
* ❌ **Sistema de monetización operativo como requisito:** Sin bloqueos publicitarios para declarar Alpha funcional.

> *Regla Arquitectónica:* La arquitectura mantiene previstos los puntos de extensión limpios, pero ninguna de estas capacidades puede condicionar o retrasar la entrega de Alpha 0.1.

---

## 12. Historial de Planificación (Changelog)

| Fecha | Cambio Registrado | Motivo | Aprobado por |
| :--- | :--- | :--- | :---: |
| **2026-09-06** | Creación del Plan Maestro (`docs/PLANNING.md`) | Establecer tablero operativo central de alineación Director/PM/Dev | Director / PM (`WHO-004`) |
| **2026-09-06** | Incorporación de decisiones de entidad animal/carta, inmutabilidad, rareza dinámica y serial | Consolidación de memoria y reglas de producto (WHO-005A) | Director / PM (`WHO-005A`) |
| **2026-09-06** | Incorporación de capacidades futuras (PVP, intercambio, artwork único/ilustradores, privacidad GPS) | Blindaje arquitectónico para esquemas de datos sin cerrar opciones (WHO-005B-A) | Director / PM (`WHO-005B-A`) |
| **2026-09-06** | Resolución A: Semántica formal y alcance de `population_at_issuance` (DEC-033) | Eliminar ambigüedad entre población zoológica y emisión de cartas en la colección | Director / PM (`WHO-005B-C`) |
| **2026-09-06** | Auditoría y resolución de `verification` y estados de autenticación (DEC-034) | Formalizar verification_status como CURRENT STATE desacoplado de la identidad | Director / PM (`WHO-005B-D`) |
| **2026-09-06** | Canonicalización de `verification_status` y semántica de `null` vs `UNVERIFIED` (DEC-035) | Establecer UNVERIFIED como estado inicial y null exclusivo para compatibilidad histórica | Director / PM (`WHO-005B-D.1`) |
| **2026-09-06** | Definición conceptual de `sex` en Animal/Capture (DEC-036) | Atribuir sexo biológico a Capture/Specimen y desacoplarlo del modelo zoológico Animal | Director / PM (`WHO-005C.1`) |
| **2026-09-06** | Auditoría semántica final de la estructura `Card` (19 campos canónicos) | Consolidación y cierre de los 19 campos y 6 módulos funcionales antes de la serialización | Director / PM (`WHO-005B-E`) |
| **2026-09-06** | Cierre semántico de `rank` y `rarity` (ortogonalidad y ejemplos no contractuales) | Delimitar estrictamente rank (mutable/progresión) y rarity (inmutable/emisión) | Director / PM (`WHO-005B-E.1`) |
| **2026-09-06** | Definición formal de tipos, obligatoriedad y nullability de los 19 campos de Card | Establecer contrato tipológico vinculante previo a la implementación de esquemas | Director / PM (`WHO-006A`) |
| **2026-09-06** | Corrección del contrato de obligatoriedad, nullability y defaults de Card (WHO-006A.1) | Eliminar defaults no aprobados, formalizar Optional vs Nullable y proteger origen histórico de display_location | Director / PM (`WHO-006A.1`) |
| **2026-09-06** | Implementación formal del modelo de dominio `Card` (19 campos canónicos) | Modelado y validación técnica según contrato WHO-006A.1 (WHO-006B) | Director / PM (`WHO-006B`) |
| **2026-09-06** | Corrección de contrato técnico del modelo Card (WHO-006B.1) | Alineación estricta UUIDv4, ausencia vs null en edition, artwork tipo abierto, rank int/str y 30 tests unitarios | Developer (`WHO-006B.1`) |
| **2026-09-06** | Formalización del dominio Capture/Specimen (WHO-006C) | Entidad Capture mínima con capture_id UUIDv4 y sex (DEC-036), frontera ontológica Animal ≠ Capture ≠ Card y 44 tests | Developer (`WHO-006C`) |
| **2026-09-06** | Formalización del Modelo de Monetización (WHO-006D) | Documentar modelo gratuito y uso de publicidad externa como infraestructura, protegiendo datos biológicos (DEC-038) | Developer (`WHO-006D`) |
| **2026-09-06** | Corrección de Capture.sex (WHO-006C.2) | Implementación de `sex` como OPTIONAL y NULLABLE sin valor default (DEC-039) y actualización de tests y documentación | Developer (`WHO-006C.2`) |
| **2026-09-06** | Creación del modelo formal AnimalProfile (WHO-007) | Implementación de `AnimalProfile` como fuente única de verdad para la especie, separado de `Capture` y `Card`. | Developer (`WHO-007`) |
| **2026-09-06** | Formalización Historia Personal (WHO-008A) | Formalización documental del Lore como Historia Personal escrita por el usuario y asociada a una carta. | Developer (`WHO-008A`) |
| **2026-09-06** | Reglas Edición Historia Personal (WHO-008B) | Formalización documental de los límites de edición del Lore: 3 ediciones por cuenta y solicitud oficial. | Developer (`WHO-008B`) |
| **2026-09-06** | Conexión y publicación inicial (WHO-010) | Configuración de infraestructura Git/GitHub y sincronización. | Developer (`WHO-010`) |
| **2026-09-06** | Auditoría y Sincronización Integral (WHO-010A) | Consolidación y validación de la documentación del proyecto, corrección de IDs duplicados. | Developer (`WHO-010A`) |
| **2026-09-06** | Infraestructura del Banco de Datos Zoológico (WHO-011A) | Creación de esquemas, JSON validador y estructura de directorios. | Developer (`WHO-011A`) |
| **2026-09-06** | Índice Taxonómico Oficial (WHO-011B) | Implementación de `TaxonomyIndex` para validar rutas biológicas sin BD. | Developer (`WHO-011B`) |
| **2026-09-06** | Catálogo Zoológico Oficial Inicial (WHO-011C) | Implementación de las primeras 28 especies JSON validadas estrictamente. | Developer (`WHO-011C`) |
| **2026-09-06** | Enriquecimiento Científico del Catálogo Zoológico (WHO-011D) | Incorporación de campos científicos (hábitat, dieta, peso, etc.). | Developer (`WHO-011D`) |
| **2026-09-07** | Implementación del Motor de Observaciones (WHO-012A) | Creación del modelo `Observation` como puente efímero. | Developer (`WHO-012A`) |
| **2026-09-07** | Resultado de Identificación Zoológica (WHO-012B) | Creación de `IdentificationResult` separando confianza y aceptación. | Developer (`WHO-012B`) |
| **2026-09-07** | Decisión sobre Identificación (WHO-012C) | Creación de `IdentificationDecision` formalizando decisión explícita. | Developer (`WHO-012C`) |
| **2026-09-07** | Puente Decisión a Captura (WHO-012D) | Formalización del puente IdentificationDecision ACCEPTED a Capture (DEC-046). | Developer (`WHO-012D`) |
| **2026-09-07** | Consolidación de Arquitectura y Mínimo Funcional (WHO-013) | Auditoría maestra de objetivos, formalización del Mínimo Funcional Android (11 pasos) y Roadmap en 5 fases (DEC-047). | Developer (`WHO-013`) |
| **2026-09-07** | Formalización de Alpha y Horizontes Estratégicos (WHO-013.1) | Formalización canónica de Alpha 0.1, Golden Path, delimitación de Storage (10x30=300), exclusiones y 4 horizontes estratégicos (DEC-048). | Developer (`WHO-013.1`) |
| **2026-09-07** | Servicio de Ensamblaje y Generación de Cartas (WHO-014) | Implementación del servicio de dominio `CardGeneratorService` (Capture → AnimalCard), protección ontológica y 15 tests específicos. | Developer (`WHO-014`) |
| **2026-09-07** | Fundación del Cliente Android (WHO-015) | Configuración de Android nativo (Kotlin, Jetpack Compose, Material 3, Navigation Compose, contratos de frontera de dominio, tests y build exitoso; DEC-049). | Developer (`WHO-015`) |
| **2026-09-07** | Pipeline de Identificación Zoológica (WHO-016) | Implementación de `IdentificationService` y `DeterministicIdentificationProvider` (Observation → IdentificationResult) en Python y Android, UI de presentación de candidatos y tests de contrato. | Developer (`WHO-016`) |
| **2026-09-07** | Persistencia Local Alpha (WHO-017) | Implementación de persistencia local offline con Room + SQLite para cartas y colección (10x30=300), validaciones de integridad, tests unitarios Robolectric y simulación de reinicio (DEC-050). | Developer (`WHO-017`) |
| **2026-09-07** | Login Alpha Local (WHO-018A) | Implementación de perfil de explorador local (ExplorerProfile), Room DAO/Entity, validación de nombre, pantallas Welcome y CreateProfile, navegación condicional Splash → Home/Welcome y tests Robolectric (DEC-051). | Developer (`WHO-018A`) |
| **2026-09-07** | Captura y Cámara Alpha con CameraX (WHO-018B) | Integración de CameraX (PreviewView, ImageCapture), permisos de cámara runtime, almacenamiento efímero de foto en cacheDir, creación de Observation real que alimenta IdentificationService sin modificar contratos, tests Robolectric y builds Debug y Release exitosos (DEC-052). | Developer (`WHO-018B`) |
| **2026-09-07** | Product Vision & Roadmap Rebaseline (WHO-DOC-001) | Rebaseline integral de documentación: creación de `PRODUCT_VISION.md`, formalización de 3 capas (Core, Game, Social), Free-to-Play por diseño, arquitectura AdService desacoplada, economía ética no-P2W, duelos PvP (10 cartas), rebaseline del Roadmap a 7 fases y sincronización transversal (DEC-053). | Developer (`WHO-DOC-001`) |
| **2026-09-07** | Visualización y Giro 3D de Carta (WHO-018C) | Implementación de `CardPresentationScreen` con renderizado de doble cara desacoplado, animación de giro tridimensional táctil en 3D, separación de Ciencia vs Lore y guardado atómico en colección (DEC-054). | Developer (`WHO-018C`) |
| **2026-09-07** | Auditoría de Integración del Golden Path y UX (WHO-018D) | Auditoría funcional end-to-end de 11 pasos del Core Loop, verificación de flujo continuo y detección de la brecha funcional de reapertura en Baúl (DEC-055). | Developer (`WHO-018D`) |
| **2026-09-07** | Grid de Colección y Reapertura de Cartas (WHO-018E) | Implementación de `CollectionScreen` con navegación por contenedores C-1 a C-10, grid de cartas de 2 columnas, reapertura inmutable sin regeneración, modo `PERSISTED_CARD` y Room v2 con `imagePath` y `personalLore` (DEC-056). | Developer (`WHO-018E`) |
| **2026-09-07** | Rebaseline Operativo de Alpha 0.1 post-Golden Path (WHO-XXX) | Auditoría documental integral, sincronización de estado real, formalización del cierre del Golden Path, registro de ADRs DEC-054 a DEC-056 y especificación formal de `WHO-019` para packaging y tag v0.1.0-alpha. | Developer (`WHO-XXX`) |
| **2026-09-07** | Alpha 0.1 Release Packaging & Tagging (WHO-019) | Configuración de versionCode = 2 y versionName = "0.1.0-alpha", verificación de assembleRelease, registro de hash SHA-256 en RELEASES.md y emisión del tag canónico v0.1.0-alpha. | Developer (`WHO-019`) |
| **2026-09-07** | Auditoría Post-Release Alpha 0.1 y Madurez Beta (WHO-020) | Auditoría integral por capas (Core, Game, Social, Platform), validación de evidencia en código, plan de cierre de deuda y definición de la hoja de ruta Beta WHO-021 a WHO-026 (DEC-057). | Developer (`WHO-020`) |
| **2026-09-07** | Sincronización de Catálogo de Especies y Unificación de Dominio Android (WHO-021) | Unificación de la fuente única de verdad zoológica (data/species/ a Android assets), erradicación de listas hardcodeadas en Kotlin, repositorio y parser dedicados, compatibilidad con 28 especies y tests unitarios 100% PASS (DEC-058). | Developer (`WHO-021`) |
| **2026-09-07** | Externalización de Textos de UI y Fundación i18n (WHO-022) | Extracción de strings de interfaz en Android Resources con paridad español/inglés, preservación ontológica UI ≠ Ciencia ≠ Lore y tests de recursos (DEC-059). | Developer (`WHO-022`) |
| **2026-09-07** | Edición Interactiva de Lore y Liberación de Cartas desde el Baúl (WHO-023) | Lore personal editable (máx 300 caracteres), cuota atómica de 3 ediciones por cuenta persistida en Profile (DEC-041), liberación segura con modal y recuperación íntegra de slots en Room (DEC-060). | Developer (`WHO-023`) |

---

## 13. Reglas Fundamentales de Gobernanza

> 📌 **`docs/PLANNING.md` es el tablero operativo de alineación del proyecto.**

* **El Director Creativo** define qué se quiere construir, custodia la visión y aprueba los objetivos.
* **El Project Manager** estructura, prioriza, analiza dependencias y formula los objetivos.
* **El Developer** implementa exclusivamente los objetivos autorizados y reporta resultados.
* **Ningún objetivo futuro debe comenzar automáticamente porque el anterior haya terminado.**

---

## 14. Relación Objetivo ↔ Versión

* Un objetivo técnico individual (`WHO-xxx`) y una versión de producto **NO son necesariamente equivalentes**.
* Múltiples objetivos completados pueden coexistir dentro de una misma versión (`0.0.1`).
* La versión solo se incrementará cuando el conjunto de cambios configure formalmente una nueva iteración o hito según las reglas de [docs/VERSIONING.md](VERSIONING.md).

---

## 15. Reglas de Mantenimiento de Este Documento

1. `docs/PLANNING.md` debe mantenerse rigurosamente actualizado al completar o cambiar el estado de cualquier objetivo.
2. El estado reflejado debe coincidir con el estado real y verificable del repositorio.
3. Jamás marcar un objetivo como `APROBADO` sin la autorización explícita del Director.
4. No marcar una versión como publicada sin la existencia y comprobación del artefacto.
5. No inventar builds de APK/AAB ficticias.
6. No inventar decisiones de producto no tomadas.
7. No eliminar el historial ni el registro de cambios de planificación.
8. Si surge una contradicción entre documentos, detener el desarrollo y reportar el conflicto.
9. La fuente oficial de decisiones es [docs/DECISIONS.md](DECISIONS.md).
10. La fuente oficial de reglas de versión es [docs/VERSIONING.md](VERSIONING.md).
11. La fuente oficial del roadmap estratégico es [docs/ROADMAP.md](ROADMAP.md).
12. El procedimiento operativo de ejecución es [docs/WORKFLOW.md](WORKFLOW.md).
