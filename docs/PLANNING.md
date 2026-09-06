# Plan Maestro de Desarrollo (Master Development Planning) — WHO Animal

**Documento:** `docs/PLANNING.md`  
**Propósito:** Tablero operativo central de alineación entre Director Creativo, Project Manager y Developer Principal.  
**Estado:** Activo y dinámico  
**Última actualización:** 2026-09-06 (Actualización de ciclo WHO-005B-A)

---

## 1. Estado Actual del Proyecto

| Campo | Estado |
| :--- | :--- |
| **Proyecto** | WHO Animal |
| **Fase actual** | Alpha |
| **Versión actual** | `0.0.1` |
| **Estado** | Foundation |
| **Objetivo activo** | Ninguno (Ciclo WHO-005B-A completado; a la espera de autorización para WHO-005B-B) |
| **Último objetivo completado** | `WHO-005B-A` — Actualización Documental de Capacidades Futuras |
| **Próximo objetivo propuesto** | `WHO-005B-B` — Validación de Esquemas y Serialización JSON |
| **Bloqueos** | Ninguno |
| **Decisiones pendientes** | `DEC-009` a `DEC-012`, `DEC-020` a `DEC-025` |
| **Último commit** | `134637c` |
| **Última actualización** | 2026-09-06 |

---

## 2. Versión Actualmente en Desarrollo

```text
=====================================================
          VERSIÓN ACTUAL EN DESARROLLO: 0.0.1
                      Fase: ALPHA
=====================================================
```

### Significado de la Versión según WHO Animal:
La versión no sigue la semántica convencional de SemVer de librerías, sino el esquema propio definido en [docs/VERSIONING.md](VERSIONING.md):

$$\text{FASE} . \text{CORRECCIONES} . \text{ITERACIÓN}$$

* **Primer número (`0` = FASE):** Representa la madurez global del producto (`0 = Alpha`, `1 = Beta`, `2 = Release`).
* **Segundo número (`0` = CORRECCIONES):** Nivel acumulado de parches, correcciones y ajustes internos en el ciclo.
* **Tercer número (`1` = ITERACIÓN):** Primera entrega o iteración funcional del producto dentro de la fase correspondiente (Fundación arquitectónica y documental).

---

## 3. Objetivos de la Versión (`0.0.1`)

| ID | Objetivo | Estado | Prioridad | Versión | Aprobado |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **WHO-001** | Exploración Inicial y Estado Cero | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-002** | Fundación de Arquitectura y Especificación de Cartas | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-003** | Gobernanza, Memoria del Proyecto y Versionado | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-004** | Plan Maestro de Desarrollo y Alineación por Versión | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005A**| Incorporación de Nuevas Decisiones al Contexto | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-A**| Actualización Documental de Capacidades Futuras | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-B**| Validación de Esquemas y Serialización JSON | `PROPUESTO` | Alta | 0.0.1 | No |
| **WHO-006** | Banco de Datos Inicial de Fauna (Semilla Educativa) | `PROPUESTO` | Media | 0.0.1 | No |

---

