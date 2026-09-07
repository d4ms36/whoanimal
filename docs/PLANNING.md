# Plan Maestro de Desarrollo (Master Development Planning) — WHO Animal

**Documento:** `docs/PLANNING.md`  
**Propósito:** Tablero operativo central de alineación entre Director Creativo, Project Manager y Developer Principal.  
**Estado:** Activo y dinámico  
**Última actualización:** 2026-09-06 (Actualización de ciclo WHO-006B)

---

## 1. Estado Actual del Proyecto

| Campo | Estado |
| :--- | :--- |
| **Proyecto** | WHO Animal |
| **Fase actual** | Alpha |
| **Versión actual** | `0.0.1` |
| **Estado** | Foundation |
| **Objetivo activo** | Ninguno (Ciclo WHO-011C completado; listo para revisión de PM) |
| **Último objetivo completado** | `WHO-011C` — Implementación del Catálogo Zoológico Oficial Inicial |
| **Próximo objetivo propuesto** | Pendiente de aprobación |
| **Bloqueos** | Ninguno |
| **Decisiones pendientes** | `DEC-009` a `DEC-012`, `DEC-020` a `DEC-025`, `DEC-037-PENDING` |
| **Último commit** | `1fa87b6` |
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
| **WHO-005B-C**| Resolución A: Semántica de `population_at_issuance` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-D**| Auditoría y resolución de `verification` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-D.1**| Canonicalización de `verification_status` y `null` vs `UNVERIFIED` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005C.1**| Definición conceptual de `sex` en Animal/Capture | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-E**| Auditoría semántica final de la estructura `Card` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-005B-E.1**| Cierre semántico de `rank` y `rarity` | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006A**| Definición formal de tipos y obligatoriedad de los 19 campos de Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006A.1**| Corrección del contrato de obligatoriedad, nullability y defaults de Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006B**| Implementación formal del modelo Card (19 campos canónicos) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006B.1**| Corrección de contrato técnico del modelo Card | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006C**| Formalizar dominio Capture/Specimen (DEC-036) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006C.2**| Cierre técnico de Capture/Specimen y sex (DEC-039) | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-006D**| Formalizar Modelo de Monetización Gratuito + Publicidad | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-007** | Banco de Datos Inicial de Fauna (Semilla Educativa) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-008A** | Formalizar Historia Personal de la Carta (Personal Lore) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-008B** | Definir reglas de edición de la Historia Personal (Lore) | `COMPLETADO` | Media | 0.0.1 | Sí |
| **WHO-010** | Conexión y publicación inicial del repositorio | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-010A** | Auditoría y Sincronización Integral de Documentación | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011A** | Infraestructura del Banco de Datos Zoológico | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011B** | Implementación del Índice Taxonómico Oficial | `COMPLETADO` | Alta | 0.0.1 | Sí |
| **WHO-011C** | Implementación del Catálogo Zoológico Oficial Inicial | `COMPLETADO` | Alta | 0.0.1 | Sí |

---

