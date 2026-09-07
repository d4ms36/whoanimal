# Arquitectura Técnica — WHO Animal

**Versión:** 1.0 (Rebaseline Oficial — WHO-DOC-001 / DEC-053)  
**Tecnología Dual:**
* **Python 3.10+:** Dominio biológico de referencia, servicios taxonómicos (`TaxonomyIndex`), validación de contratos JSON (`Card`, `AnimalProfile`) y tooling de pruebas unitarias.
* **Android Nativo (Kotlin 2.0+):** Cliente móvil interactivo, interfaz declarativa (Jetpack Compose, Material 3, Navigation Compose), captura fotográfica nativa (AndroidX CameraX) y persistencia local (Room / SQLite).  
**Paradigma:** Clean Architecture / Domain-Driven Design (DDD) modular y desacoplado.

---

## 1. Visión y Principios Arquitectónicos

La arquitectura de WHO Animal está concebida para crecer de forma modular sin acoplamientos prematuros:

1. **Aislamiento del Dominio (`domain`):** La lógica de negocio, las reglas ontológicas de las cartas y la separación entre *Ciencia* y *Lore* no dependen de ningún framework visual, base de datos ni motor de IA específico.
2. **Separación de Tres Capas de Producto:**
   * **CORE:** Capture $\rightarrow$ Observation $\rightarrow$ IdentificationResult $\rightarrow$ IdentificationDecision $\rightarrow$ Capture $\rightarrow$ Card $\rightarrow$ Persistence $\rightarrow$ Collection.
   * **GAME:** Rarezas de colección, estadísticas de combate (HP/ATK/DEF/SPD/TYPE/SPECIAL), duelos PvP, progresión y tienda cosmética.
   * **SOCIAL:** Perfiles de usuario, amigos, intercambio (*trading* con metadatos de emisión congelados), UGC y moderación.
3. **Contratos Desacoplados de Identificación (`services`):** El `IdentificationService` opera mediante contratos de frontera (`ObservationContract` $\rightarrow$ `IdentificationResultContract`), permitiendo alternar motores locales ligeros (ONNX / MobileNet) con servicios remotos sin impactar a la UI ni al dominio.
4. **Almacenamiento Efímero vs. Persistente:**
   * Las fotos capturadas residen temporalmente en el caché local (`cacheDir`) para alimentar la `Observation`.
   * Solo las capturas aceptadas formalmente derivan en una `Card` almacenada en Room/SQLite.
5. **Arquitectura Publicitaria Desacoplada (`AdService`):** Los SDKs de publicidad operan como infraestructura externa desacoplada, exponiendo interfaces limpias (`BannerPlacement`, `InterstitialPolicy`, `RewardedAdService`, `FrequencyPolicy`). Ninguna entidad del dominio conoce de anuncios.

---

## 2. Mapa Arquitectónico del Sistema

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        ANDROID CLIENT (UI LAYER)                       │
│  • Jetpack Compose & Material 3 Screens (Splash, Home, Capture, Album) │
│  • CameraX Engine (PreviewView, ImageCapture, LifecycleOwner)          │
│  • AppNavHost (Centralized Navigation Compose)                         │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                    BOUNDARY & CONTRACTS LAYER (DOMAIN)                 │
│  • ObservationContract, CandidateSpeciesContract                       │
│  • IdentificationResultContract, IdentificationDecisionContract        │
│  • CardContract (19 Canonical Fields), ExplorerProfile                 │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                   SERVICES & LOCAL INFRASTRUCTURE                      │
│  • IdentificationService (Deterministic / On-Device ML)                │
│  • CardGeneratorService (Capture → AnimalCard Assembly)                │
│  • Room Persistence (WhoAnimalDatabase, ProfileDao, CardDao, SlotDao)   │
│  • AdService (Decoupled Banners, Interstitials & Opt-in Rewarded Ads)  │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                   PYTHON DOMAIN & TAXONOMIC SEED                       │
│  • Strict Domain Models (Observation, IdentificationResult, Capture)   │
│  • 19 Canonical Fields Card Model (Immutable post-issuance)            │
│  • TaxonomyIndex & Official Species Catalog (JSON Schemas)             │
│  • Test Suite (150+ Unit Tests)                                        │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Hoja de Ruta de Integración Técnica

| Fase | Enfoque Técnico | Componentes y Tecnologías Clave |
| :--- | :--- | :--- |
| **1. FOUNDATION** *(Completada)* | Modelos de dominio puros, catálogo inicial de 28 especies y suite de pruebas | Python stdlib, pytest, JSON Schema |
| **2. ALPHA** *(En Curso)* | Core Loop ejecutable end-to-end en Android con persistencia offline | Kotlin 2.0, Compose, CameraX, Room (10x30=300 slots) |
| **3. BETA** | Estabilidad, compatibilidad multi-dispositivo, rendimiento y pre-AdService | Android Profiler, a11y, localization i18n, AdService stubs |
| **4. RELEASE 1.0** | Lanzamiento público en Google Play Store con Core Loop + Baúl + Shop cosmética | Google Play SDKs, AdMob desacoplado, Room Database |
| **5. RELEASE 1.x (GAME)** | Capa Game: Duelos PvP, equipos de 10 cartas y estadísticas de combate | Motor ligero de combate por turnos, local/matchmaking |
| **6. RELEASE 2.x (SOCIAL)** | Capa Social: Amigos, intercambio seguro y perfiles públicos | Protocolos P2P / Backend social ligero, moderación UGC |
| **7. RELEASE 3.x (WORLD)** | Expansión global del catálogo zoológico y nube multidispositivo | Cloud Storage, sincronización remota de colección |
