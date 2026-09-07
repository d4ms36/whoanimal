# WHO Animal 🐾

> *"Descubre. Identifica. Colecciona."*
> 
> $$\text{WHO Animal} = \text{Identificación} + \text{Cartas Coleccionables} + \text{Enciclopedia Personal} + \text{Juego Ligero} + \text{Capa Social}$$

**WHO Animal** es una experiencia interactiva de descubrimiento, aprendizaje y colección de fauna silvestre mediante un sistema visual de **cartas coleccionables de doble cara**, complementadas con información biológica rigurosa y una Historia Personal escrita por el usuario (*Lore*). Todo bajo un principio transversal e innegociable: **100% Pet Friendly** y un modelo **Free-to-Play por diseño**.

---

## 📌 Estado Actual del Proyecto

El proyecto ha completado la **Fase 1 — Foundation** y se encuentra actualmente en la **Fase 2 — Alpha (Alpha Funcional 0.1 en Android)**:

* **Foundation completada:** Modelos de dominio inmutables, catálogo oficial inicial de 28 especies zoológicas validadas, arquitectura limpia, contratos desacoplados y suite completa de tests unitarios (Python).
* **Alpha 0.1 en marcha:** Aplicación Android nativa con Jetpack Compose, Material 3, captura visual con CameraX, almacenamiento efímero en caché, pipeline de identificación determinista y persistencia local de colección y perfiles offline con Room (SQLite).
* **Visión Rebaselinada (DEC-053):** Formalización de las tres capas de producto (**CORE**, **GAME**, **SOCIAL**), arquitectura publicitaria desacoplada (`AdService`), economía ética sin Pay-to-Win con moneda gratuita y tienda de cosméticos, duelos PvP ligeros (equipos de 10 cartas) y roadmap estratégico de 7 fases.

Consulta el documento maestro de visión: [docs/PRODUCT_VISION.md](docs/PRODUCT_VISION.md).

---

## 🧭 Las Tres Capas de Producto

1. **CAPA CORE (Irrenunciable):** Identificar fauna mediante cámara o imagen, generar la carta correspondiente, voltearla en 3D para explorar la ciencia vs. Lore personal y guardarla de forma persistente en el Baúl.
2. **CAPA GAME (Juego Ligero):** Rareza de colección, estadísticas lúdicas ficticias (HP/ATK/DEF/SPD/TYPE/SPECIAL), duelos PvP con mazos de 10 cartas configurables semanalmente, progresión, moneda gratuita y tienda cosmética.
3. **CAPA SOCIAL (Comunidad):** Perfiles públicos, amigos, regalos, intercambio de cartas (*trading* con metadatos de emisión congelados), contenido generado por usuario (UGC) y herramientas de moderación.

---

## 🛠️ Realidad Tecnológica Dual

* **Python 3.10+ (`src/`, `tests/`):**
  * Dominio biológico y ontológico de referencia.
  * Catálogo de especies y validación de esquemas JSON.
  * Servicio `TaxonomyIndex` y suite de verificación de contratos (150+ tests).
* **Android Nativo (`android/`):**
  * Kotlin 2.0+, Jetpack Compose, Material 3, Navigation Compose.
  * Captura fotográfica nativa con AndroidX CameraX y retícula de fauna.
  * Persistencia local offline de alto rendimiento mediante Room / SQLite (Perfiles y Cartas).
  * Pruebas unitarias con JUnit 4 y Robolectric.

Para más detalles, consulta [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

---

## 📂 Estructura del Proyecto

```text
whoanimal/
├── android/                     # Aplicación móvil nativa Android
│   ├── app/src/main/            # Código Kotlin (UI, Compose, Room, CameraX)
│   └── app/src/test/            # Tests unitarios Robolectric
├── data/
│   └── species/                 # Catálogo oficial zoológico en JSON
├── docs/
│   ├── PRODUCT_VISION.md        # Visión integral y marco estratégico del producto
│   ├── ARCHITECTURE.md          # Arquitectura técnica dual y capas
│   ├── CARD_SPEC.md             # Especificación técnica y 19 campos de cartas
│   ├── DECISIONS.md             # Registro formal de decisiones (ADR)
│   ├── DISCLAIMER.md            # Aviso de responsabilidad educativa y de seguridad
│   ├── GDD.md                   # Game Design Document (visión, cartas, core loop)
│   ├── GOVERNANCE.md            # Modelo de roles (Director, PM, Developer) y flujo
│   ├── PLANNING.md              # Plan Maestro de desarrollo y alineación de versión
│   ├── PRODUCT_RULES.md         # Reglas éticas, F2P, AdService y no Pay-to-Win
│   ├── PROJECT_CONTEXT.md       # Memoria oficial y principios transversales
│   ├── RELEASES.md              # Registro histórico de versiones y builds Android
│   ├── ROADMAP.md               # Planificación estratégica oficial en 7 fases
│   └── VERSIONING.md            # Esquema semántico y control de versionCode
├── src/
│   └── whoanimal/               # Dominio Python, catálogo y servicios taxonómicos
├── tests/                       # Pruebas unitarias de dominio en Python
├── AGENTS.md                    # Manual normativo obligatorio para agentes de desarrollo
├── ALPHA_CHECKLIST.md           # Criterios de aceptación y checklist de Alpha 0.1
├── pyproject.toml               # Configuración estándar PEP 621
└── README.md                    # Presentación general del proyecto
```

---

## 🧪 Verificación y Pruebas

### 1. Pruebas de Dominio Python
```bash
pytest -v tests
```

### 2. Pruebas Unitarias Android
En Windows (PowerShell):
```powershell
cd android
.\gradlew.bat testDebugUnitTest
```

### 3. Compilación de APKs Android
```powershell
cd android
.\gradlew.bat assembleDebug
.\gradlew.bat assembleRelease
```

---

## 📚 Documentación de Referencia

* **Visión y Estrategia:**
  * [Visión de Producto (PRODUCT_VISION.md)](docs/PRODUCT_VISION.md)
  * [Game Design Document (GDD)](docs/GDD.md)
  * [Hoja de Ruta (Roadmap)](docs/ROADMAP.md)
  * [Memoria del Proyecto](docs/PROJECT_CONTEXT.md)
* **Gobernanza y Operación:**
  * [Manual para Agentes (AGENTS.md)](AGENTS.md)
  * [Plan Maestro de Desarrollo](docs/PLANNING.md)
  * [Modelo de Gobernanza](docs/GOVERNANCE.md)
  * [Flujo de Trabajo por Objetivos](docs/WORKFLOW.md)
  * [Registro de Decisiones (ADR)](docs/DECISIONS.md)
* **Reglas y Ética:**
  * [Reglas de Producto (100% Pet Friendly, F2P, Ética)](docs/PRODUCT_RULES.md)
  * [Aviso de Responsabilidad](docs/DISCLAIMER.md)
  * [Especificación de la Carta](docs/CARD_SPEC.md)
* **Arquitectura y Builds:**
  * [Arquitectura Técnica](docs/ARCHITECTURE.md)
  * [Checklist Oficial de Alpha](docs/ALPHA_CHECKLIST.md)
  * [Sistema de Versionado](docs/VERSIONING.md)
  * [Registro de Releases](docs/RELEASES.md)
