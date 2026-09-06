# Registro Oficial de Releases y Entregas (Releases Log) — WHO Animal

**Documento:** `docs/RELEASES.md`  
**Propósito:** Trazabilidad histórica de versiones de producto, números de build y artefactos de distribución en Android.

---

## 1. Tabla Histórica de Releases

| Version Name | Version Code | Fase | Tipo | Fecha | Estado | Notas |
| :--- | :---: | :--- | :--- | :---: | :--- | :--- |
| **0.0.1** | **1** | Alpha | Foundation | 2026-09-06 | `INTERNAL/FOUNDATION` | Primera versión lógica del proyecto. Arquitectura de dominio, especificación de cartas, gobernanza y reglas éticas. *Nota: Representa la versión lógica de fundación; el primer binario físico APK/AAB será emitido y validado cuando se configure el entorno móvil.* |

---

## 2. Glosario de Estados de Release

* **`INTERNAL/FOUNDATION`:** Versión lógica de arquitectura, código base o gobernanza; no incluye artefactos binarios de distribución aún.
* **`PENDING_BUILD`:** Versión planificada con `versionCode` reservado previa a la compilación.
* **`INTERNAL_TRACK`:** Binario generado y distribuido a través de vías internas de prueba (Google Play Internal Testing o instalación directa ad-hoc).
* **`CLOSED_BETA`:** Binario desplegado en canal Beta para grupo seleccionado de evaluadores.
* **`OPEN_BETA`:** Despliegue abierto para recolección de métricas y validación masiva.
* **`PRODUCTION`:** Versión oficial aprobada y disponible públicamente en tiendas de aplicaciones.
* **`DEPRECATED`:** Versión antigua descontinuada o retirada por parches de seguridad.

---

## 3. Registro de Artefactos Físicos (APK / AAB)

*Actualmente no existen artefactos compilados (APK o AAB) en el repositorio. Esta sección registrará los hashes SHA-256 de los binarios una vez que se inicie la fase de compilación móvil.*