## 4. Objetivo Activo

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│                           OBJETIVO ACTIVO ACTUAL                            │
├──────────────────┬──────────────────────────────────────────────────────────┤
│ ID               │ Ninguno                                                  │
│ Nombre           │ N/A                                                      │
│ Propósito        │ N/A                                                      │
│ Estado           │ N/A                                                      │
│ Versión Asociada │ N/A                                                      │
│ Requisitos       │ N/A                                                      │
│ Responsable      │ N/A                                                      │
└──────────────────┴──────────────────────────────────────────────────────────┘
```

* **Criterios de Aceptación:** N/A
* **Archivos Afectados:** N/A
* **Dependencias:** N/A
* **Bloqueos:** Ninguno.
* **Resultado Esperado:** Esperando aprobación formal del Director o Project Manager para iniciar un nuevo objetivo.

---

## 5. Próximos Objetivos

Ordenados por prioridad técnica y estratégica.

| ID | Nombre | Propósito | Prioridad | Dependencias | Estado | Req. Aprobación Director |
| :--- | :--- | :--- | :---: | :--- | :---: | :---: |
| **WHO-005B-B** | Validación de Esquemas y Serialización JSON | Importación/exportación estándar de cartas y perfiles compatibles con extensiones | Alta | WHO-002, WHO-005B-A | `PROPUESTO` | **Sí** |
| **WHO-011** | Evaluación y Prototipo de Ingesta Taxonómica | Conector experimental con APIs de biodiversidad (GBIF/iNat) | Media | WHO-005B-B, DEC-009 | `PROPUESTO` | **Sí** |
| **WHO-012** | Prototipo del Servicio de Generación de Cartas | Implementación del ensamblador en base al protocolo | Media | WHO-005B-B | `PROPUESTO` | **Sí** |
| **WHO-013** | Prototipo del Motor de Identificación por Visión | Implementación experimental de `IdentificationService` | Alta | WHO-005B-B, DEC-009, DEC-021 | `PROPUESTO` | **Sí** |
| **WHO-014** | Motor de Persistencia y Álbum de Colección | Almacenamiento local para inventario y álbum | Media | WHO-005B-B | `PROPUESTO` | **Sí** |
| **WHO-015** | Interfaz Gráfica / Prototipo Cliente Móvil | Primer frontend visual interactivo para volteo de cartas | Alta | WHO-004, DEC-012 | `PROPUESTO` | **Sí** |

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
| **WHO-005B-C**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-D**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-D.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005C.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-E**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-005B-E.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006A**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006A.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006B**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006B.1**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006C**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006C.2**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-006D**| `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-007** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-008A** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-008B** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-010** | `APROBADO` | `VALIDADO` | `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-010A** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011A** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011B** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |
| **WHO-011C** | `APROBADO` | `VALIDADO`| `COMPLETADO` | `APPROVED_COMPLETE` |

> **Regla:** El Developer no puede auto-aprobar objetivos. La autorización debe ser explícita por parte del Director Creativo y estructurada por el Project Manager.

---

## 7. Decisiones que Necesitamos Tomar

Vista operativa de las decisiones pendientes documentadas oficialmente en [docs/DECISIONS.md](DECISIONS.md):

| ID | Decisión | Impacto | ¿Bloquea desarrollo inmediato? | Estado Oficial |
| :--- | :--- | :---: | :---: | :---: |
| **DEC-009** | Motor definitivo de identificación visual (on-device vs. API vs. nube) | Alto | Sí (bloquea WHO-013) | `PENDING` |
| **DEC-011** | Convención definitiva de identificadores `card_id` y códigos de colección | Medio | No (temporal en v0.1) | `PENDING` |
| **DEC-012** | Tecnología cliente definitiva para la app de usuario (Flutter / nativo) | Alto | Sí (bloquea WHO-015) | `PENDING` |
| **DEC-020** | Diseño definitivo del esquema JSON de datos (`Animal` y `Card`) | Alto | Sí (requerido para WHO-005B-B)| `PENDING` |
| **DEC-021** | Umbrales definitivos de confianza en la identificación por visión/cámara | Medio | Sí (para WHO-013) | `PENDING` |
| **DEC-022** | Algoritmo matemático y curvas de probabilidad para rareza dinámica | Alto | No (en fase documental) | `PENDING` |
| **DEC-023** | Reglas anti-abuso para validación de observaciones y población | Alto | No (en fase documental) | `PENDING` |
| **DEC-024** | Sistema definitivo de autenticación y verificación de cartas | Medio | No (en fase documental) | `PENDING` |
| **DEC-025** | Estrategia de compatibilidad y migración entre versiones del esquema JSON | Medio | No (definible en WHO-005B-B)| `PENDING` |
| **DEC-037** | Sistema definitivo de progresión, niveles y mecánicas de `rank` | Medio | No (en fase documental) | `PENDING` |

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
| **2026-09-06** | Resolución A: Semántica formal y alcance de `population_at_issuance` (DEC-033) | Eliminar ambigüedad entre población zoológica y emisión de cartas en la colección | Director / PM (`WHO-005B-C`) |
| **2026-09-06** | Auditoría y resolución de `verification` y estados de autenticación (DEC-034) | Formalizar verification_status como CURRENT STATE desacoplado de la identidad | Director / PM (`WHO-005B-D`) |
| **2026-09-06** | Canonicalización de `verification_status` y semántica de `null` vs `UNVERIFIED` (DEC-035) | Establecer UNVERIFIED como estado inicial y null exclusivo para compatibilidad histórica | Director / PM (`WHO-005B-D.1`) |
| **2026-09-06** | Definición conceptual de `sex` en Animal/Capture (DEC-036) | Atribuir sexo biológico a Capture/Specimen y desacoplarlo del modelo zoológico Animal | Director / PM (`WHO-005C.1`) |
| **2026-09-06** | Auditoría semántica final de la estructura `Card` (19 campos canónicos) | Consolidación y cierre de los 19 campos y 6 módulos funcionales antes de la serialización | Director / PM (`WHO-005B-E`) |
| **2026-09-06** | Cierre semántico de `rank` y `rarity` (ortogonalidad y ejemplos no contractuales) | Delimitar estrictamente rank (mutable/progresión) y rarity (inmutable/emisión) | Director / PM (`WHO-005B-E.1`) |
| **2026-09-06** | Definición formal de tipos, obligatoriedad y nullability de los 19 campos de Card | Establecer contrato tipológico vinculante previo a la implementación de esquemas | Director / PM (`WHO-006A`) |
| **2026-09-06** | Corrección del contrato de obligatoriedad, nullability y defaults de Card (WHO-006A.1) | Eliminar defaults no aprobados, formalizar Optional vs Nullable y proteger origen histórico de display_location | Director / PM (`WHO-006A.1`) |
| **2026-09-06** | Implementación formal del modelo de dominio `Card` (19 campos canónicos) | Modelado y validación técnica según contrato WHO-006A.1 (WHO-006B) | Director / PM (`WHO-006B`) |
| **2026-09-06** | Corrección de contrato técnico del modelo Card (WHO-006B.1) | Alineación estricta UUIDv4, ausencia vs null en edition, artwork tipo abierto, rank int/str y 30 tests unitarios | Developer (`WHO-006B.1`) |
| **2026-09-06** | Formalización del dominio Capture/Specimen (WHO-006C) | Entidad Capture mínima con capture_id UUIDv4 y sex (DEC-036), frontera ontológica Animal ≠ Capture ≠ Card y 44 tests | Developer (`WHO-006C`) |
| **2026-09-06** | Formalización del Modelo de Monetización (WHO-006D) | Documentar modelo gratuito y uso de publicidad externa como infraestructura, protegiendo datos biológicos (DEC-038) | Developer (`WHO-006D`) |
| **2026-09-06** | Corrección de Capture.sex (WHO-006C.2) | Implementación de `sex` como OPTIONAL y NULLABLE sin valor default (DEC-039) y actualización de tests y documentación | Developer (`WHO-006C.2`) |
| **2026-09-06** | Creación del modelo formal AnimalProfile (WHO-007) | Implementación de `AnimalProfile` como fuente única de verdad para la especie, separado de `Capture` y `Card`. | Developer (`WHO-007`) |
| **2026-09-06** | Formalización Historia Personal (WHO-008A) | Formalización documental del Lore como Historia Personal escrita por el usuario y asociada a una carta. | Developer (`WHO-008A`) |
| **2026-09-06** | Reglas Edición Historia Personal (WHO-008B) | Formalización documental de los límites de edición del Lore: 3 ediciones por cuenta y solicitud oficial. | Developer (`WHO-008B`) |
| **2026-09-06** | Conexión y publicación inicial (WHO-010) | Configuración de infraestructura Git/GitHub y sincronización. | Developer (`WHO-010`) |
| **2026-09-06** | Auditoría y Sincronización Integral (WHO-010A) | Consolidación y validación de la documentación del proyecto, corrección de IDs duplicados. | Developer (`WHO-010A`) |
| **2026-09-06** | Infraestructura del Banco de Datos Zoológico (WHO-011A) | Creación de esquemas, JSON validador y estructura de directorios. | Developer (`WHO-011A`) |
| **2026-09-06** | Índice Taxonómico Oficial (WHO-011B) | Implementación de `TaxonomyIndex` para validar rutas biológicas sin BD. | Developer (`WHO-011B`) |
| **2026-09-06** | Catálogo Zoológico Oficial Inicial (WHO-011C) | Implementación de las primeras 28 especies JSON validadas estrictamente. | Developer (`WHO-011C`) |

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
