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
| Alpha E2E          | ⏳      |

---

# Checklist funcional

## Cuenta

| Función          | Estado |
| ---------------- | ------ |
| Login local      | ⬜      |
| Crear perfil     | ⬜      |
| Recordar usuario | ⬜      |

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
| CameraX     | ⬜      |
| Permisos    | ⬜      |
| Tomar foto  | ⬜      |

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
| Giro 3D      | ⬜      |
| Frente       | 🟡     |
| Reverso      | 🟡     |

## Colección

| Función          | Estado |
| ---------------- | ------ |
| Persistencia     | ✅      |
| 10 contenedores  | ✅      |
| 30 slots         | ✅      |
| Mostrar cartas   | 🟡     |
| Eliminar         | 🟡     |
| Editar permitido | ⬜      |

---

# Golden Path Alpha

El siguiente recorrido debe funcionar completo para declarar Alpha terminada.

* [ ] Abrir la aplicación.
* [ ] Login local.
* [ ] Entrar al Home.
* [ ] Abrir Captura.
* [ ] Tomar una foto.
* [ ] Crear Observation.
* [ ] Obtener IdentificationResult.
* [ ] Aceptar identificación.
* [ ] Crear Capture.
* [ ] Generar AnimalCard.
* [ ] Revisar la carta.
* [ ] Guardarla.
* [ ] Verla en Collection.
* [ ] Cerrar la aplicación.
* [ ] Volver a abrirla.
* [ ] Recuperar la carta.

---

# Bugs conocidos

| ID | Estado | Descripción   |
| -- | ------ | ------------- |
| —  | —      | Sin registrar |

---

# Criterio de lanzamiento

Se crea el tag:

`v0.1.0-alpha`

únicamente cuando:

* todas las casillas críticas del Golden Path estén completas;
* Android compile en Release;
* la suite Python continúe pasando;
* no existan bloqueos críticos abiertos.
