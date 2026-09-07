# Flujo de Trabajo Basado en Objetivos (Workflow) â€” WHO Animal

**Documento:** `docs/WORKFLOW.md`  
**PropÃ³sito:** Definir el ciclo de vida, la estructura estÃ¡ndar y las reglas de transiciÃ³n para cada unidad de trabajo en el proyecto.

---

## 1. FilosofÃ­a: Desarrollo Orientado a Objetivos Cerrados

En WHO Animal, el trabajo tÃ©cnico no se ejecuta de forma dispersa ni en cadenas continuas desatendidas. Toda labor se estructura en **Objetivos AtÃ³micos y Trazables** (`WHO-xxx`).

> **Regla Cardinal:**  
> **El Developer NO debe continuar automÃ¡ticamente hacia objetivos futuros solo porque terminÃ³ el actual.**  
> Cada objetivo debe cerrarse, verificarse y someterse a revisiÃ³n antes de autorizar el inicio del siguiente.

---

## 2. Estados de un Objetivo (Lifecycle States)

```mermaid
stateDiagram-v2
    [*] --> PROPOSED
    PROPOSED --> PLANNED: PM estructura y dimensiona
    PLANNED --> APPROVED: Director aprueba alcance
    APPROVED --> IN_PROGRESS: Developer inicia tarea
    IN_PROGRESS --> REVIEW: Developer entrega reporte + tests
    REVIEW --> APPROVED_COMPLETE: Director y PM validan
    REVIEW --> IN_PROGRESS: Ajustes solicitados
    PROPOSED --> REJECTED: Descartado
    PLANNED --> REJECTED: Descartado
    IN_PROGRESS --> BLOCKED: Conflicto o impedimento
    BLOCKED --> IN_PROGRESS: Desbloqueado por PM/Director
    APPROVED_COMPLETE --> [*]
```

* **`PROPOSED`:** Idea o necesidad inicial planteada por el Director o el PM.
* **`PLANNED`:** Objetivo analizado, desglosado con alcance tÃ©cnico y criterios de aceptaciÃ³n definidos por el PM.
* **`APPROVED`:** Autorizado oficialmente por el Director para su ejecuciÃ³n inmediata.
* **`IN_PROGRESS`:** En desarrollo activo por parte del Developer.
* **`REVIEW`:** ImplementaciÃ³n completada con tests; en revisiÃ³n tÃ©cnica por el PM y Director.
* **`APPROVED_COMPLETE`:** Verificado satisfactoriamente, commiteado y formalmente cerrado.
* **`REJECTED`:** Propuesta descartada o cancelada.
* **`BLOCKED`:** Detenido por contradicciones, dependencias externas o decisiones pendientes del Director.

---

## 3. Plantilla EstÃ¡ndar de un Objetivo (`WHO-xxx`)

Cada tarea u objetivo debe documentarse utilizando la siguiente ficha estructural:

```markdown
### [ID]: [Nombre del Objetivo]

* **PropÃ³sito:** Â¿QuÃ© valor aporta o quÃ© problema resuelve este objetivo?
* **Estado:** [PROPOSED | PLANNED | APPROVED | IN_PROGRESS | REVIEW | APPROVED_COMPLETE | REJECTED | BLOCKED]
* **Alcance:** DelimitaciÃ³n estricta de lo que se incluye y lo que queda excluido.
* **Requisitos:** Condiciones previas necesarias para comenzar.
* **Archivos Afectados:** Lista explÃ­cita de archivos creados, modificados o eliminados.
* **Criterios de AceptaciÃ³n:** Condiciones verificables que determinan el Ã©xito del objetivo.
* **Pruebas:** Estrategia de testing (unitario, integraciÃ³n o validaciÃ³n manual) ejecutada.
* **Resultado:** Resumen de la ejecuciÃ³n tÃ©cnica e incidencias resueltas.
* **Commit Asociado:** Hash y mensaje semÃ¡ntico del commit en Git.
* **VersiÃ³n Asociada:** VersiÃ³n de proyecto vinculada (ej. `0.0.1`).
```

