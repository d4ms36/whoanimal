# Hoja de Ruta del Proyecto (Roadmap) — WHO Animal

**Documento:** `docs/ROADMAP.md`  
**Propósito:** Planificación estratégica y técnica oficial por fases y objetivos atómicos (DEC-047 / DEC-048).  
**Principio Rector:** Separación estricta entre el producto funcional Alpha y las capacidades estratégicas futuras para prevenir el desvío de alcance (*scope creep*).

---

## Estructura de Horizontes y Fases del Proyecto

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        WHO ANIMAL ROADMAP                              │
├───────────────────┬───────────────────┬──────────────────┬─────────────┤
│      PHASE 0      │      PHASE 1      │     PHASE 2      │   PHASE 3   │
│    FOUNDATION     │ALPHA FUNCIONAL 0.1│       BETA       │   RELEASE   │
│   (COMPLETADA)    │ (PRÓXIMO PRODUCTO)│(SIGUIENTE HORIZ.)│ (PUBLICACIÓN│
│                   │                   │                  │   PÚBLICA)  │
├───────────────────┼───────────────────┼──────────────────┼─────────────┤
│ Core de dominio,  │ Primer producto   │ Pruebas reales,  │ Lanzamiento │
│ arquitectura,     │ Android funcional:│ calibración IA,  │ comercial en│
│ catálogo inicial, │ Login → Capture → │ fluidez, testing │ Google Play │
│ decisiones y      │ Card → Storage    │ y legalidad.     │ Store.      │
│ gobernanza.       │ (10x30=300 cartas)│                  │             │
└───────────────────┴───────────────────┴──────────────────┴─────────────┘
                                  │
                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                   PHASE 4: ECOSYSTEM (LARGO PLAZO)                     │