## 4. Objetivo Activo

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│                           OBJETIVO ACTIVO ACTUAL                            │
├──────────────────┬──────────────────────────────────────────────────────────┤
│ ID               │ WHO-005B-A                                               │
│ Nombre           │ Actualización Documental de Capacidades Futuras          │
│ Propósito        │ Registrar en la memoria y gobernanza las decisiones sobre│
│                  │ PVP, comercio de cartas, red de ilustradores y privacidad│
│                  │ para evitar bloqueos arquitectónicos en esquemas futuros │
│ Estado           │ COMPLETADO                                                │
│ Versión Asociada │ 0.0.1                                                    │
│ Requisitos       │ WHO-005A completado y aprobado                           │
│ Responsable      │ Developer Principal (Antigravity)                        │
└──────────────────┴──────────────────────────────────────────────────────────┘
```

* **Criterios de Aceptación:**
  1. `docs/DECISIONS.md` actualizado con decisiones `DEC-026` a `DEC-032` catalogadas como `APPROVED — FUTURE EXTENSION`.
  2. `docs/PROJECT_CONTEXT.md` y `docs/CARD_SPEC.md` actualizados con `specimen_number`, privacidad GPS vs. pública, inmutabilidad ante transferencia y capa de artwork único.
  3. `docs/GDD.md` y `docs/ROADMAP.md` sincronizados reflejando estas capacidades en fases posteriores.
  4. Cero código implementado (sin modelos PVP, sin trading, sin marketplace, sin perfiles de ilustrador).
  5. Pruebas unitarias ejecutadas y aprobadas al 100%.
  6. Detención formal tras la entrega sin encadenar `WHO-005B-B`.
* **Archivos Afectados:** `docs/DECISIONS.md`, `docs/PROJECT_CONTEXT.md`, `docs/CARD_SPEC.md`, `docs/GDD.md`, `docs/ROADMAP.md`, `docs/WORKFLOW.md`, `docs/PLANNING.md`.
* **Dependencias:** Ninguna externa.
* **Bloqueos:** Ninguno.
* **Resultado Esperado:** Memoria documental blindada para soportar extensiones futuras sin corromper la arquitectura base.

---

## 5. Próximos Objetivos

Ordenados por prioridad técnica y estratégica.

| ID | Nombre | Propósito | Prioridad | Dependencias | Estado | Req. Aprobación Director |
| :--- | :--- | :--- | :---: | :--- | :---: | :---: |
| **WHO-005B-B** | Validación de Esquemas y Serialización JSON | Importación/exportación estándar de cartas y perfiles compatibles con extensiones | Alta | WHO-002, WHO-005B-A | `PROPUESTO` | **Sí** |
| **WHO-006** | Banco de Datos Inicial de Fauna (Semilla) | Dotar al sistema de especímenes reales con datos y Lore | Media | WHO-005B-B | `PROPUESTO` | **Sí** |
| **WHO-007** | Prototipo del Servicio de Generación de Cartas | Implementación del ensamblador en base al protocolo | Media | WHO-005B-B | `PROPUESTO` | **Sí** |
| **WHO-008** | Evaluación y Prototipo de Ingesta Taxonómica | Conector experimental con APIs de biodiversidad (GBIF/iNat) | Media | WHO-005B-B, DEC-009 | `PROPUESTO` | **Sí** |
| **WHO-009** | Prototipo del Motor de Identificación por Visión | Implementación experimental de `IdentificationService` | Alta | WHO-005B-B, DEC-009, DEC-021 | `PROPUESTO` | **Sí** |
| **WHO-010** | Motor de Persistencia y Álbum de Colección | Almacenamiento local para inventario y álbum | Media | WHO-005B-B | `PROPUESTO` | **Sí** |
| **WHO-011** | Interfaz Gráfica / Prototipo Cliente Móvil | Primer frontend visual interactivo para volteo de cartas | Alta | WHO-004, DEC-012 | `PROPUESTO` | **Sí** |

> ⚠️ **Aviso de Gobernanza:**  
> Que un objetivo aparezca en esta tabla **NO constituye autorización para su desarrollo**.  
> Solo los objetivos marcados como `APROBADO` en la sección de control de aprobaciones pueden ser ejecutados por el Developer.

---

## 6. Control de Aprobación (Aprobaciones)

| Objetivo | Director Creativo | Project Manager | Developer | Estado Global |
| :--- | :---: | :---: | :---: | :--- |
| **WHO-001** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-002** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-003** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-004** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-B**| *PENDIENTE* | `PLANIFICADO` | `EN ESPERA` | `PROPOSED` |
| **WHO-006** | *PENDIENTE* | *EN EVALUACIÓN* | `EN ESPERA` | `PROPOSED` |

> **Regla:** El Developer no puede auto-aprobar objetivos. La autorización debe ser explícita por parte del Director Creativo y estructurada por el Project Manager.

---

## 7. Decisiones que Necesitamos Tomar

Vista operativa de las decisiones pendientes documentadas oficialmente en [docs/DECISIONS.md](DECISIONS.md):

| ID | Decisión | Impacto | ¿Bloquea desarrollo inmediato? | Estado Oficial |
| :--- | :--- | :---: | :---: | :---: |
| **DEC-009** | Motor definitivo de identificación visual (on-device vs. API vs. nube) | Alto | Sí (bloquea WHO-009) | `PENDING` |
| **DEC-010** | Estilo y universo del Lore (mitología única vs. folclore regional) | Medio | No (en fase de fundación) | `PENDING` |
| **DEC-011** | Convención definitiva de identificadores `card_id` y códigos de colección | Medio | No (temporal en v0.1) | `PENDING` |
| **DEC-012** | Tecnología cliente definitiva para la app de usuario (Flutter / nativo) | Alto | Sí (bloquea WHO-011) | `PENDING` |
| **DEC-020** | Diseño definitivo del esquema JSON de datos (`Animal` y `Card`) | Alto | Sí (requerido para WHO-005B-B)| `PENDING` |
| **DEC-021** | Umbrales definitivos de confianza en la identificación por visión/cámara | Medio | Sí (para WHO-009) | `PENDING` |
| **DEC-022** | Algoritmo matemático y curvas de probabilidad para rareza dinámica | Alto | No (en fase documental) | `PENDING` |
| **DEC-023** | Reglas anti-abuso para validación de observaciones y población | Alto | No (en fase documental) | `PENDING` |
| **DEC-024** | Sistema definitivo de autenticación y verificación de cartas | Medio | No (en fase documental) | `PENDING` |
| **DEC-025** | Estrategia de compatibilidad y migración entre versiones del esquema JSON | Medio | No (definible en WHO-005B-B)| `PENDING` |

---

## 8. Bloqueos (Blockers)

| Bloqueo | Causa | Objetivo Afectado | Responsable | Acción Necesaria | Estado |
| :--- | :--- | :---: | :---: | :--- | :---: |
| **Ninguno.** | N/A | N/A | N/A | N/A | `CLEAR` |

*Actualmente el flujo de trabajo opera sin impedimentos técnicos ni bloqueos externos.*

---

## 9. Registro Resumido de Versiones

| Versión | Fase | Estado | Objetivos Asociados | VersionCode | Tipo |
| :--- | :--- | :--- | :--- | :---: | :--- |
| **0.0.1** | Alpha | Foundation | WHO-001 a WHO-005B-A | **1\*** | Internal / Foundation |

*\*Nota: El `versionCode` 1 es de carácter lógico y documental en la fundación del repositorio. Todavía no se ha generado ningún binario físico APK o AAB.*  
*La fuente histórica y vinculante completa reside en [docs/RELEASES.md](RELEASES.md).*

---

## 10. Regla APK / AAB de Distribución

Recordatorio mandatorio del protocolo establecido en [docs/VERSIONING.md](VERSIONING.md):

Antes de compilar y distribuir cualquier paquete Android (APK/AAB):
1. Comprobar la versión actual en [docs/RELEASES.md](RELEASES.md).
2. Determinar el nuevo `versionName`.
3. Incrementar obligatoriamente `versionCode` (entero estrictamente mayor, único).
4. No reutilizar jamás un `versionCode` ya empleado.
5. Registrar el cambio en [docs/RELEASES.md](RELEASES.md).
6. Generar el binario físico.
7. Ejecutar validaciones de integridad y testing de instalación.
8. Registrar el artefacto con su hash SHA-256 oficial.

---

## 11. Fuera del Alcance Actual (Qué NO Estamos Haciendo)

Para prevenir el desvío del alcance (*scope creep*) y asegurar una base sólida, las siguientes áreas quedan **estrictamente fuera del alcance de la iteración actual**:

* ❌ **UI definitiva o componentes visuales de producción:** Aún no aprobados.
* ❌ **Identificación visual definitiva mediante IA:** Pendiente de decisión arquitectónica (`DEC-009`).
* ❌ **Backend definitivo o infraestructura cloud:** No autorizado en Fase 0.
* ❌ **Bases de datos definitivas o conectores externos:** No autorizado en Fase 0.
* ❌ **Sistema de cuentas, login o autenticación:** No contemplado para Alpha temprana.
* ❌ **Motor de enfrentamientos PVP:** Capacidad futura aprobada (`DEC-026`), prohibida su implementación en Fase 0.
* ❌ **Sistema de intercambio, marketplace o comercio:** Capacidad futura aprobada (`DEC-027`), prohibida su implementación en Fase 0.
* ❌ **Gestión de ilustradores o pasarelas de pago:** Capacidad futura aprobada (`DEC-028` / `DEC-029`), prohibida su implementación en Fase 0.
* ❌ **Publicación en Google Play Store:** No aplicable en Fase 0.
* ❌ **Generador automático o asistido de Lore por LLM en runtime:** No autorizado.

---

## 12. Historial de Planificación (Changelog)

| Fecha | Cambio Registrado | Motivo | Aprobado por |
| :--- | :--- | :--- | :---: |
| **2026-09-06** | Creación del Plan Maestro (`docs/PLANNING.md`) | Establecer tablero operativo central de alineación Director/PM/Dev | Director / PM (`WHO-004`) |
| **2026-09-06** | Incorporación de decisiones de entidad animal/carta, inmutabilidad, rareza dinámica y serial | Consolidación de memoria y reglas de producto (WHO-005A) | Director / PM (`WHO-005A`) |
| **2026-09-06** | Incorporación de capacidades futuras (PVP, intercambio, artwork único/ilustradores, privacidad GPS) | Blindaje arquitectónico para esquemas de datos sin cerrar opciones (WHO-005B-A) | Director / PM (`WHO-005B-A`) |

---

## 13. Reglas Fundamentales de Gobernanza

> 📌 **`docs/PLANNING.md` es el tablero operativo de alineación del proyecto.**

* **El Director Creativo** define qué se quiere construir, custodia la visión y aprueba los objetivos.
* **El Project Manager** estructura, prioriza, analiza dependencias y formula los objetivos.
* **El Developer** implementa exclusivamente los objetivos autorizados y reporta resultados.
* **Ningún objetivo futuro debe comenzar automáticamente porque el anterior haya terminado.**

---

## 14. Relación Objetivo ↔ Versión

* Un objetivo técnico individual (`WHO-xxx`) y una versión de producto **NO son necesariamente equivalentes**.
* Múltiples objetivos completados pueden coexistir dentro de una misma versión (`0.0.1`).
* La versión solo se incrementará cuando el conjunto de cambios configure formalmente una nueva iteración o hito según las reglas de [docs/VERSIONING.md](VERSIONING.md).

---

## 15. Reglas de Mantenimiento de Este Documento

1. `docs/PLANNING.md` debe mantenerse rigurosamente actualizado al completar o cambiar el estado de cualquier objetivo.
2. El estado reflejado debe coincidir con el estado real y verificable del repositorio.
3. Jamás marcar un objetivo como `APROBADO` sin la autorización explícita del Director.
4. No marcar una versión como publicada sin la existencia y comprobación del artefacto.
5. No inventar builds de APK/AAB ficticias.
6. No inventar decisiones de producto no tomadas.
7. No eliminar el historial ni el registro de cambios de planificación.
8. Si surge una contradicción entre documentos, detener el desarrollo y reportar el conflicto.
9. La fuente oficial de decisiones es [docs/DECISIONS.md](DECISIONS.md).
10. La fuente oficial de reglas de versión es [docs/VERSIONING.md](VERSIONING.md).
11. La fuente oficial del roadmap estratégico es [docs/ROADMAP.md](ROADMAP.md).
12. El procedimiento operativo de ejecución es [docs/WORKFLOW.md](WORKFLOW.md).
