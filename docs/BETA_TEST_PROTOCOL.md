# WHO Animal — Beta Test Protocol (WHO-027)

## 1. Objetivo de la Beta
Validar la estabilidad, usabilidad y correcto funcionamiento del Core Loop de WHO Animal (v0.2.0-beta) en un entorno de campo real, bajo condiciones diversas de hardware y conectividad, asegurando el cumplimiento de las restricciones biológicas y de seguridad (DEC-002, DEC-003) sin fallos críticos.

## 2. Requisitos del Dispositivo
- **Sistema Operativo:** Android 8.0 (API 26) o superior.
- **Hardware:**
  - Cámara principal funcional.
  - Almacenamiento disponible para base de datos local y caché de imágenes temporales.
  - Memoria RAM sugerida: 2GB+.
- **Permisos requeridos en tiempo de ejecución:** `CAMERA`.

## 3. Checklist de Instalación
- [ ] Asegurarse de tener el `app-release.aab` desplegado en la pista de Internal Testing de Google Play o el APK instalado manualmente.
- [ ] Eliminar versiones previas (Alpha) o limpiar el almacenamiento de la aplicación para evitar conflictos de esquemas de Room.
- [ ] Verificar que la aplicación solicite el permiso de cámara únicamente al intentar hacer una captura, no durante el inicio.

## 4. Casos de Prueba Manuales
Consultar la matriz exhaustiva en [MANUAL_TEST_MATRIX.md](MANUAL_TEST_MATRIX.md). Todo tester deberá completar esta matriz en al menos un dispositivo físico.

## 5. Registro de Incidencias
Todo tester (incluso sin experiencia técnica) debe reportar cualquier comportamiento inesperado o regresión funcional documentándolo con la plantilla oficial [ISSUE_TEMPLATE.md](ISSUE_TEMPLATE.md). Para ello:
1. Indica **qué hiciste** (Pasos realizados).
2. Indica **qué observaste** (Resultado observado).
3. Indica **qué debería ocurrir** (Resultado esperado según el caso de prueba).
4. Clasifica adecuadamente según su prioridad (Blocker, High, Medium, Low) y categoría.

No es necesario el uso de herramientas técnicas (como Android Studio, terminal o `adb`). Tu descripción clara de los pasos y capturas de pantalla/video serán la evidencia principal.

## 6. Criterios de Aceptación (Release to Production)
Para que esta fase Beta se considere exitosa y apruebe el paso a Producción:
- **Cero** bloqueadores (`Blocker/High`) en el Core Loop (Captura, Identificación, Guardado, Visualización).
- Tasa de Crash-Free sessions mayor al **99.5%**.
- Accesibilidad probada sin regresiones (DEC-058).
- Todos los dispositivos clave de la [Device Compatibility Matrix](DEVICE_MATRIX.md) marcados como `PASS`.
