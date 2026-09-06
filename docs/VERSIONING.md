# Sistema Oficial de Versionado (Versioning Scheme) — WHO Animal

**Documento:** `docs/VERSIONING.md`  
**Propósito:** Definir la nomenclatura semántica, el ciclo de versiones de producto y el control estricto de builds para distribución móvil (Android / Google Play).

---

## 1. Esquema Semántico de Versionado

El número de versión visible del producto (`versionName`) utiliza la sintaxis estándar:

$$\text{FASE} . \text{CORRECCIONES} . \text{ITERACIÓN}$$

$$(MAJOR . MINOR . PATCH)$$

En **WHO Animal**, cada segmento tiene una definición semántica adaptada a la naturaleza del proyecto:

```text
       0        .        0        .        1
  ▲                 ▲                 ▲
  │                 │                 │
FASE           CORRECCIONES       ITERACIÓN / VERSIÓN
(0=Alpha,      (Nivel acumulado   (Nueva entrega funcional
 1=Beta,        de fixes/parches   o ciclo de objetivos)
 2=Release)     en el ciclo)
```

### 1.1. Primer Número — FASE (Major)
Define la madurez global del producto:
* **`0` = ALPHA:** Fundación, diseño conceptual, arquitectura, validación técnica del núcleo y pruebas internas sin usuarios finales masivos.
* **`1` = BETA:** Producto funcional integrado, pruebas con usuarios reales, estabilidad, optimización y refinamiento de UX.
* **`2` = RELEASE:** Versión pública comercialmente estable, con distribución masiva y soporte continuo.

### 1.2. Segundo Número — CORRECCIONES (Minor)
Representa el nivel de correcciones o parches acumulados dentro del ciclo de la fase actual:
* Se incrementa cuando se aplican correcciones de errores (bug fixes), parches de seguridad, refactorizaciones internas o ajustes menores sin alteración del core loop.

### 1.3. Tercer Número — ITERACIÓN / VERSIÓN (Patch)
Representa una nueva iteración o versión funcional del producto dentro de la fase correspondiente:
* Se incrementa al completar un hito funcional, un conjunto cerrado de objetivos del roadmap o una nueva entrega desplegable.

---

## 2. Ejemplos de Evolución

* `0.0.1`: Primera versión Alpha de fundación (código inicial, arquitectura y documentación).
* `0.0.2`: Segunda iteración Alpha (incorporación de validación de esquemas y modelos).
* `0.1.0`: Versión Alpha con ciclo acumulado de correcciones y estabilización.
* `1.0.0`: Salto a la primera versión Beta cerrada con usuarios de prueba.
* `2.0.0`: Lanzamiento oficial del producto en tiendas de aplicaciones.

---

## 3. Relación entre Objetivos (`WHO-xxx`) y Versiones

> **Regla de Oro:**  
> **Completar un objetivo NO genera automáticamente una nueva versión del producto.**

Un objetivo individual (`WHO-001`, `WHO-002`, `WHO-003`, etc.) puede limitarse a:
* Ajustar documentación o gobernanza.
* Modificar arquitectura interna o refactorizar código.
* Corregir un fallo puntual o preparar infraestructura de CI.

La versión del producto solo se incrementará cuando el conjunto de cambios configure formalmente una **nueva entrega o iteración planificada en el Roadmap**.

---

## 4. Control de Distribución en Android: `versionName` vs `versionCode`

Para el ecosistema móvil de Google Play y la distribución de instaladores Android (APK / AAB), se aplican dos identificadores complementarios pero independientes:

| Atributo | Propósito | Formato | Ejemplo |
| :--- | :--- | :--- | :--- |
| **`versionName`** | Versión visible para el usuario en tiendas y menús de la app. | Texto semántico | `"0.0.1"` |
| **`versionCode`** | Identificador interno secuencial obligatorio para Android y Google Play. | Entero positivo estrictamente creciente | `1`, `2`, `3`... |

### 4.1. Reglas Inviolables para `versionCode`
1. **Entero Positivo y Único:** Debe ser siempre un número entero (`1`, `2`, `3`...).
2. **Estrictamente Creciente:** Cada nueva build distribuible debe tener un `versionCode` estrictamente mayor al anterior. Google Play rechaza cualquier binario con un código igual o inferior.
3. **Consumo de Código por Build Distribuible:** Generar un APK o AAB destinado a pruebas o distribución implica consumir irreversiblemente un nuevo `versionCode`.
4. **Prohibición de Reutilización:** Jamás se debe recompilar y redistribuir una entrega utilizando un `versionCode` ya empleado.

---

## 5. Protocolo Obligatorio Antes de Generar un APK / AAB de Distribución

Antes de compilar y firmar cualquier paquete de distribución para Android:

1. **Comprobar versión actual:** Verificar el último registro en [docs/RELEASES.md](RELEASES.md).
2. **Determinar nuevo `versionName`:** Validar si corresponde incrementar iteración o fase.
3. **Incrementar `versionCode`:** Asignar el siguiente entero correlativo (`último_código + 1`).
4. **Registrar en RELEASES.md:** Añadir la nueva fila en estado `PENDING_BUILD`.
5. **Generar el binario (APK/AAB):** Ejecutar el comando de compilación correspondiente.
6. **Ejecutar validaciones:** Comprobar integridad de firma, tests de instalación y rendimiento.
7. **Consolidar registro:** Actualizar el estado a `STAGING`, `INTERNAL_TRACK` o `PRODUCTION` con el hash SHA-256 del binario generado.
8. **Bloquear el `versionCode`:** Queda vetado volver a utilizar este identificador.

---

## 6. Fuente Única de Verdad para la Versión

Para evitar duplicaciones y divergencias manuales:
* La versión canónica del proyecto reside en `src/whoanimal/__init__.py::__version__` y se sincroniza con `pyproject.toml`.
* En futuras integraciones con clientes móviles (Android/Flutter), los archivos de configuración (`pubspec.yaml` / `app/build.gradle`) se alimentarán o verificarán contra esta fuente de verdad oficial.
