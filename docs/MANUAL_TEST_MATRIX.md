# WHO Animal — Manual Test Matrix (WHO-027)

## Casos de Prueba

| ID | Área | Pasos a ejecutar | Resultado Esperado | Severidad | Estado |
|---|---|---|---|---|---|
| **TC-001** | Primer Inicio | 1. Instalar la app.<br>2. Abrir la app. | Pantalla de Splash, transición al Home. Solicitud de creación de perfil si es la primera vez. | High | Pendiente |
| **TC-002** | Creación Perfil | 1. Ingresar nombre y continuar. | Nombre persistido, bienvenida en Home, no vuelve a pedir nombre en reinicios. | High | Pendiente |
| **TC-003** | Cámara (Permisos) | 1. Presionar "Capturar".<br>2. Rechazar permiso.<br>3. Reintentar y aceptar. | Muestra rationale si se rechaza. Abre CameraX si se acepta. | Blocker | Pendiente |
| **TC-004** | Identificación | 1. Tomar foto.<br>2. Ver resultado. | Muestra Provider Determinista con confianza %. Cero ads visualizados. | Blocker | Pendiente |
| **TC-005** | Guardado | 1. Aceptar Identificación. | Pasa al proceso de guardar, muestra Card completa con campos canónicos. | Blocker | Pendiente |
| **TC-006** | Baúl | 1. Ir a CollectionScreen.<br>2. Revisar C-1. | Grid visualiza la carta. No hay crash al cargar el slot ocupado. | Blocker | Pendiente |
| **TC-007** | Edición Lore | 1. Abrir carta del baúl.<br>2. Voltear (Reverso).<br>3. Editar Lore y guardar. | Máximo 300 caracteres, contador de ediciones (max 3), se persiste correctamente. | Medium | Pendiente |
| **TC-008** | Liberación | 1. Presionar "Liberar".<br>2. Confirmar en diálogo modal. | Carta se elimina de Room, el slot queda libre, redirección al baúl. | High | Pendiente |
| **TC-009** | Cambio Idioma | 1. Cambiar OS a Inglés.<br>2. Abrir app. | UI completamente en inglés, placeholders funcionan, Ad Service A11y correcto. | Medium | Pendiente |
| **TC-010** | Rotación | 1. Rotar pantalla durante cámara o visualización de carta. | Layout adaptable, no hay crashes, se mantiene el estado de la UI (sin recarga innecesaria). | Medium | Pendiente |
| **TC-011** | Reinicio App | 1. Forzar cierre (Kill process).<br>2. Reabrir. | Carga rápida, retiene progreso (Colección intacta), sin pedir perfil nuevo. | High | Pendiente |
| **TC-012** | Sin Cámara | 1. Usar Emulador/Dispositivo sin cámara.<br>2. Abrir módulo de captura. | Muestra pantalla de Fallback/Simulada, no se rompe CameraX. | High | Pendiente |
| **TC-013** | Baja Memoria | 1. Llenar RAM del sistema.<br>2. Navegar y cargar imágenes grandes en baúl. | No ocurren OOM (Out Of Memory) Crashes, Coil maneja la caché eficientemente. | Blocker | Pendiente |
| **TC-014** | Descarte | 1. Tomar foto.<br>2. Ver resultado.<br>3. Elegir descartar/rechazar la identificación. | El flujo permite no guardar la carta y continuar/regresar. No se guarda la carta en la colección accidentalmente ni hay slots fantasmas. | High | Pendiente |
| **TC-015** | Editable vs No Editable | 1. Abrir carta del baúl.<br>2. Intentar editar datos científicos (Especie, Nombre).<br>3. Intentar editar Lore. | Los campos científicos están protegidos (no editables). Solo el Lore permite edición (editable). | Blocker | Pendiente |
| **TC-016** | Fecha de Identificación | 1. Revisar una carta guardada en el baúl. | La fecha de la captura/identificación se muestra correctamente y no desaparece al reabrir. | Medium | Pendiente |
| **TC-017** | Privacidad de Ubicación | 1. Revisar una carta guardada en el baúl.<br>2. Leer ubicación en la interfaz. | La ubicación debe ser GENERAL/APROXIMADA. NUNCA debe mostrar calle, número o coordenadas GPS exactas. | Blocker (Categoría: Privacidad) | Pendiente |
