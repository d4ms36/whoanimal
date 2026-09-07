# Registro Oficial de Releases y Entregas (Releases Log) â€” WHO Animal

**Documento:** `docs/RELEASES.md`  
**PropÃ³sito:** Trazabilidad histÃ³rica de versiones de producto, nÃºmeros de build y artefactos de distribuciÃ³n en Android.

---

## 1. Tabla HistÃ³rica de Releases

| Version Name | Version Code | Fase | Tipo | Fecha | Estado | Notas |
| :--- | :---: | :--- | :--- | :---: | :--- | :--- |
| **0.0.1** | **1** | Alpha | Foundation | 2026-09-06 | `INTERNAL/FOUNDATION` | Primera versiÃ³n lÃ³gica del proyecto. Arquitectura de dominio, especificaciÃ³n de cartas, gobernanza y reglas Ã©ticas. |
| **0.1.0-alpha** | **2** | Alpha | Internal Alpha | 2026-09-07 | `ALPHA` | Primer release tÃ©cnico y binario ejecutable Android de WHO Animal. Cierre integral del Golden Path Alpha 0.1 (`Login â†’ Home â†’ Capture â†’ CameraX â†’ Observation â†’ Identification â†’ Decision â†’ Capture â†’ Card â†’ Flip â†’ Save â†’ Storage/BaÃºl â†’ Reopen`). |
| **0.2.0-beta.1** | **3** | Beta | Internal Beta | 2026-09-07 | `CLOSED_BETA` | Pipeline de Firma de Release y Google Play Internal Testing. ImplementaciÃ³n de AdService desacoplado. |

---

## 2. Glosario de Estados de Release

* **`INTERNAL/FOUNDATION`:** VersiÃ³n lÃ³gica de arquitectura, cÃ³digo base o gobernanza; no incluye artefactos binarios de distribuciÃ³n aÃºn.
* **`PENDING_BUILD`:** VersiÃ³n planificada con `versionCode` reservado previa a la compilaciÃ³n.
* **`INTERNAL_TRACK`:** Binario generado y distribuido a travÃ©s de vÃ­as internas de prueba (Google Play Internal Testing o instalaciÃ³n directa ad-hoc).
* **`ALPHA`:** Release formal de fase Alpha con binario compilado y verificado.
* **`CLOSED_BETA`:** Binario desplegado en canal Beta para grupo seleccionado de evaluadores.
* **`OPEN_BETA`:** Despliegue abierto para recolecciÃ³n de mÃ©tricas y validaciÃ³n masiva.
* **`PRODUCTION`:** VersiÃ³n oficial aprobada y disponible pÃºblicamente en tiendas de aplicaciones.
* **`DEPRECATED`:** VersiÃ³n antigua descontinuada o retirada por parches de seguridad.

---

## 3. Registro de Artefactos FÃ­sicos (APK / AAB)

### Release `v0.1.0-alpha` (Build 2)
* **Fecha:** 2026-09-07
* **Version Name:** `0.1.0-alpha`
* **Version Code:** `2`
* **Tipo de Build:** Release (`assembleRelease`)
* **Artefacto:** `app-release-unsigned.apk`
* **Ruta de compilaciÃ³n:** `android/app/build/outputs/apk/release/app-release-unsigned.apk`
* **TamaÃ±o del binario:** `12,019,197 bytes` (~11.46 MB)
* **Algoritmo:** `SHA-256`
* **Hash SHA-256:** `17DA1F7297E55C14327A91F9E2E310E73931DF6CA61D07B3B8862DEC4F22F83E`
* **Tag Git:** `v0.1.0-alpha`
* **Validaciones de RegresiÃ³n Ejecutadas:**
  * Python Domain Tests: `150/150 PASS` (`Ran 150 tests in 0.152s. OK`).
  * Android Unit Tests: `63/63 PASS` (9 test suites, 0 failures, 0 skipped).
  * Build Release Android: `BUILD SUCCESSFUL` en 1m 6s.
* **Alcance del Release:**
  * Perfil de explorador local y sesiÃ³n offline (`ExplorerProfile`, Room).
  * MenÃº Home y navegaciÃ³n centralizada (`AppNavHost`).
  * Captura visual real con AndroidX CameraX (Preview en vivo, retÃ­cula de fauna, permisos en runtime y almacenamiento efÃ­mero en `cacheDir`).
  * Motor de identificaciÃ³n determinista con candidatos taxonÃ³micos y gestiÃ³n de confianza.
  * DecisiÃ³n explÃ­cita (`ACCEPTED`) y puente hacia `Capture`.
  * GeneraciÃ³n y ensamblaje de `AnimalCard` con los 19 campos canÃ³nicos del dominio.
  * PresentaciÃ³n interactiva de carta con giro tÃ¡ctil tridimensional en 3D (`CardFrontView` / `CardBackView`), desacoplamiento estricto entre Ciencia y Lore.
  * Persistencia local en base de datos Room SQLite v2 (10 contenedores Ã— 30 espacios = 300 slots).
  * Pantalla de BaÃºl (`CollectionScreen`) con selector de contenedores `C-1` a `C-10` y grid visual de 2 columnas con miniaturas y fallbacks.
  * Reapertura inmutable de cartas persistidas desde el BaÃºl (sin regeneraciÃ³n, sin mutaciÃ³n de identificadores ni alteraciÃ³n de atributos de emisiÃ³n).
* **Limitaciones Conocidas / Deuda No Bloqueante:**
  * La ediciÃ³n interactiva de Historia Personal / Lore con lÃ­mite de 3 ediciones (`DEC-041`) queda registrada para la siguiente iteraciÃ³n de pulido (el campo `personalLore` se persiste y visualiza correctamente).
  * La acciÃ³n de eliminar/liberar carta directamente desde el grid del BaÃºl se encuentra protegida para evitar borrados accidentales; se incorporarÃ¡ con diÃ¡logo modal de confirmaciÃ³n en la siguiente iteraciÃ³n.
  * El binario no estÃ¡ firmado con keystore de producciÃ³n (unsigned APK para testing interno / evaluaciÃ³n local).

### Release `v0.2.0-beta.1` (Build 3)
* **Fecha:** 2026-09-07
* **Version Name:** `0.2.0-beta.1`
* **Version Code:** `3`
* **Tipo de Build:** Release (`assembleRelease bundleRelease`)
* **Artefacto:** `app-release.aab`
* **Ruta de compilación:** `android/app/build/outputs/bundle/release/app-release.aab`
* **Tamaño del binario:** `4,157,903 bytes` (~3.96 MB)
* **Algoritmo:** `SHA-256`
* **Hash SHA-256:** `A50B8B2EA092CAD6F49EE2B713CAB7989C9CCB25A547508D73C37D46A2E6F399`
* **Tag Git:** `v0.2.0-beta.1`
* **Validaciones de Regresión Ejecutadas:**
  * Python Domain Tests: `150/150 PASS`
  * Android Unit Tests: `110/110 PASS` (BUILD SUCCESSFUL en 1m)
  * Build Release Android: `BUILD SUCCESSFUL` en 2m 2s.
* **Alcance del Release:**
  * Implementación del pipeline de firma de release con fallback a keystore.properties (DEC-062).
  * Optimización de R8 y ProGuard habilitada (minifyEnabled = true).
  * Contrato AdService desacoplado con stubs locales para pruebas de accesibilidad y UI.

