# WHO Animal — ALPHA_CHECKLIST.md

**Versión objetivo:** `v0.1.0-alpha`

## Propósito

Este documento define el criterio oficial para declarar completada la Alpha de WHO Animal.

Una funcionalidad solo puede marcarse como completada cuando:

* funciona realmente;
* tiene pruebas suficientes;
* no rompe el dominio;
* la documentación está sincronizada.

---

# Estado general

| Área               | Estado |
| ------------------ | ------ |
| Foundation         | ✅      |
| Android Foundation | ✅      |
| Card Generator     | ✅      |
| Identification     | ✅      |
| Persistence        | ✅      |
| Alpha E2E          | ✅      |

---

# Checklist funcional

## Cuenta

| Función          | Estado |
| ---------------- | ------ |
| Login local      | ✅      |
| Crear perfil     | ✅      |
| Recordar usuario | ✅      |

## Home

| Función    | Estado |
| ---------- | ------ |
| Splash     | ✅      |
| Home       | ✅      |
| Navegación | ✅      |

## Captura

| Función     | Estado |
| ----------- | ------ |
| Placeholder | ✅      |
| CameraX     | ✅      |
| Permisos    | ✅      |
| Tomar foto  | ✅      |

## Identificación

| Función               | Estado |
| --------------------- | ------ |
| Observation           | ✅      |
| IdentificationService | ✅      |
| Resultados            | ✅      |
| Errores               | ✅      |
| Decision              | ✅      |

## Carta

| Función      | Estado |
| ------------ | ------ |
| Generación   | ✅      |
| Guardar      | ✅      |
| Persistencia | ✅      |
| Giro 3D      | ✅      |
| Frente       | ✅      |
| Reverso      | ✅      |

## Colección

| Función          | Estado |
| ---------------- | ------ |
| Persistencia     | ✅      |
| 10 contenedores  | ✅      |
| 30 slots         | ✅      |
| Mostrar cartas   | ✅      |
| Eliminar         | 🟡     |
| Editar permitido | ⬜      |

*Nota sobre Colección:*  
* *Eliminar:* En Alpha 0.1 está implementado en la revisión inicial de la carta mediante la acción modal "Descartar / Liberar". En el Baúl (`PERSISTED_CARD`), la eliminación directa se encuentra protegida para salvaguardar la colección; la eliminación con diálogo de confirmación desde el Baúl se reserva para una iteración posterior sin condicionar el Golden Path.  
* *Editar permitido:* El campo de Lore personal (`personalLore`) se almacena y recupera íntegramente en la base de datos local (Room v2); la pantalla de edición interactiva de Lore con el límite de 3 ediciones (DEC-041) se incorporará en el pulido de personalización sin bloquear el hito Alpha 0.1.

---

# Golden Path Alpha

El siguiente recorrido debe funcionar completo para declarar Alpha terminada:

* [x] Abrir la aplicación.
* [x] Login local.
* [x] Entrar al Home.
* [x] Abrir Captura.
* [x] Tomar una foto.
* [x] Crear Observation.
* [x] Obtener IdentificationResult.
* [x] Aceptar identificación.
* [x] Crear Capture.
* [x] Generar AnimalCard.
* [x] Revisar la carta.
* [x] Guardarla.
* [x] Verla en Collection.
* [x] Cerrar la aplicación.
* [x] Volver a abrirla.
* [x] Recuperar la carta.

> ✅ **Resultado:** Golden Path 100% verificado y funcional en Android (`edee173`).

---

# Bugs conocidos

| ID | Estado | Descripción |
| -- | ------ | ----------- |
| — | — | Ninguno activo; suite de 63 tests Android y 150 tests Python pasando limpiamente |

---

# Criterio de lanzamiento

Se crea el tag:

`v0.1.0-alpha`

únicamente cuando:

* todas las casillas críticas del Golden Path estén completas (✅ COMPLETADO);
* Android compile en Release (✅ assembleRelease BUILD SUCCESSFUL en WHO-019);
* la suite Python continúe pasando (✅ 150/150 PASS);
* no existan bloqueos críticos abiertos (✅ CERO BLOQUEOS).

*Estado:* ✅ RELEASE v0.1.0-alpha COMPLETADO. Binario `app-release-unsigned.apk` (SHA-256 verificado en `docs/RELEASES.md`) generado y tag `v0.1.0-alpha` creado.