│                        (CAPACIDADES AVANZADAS)                         │
├────────────────────────────────────────────────────────────────────────┤
│ Comercio, PVP, Cloud, Cuentas avanzadas, Marketplace, Ilustradores,    │
│ Rarezas dinámicas, Sincronización nube y Gamificación profunda.        │
└────────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             FUTURO ABIERTO                             │
│                         (SANDBOX CONCEPTUAL)                           │
├────────────────────────────────────────────────────────────────────────┤
│ Ideas en gestación sin alcance, versión, prioridad ni criterios fijados│
└────────────────────────────────────────────────────────────────────────┘
```

### Línea Temporal Única Oficial:
```text
FOUNDATION = COMPLETADA
ALPHA = PRÓXIMO PRODUCTO FUNCIONAL (WHO-014 → WHO-018)
BETA = SIGUIENTE HORIZONTE
RELEASE = LANZAMIENTO PÚBLICO
ECOSYSTEM = CAPACIDADES AVANZADAS
```

---

## 1. PHASE 0 — FOUNDATION (Completada)

**Objetivo Central:** Construir, blindar y validar el núcleo conceptual, arquitectónico, taxonómico y lógico de WHO Animal.

### Objetivos Atómicos de la Fase Foundation
* **`WHO-001` — Evaluación de Requisitos y Estado Cero** `[COMPLETADO]`
  * Inspección del espacio de trabajo, verificación de no-código y alineación inicial.
* **`WHO-002` — Arquitectura Base y Especificación de Cartas** `[COMPLETADO]`
  * Creación de modelos de dominio inmutables en Python (`ScientificInfo`, `AnimalProfile`, `LoreProfile`, `AnimalCard`), Clean Architecture y primera suite de pruebas.
* **`WHO-003` — Gobernanza, Memoria del Proyecto y Versionado** `[COMPLETADO]`
  * Creación de `AGENTS.md`, `PROJECT_CONTEXT.md`, gobernanza de 3 roles, esquema de versionado y registro formal de decisiones ADR.
* **`WHO-004` — Plan Maestro de Desarrollo y Alineación por Versión** `[COMPLETADO]`
  * Creación del tablero operativo central (`docs/PLANNING.md`) para sincronización entre Director, PM y Developer.
* **`WHO-005A` — Incorporación de Nuevas Decisiones al Contexto** `[COMPLETADO]`
  * Consolidación en memoria de la frontera Animal vs. Carta, inmutabilidad, rareza dinámica y autenticación.
* **`WHO-005B-A` — Actualización Documental de Capacidades Futuras** `[COMPLETADO]`
  * Blindaje arquitectónico documental de extensiones: PVP, comercio, ilustradores y privacidad de ubicación.
* **`WHO-005B-C` — Semántica de population_at_issuance** `[COMPLETADO]`
  * Definición formal, inmutabilidad y desacoplamiento estricto de censos biológicos reales (DEC-033).
* **`WHO-005B-D` — Auditoría y Resolución de verification** `[COMPLETADO]`
  * Formalización canónica de `verification_status` como estado actual mutable desacoplado del serial (DEC-034).
* **`WHO-005B-D.1` — Canonicalización de verification_status y null vs UNVERIFIED** `[COMPLETADO]`
  * Estado inicial obligatorio `UNVERIFIED` y reserva de `null` para esquemas históricos (DEC-035).
* **`WHO-005C.1` — Definición Conceptual de sex en Animal/Capture** `[COMPLETADO]`
  * Delimitación ontológica del sexo biológico en Capture/Specimen y exclusión del modelo Animal (DEC-036).
* **`WHO-005B-E` — Auditoría Semántica Final de la Estructura Card** `[COMPLETADO]`
  * Auditoría y cierre conceptual de los 19 campos canónicos de `Card` distribuidos en 6 módulos funcionales.
* **`WHO-005B-E.1` — Cierre Semántico de rank y rarity** `[COMPLETADO]`
  * Ortogonalidad absoluta entre `rank` (mutable/progresión) y `rarity` (inmutable/emisión) (DEC-032).
* **`WHO-006A` — Definición Formal de Tipos y Obligatoriedad de Card** `[COMPLETADO]`
  * Formalización contractual de tipos técnicos, requiredness, nullability e inmutabilidad previa a código.
* **`WHO-006A.1` — Corrección del Contrato de Obligatoriedad y Defaults de Card** `[COMPLETADO]`
  * Eliminación de defaults no aprobados, formalización de Optional vs. Nullable y protección anti-GPS.
* **`WHO-006B` — Implementación Formal del Modelo Card** `[COMPLETADO]`
  * Implementación del modelo de dominio `Card` con los 19 campos canónicos y validación estricta.
* **`WHO-006B.1` — Corrección de Contrato Técnico del Modelo Card** `[COMPLETADO]`
  * Alineación estricta: UUIDv4 canónico, ausencia vs. null en `edition`, `artwork` como tipo abierto, `rank` int/str y 30 tests unitarios.
* **`WHO-006C` — Formalizar Dominio Capture/Specimen** `[COMPLETADO]`
  * Entidad `Capture` con `capture_id` UUIDv4 y `sex ∈ {MALE, FEMALE, UNKNOWN}` (DEC-036).
* **`WHO-006C.2` — Cierre Técnico de Capture/Specimen y sex** `[COMPLETADO]`
  * Corrección para establecer `Capture.sex` como `OPTIONAL`, `NULLABLE` y sin valor default (DEC-039).
* **`WHO-006D` — Formalizar Modelo de Monetización Gratuito + Publicidad** `[COMPLETADO]`
  * Producto 100% gratuito, desacoplamiento arquitectónico de AdMob y prioridad de Rewarded Ads sin economía in-app (DEC-038).
* **`WHO-007` — Banco de Datos Inicial de Fauna (Semilla Educativa)** `[COMPLETADO]`
  * Implementación del modelo formal `AnimalProfile` como fuente única de verdad zoológica.
* **`WHO-008A` — Formalizar Historia Personal de la Carta (Personal Lore)** `[COMPLETADO]`
  * Definición del Lore como Historia Personal del usuario (hasta 300 caracteres) asociada a una carta.
* **`WHO-008B` — Definir reglas de edición de la Historia Personal (Lore)** `[COMPLETADO]`
  * Límite global de 3 ediciones por cuenta y procedimiento oficial para cambios excepcionales (DEC-041).
* **`WHO-010` — Conexión y publicación inicial del repositorio** `[COMPLETADO]`
  * Configuración de infraestructura Git/GitHub y sincronización remota.
* **`WHO-010A` — Auditoría y Sincronización Integral de Documentación** `[COMPLETADO]`
  * Revisión exhaustiva y consolidación de directrices técnicas de gobernanza y planificación.
* **`WHO-011A` — Infraestructura del Banco de Datos Zoológico** `[COMPLETADO]`
  * Esquemas y validador de datasets estáticos en JSON dentro de `data/species/`.
* **`WHO-011B` — Implementación del Índice Taxonómico Oficial** `[COMPLETADO]`
  * Servicio `TaxonomyIndex` para validación y búsqueda de jerarquías taxonómicas en memoria sin base de datos.
* **`WHO-011C` — Implementación del Catálogo Zoológico Oficial Inicial** `[COMPLETADO]`
  * Catálogo oficial de 28 especies zoológicas reales validadas estrictamente.
* **`WHO-011D` — Enriquecimiento Científico del Catálogo Zoológico** `[COMPLETADO]`
  * Enriquecimiento con datos de hábitat, dieta, peso, esperanza de vida, tamaño y ciclo de actividad (DEC-042).
* **`WHO-012A` — Implementación del Motor de Observaciones** `[COMPLETADO]`
  * Modelo `Observation` como puente efímero entre captura de imagen y registro (DEC-043).
* **`WHO-012B` — Implementación del Resultado de Identificación Zoológica** `[COMPLETADO]`
  * Modelo `IdentificationResult` desacoplando formalmente confianza cuantitativa de aceptación (DEC-044).
* **`WHO-012C` — Formalizar la decisión explícita sobre un IdentificationResult** `[COMPLETADO]`
  * Modelo `IdentificationDecision` (`ACCEPTED`, `REJECTED`, `CANCELLED`) con validación estricta de candidatos (DEC-045).
* **`WHO-012D` — Implementación del puente IdentificationDecision → Capture** `[COMPLETADO]`
  * Formalización del puente de dominio: solo decisiones `ACCEPTED` generan `Capture` (`animal_id`, `identification_id`, `sex` inmutables) (DEC-046).
* **`WHO-013` — Consolidación de Arquitectura, Roadmap y Definición del Mínimo Funcional** `[COMPLETADO]`
  * Auditoría maestra de objetivos, resolución de colisiones históricas, formalización del Mínimo Funcional Android y reestructuración por fases (DEC-047).
* **`WHO-013.1` — Formalización de Alpha y Horizontes Estratégicos** `[COMPLETADO]`
  * Formalización canónica de Alpha 0.1, Golden Path, delimitación de Storage (10x30=300), exclusiones explícitas y definición de los 4 horizontes estratégicos (DEC-048).

---

## 2. CORTO PLAZO — PHASE 1: ALPHA FUNCIONAL 0.1 (Próximo Producto Funcional)

**Objetivo Rector de Alpha 0.1:**
> *"Un usuario nuevo debe poder entrar a WHO Animal, fotografiar un animal, obtener una identificación, generar una carta, revisarla, guardarla en su colección, cerrar la aplicación, volver a abrirla y encontrar la carta nuevamente."*

### Golden Path Oficial:
```text
LOGIN → HOME → CAPTURE → CAMERA → OBSERVATION → IDENTIFICATION → RESULT → DECISION → CAPTURE → CARD → REVIEW / EDIT → SAVE → STORAGE
```

### Flujo Alternativo (Gestión y Consulta):
```text
HOME → STORAGE → CONTAINER → CARD → VIEW / EDIT / DELETE
```

### Alcance Oficial de Alpha:
* **Cuenta:** Registro, Login, Logout locales básicos.
* **Home:** Menú simple con accesos destacados a Capture y Storage.
* **Capture:** Permisos de cámara, toma de fotografía, `Observation`, identificación, `IdentificationResult`, decisión explícita (aceptar/descartar) y manejo de errores.
* **Card:** Generación automática tras decisión válida, vista frontal, volteo (*flip*), vista posterior con datos zoológicos y captura, edición restringida de historia personal (Lore), campos de dominio inmutables, descartar/guardar.
* **Location:** Ubicación generalizada (`display_location`), sin dirección residencial exacta, desacoplada de telemetría interna (`precise_location`).
* **Storage:** Persistencia local robusta en 10 containers x 30 espacios (capacidad 300 cartas), selector de container, ocupación, visualización, apertura, eliminación confirmada y persistencia tras reinicio.
* **UX Mínima:** Splash, estados de carga, *empty state*, confirmación de borrado, animación de flip, navegación Back/Home, mensajes claros y coherencia visual.

### Fuera del Alcance de Alpha (Exclusiones Explícitas):
Trading, PVP, Marketplace, economía completa, Cloud/backend, sincronización multidispositivo, cuentas avanzadas, red social, red de ilustradores, rarezas dinámicas con curvas matemáticas, gamificación profunda, cobertura mundial completa de especies, IA perfecta, publicación en Google Play y monetización operativa.

### Secuencia Inicial Propuesta (Construcción de Alpha):
* **`WHO-014` — Servicio de Ensamblaje y Generación de Cartas (`Capture → Card`)** `[COMPLETADO]`
  * Implementación del servicio de dominio `CardGeneratorService` que emite una `Card` formal a partir de una `Capture` validada.
* **`WHO-015` — Fundación del Cliente Android y Decisión Tecnológica Móvil** `[COMPLETADO]`
  * Configuración del proyecto base Android y formalización de la tecnología de interfaz de usuario.
* **`WHO-016` — Servicio de Identificación de Especies (`Observation → IdentificationResult`)** `[COMPLETADO]`
  * Implementación del servicio `IdentificationService` mediante motor local ligero/on-device.
* **`WHO-017` — Motor de Persistencia Local y Colección (`Collection Album`)** `[PROPUESTO]`
  * Almacenamiento local para inventario, capturas y visualización de cartas en el álbum del usuario (10 containers x 30 espacios).
* **`WHO-018` — Integración del Mínimo Funcional Android (End-to-End Core Loop)** `[PROPUESTO]`
  * Cierre integral del flujo de 11 pasos ejecutable en dispositivo o emulador Android.

---

## 3. MEDIANO PLAZO — PHASE 2: BETA & PHASE 3: RELEASE (Siguiente Horizonte)

Documentado a nivel estratégico para guiar la evolución tras consolidar la Alpha:

### Phase 2 — Beta
* Pruebas con usuarios reales y feedback cualitativo.
* Mejora y pulido integral de UX y micro-interacciones.
* Estabilidad del sistema y optimización de rendimiento en múltiples gamas.
* Calibración y afinamiento del motor de identificación de fauna.
* Expansión inicial controlada del catálogo zoológico.
* Testing exhaustivo en Android.
* Preparación para distribución controlada (**Google Play Internal Testing**).
* Auditoría formal de términos de servicio y aviso legal (`DISCLAIMER.md`).

### Phase 3 — Release
* Lanzamiento público comercial en Google Play Store.
* Álbum de colección completo con logros éticos de descubrimiento.
* Estabilización de infraestructura y soporte continuo del catálogo de especies.

> *Nota de Gobernanza:* Los puntos de Beta y Release representan dirección táctica y **no se convierten en objetivos atómicos ejecutables** hasta que Alpha esté cerrada y aprobada.

---

## 4. LARGO PLAZO — PHASE 4: ECOSYSTEM (Capacidades Estratégicas Avanzadas)

Las 13 capacidades estratégicas que representan la visión integral del ecosistema. La arquitectura base prepara sus puntos de extensión, pero **ninguna bloquea ni condiciona Alpha ni Beta**:

1. **Comercio de cartas (Trading):** Intercambio seguro entre usuarios preservando inmutabilidad histórica.
2. **Sistema de Duelos PVP:** Enfrentamientos lúdicos desacoplados de atributos zoológicos.
3. **Cuentas avanzadas y perfiles:** Autenticación remota y respaldo de usuario.
4. **Infraestructura Cloud:** Backend escalable y APIs seguras.
5. **Economía del ecosistema:** Progresión balanceada y recompensas in-app.
6. **Marketplace:** Mercado in-app para adquisición controlada de cartas.
7. **Sistema completo de rarezas:** Algoritmo dinámico y curvas de probabilidad basadas en emisión (`population_at_issuance`, DEC-022-PENDING).
8. **Red de Ilustradores:** Encargos y soporte de arte personalizado para cartas.
9. **Sincronización multidispositivo:** Persistencia en la nube cruzada.
10. **Publicación pública global:** Expansión territorial multirregión.
11. **Expansión progresiva del catálogo zoológico:** Crecimiento continuo hacia cobertura global sin promesas engañosas de "todas las especies".
12. **Evolución continua del sistema de IA de identificación:** Mejora incremental de precisión sin prometer "IA perfecta".
13. **Gamificación completa:** Sistema de progresión, medallas por biomas y mecánicas avanzadas de `rank` (DEC-037-PENDING).

---

## 5. FUTURO ABIERTO (Sandbox Conceptual)

Espacio de reserva para ideas y conceptos en gestación que todavía **no tienen**:
* Alcance definido.
* Prioridad asignada.
* Dependencias técnicas resueltas.
* Versión objetivo formalizada.
* Criterios de aceptación estructurados.

**Regla de Gobernanza:** Ninguna idea contenida en esta sección se convertirá automáticamente en un objetivo ejecutable `WHO` sin la aprobación previa del Director y la formulación técnica del PM.

Iniciativas en exploración conceptual:
* Desafíos comunitarios de bioacústica (identificación por cantos o sonidos animales).
* Fichas de hábitats y biomas como coleccionables complementarios.
* Integración con guías de reservas naturales y parques protegidos.
* Eventos estacionales y seguimiento de rutas migratorias reales.
* Realidad Aumentada (AR) para observación e inspección de especímenes.
