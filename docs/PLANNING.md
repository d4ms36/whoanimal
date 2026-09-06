# Plan Maestro de Desarrollo (Master Development Planning) — WHO Animal

**Documento:** `docs/PLANNING.md`  
**Propósito:** Tablero operativo central de alineación entre Director Creativo, Project Manager y Developer Principal.  
**Estado:** Activo y dinámico  
**Última actualización:** 2026-09-06

---

## 1. Estado Actual del Proyecto

| Campo | Estado |
| :--- | :--- |
| **Proyecto** | WHO Animal |
| **Fase actual** | Alpha |
| **Versión actual** | `0.0.1` |
| **Estado** | Foundation |
| **Objetivo activo** | `WHO-004` — Plan Maestro de Desarrollo y Alineación por Versión |
| **Último objetivo completado** | `WHO-003` — Gobernanza, Memoria del Proyecto y Versionado |
| **Próximo objetivo propuesto** | `WHO-005` — Validación de Esquemas y Serialización JSON |
| **Bloqueos** | Ninguno |
| **Decisiones pendientes** | `DEC-009`, `DEC-010`, `DEC-011`, `DEC-012` |
| **Último commit** | `66d6c67` |
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
| **WHO-004** | Plan Maestro de Desarrollo y Alineación por Versión | `EN DESARROLLO` | Alta | 0.0.1 | Sí |
| **WHO-005** | Validación de Esquemas y Serialización JSON | `PROPUESTO` | Alta | 0.0.1 | No |
| **WHO-006** | Banco de Datos Inicial de Fauna (Semilla Educativa) | `PROPUESTO` | Media | 0.0.1 | No |

---

## 4. Objetivo Activo

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│                           OBJETIVO ACTIVO ACTUAL                            │
├──────────────────┬──────────────────────────────────────────────────────────┤
│ ID               │ WHO-004                                                  │
│ Nombre           │ Plan Maestro de Desarrollo y Alineación por Versión       │
│ Propósito        │ Crear el tablero operativo central docs/PLANNING.md      │
│ Estado           │ IN_PROGRESS                                              │
│ Versión Asociada │ 0.0.1                                                    │
│ Requisitos       │ WHO-001, WHO-002 y WHO-003 completados y aprobados        │
│ Responsable      │ Developer Principal (Antigravity)                        │
└──────────────────┴──────────────────────────────────────────────────────────┘
```

* **Criterios de Aceptación:**
  1. `docs/PLANNING.md` creado con todas las secciones estructurales requeridas.
  2. Coherencia total con `AGENTS.md`, `GOVERNANCE.md`, `WORKFLOW.md`, `DECISIONS.md`, `VERSIONING.md` y `ROADMAP.md`.
  3. Inclusión de `docs/PLANNING.md` en el índice de [README.md](../README.md).
  4. Ejecución y paso del 100% de los tests unitarios.
  5. Detención estricta al finalizar la tarea sin iniciar automáticamente `WHO-005`.
* **Archivos Afectados:** `docs/PLANNING.md`, `README.md`, `docs/WORKFLOW.md`, `docs/ROADMAP.md`.
* **Dependencias:** Ninguna externa.
* **Bloqueos:** Ninguno.
* **Resultado Esperado:** Tablero de alineación maestro publicado y enlazado, commit semántico creado y reporte final entregado al PM y Director.

---

## 5. Próximos Objetivos

Ordenados por prioridad técnica y estratégica.

| ID | Nombre | Propósito | Prioridad | Dependencias | Estado | Req. Aprobación Director |
| :--- | :--- | :--- | :---: | :--- | :---: | :---: |
| **WHO-005** | Validación de Esquemas y Serialización JSON | Garantizar importación/exportación estándar de cartas y perfiles | Alta | WHO-002 | `PROPUESTO` | **Sí** |
| **WHO-006** | Banco de Datos Inicial de Fauna (Semilla) | Dotar al sistema de especímenes reales con datos y Lore | Media | WHO-005 | `PROPUESTO` | **Sí** |
| **WHO-007** | Prototipo de Ingesta Taxonómica | Conector experimental con APIs de biodiversidad (GBIF/iNat) | Media | WHO-005, DEC-009 | `PROPUESTO` | **Sí** |
| **WHO-008** | Prototipo del Motor de Identificación por Visión | Implementación experimental de `IdentificationService` | Alta | WHO-005, DEC-009 | `PROPUESTO` | **Sí** |
| **WHO-009** | Motor de Persistencia y Álbum de Colección | Almacenamiento local para inventario y álbum | Media | WHO-005 | `PROPUESTO` | **Sí** |
| **WHO-010** | Interfaz Gráfica / Prototipo Cliente Móvil | Primer frontend visual interactivo para volteo de cartas | Alta | WHO-004, DEC-012 | `PROPUESTO` | **Sí** |

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
| **WHO-004** | `APROBADO` | `PLANIFICADO` | `EN CURSO` | `IN_PROGRESS` |
| **WHO-005** | *PENDIENTE* | `PLANIFICADO` | `EN ESPERA` | `PROPOSED` |
| **WHO-006** | *PENDIENTE* | *EN EVALUACIÓN* | `EN ESPERA` | `PROPOSED` |

> **Regla:** El Developer no puede auto-aprobar objetivos. La autorización debe ser explícita por parte del Director Creativo y estructurada por el Project Manager.

---

## 7. Decisiones que Necesitamos Tomar

Vista operativa de las decisiones pendientes documentadas oficialmente en [docs/DECISIONS.md](DECISIONS.md):

| ID | Decisión | Impacto | ¿Bloquea desarrollo inmediato? | Estado Oficial |
| :--- | :--- | :---: | :---: | :---: |
| **DEC-009** | Motor definitivo de identificación visual (on-device vs. API vs. nube) | Alto | Sí (bloquea WHO-008) | `PENDING` |
| **DEC-010** | Estilo y universo del Lore (mitología única vs. folclore regional) | Medio | No (en fase de fundación) | `PENDING` |
| **DEC-011** | Formato definitivo de códigos de colección de carta (`WA-MAM-0001`) | Medio | No | `PENDING` |
| **DEC-012** | Tecnología cliente definitiva para la app de usuario (Flutter / nativo) | Alto | Sí (bloquea WHO-010) | `PENDING` |

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
| **0.0.1** | Alpha | Foundation | WHO-001 a WHO-004 | **1\*** | Internal / Foundation |

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
5. Registrar el cambio en `docs/RELEASES.md`.
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
* ❌ **Sistemas de combate, duelos o estadísticas RPG de juego:** No aprobados.
* ❌ **Economía, compras in-app o monetización:** Prohibido en esta etapa.
* ❌ **Publicación en Google Play Store:** No aplicable en Fase 0.
* ❌ **Generador automático o asistido de Lore por LLM en runtime:** No autorizado.

---

## 12. Historial de Planificación (Changelog)

| Fecha | Cambio Registrado | Motivo | Aprobado por |
| :--- | :--- | :--- | :---: |
| **2026-09-06** | Creación del Plan Maestro (`docs/PLANNING.md`) | Establecer tablero operativo central de alineación Director/PM/Dev | Director / PM (`WHO-004`) |

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