---

## 4. Registro HistÃ³rico de Objetivos Iniciales

### WHO-001: ExploraciÃ³n Inicial y EvaluaciÃ³n de Requisitos
* **PropÃ³sito:** Revisar el estado cero del repositorio, confirmar detenciÃ³n de desarrollo y verificar que no haya cÃ³digo prematuro.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** N/A (Fase de inspecciÃ³n previa a Git).
* **VersiÃ³n:** `0.0.1-pre`

### WHO-002: CreaciÃ³n de la Estructura Documental y FundaciÃ³n de CÃ³digo
* **PropÃ³sito:** Establecer la primera arquitectura modular en Python, contratos de cartas y documentaciÃ³n conceptual inicial.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `97a2d32` (*feat(init): base architecture, product documentation, and domain models v0.1*)
* **VersiÃ³n:** `0.0.1`

### WHO-003: Gobernanza, Memoria del Proyecto y Versionado
* **PropÃ³sito:** Establecer el marco de gobernanza, memoria oficial, reglas para agentes, sistema de versionado semÃ¡ntico/Android y roadmap estructurado.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `66d6c67` (*docs(governance): establish project memory, workflow and versioning*)
* **VersiÃ³n:** `0.0.1`

### WHO-004: Plan Maestro de Desarrollo y AlineaciÃ³n por VersiÃ³n
* **PropÃ³sito:** Crear el tablero operativo central `docs/PLANNING.md` para alinear en tiempo real al Director, Project Manager y Developer.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `4e90895` (*docs(planning): establish master development planning*)
* **VersiÃ³n:** `0.0.1`

