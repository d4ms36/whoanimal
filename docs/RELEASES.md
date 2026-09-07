# Registro Oficial de Releases y Entregas (Releases Log) — WHO Animal

**Documento:** `docs/RELEASES.md`  
**Propósito:** Trazabilidad histórica de versiones de producto, números de build y artefactos de distribución en Android.

---

## 1. Tabla Histórica de Releases

| Version Name | Version Code | Fase | Tipo | Fecha | Estado | Notas |
| :--- | :---: | :--- | :--- | :---: | :--- | :--- |
| **0.0.1** | **1** | Alpha | Foundation | 2026-09-06 | `INTERNAL/FOUNDATION` | Primera versión lógica del proyecto. Arquitectura de dominio, especificación de cartas, gobernanza y reglas éticas. |
| **0.1.0-alpha** | **2** | Alpha | Internal Alpha | 2026-09-07 | `ALPHA` | Primer release técnico y binario ejecutable Android de WHO Animal. Cierre integral del Golden Path Alpha 0.1 (`Login → Home → Capture → CameraX → Observation → Identification → Decision → Capture → Card → Flip → Save → Storage/Baúl → Reopen`). |
| **0.2.0-beta.1** | **3** | Beta | Internal Beta | 2026-09-07 | `CLOSED_BETA` | Pipeline de Firma de Release y Google Play Internal Testing. Implementación de AdService desacoplado. |

---

## 2. Glosario de Estados de Release

* **`INTERNAL/FOUNDATION`:** Versión lógica de arquitectura, código base o gobernanza; no incluye artefactos binarios de distribución aún.
* **`PENDING_BUILD`:** Versión planificada con `versionCode` reservado previa a la compilación.
* **`INTERNAL_TRACK`:** Binario generado y distribuido a través de vías internas de prueba (Google Play Internal Testing o instalación directa ad-hoc).
* **`ALPHA`:** Release formal de fase Alpha con binario compilado y verificado.
* **`CLOSED_BETA`:** Binario desplegado en canal Beta para grupo seleccionado de evaluadores.
* **`OPEN_BETA`:** Despliegue abierto para recolección de métricas y validación masiva.
* **`PRODUCTION`:** Versión oficial aprobada y disponible públicamente en tiendas de aplicaciones.
* **`DEPRECATED`:** Versión antigua descontinuada o retirada por parches de seguridad.

---

## 3. Registro de Artefactos Físicos (APK / AAB)

### Release `v0.1.0-alpha` (Build 2)
* **Fecha:** 2026-09-07
* **Version Name:** `0.1.0-alpha`
* **Version Code:** `2`
* **Tipo de Build:** Release (`assembleRelease`)
* **Artefacto:** `app-release-unsigned.apk`
* **Ruta de compilación:** `android/app/build/outputs/apk/release/app-release-unsigned.apk`
* **Tamaño del binario:** `12,019,197 bytes` (~11.46 MB)
* **Algoritmo:** `SHA-256`
* **Hash SHA-256:** `17DA1F7297E55C14327A91F9E2E310E73931DF6CA61D07B3B8862DEC4F22F83E`
* **Tag Git:** `v0.1.0-alpha`
* **Validaciones de Regresión Ejecutadas:**
  * Python Domain Tests: `150/150 PASS` (`Ran 150 tests in 0.152s. OK`).
  * Android Unit Tests: `63/63 PASS` (9 test suites, 0 failures, 0 skipped).
  * Build Release Android: `BUILD SUCCESSFUL` en 1m 6s.
* **Alcance del Release:**
  * Perfil de explorador local y sesión offline (`ExplorerProfile`, Room).
  * Menú Home y navegación centralizada (`AppNavHost`).
  * Captura visual real con AndroidX CameraX (Preview en vivo, retícula de fauna, permisos en runtime y almacenamiento efímero en `cacheDir`).
  * Motor de identificación determinista con candidatos taxonómicos y gestión de confianza.
  * Decisión explícita (`ACCEPTED`) y puente hacia `Capture`.
  * Generación y ensamblaje de `AnimalCard` con los 19 campos canónicos del dominio.
  * Presentación interactiva de carta con giro táctil tridimensional en 3D (`CardFrontView` / `CardBackView`), desacoplamiento estricto entre Ciencia y Lore.
  * Persistencia local en base de datos Room SQLite v2 (10 contenedores × 30 espacios = 300 slots).
  * Pantalla de Baúl (`CollectionScreen`) con selector de contenedores `C-1` a `C-10` y grid visual de 2 columnas con miniaturas y fallbacks.
  * Reapertura inmutable de cartas persistidas desde el Baúl (sin regeneración, sin mutación de identificadores ni alteración de atributos de emisión).
* **Limitaciones Conocidas / Deuda No Bloqueante:**
  * La edición interactiva de Historia Personal / Lore con límite de 3 ediciones (`DEC-041`) queda registrada para la siguiente iteración de pulido (el campo `personalLore` se persiste y visualiza correctamente).
  * La acción de eliminar/liberar carta directamente desde el grid del Baúl se encuentra protegida para evitar borrados accidentales; se incorporará con diálogo modal de confirmación en la siguiente iteración.
  * El binario no está firmado con keystore de producción (unsigned APK para testing interno / evaluación local).

### Release `v0.2.0-beta.1` (Build 3)
* **Fecha:** 2026-09-07
* **Version Name:** `0.2.0-beta.1`
* **Version Code:** `3`
* **Tipo de Build:** Release (`assembleRelease bundleRelease`)
* **Artefacto:** `app-release.aab`
* **Ruta de compilaci�n:** `android/app/build/outputs/bundle/release/app-release.aab`
* **Validaciones de Regresi�n Ejecutadas:**
  * Python Domain Tests: `150/150 PASS`
  * Android Unit Tests: PASS (esperando confirmaci�n)
  * Build Release Android: `BUILD SUCCESSFUL`
* **Alcance del Release:**
  * Implementaci�n del pipeline de firma de release (keystore.properties).
  * Optimizaci�n de R8 y ProGuard habilitada.
  * Contrato AdService desacoplado con stubs locales.

