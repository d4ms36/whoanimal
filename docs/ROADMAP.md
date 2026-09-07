# Hoja de Ruta Oficial (Roadmap) — WHO Animal

**Documento:** `docs/ROADMAP.md`  
**Versión:** 1.0 (Rebaseline Oficial — WHO-DOC-001 / DEC-053)  
**Propósito:** Planificación estratégica y técnica oficial del proyecto estructurada en 7 fases consecutivas para asegurar el crecimiento ordenado del producto sin comprometer su núcleo ético ni su rigor pedagógico.

---

## Estructura Estratégica del Roadmap (7 Fases)

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        WHO ANIMAL ROADMAP                              │
├────────────────────────────────────────────────────────────────────────┤
│  1. FOUNDATION (Completada)                                            │
│     Core de dominio, arquitectura, catálogo zoológico, decisiones.     │
├────────────────────────────────────────────────────────────────────────┤
│  2. ALPHA (Alpha Funcional 0.1 — En Curso)                             │
│     Core Loop end-to-end: Login → Capture → Identify → Card → Storage  │
├────────────────────────────────────────────────────────────────────────┤
│  3. BETA (Estabilidad y Pulido)                                        │
│     Pruebas en dispositivos reales, UX, a11y, i18n, pre-AdService.    │
├────────────────────────────────────────────────────────────────────────┤
│  4. RELEASE 1.0 (Lanzamiento Comercial Público)                        │
│     Core Loop + Baúl + Enciclopedia + Free Currency + Shop + Ads       │
├────────────────────────────────────────────────────────────────────────┤
│  5. RELEASE 1.x — GAME                                                 │
│     Duelos PvP, equipos de 10 cartas, temporadas, eventos, logros.     │
├────────────────────────────────────────────────────────────────────────┤
│  6. RELEASE 2.x — SOCIAL                                               │
│     Amigos, regalos, intercambio (Trading), perfiles públicos, UGC.    │
├────────────────────────────────────────────────────────────────────────┤
│  7. RELEASE 3.x — WORLD                                                │
│     Biomas globales, países, enciclopedia ampliada, cloud multi-device.│
└────────────────────────────────────────────────────────────────────────┘
```

---

## 1. PHASE 1 — FOUNDATION (Completada)

**Objetivo:** Construir, blindar y validar el núcleo conceptual, arquitectónico, taxonómico y lógico de WHO Animal.

### Resumen de Objetivos Atómicos Completados en Foundation
* **`WHO-001` a `WHO-004`:** Estado cero, arquitectura base, manual `AGENTS.md`, gobernanza y tablero `PLANNING.md`.
* **`WHO-005A` a `WHO-006D`:** Formalización de la inmutabilidad de cartas, ancla poblacional (`population_at_issuance`, DEC-033), estado de verificación (`verification_status`, DEC-034/035), sexo biológico a nivel captura (`Capture.sex`, DEC-036/039), modelo de monetización ético (DEC-038), ortogonalidad `rank` vs. `rarity` (DEC-032), contrato e implementación de los 19 campos canónicos de `Card` (WHO-006B.1).
* **`WHO-007` a `WHO-008B`:** Modelo formal `AnimalProfile`, definición del Lore como Historia Personal (DEC-040) y reglas de edición limitada (DEC-041).
* **`WHO-010` a `WHO-011D`:** Repositorio remoto, esquemas JSON de especies, servicio `TaxonomyIndex` y catálogo oficial de 28 especies validadas con enriquecimiento científico (DEC-042).
* **`WHO-012A` a `WHO-012D`:** Motor de observaciones efímeras (`Observation`, DEC-043), resultado de identificación (`IdentificationResult`, DEC-044), decisión explícita (`IdentificationDecision`, DEC-045) y puente formal `IdentificationDecision(ACCEPTED) → Capture` (DEC-046).
* **`WHO-013` a `WHO-013.1`:** Consolidación de arquitectura, definición del Mínimo Funcional (DEC-047) y formalización de Alpha 0.1 y horizontes estratégicos (DEC-048).

---

## 2. PHASE 2 — ALPHA (Alpha Funcional 0.1 — En Curso)

**Objetivo Rector de Alpha 0.1:**
> *"Un usuario nuevo debe poder entrar a WHO Animal, fotografiar un animal, obtener una identificación, generar una carta, revisarla, guardarla en su colección, cerrar la aplicación, volver a abrirla y encontrar la carta nuevamente."*

### Golden Path Oficial de Alpha (Completado):
```text
LOGIN → HOME → CAPTURE → CAMERA → OBSERVATION → IDENTIFICATION → RESULT → DECISION → CAPTURE → CARD → REVIEW / FLIP → SAVE → STORAGE → REOPEN
```

### Alcance Oficial de Alpha:
* **Cuenta:** Perfil de explorador local y sesión offline (Room).
* **Home:** Menú principal con accesos destacados a Capture y Storage.
* **Capture:** Permisos en runtime, cámara en vivo con CameraX, retícula de fauna, almacenamiento efímero en `cacheDir`, `Observation`, identificación determinista, `IdentificationResult`, decisión explícita y manejo de errores.
* **Card:** Generación automática tras decisión `ACCEPTED`, vista frontal, animación interactiva de volteo 3D (*flip*), vista posterior con datos zoológicos contrastados y Lore personal, inmutabilidad de campos canónicos y guardado/descarte.
* **Location:** Ubicación generalizada (`display_location`), sin dirección residencial exacta, protegiendo a la fauna silvestre.
* **Storage (Baúl):** Persistencia local robusta en 10 containers x 30 espacios (capacidad de 300 cartas de la fase Alpha/Foundation), selector de contenedor, visualización en grid, reapertura inmutable de la carta persistida y supervivencia garantizada tras reinicio de la app.
* **UX Mínima:** Splash, estados de carga pedagógicos, *empty state*, diálogo de confirmación de borrado, navegación Back/Home y micro-interacciones pulidas.

### Secuencia Atómica de Construcción de Alpha:
* **`WHO-014` — Servicio de Ensamblaje y Generación de Cartas (`Capture → Card`)** `[COMPLETADO]`
* **`WHO-015` — Fundación del Cliente Android y Decisión Tecnológica Móvil** `[COMPLETADO]`
* **`WHO-016` — Servicio de Identificación de Especies (`Observation → IdentificationResult`)** `[COMPLETADO]`
* **`WHO-017` — Motor de Persistencia Local y Colección (`Collection Album`)** `[COMPLETADO]`
* **`WHO-018A` — Login Alpha Local (Perfil y Sesión Offline)** `[COMPLETADO]`
* **`WHO-018B` — Captura y Cámara Alpha (CameraX / Visual Input)** `[COMPLETADO]`
* **`WHO-018C` — Visualización y Giro 3D de Carta (Card View & Flip)** `[COMPLETADO]`
  * Pantalla de visualización interactiva de carta con ambas caras, animación de giro tridimensional táctil, revisión de atributos zoológicos y guardado en la colección persistente (DEC-054).
* **`WHO-018D` — Auditoría de Integración del Golden Path y UX** `[COMPLETADO]`
  * Verificación integral del flujo de 11 pasos, detección y documentación de la brecha funcional de reapertura (DEC-055).
* **`WHO-018E` — Grid de Colección y Reapertura de Cartas Persistidas (Collection Grid & Reopening)** `[COMPLETADO]`
  * Grid de 2 columnas en Baúl, selector de contenedores C-1 a C-10, reapertura inmutable sin regeneración de carta, modo `PERSISTED_CARD` y Room versión 2 con `imagePath` y `personalLore` (DEC-056).
* **`WHO-018` — Integración del Mínimo Funcional Android (End-to-End Core Loop)** `[COMPLETADO]`
  * Consolidado y completado exitosamente a través de la secuencia atómica `WHO-018A` a `WHO-018E`.
* **`WHO-019` — Alpha 0.1 Release Packaging & Tagging (`v0.1.0-alpha`)** `[COMPLETADO]`
  * Verificación formal de empaquetado de Release Android (`assembleRelease`), sincronización de `versionCode = 2` y `versionName = "0.1.0-alpha"`, registro de artefacto con hash SHA-256 en `docs/RELEASES.md` y creación del tag canónico `v0.1.0-alpha`.

---

## 3. PHASE 3 — BETA (Estabilidad y Pulido Integral)

**Enfoque:** Preparar el producto para el uso intensivo en el mundo real antes de su distribución comercial masiva.

* **Experiencia de Usuario (UX):** Pulido fino de animaciones, fluidez de interfaz, micro-interacciones y transiciones naturales.
* **Estabilidad y Rendimiento:** Detección de fugas de memoria, optimización del renderizado de cartas y pruebas de rendimiento en terminales Android de gama de entrada, media y alta.
* **Compatibilidad de Dispositivos:** Pruebas exhaustivas de CameraX en múltiples fabricantes, formatos de pantalla y sensores de cámara.
* **Accesibilidad (a11y):** Soporte para lectores de pantalla (TalkBack), contraste tipográfico y tamaños dinámicos de texto.
* **Internacionalización Básica (i18n):** Extracción de cadenas de interfaz a recursos localizados (`strings.xml`), asegurando la regla $\text{UI} \neq \text{Ciencia} \neq \text{Lore}$.
* **Privacidad y Cumplimiento:** Auditoría legal de políticas de privacidad, términos de servicio y aviso legal ([DISCLAIMER.md](DISCLAIMER.md)).
* **Telemetría y Analytics Mínimos:** Registro no invasivo de estabilidad y caídas (*crash analytics*) sin recolectar datos personales sensibles.
* **Preparación de Infraestructura Publicitaria:** Implementación del contrato desacoplado `AdService` con stubs y pruebas de políticas de frecuencia.
* **Distribución Controlada:** Despliegue de builds internas vía **Google Play Internal Testing** para recolección de feedback cualitativo.

### Secuencia Atómica de Construcción de Beta:
* **`WHO-020` — Auditoría Post-Release Alpha 0.1 y Madurez Beta (Beta Readiness)** `[COMPLETADO]`
  * Auditoría integral por capas (Core, Game, Social, Platform), validación de evidencia en código, plan de cierre de deuda y definición de la hoja de ruta Beta (DEC-057).
* **`WHO-021` — Ingesta y Sincronización del Catálogo JSON como Android Assets** `[COMPLETADO]`
  * Unificación de la fuente única de verdad biológica (`data/species/`), eliminando datos duplicados en código Kotlin y sincronizando los 28 especímenes.
* **`WHO-022` — Extracción y Externalización de Cadenas de UI (i18n Foundation)** `[COMPLETADO]`
  * Migración sistemática de cadenas hardcodeadas de Compose a `res/values/strings.xml`, asegurando la separación $\text{UI} \neq \text{Ciencia} \neq \text{Lore}$ y proveyendo fundación inicial en inglés (`values-en/strings.xml`).
* **`WHO-023` — Edición Interactiva de Lore (DEC-041) y Liberación de Cartas en Baúl** `[COMPLETADO]`
  * Cierre de deuda de UX: UI de edición de Historia Personal con límite de 3 ediciones por cuenta persistido en Profile (DEC-041/DEC-060) y acción segura de liberar carta desde Baúl con diálogo modal de confirmación y recuperación íntegra de slots en Room.
* **`WHO-024` — Accesibilidad (a11y) y Robustecimiento de Compatibilidad CameraX** `[COMPLETADO]`
  * Soporte TalkBack, roles y semantics explícitos, target táctiles mínimos de 48dp, anuncios en liveRegion, unbind limpio en lifecycle de CameraX, fallback con sensor check y listener dinámico de rotación (DEC-061).
* **`WHO-025` — Contrato Desacoplado `AdService` y Stubs de Infraestructura (DEC-053)** `[COMPLETADO]`
  * Definición de interfaces limpias (`BannerPlacement`, `InterstitialPolicy`, `RewardedAdService`, `FrequencyPolicy`) y stubs locales sin SDKs de terceros.
* **`WHO-026` — Pipeline de Firma de Release y Preparación para Google Play Internal Testing** `[COMPLETADO]`
  * Configuración de keystore de release, optimización R8/ProGuard y preparación del paquete de distribución interna `v0.2.0-beta.1` (versionCode 3).
* **`WHO-027` — Beta Validation Protocol & Field Testing** `[COMPLETADO]`
  * Creación del protocolo de pruebas, matrices de compatibilidad (Android 8-15) y Smoke Tests para la validación física en dispositivos reales.

---

## 4. PHASE 4 — RELEASE 1.0 (Lanzamiento Comercial Público)

**Enfoque:** Lanzamiento oficial de WHO Animal en Google Play Store con el Core Loop maduro y la base del modelo Free-to-Play.

* **Núcleo Completo:** Flujo de registro, captura en vivo, identificación zoológica, acuñación de cartas, lectura de enciclopedia y gestión completa del Baúl/Corral.
* **Enciclopedia y Catálogo Zoológico:** Catálogo enriquecido y accesible para consulta de fauna descubierta.
* **Personalización y Temas Básicos:** Marcos visuales de cartas y temas de colección seleccionables.
* **Economía F2P y Moneda Gratuita:** Recompensas por descubrimientos y nuevas especies avistadas.
* **Tienda Cosmética Básica (Shop):** Adquisición de temas visuales, avatares y estilos de contenedor mediante moneda gratuita del juego.
* **Publicidad Desacoplada (`AdService`):** Integración operativa de AdMob (Banners no intrusivos en pantallas secundarias e Interstitials controlados por frecuencia; Rewarded Ads voluntarios para beneficios lúdicos).
* **Gestión de Perfil:** Estadísticas personales de avistamiento y medallas de explorador.
* **Regla de Lanzamiento:** *El sistema de combate PvP no bloquea el Release 1.0; la prioridad absoluta es la estabilidad y la magia del Core Loop.*

---

## 5. PHASE 5 — RELEASE 1.x: GAME (Juego Ligero y Duelos)

**Enfoque:** Desplegar la Capa Game para dotar de profundidad lúdica y rejugabilidad competitiva sana al coleccionismo de cartas.

* **Sistema de Duelos PvP:** Combates por turnos sencillos, ágiles y estratégicos inspirados en mecánicas RPG livianas.
* **Equipo de Exploración (Mazo de 10 Cartas):** Cada explorador selecciona y entrena un mazo de 10 cartas.
* **Ventana Semanal de Configuración:** El mazo activo solo puede modificarse dentro de un período semanal determinado, premiando la planificación sobre el cambio compulsivo.
* **Estadísticas de Juego Ficticias:** Atributos numéricos de combate (HP, ATK, DEF, SPD, TYPE, SPECIAL) puramente lúdicos y desacoplados de la biología real.
* **Temporadas y Eventos Especiales:** Temporadas competitivas de exploración, tablas de clasificación (*Rankings*) éticas y eventos estacionales.
* **Misiones y Desafíos de Avistamiento:** Retos temáticos por biomas, familias y estaciones climáticas.
* **Ampliación de Tienda:** Nuevos cosméticos avanzados, animaciones de cartas y efectos holográficos.

---

## 6. PHASE 6 — RELEASE 2.x: SOCIAL (Comunidad y Conexión)

**Enfoque:** Conectar a la comunidad global de naturalistas mediante dinámicas sociales constructivas y respetuosas.

* **Perfiles Públicos de Explorador:** Vitrinas virtuales donde compartir colecciones y avistamientos destacados.
* **Amigos y Regalos:** Sistema de contactos para enviar paquetes diarios de exploración.
* **Intercambio Seguro de Cartas (Trading):** Transferencia formal de propiedad entre coleccionistas manteniendo estrictamente congelada la identidad histórica original (`card_id`, espécimen, generación, rareza y serial de emisión).
* **Contenido Generado por Usuarios (UGC):** Historias Personales (*Lore*) compartidas públicamente en la comunidad.
* **Seguridad, Moderación y Protección de Menores:** Herramientas completas de reporte de abusos, bloqueo de usuarios, filtros de lenguaje y cumplimiento de normativas de privacidad infantil.

---

## 7. PHASE 7 — RELEASE 3.x: WORLD (Ecosistema Global y Nube)

**Enfoque:** Expansión del producto a escala planetaria con soporte multi-dispositivo y experiencias inmersivas.

* **Expansión Global del Catálogo Zoológico:** Crecimiento continuo de la base zoológica cubriendo biomas mundiales, fauna endémica y especies protegidas globales.
* **Sincronización Multidispositivo en la Nube:** Respaldo y sincronización en tiempo real de cuentas, colecciones y mazos en cualquier dispositivo.
* **Eventos Biogeográficos Mundiales:** Dinámicas sincronizadas con eventos naturales reales (migraciones globales, solsticios, semanas de la biodiversidad).
* **Experiencias Comunitarias:** Retos colaborativos globales para registrar y proteger la biodiversidad.
* **Futuro Abierto:** Espacio para exploración de Realidad Aumentada (AR), bioacústica animal y alianzas con reservas naturales.
