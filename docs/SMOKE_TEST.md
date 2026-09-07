# WHO Animal — Smoke Test Checklist (WHO-027)

*Este test está diseñado para ser completado de manera rápida (≤ 5 minutos) en cualquier build nueva.*

### Pre-Requisitos
- [ ] Instalación limpia (Clear Data o Primera instalación).

### Flujo Rápido (Golden Path Validation)
1. **[ ] Onboarding:** La app inicia, solicita nombre de perfil y navega al Home sin cierres.
2. **[ ] Cámara:** Pulsar "Capturar", conceder permisos y la preview de la cámara arranca (o fallback) sin crashear.
3. **[ ] Identificación Determinista:** Tomar la captura y confirmar que muestra la pantalla de resultados (Identification Result).
4. **[ ] Aceptación:** Pulsar en Aceptar e ingresar al detalle frontal de la Carta 3D.
5. **[ ] Baúl y Persistencia:** Volver al Home, abrir la Colección, verificar que la carta está en el primer slot del C-1.
6. **[ ] Reverso y Lore:** Abrir la carta desde la colección, voltearla en 3D (doble tap/botón) y verificar que la ficha técnica se lee correctamente.

### Resultado del Smoke Test
Si todos los checks están marcados, el build se considera **ESTABLE PARA TESTING PROFUNDO**.
Si **cualquier** check falla, el build debe ser rechazado inmediatamente.