### WHO-005A: IncorporaciÃ³n de Nuevas Decisiones al Contexto del Proyecto
* **PropÃ³sito:** Incorporar formalmente a la memoria y gobernanza las decisiones sobre entidad Animal vs. Carta, inmutabilidad de cartas, rareza dinÃ¡mica ligada a poblaciÃ³n, serial visual de autenticaciÃ³n y criterios de validaciÃ³n funcional.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `134637c` (*docs(context): incorporate animal card and rarity design principles*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-A: ActualizaciÃ³n Documental de Capacidades Futuras
* **PropÃ³sito:** Registrar en la memoria y gobernanza las decisiones de diseÃ±o para extensiones futuras (PVP, comercio/intercambio, artwork Ãºnico/red de ilustradores y privacidad GPS vs. ubicaciÃ³n generalizada) asegurando que la arquitectura de esquemas no cierre estas capacidades.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `172203d` (*docs(extensions): document future capabilities for pvp trading custom artwork and privacy*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-C: ResoluciÃ³n A: SemÃ¡ntica de population_at_issuance
* **PropÃ³sito:** Formalizar la definiciÃ³n canÃ³nica, alcance (`ANIMAL/SPECIES`), inmutabilidad y relaciÃ³n con rareza de `population_at_issuance` (DEC-033), desacoplÃ¡ndolo de censos biolÃ³gicos reales y mÃ©tricas de plataforma.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `507e66e` (*docs(card): define population at issuance semantics*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-D: AuditorÃ­a y ResoluciÃ³n de verification
* **PropÃ³sito:** Resolver la semÃ¡ntica de `verification` (DEC-034), formalizando `verification_status` como campo mutable de estado actual (`UNVERIFIED`, `VERIFIED`, `FLAGGED`, `REVOKED`), desacoplÃ¡ndolo del ancla inmutable `serial` y de la identidad histÃ³rica.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `6c2b8b1` (*docs(card): define verification semantics*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-D.1: CanonicalizaciÃ³n de verification_status y SemÃ¡ntica null vs UNVERIFIED
* **PropÃ³sito:** Formalizar `verification_status` como nombre canÃ³nico oficial (DEC-035), resolver la ambigÃ¼edad contractual fijando `UNVERIFIED` como estado inicial obligatorio de toda Card nueva bajo schema actual y reservar `null` exclusivamente para compatibilidad histÃ³rica con schemas previos.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `8f661d3` (*docs(card): clarify verification status semantics*)
* **VersiÃ³n:** `0.0.1`

### WHO-005C.1: DefiniciÃ³n Conceptual de sex en Animal/Capture
* **PropÃ³sito:** Formalizar la delimitaciÃ³n ontolÃ³gica de `sex` asignÃ¡ndolo a `Capture / Specimen` y excluyÃ©ndolo del modelo taxonÃ³mico `Animal` (DEC-036), definiendo los valores `MALE`, `FEMALE` y `UNKNOWN`, la regla anti-inferencia y la proyecciÃ³n visual en `Card`.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `a66f069` (*docs(domain): define biological sex at capture level*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-E: AuditorÃ­a SemÃ¡ntica Final de la Estructura Card
* **PropÃ³sito:** Realizar una auditorÃ­a semÃ¡ntica exhaustiva de los 19 campos conceptuales de la entidad `Card` distribuidos en 6 mÃ³dulos funcionales (`identity`, `issuance`, `provenance`, `presentation`, `ownership`, `authentication`), verificando inmutabilidad, mutabilidad, lÃ­mites ontolÃ³gicos y ausencia de ambigÃ¼edades previo a la implementaciÃ³n de esquemas JSON.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `e88e53f` (*docs(card): audit final card semantics*)
* **VersiÃ³n:** `0.0.1`

### WHO-005B-E.1: Cierre SemÃ¡ntico de rank y rarity
* **PropÃ³sito:** Corregir y precisar las definiciones de `rank` y `rarity`: establecer que las listas de tiers de rareza son ejemplos no contractuales (curvas pendientes en DEC-022-PENDING), delimitar `rank` a la progresiÃ³n/experiencia del usuario sin implicaciÃ³n biolÃ³gica ni de combate, y consagrar la ortogonalidad absoluta e independencia bidireccional entre ambos conceptos (`rank â‰  rarity`).
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `8844010` (*docs(card): clarify rank and rarity semantics*)
* **VersiÃ³n:** `0.0.1`

### WHO-006A: DefiniciÃ³n Formal de Tipos y Obligatoriedad de los 19 Campos de Card
* **PropÃ³sito:** Formalizar documentalmente el contrato tÃ©cnico de tipos, obligatoriedad (required/optional), nullability, valores por defecto e inmutabilidad de los 19 campos canÃ³nicos de `Card`, estableciendo las reglas de validaciÃ³n precisas sin inventar taxonomÃ­as de tiers ni mecÃ¡nicas de progresiÃ³n pendientes.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `f76fca2` (*docs(card): formalize field types and requiredness*)
* **VersiÃ³n:** `0.0.1`

### WHO-006A.1: CorrecciÃ³n del Contrato de Obligatoriedad, Nullability y Defaults de Card
* **PropÃ³sito:** Auditar y corregir el contrato formal de los 19 campos canÃ³nicos de `Card` eliminando defaults no aprobados (`schema_version`, `edition`, `generation`, `issued_at`, `rank`, `owner_id`, `display_location`, `artwork`), consagrando la regla fundamental de no inventar defaults tÃ©cnicos, diferenciando formalmente `Optional` (`Required = No`) de `Nullable`, protegiendo el origen histÃ³rico de `display_location` (`historical source = protected`, `presentation = evolvable`, sin GPS exacto) y manteniendo abiertas `DEC-022-PENDING` y `DEC-037-PENDING`.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `e05a902` (*docs(card): correct field defaults and nullability*)
* **VersiÃ³n:** `0.0.1`

### WHO-006B: ImplementaciÃ³n Formal del Modelo Card
* **PropÃ³sito:** Implementar el modelo de dominio `Card` correspondiente a los 19 campos canÃ³nicos respetando estrictamente el contrato formal aprobado en WHO-006A.1 (requiredness, nullability, tipos abiertos para rarity y rank, defaults contractuales Ãºnicos UNVERIFIED y [], inmutabilidad post-emisiÃ³n y validaciÃ³n tÃ©cnica sin dependencias externas).
* **Estado:** `NEEDS_CORRECTION`
* **Commit:** `a051d0c` (*feat(card): implement formal card domain model*)
* **VersiÃ³n:** `0.0.1`

### WHO-006B.1: CorrecciÃ³n de Contrato TÃ©cnico del Modelo Card
* **PropÃ³sito:** Corregir discrepancias tÃ©cnicas con el contrato formal: exigir estrictamente UUIDv4 en `card_id` y `capture_id` (rechazando UUIDv5/v1), formalizar `edition` como Optional no-nullable omitiÃ©ndolo de la serializaciÃ³n cuando estÃ¡ ausente y rechazando `None`, ampliar `artwork` a tipo abierto (objeto/dict, string o None), restringir `rank` a int o str sin inventar niveles, aÃ±adir protecciÃ³n razonable anti-GPS en `display_location` y validar el contrato con 30 tests unitarios pasando al 100%.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `018fc4e` (*fix(card): align domain model with card contract*)
* **VersiÃ³n:** `0.0.1`

### WHO-006C: Formalizar Dominio Capture/Specimen
* **PropÃ³sito:** Formalizar en el dominio la entidad `Capture / Specimen` (`capture_id` UUIDv4 estricto, `sex âˆˆ {MALE, FEMALE, UNKNOWN}` conforme a DEC-036), estableciendo la frontera ontolÃ³gica inquebrantable `Animal â‰  Capture â‰  Card`, consagrando que el sexo biolÃ³gico pertenece al individuo observado y no a la especie (`Animal`) ni es campo canÃ³nico de `Card`, con 44 tests unitarios pasando al 100%.
* **Estado:** `NEEDS_CORRECTION` (Subsumido por WHO-006C.2)
* **Commit:** `edad99b` (*feat(domain): introduce capture specimen model*)
* **VersiÃ³n:** `0.0.1`

### WHO-006C.2: Cierre tÃ©cnico de Capture/Specimen y sex
* **PropÃ³sito:** Corregir la cardinalidad y obligatoriedad de `Capture.sex` en base a la decisiÃ³n de dominio `DEC-039`, haciÃ©ndolo OPTIONAL, NULLABLE y sin valor default `UNKNOWN`, adaptando su constructuor, serializaciÃ³n, y actualizando la baterÃ­a de pruebas unitarias asociadas.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `fd96ae8` (*fix(domain): finalize capture sex contract*)
* **VersiÃ³n:** `0.0.1`

### WHO-006D: Formalizar Modelo de MonetizaciÃ³n Gratuito + Publicidad
* **PropÃ³sito:** Formalizar conceptualmente el modelo comercial inicial de WHO Animal como un producto 100% gratuito (Free-to-Play) en su nÃºcleo y soportado mediante publicidad externa (DEC-038). Establecer que la publicidad (ej. AdMob) opera estrictamente en infraestructura y aislar los datos biolÃ³gicos y la rareza histÃ³rica para prohibir expresamente la monetizaciÃ³n o alteraciÃ³n retroactiva de la verdad zoolÃ³gica o identidad de colecciÃ³n.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `73e1902` (*docs(monetization): define free product and advertising model*)
* **VersiÃ³n:** `0.0.1`

### WHO-007: Banco de Datos Inicial de Fauna (Semilla Educativa)
* **PropÃ³sito:** CreaciÃ³n e implementaciÃ³n formal del modelo de dominio `AnimalProfile` como fuente Ãºnica de verdad para la identidad zoolÃ³gica, estableciendo la frontera ontolÃ³gica inquebrantable frente a `Capture` y `Card`. Se definieron campos obligatorios (`animal_id`, `scientific_name`, `common_name`, `taxonomy`) y opcionales sin romper las reglas preexistentes.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `45df12a` (*feat(domain): implement formal AnimalProfile contract*)
* **VersiÃ³n:** `0.0.1`

### WHO-008A: Formalizar Historia Personal de la Carta (Personal Lore)
* **PropÃ³sito:** FormalizaciÃ³n documental del concepto de "Lore" como Historia Personal escrita por el usuario. Limita el contenido a 300 caracteres, lo asocia a una carta especÃ­fica (1 Card -> 0..1 Personal Story) y asegura que no interfiera con los 19 campos canÃ³nicos de la entidad Card. Quedan pendientes las mecÃ¡nicas de ediciÃ³n controlada.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `3f2b279` (*docs(WHO-008A): formalize personal card story*)
* **VersiÃ³n:** `0.0.1`

### WHO-008B: Definir reglas de ediciÃ³n de la Historia Personal (Lore)
* **PropÃ³sito:** Formalizar documentalmente las reglas de ediciÃ³n del Lore: lÃ­mite global de 3 ediciones por cuenta, la creaciÃ³n no consume ediciÃ³n, y solicitud oficial para cambios excepcionales (DEC-041).
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `7a32d78` (*docs(WHO-008B): define personal story edit rules*)
* **VersiÃ³n:** `0.0.1`

### WHO-010: ConexiÃ³n y publicaciÃ³n inicial del repositorio
* **PropÃ³sito:** Configurar la infraestructura Git conectando el repositorio local a GitHub y publicando el historial limpio.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (OperaciÃ³n de infraestructura, sin commit funcional adicional)
* **VersiÃ³n:** `0.0.1`

### WHO-010A: AuditorÃ­a y SincronizaciÃ³n Integral de DocumentaciÃ³n
* **PropÃ³sito:** Consolidar y auditar la documentaciÃ³n del proyecto para garantizar la alineaciÃ³n estricta de todos los documentos con las decisiones aprobadas hasta WHO-010.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `2472cc9` (*docs(WHO-010A): synchronize project planning and governance*)
* **VersiÃ³n:** `0.0.1`

### WHO-011A: Infraestructura del Banco de Datos Zoológico
* **Propósito:** Crear la estructura base y el validador JSON de las especies.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (Pendiente de commit) (*feat(dataset): implement zoological dataset infrastructure*)
* **Versión:** `0.0.1`


### WHO-011B: Implementación del Índice Taxonómico Oficial
* **Propósito:** Implementar el índice taxonómico oficial para organizar especies sin base de datos y validar rutas taxonómicas.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (Pendiente de commit) (*feat(taxonomy): implement official taxonomy index*)
* **Versión:** `0.0.1`


### WHO-011C: Implementación del Catálogo Zoológico Oficial Inicial
* **Propósito:** Construir y validar el primer lote de 28 especies para poblar el catálogo base.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (Pendiente de commit) (*feat(dataset): build official starter species catalog*)
* **Versión:** `0.0.1`


### WHO-011D: Enriquecimiento Científico del Catálogo Zoológico
* **Propósito:** Enriquecer las 28 especies del catálogo con información científica estable (habitat, diet, lifespan_years, size_cm, weight_kg, ctivity_cycle, 
ative_regions).
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (Pendiente de commit) (*feat(dataset): enrich official species catalog*)
* **Versión:** `0.0.1`


### WHO-012A: Implementación del Motor de Observaciones
* **Propósito:** Crear la entidad de dominio `Observation` como puente entre fotografía y `Capture`.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** (Pendiente de commit) (*feat(domain): implement observation pipeline foundation*)
* **Versión:** `0.0.1`


### WHO-012B: Implementación del Resultado de Identificación Zoológica
* **Propósito:** Crear la entidad de dominio `IdentificationResult` separando el valor de confianza de la decisión de aceptación.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `60b67bf`
* **Versión:** `0.0.1`


### WHO-012C: Formalizar la decisión explícita sobre un IdentificationResult
* **Propósito:** Formalizar la entidad de dominio `IdentificationDecision` para registrar la decisión explícita (`ACCEPTED`, `REJECTED`, `CANCELLED`) sobre un `IdentificationResult`, desacoplando la decisión de la confianza y validando el animal seleccionado.
* **Estado:** `APPROVED_COMPLETE`
* **Commit:** `045e3e6`
* **Versión:** `0.0.1`

