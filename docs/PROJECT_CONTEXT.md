# Memoria Oficial del Proyecto (Project Context) — WHO Animal

**Documento:** `docs/PROJECT_CONTEXT.md`  
**Propósito:** Memoria y contexto fundamental de WHO Animal para asegurar coherencia transversal a lo largo de todo el ciclo de vida del producto.  
**Última actualización:** 2026-09-07 (Rebaseline Oficial de Visión y Roadmap — WHO-DOC-001 / DEC-053)

---

## 1. ¿Qué es WHO Animal?

> **WHO Animal = Identificación de Animales + Cartas Coleccionables + Enciclopedia Personal + Juego Ligero + Capa Social.**
> 
> *"Descubre. Identifica. Colecciona."*

**WHO Animal** es una **experiencia interactiva integral** que conecta el mundo natural con el coleccionismo lúdico y el aprendizaje zoológico. Trasciende el concepto de una simple herramienta utilitaria de escaneo o identificación visual de especies: la identificación mediante cámara o imagen es la puerta de entrada a un universo estructurado en tres capas (*Core*, *Game*, *Social*) y diseñado como un producto **Free-to-Play por diseño**.

El ciclo conceptual oficial se estructura como:

```text
IDENTIFICAR → DESCUBRIR → OBTENER CARTA → EXPLORAR INFORMACIÓN → ESCRIBIR LORE → COLECCIONAR → SEGUIR DESCUBRIENDO
```

### Realidad Tecnológica Dual
* **Python (3.10+):** Dominio de referencia zoológico, servicios taxonómicos (`TaxonomyIndex`), validación de contratos de esquemas JSON y tooling de testing.
* **Android Nativo (Kotlin 2.0+):** Aplicación móvil interactiva para el usuario final, interfaz declarativa moderna con Jetpack Compose y Material 3, captura nativa con CameraX y persistencia local de alto rendimiento con Room / SQLite.

---

## 2. Las Tres Capas de Producto

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        WHO ANIMAL ECOSYSTEM                            │
├────────────────────────────────────────────────────────────────────────┤
│  CAPA SOCIAL                                                           │
│  • Perfiles públicos  • Amigos  • Regalos  • Intercambio (Trading)     │
│  • Colecciones compartidas  • UGC  • Moderación, Reporte y Bloqueo     │
├────────────────────────────────────────────────────────────────────────┤
│  CAPA GAME                                                             │
│  • Rarezas de colección  • Estadísticas de juego (HP/ATK/DEF/SPD...)   │
│  • Duelos PvP (equipos de 10)  • Progresión y Logros  • Moneda y Shop  │
│  • Temporadas y Eventos  • Desafíos de avistamiento                    │
├────────────────────────────────────────────────────────────────────────┤
│  CAPA CORE (Irrenunciable)                                             │
│  Capture → Observation → IdentificationResult → IdentificationDecision │
│       ↓                                                                │
│    Capture → Card → Review / Flip → Persistence → Collection (Baúl)    │
└────────────────────────────────────────────────────────────────────────┘
```

1. **CAPA CORE:** Núcleo irrenunciable. Si se retiran Game o Social, WHO Animal sigue siendo plenamente operativa como guía de campo y álbum coleccionable.
2. **CAPA GAME:** Mecánicas de juego ligero, rarezas de cartas, atributos lúdicos (HP/ATK/DEF/SPD/TYPE/SPECIAL), duelos PvP con mazos de 10 cartas configurables semanalmente, progresión, moneda gratuita y tienda cosmética.
3. **CAPA SOCIAL:** Amigos, regalos diarios, intercambio seguro preservando inmutable la identidad histórica original, vitrinas públicas y moderación activa.

---

## 3. Separación Ontológica Tripartita

Toda información asociada a un animal o a una carta en WHO Animal pertenece conceptualmente a uno de estos tres dominios estancos:

```text
┌────────────────────────────────────────────────────────────────────────┐
│                          WHO ANIMAL ECOSYSTEM                          │
├───────────────────┬───────────────────────────────┬────────────────────┤
│  INFORMACIÓN REAL │          EXPERIENCIA          │        LORE        │
│                   │                               │ (Historia Personal)│
├───────────────────┼───────────────────────────────┼────────────────────┤
│ • Nombre común    │ • Categoría de carta          │ • Experiencia del  │
│ • Nombre científ. │ • Habilidades de experiencia  │   usuario          │
│ • Taxonomía       │ • Características de juego    │ • Encuentro        │
│ • Hábitat y Dieta │ • Rareza de colección         │ • Relato personal  │
│ • Tamaño y Peso   │ • Generación (Genesis, Gen 1) │                    │
│ • Conducta        │ • Población al momento de     │                    │
│ • Curiosidades    │   emisión                     │                    │
│ • Protección      │ • Elementos de colección      │                    │
│ • Rareza natural  │ • Interfaz, álbum y volteo    │                    │
├───────────────────┼───────────────────────────────┼────────────────────┤
│  100% Verificable │     100% Estimulante          │   100% Personal    │
└───────────────────┴───────────────────────────────┴────────────────────┘
```

### Regla Absoluta contra Datos Científicos Inventados
* **La Información Real jamás se inventa.** No se admiten conjeturas en taxonomía, distribución, hábitats, medidas ni dietas.
* Si el sistema no dispone de un dato verídico contrastado, **debe consultarlo en una fuente válida o representarlo como desconocido (`null`)**, nunca rellenarlo con suposiciones.
* Si se desea incorporar contenido personal o narrativo sobre el encuentro, debe residir obligatoriamente en **EXPERIENCIA** o en **LORE** (Historia Personal asociada a la carta, escrita por el usuario).

---

## 4. Diferenciación Ontológica: Observation vs. IdentificationResult vs. IdentificationDecision vs. Animal vs. Capture vs. Carta

> **Flujo Ontológico de Ejecución:**
> 
> ```text
> Android
>    ↓
> Capture Image
>    ↓
> Observation
>    ↓
> IdentificationService
>    ↓
> IdentificationResult
>    ↓
> IdentificationDecision (ACCEPTED)
>    ↓
> Capture
>    ↓
> Card
>    ↓
> Collection
> ```

### Reglas de Separación Ontológica Estricta:
```text
Observation ≠ IdentificationResult
IdentificationResult ≠ IdentificationDecision
Capture ≠ Card
Animal ≠ Capture
Animal ≠ Card
```

* **Observation (`Observation`):** Representa una observación fotográfica efímera y pendiente de confirmación. Actúa como puente temporal entre una foto y una `Capture`. Su existencia **no** afecta a `AnimalProfile`, no acuña `Card` ni formaliza una `Capture` hasta ser aceptada explícitamente (DEC-043).
* **IdentificationResult (`IdentificationResult`):** Representa el resultado en bruto emitido por un motor de identificación sobre una `Observation` (DEC-044). Contiene la confianza (confidence) pero **no** representa la decisión de aceptación. Es una estructura de datos inmutable generada durante el procesamiento.
* **IdentificationDecision (`IdentificationDecision`):** Representa la decisión explícita (`ACCEPTED`, `REJECTED`, `CANCELLED`) tomada sobre un `IdentificationResult` (DEC-045). Desacopla de forma absoluta la confianza de la decisión (`confidence ≠ decision`) y valida estrictamente el `selected_animal_id` contra los candidatos en caso de ser aceptada. No genera automáticamente `Capture` ni `Card`.
* **Animal (`AnimalProfile`):** Representa la entidad biológica y taxonómica objetiva de la especie en la base de conocimiento de WHO Animal. Describe rasgos universales de la especie (taxonomía, hábitat, dieta, distribución física, esperanza de vida, tamaño, ciclo de actividad). Esta información se considera permanente y estática (ver **DEC-042**). Puede existir en el sistema sin necesidad de haber sido emitido aún en una carta para ningún usuario. **No contiene atributos particulares de individuos observados (ej. `sex ∉ Animal`)**.
* **Captura / Espécimen (`Capture / Specimen`):** Representa el registro concreto de un individuo animal observado, creado estrictamente a partir de una `IdentificationDecision` en estado `ACCEPTED` (DEC-046). Contiene de forma inmutable `capture_id`, `animal_id` (proveniente de `selected_animal_id`) e `identification_id`. **Aquí reside el sexo biológico del ejemplar observado (`sex ∈ Capture`, valores: `MALE`, `FEMALE`, `UNKNOWN`; ver DEC-036). Se trata de un dato opcional y nullable sin valor por defecto (DEC-039), diferenciando la imposibilidad de identificación (`UNKNOWN`) de la ausencia de dato (`null`)**. No genera automáticamente `Card`.
* **Carta (`AnimalCard`):** Representa un ejemplar coleccionable individual, acuñado y emitido en un momento histórico concreto para el álbum de un jugador, vinculado a un espécimen animal pero dotado de propiedades de colección, generación, rareza y autenticación propias. Puede proyectar datos de la captura (como el sexo del individuo observado) en su visualización, pero la fuente primaria de verdad es la captura.

---

## 4.1. Producto Funcional Alpha (Alpha 0.1) y Mínimo Funcional

El **Producto Funcional Alpha (Alpha 0.1)** formaliza el objetivo técnico y funcional prioritario de WHO Animal: construir un producto Android funcional y demostrable en el menor tiempo razonable, erradicando el *scope creep* sin imponer una rigidez excesiva.

### Definición Oficial de Alpha 0.1 (Objetivo Rector)
> *"Un usuario nuevo debe poder entrar a WHO Animal, fotografiar un animal, obtener una identificación, generar una carta, revisarla, guardarla en su colección, cerrar la aplicación, volver a abrirla y encontrar la carta nuevamente."*

### Golden Path Oficial de Alpha:
```text
LOGIN
  ↓
HOME
  ↓
CAPTURE
  ↓
CAMERA
  ↓
OBSERVATION
  ↓
IDENTIFICATION
  ↓
RESULT
  ↓
DECISION
  ↓
CAPTURE
  ↓
CARD
  ↓
REVIEW / EDIT
  ↓
SAVE
  ↓
STORAGE
```

### Flujo Alternativo (Gestión y Consulta de Colección):
```text
HOME
  ↓
STORAGE
  ↓
CONTAINER
  ↓
CARD
  ↓
VIEW / EDIT / DELETE
```

### Alcance Oficial Obligatorio de Alpha:

1. **Cuenta:**
   * Registro local de usuario.
   * Login básico.
   * Logout.
2. **Home:**
   * Menú simple de navegación.
   * Acceso principal destacado a **Capture**.
   * Acceso principal destacado a **Storage**.
3. **Capture:**
   * Solicitar y gestionar permisos de cámara en Android.
   * Abrir la cámara del dispositivo.
   * Tomar fotografía del espécimen.
   * Crear la entidad efímera `Observation` (DEC-043).
   * Ejecutar el servicio de identificación.
   * Mostrar el `IdentificationResult` al usuario.
   * Permitir decisión explícita: Aceptar (`ACCEPTED`) o Descartar (`REJECTED`/`CANCELLED`) (DEC-045).
   * Mostrar mensajes de error comprensibles cuando el proceso de captura o identificación falle.
4. **Card:**
   * Generación automática inmediata tras una decisión válida en estado `ACCEPTED` (que formaliza `Capture`, DEC-046).
   * Vista frontal de la carta (fotografía, marco, nombres, rareza inicial).
   * Acción y animación interactiva de volteo (*flip*).
   * Vista posterior con información científica contrastada del catálogo zoológico oficial y metadatos de captura.
   * Campos personales editables únicamente cuando estén permitidos por las reglas del dominio (Historia Personal / Lore asociada a la carta, hasta 300 caracteres, respetando el límite de 3 ediciones por cuenta, DEC-041).
   * Campos de dominio estrictamente no editables e inmutables post-emisión.
   * Posibilidad de descartar la carta antes de guardarla.
   * Posibilidad de guardar la carta en Storage.
5. **Location (Ubicación):**
   * Alpha trabaja exclusivamente con una **representación de ubicación general** (`display_location`) orientada a la experiencia del usuario y a la protección de fauna silvestre.
   * No se introduce almacenamiento innecesario de una dirección residencial exacta como requisito funcional.
   * La arquitectura mantiene preparada la separación desacoplada entre:
     ```text
     display_location    (pública/generalizada en la carta)
     precise_location    (privada/telemetría interna si futuras políticas lo requieren)
     ```
6. **Storage (Almacenamiento y Colección — Baúl):**
   * Colección de cartas local y persistente.
   * Estructura organizada en **10 containers**.
   * Capacidad de **30 espacios por container** (capacidad inicial de la fase Alpha/Foundation: 300 cartas). Se mantiene como línea base operativa de optimización local y **no constituye un límite conceptual permanente del producto final**, el cual contemplará ampliaciones en fases posteriores.
   * Selector ágil de container.
   * Indicador visual claro de ocupación por container (ej. `X / 30`).
   * Visualización del catálogo de cartas almacenadas.
   * Apertura individual de carta para inspección y volteo.
   * Eliminación de carta del container (con diálogo previo de confirmación).
   * **Garantía absoluta de persistencia:** Los datos de las cartas almacenadas permanecen íntegros tras cerrar y volver a abrir la aplicación.
7. **UX Mínima de Alpha:**
   * Pantalla de splash / entrada sencilla.
   * Estados visuales claros de carga (*loading states*).
   * Estado vacío (*empty state*) informativo y estético cuando Storage no tiene cartas.
   * Diálogo modal de confirmación antes de eliminar una carta.
   * Animación fluida y sencilla de volteo (*flip*) de carta.
   * Navegación clara con botones de retroceso (Back) y retorno a inicio (Home).
   * Mensajes de error claros, comprensibles y sin códigos crudos expuestos al usuario.
   * Diseño visual coherente, moderno y con micro-interacciones pulidas.

### Fuera del Alcance de Alpha (Exclusiones Explícitas):
Las siguientes capacidades **NO son requisitos para declarar Alpha funcional** ni deben bloquear su entrega:
* ❌ Comercio de cartas (Trading).
* ❌ Enfrentamientos lúdicos (PVP).
* ❌ Mercado interno (Marketplace).
* ❌ Economía completa in-app (monedas, gemas, tiendas).
* ❌ Infraestructura Cloud o backend distribuido.
* ❌ Sincronización multidispositivo en la nube.
* ❌ Cuentas avanzadas o perfiles remotos.
* ❌ Red social o interacción entre jugadores.
* ❌ Red de ilustradores colaboradores.
* ❌ Sistema completo de rarezas dinámicas con algoritmos matemáticos complejos.
* ❌ Gamificación profunda y progresiones avanzadas de maestría.
* ❌ Cobertura mundial completa de especies (el catálogo inicial de 28 especies validadas es suficiente).
* ❌ Inteligencia artificial perfecta de visión.
* ❌ Publicación pública comercial en Google Play Store.
* ❌ Sistema de monetización publicitario operativo como requisito obligatorio de Alpha.

> *Nota Arquitectónica:* La arquitectura modular prepara los puntos de extensión para estas capacidades futuras, pero ninguna de ellas puede actuar como cuello de botella o precondición para el cierre de Alpha.

---

## 5. Identificación Mediante Cámara y Visión Artificial

La identificación visual es una de las puertas de entrada fundamentales:
* **Estructura Conceptual del Resultado:**
  * Método de identificación empleado.
  * Predicción taxonómica sugerida.
  * Nivel de confianza categórico.
  * Valor cuantitativo de confianza (e.g. porcentaje o probabilidad).
* **Naturaleza de la Confianza:** La confianza refleja la certidumbre probabilística del modelo matemático de visión, **no una garantía absoluta de verdad zoológica**.
* **Comportamiento ante Incertidumbre:** El sistema debe contemplar flujos diferenciados para identificaciones dudosas o por debajo de los umbrales de seguridad pedagógica. *(Los umbrales matemáticos definitivos quedan pendientes de diseño).*

---

## 6. Identidad de la Carta, Inmutabilidad y Serial de Autenticación

Cada carta coleccionable posee una identidad inmutable e intransferible:
* **Atributos de Identidad:**
  * `card_id` único universal (identificador técnico de sistema/persistencia).
  * `specimen_number` (Specimen # legible y visible para el coleccionista, ej. `#0042`).
  * Relación con `animal_id`.
  * Edición (cuando aplique).
  * Rareza de colección asignada.
  * Marca temporal de emisión (timestamp).
  * Población registrada de la especie en la colección al momento de la emisión (`population_at_issuance`, ver DEC-033).
  * Generación histórica (ej. Genesis / Gen 1).
  * Información y anclaje de autenticación.

### Inmutabilidad Post-Emisión
* Una vez emitida una carta, **sus propiedades históricas fundamentales quedan estrictamente congeladas**.
* Si la cantidad acumulada de cartas emitidas de esa especie en WHO Animal aumenta de 50 a 5.000.000 en los años siguientes, la carta emitida en el registro 50 conserva perpetuamente su rareza original, su generación y su registro histórico inmutable (`population_at_issuance = 50`).
* **La colección de la comunidad evoluciona; la carta histórica no muta ni se devalúa retroactivamente.**
* Asimismo, **un cambio posterior de propietario (vía intercambio o comercio futuro) no altera jamás la identidad histórica original de la pieza**.

### Serial Visual de Autenticación y Estado de Verificación
* Cada carta incorpora visualmente un serial/código discreto en la parte inferior (`auth_serial`).
* **Aviso de Seguridad:** Un código visual impreso no constituye por sí mismo un sistema de autenticación seguro. La arquitectura vincula este serial como ancla de trazabilidad con registros verificables y un estado mutable de verificación oficial (`verification_status`, ver DEC-034 y DEC-035: `UNVERIFIED`, `VERIFIED`, `FLAGGED`, `REVOKED`), donde toda Card nueva bajo el schema vigente nace obligatoriamente en `UNVERIFIED` y el valor `null` queda reservado exclusivamente a compatibilidad histórica con schemas previos, sin mezclar la identidad histórica inmutable con la infraestructura externa de auditoría.

---

## 7. Criterios de Validación: Campos Obligatorios vs. Opcionales

Para evitar ambigüedades arquitectónicas, no se utiliza la regla de *"obligatorio es lo que no puede calcularse"*. La norma oficial es:

* **Campo Obligatorio:** Es obligatorio cuando la entidad **no puede considerarse válida ni funcionar correctamente sin ese dato** (sin importar si proviene de la cámara, IA, base de datos, backend, cálculo o entrada de usuario).
* **Campo Opcional:** Es opcional cuando **el sistema puede operar con normalidad aunque ese dato no se encuentre disponible** de inmediato.

---

## 8. Manejo de Información Desconocida: Error Crítico vs. Dato Nulo

Ante la ausencia de datos en el sistema, se aplica una distinción estricta:

```text
¿Falta información?
  ├── ¿Es crítica para la identidad, validez, emisión o autenticación?
  │     └── SÍ ──> ERROR CRÍTICO (Detener proceso. Prohibido crear entidad inválida).
  │
  └── ¿Es información complementaria (peso, curiosidad, detalle de distribución)?
        └── SÍ ──> DATO DESCONOCIDO (Representar como null/desconocido. Prohibido inventar).
```

---

## 9. Desacoplamiento de Rarezas: Biológica vs. Colección, y Ortogonalidad con `rank`

* **Rareza Biológica:** Refleja la abundancia poblacional, densidad y estado de conservación de una especie en el ecosistema natural real (`AnimalProfile.is_rare_species`).
* **Rareza de Colección (`rarity`):** Expresa el valor lúdico, la dificultad de adquisición y la exclusividad de una carta dentro del sistema de coleccionismo de WHO Animal. Es asignada al momento de emisión e inmutable de por vida. *(Nota: Categorías como Común, Rara o Épica son ejemplos ilustrativos no contractuales; la escala formal y curvas matemáticas permanecen pendientes en DEC-022-PENDING)*.
* **Efectos Visuales Cosméticos:** La rareza de colección puede inducir modificaciones visuales en el marco, color, brillo o efectos holográficos/foil de la carta, sin guardar relación con la abundancia biológica de la especie en la naturaleza.
* **Ejemplo ilustrativo:** Un perro doméstico (*Canis lupus familiaris*) es biológicamente muy común y abundante en el planeta; sin embargo, una carta de perro emitida en las primeras horas de vida de la aplicación puede ser un objeto de colección de rareza colosal (a modo de ejemplo ilustrativo: *Ultra Rare / Genesis*).
* **Ortogonalidad Estricta con `rank` (`DEC-032`):**
  $$\text{rank} \ne \text{rarity}$$
  `rarity` califica la emisión y colección (inmutable); `rank` califica la maestría y progresión del usuario en el descubrimiento (mutable). Un `rank` alto no implica `rarity` alta, ni una carta de alta `rarity` confiere automáticamente un `rank` avanzado. `rank` no representa edad, tamaño, calidad zoológica ni fuerza del animal real, y sus mecánicas de progresión/XP continúan pendientes de diseño.

---

## 10. Sistema de Rareza Dinámica y Generaciones Históricas (Concepto en Diseño)

* **Determinación en Emisión:** La rareza de colección se calcula y fija dinámicamente en el instante exacto en que la carta es acuñada/emitida, tomando como factor cuantitativo de entrada la cantidad acumulada de cartas válidas emitidas para esa especie (`population_at_issuance`, ver DEC-033).
* **Generaciones Históricas:** Se introduce el concepto de generaciones (ej. **Genesis / Generación 1**). Las primeras cartas emitidas retienen un prestigio y valor histórico permanente para los pioneros de la comunidad.
* **Ventana de Oportunidad de los Primeros Registros:** Los primeros registros de una especie (ej. primeros 100 avistamientos válidos) ofrecen una probabilidad significativamente más alta de obtener cartas raras o ultra-raras. Posteriormente, a medida que la especie se registra masivamente, las probabilidades de rareza alta disminuyen progresivamente. *(Las curvas matemáticas y fórmulas exactas quedan pendientes de simulación económica).*
* **Regla Anti-Abuso para el Conteo Poblacional:** La emisión válida se sustenta en **observaciones verificadas y distintas**, no en ráfagas de fotos de un mismo ejemplar o evento repetido. Queda pendiente diseñar la lógica que prevenga el spam o fraude de registros.

---

## 11. Versionado y Evolución del Esquema de Datos

* **Independencia de Versiones:** El esquema JSON de datos posee su propio versionado semántico independiente (`schema_version`), completamente aislado del `versionName` de la aplicación y del `versionCode` de Android:
  ```text
  WHO Animal App Version:  0.0.1
  Android versionCode:     1
  JSON Data Schema:        1.0
  ```
* **Principio de Compatibilidad no Destructiva:** El esquema de datos podrá evolucionar e incorporar nuevos atributos; sin embargo, **las cartas emitidas bajo versiones anteriores del esquema deben permanecer siempre válidas, legibles e interpretables** sin sufrir mutaciones destructivas.

---

## 12. Privacidad en la Captura: GPS Exacto vs. Ubicación Pública Generalizada

En alineación estricta con el principio **100% Pet Friendly** y la protección de datos personales:
* **Telemetría de Captura (Interna/Privada):** El sistema puede almacenar internamente las coordenadas geográficas exactas (latitud/longitud) para control de unicidad de registro, verificación contra fraude o bitácora personal del usuario.
* **Exposición en la Carta (Pública/Visual):** La carta **expone únicamente una ubicación geográfica generalizada** (ej. "Costa Rica", "Península Ibérica", "Selva Misionera, Argentina").
* **Motivo de Protección Animal:** Evitar la divulgación de ubicaciones geográficas precisas que puedan ser utilizadas por cazadores furtivos, traficantes o personas que perturben los nidos o madrigueras de especies amenazadas.

---

## 13. Hoja de Ruta Estratégica Oficial (7 Fases)

La planificación estratégica de WHO Animal se estructura oficialmente en siete fases consecutivas (DEC-053), garantizando foco operativo sin restringir la evolución del producto:

```text
1. FOUNDATION (Completada)
       ↓
2. ALPHA (Alpha Funcional 0.1 — En Curso)
       ↓
3. BETA (Estabilidad, UX y Compatibilidad)
       ↓
4. RELEASE 1.0 (Lanzamiento Público Comercial)
       ↓
5. RELEASE 1.x — GAME (PvP, Equipos de 10, Temporadas)
       ↓
6. RELEASE 2.x — SOCIAL (Amigos, Trading, Vitrinas, UGC)
       ↓
7. RELEASE 3.x — WORLD (Biomas Globales, Cloud Multi-Device)
```

* **1. FOUNDATION (Completada):** Modelos de dominio inmutables, catálogo de 28 especies JSON, arquitectura limpia, contratos de frontera y suite de pruebas.
* **2. ALPHA (Golden Path Completado):** Core Loop end-to-end (`Login → Home → Capture → Camera → Observation → Identify → Card → Flip → Save → Baúl → Reopen`). Prioridad: estabilidad local y empaquetado de release v0.1.0-alpha.
* **3. BETA:** Pulido de UX, compatibilidad de dispositivos reales con CameraX, accesibilidad, internacionalización de interfaz, pre-AdService y distribución controlada vía Google Play Internal Testing.
* **4. RELEASE 1.0:** Lanzamiento público en Google Play Store con Core Loop completo, enciclopedia zoológica, Baúl personal, temas básicos, moneda gratuita, tienda cosmética y publicidad desacoplada (`AdService`). El PvP no bloquea Release 1.0.
* **5. RELEASE 1.x (GAME):** Capa Game: duelos PvP sencillos tipo RPG de cartas, equipos activos de 10 cartas configurables semanalmente, temporadas y logros.
* **6. RELEASE 2.x (SOCIAL):** Capa Social: amigos, regalos, intercambio (*trading* con metadatos históricos congelados), colecciones públicas, contenido generado por usuario (*Lore*) y herramientas de moderación.
* **7. RELEASE 3.x (WORLD):** Cobertura zoológica global, sincronización en la nube multidispositivo, eventos migratorios reales y experiencias de exploración avanzada.

---

## 14. Modelo Comercial: Free-to-Play por Diseño y Publicidad Desacoplada

En alineación formal con las decisiones **DEC-038** y **DEC-053**, el modelo comercial se rige por los siguientes principios normativos:

* **Núcleo 100% Gratuito (Free-to-Play):** WHO Animal es un producto global. Las funcionalidades fundamentales (identificar, aprender sobre fauna, acuñar cartas, explorar enciclopedia y coleccionar en el Baúl) son y serán 100% gratuitas de por vida.
* **Cero Pay-to-Win (No P2W):** Ningún pago con dinero real podrá otorgar ventajas biológicas ficticias ni superioridad competitiva injusta en duelos PvP.
* **Protección Absoluta de la Verdad Científica:**
  * Prohibido comercializar mejor precisión o "puntería" en la identificación visual.
  * Prohibido permitir la compra de modificaciones zoológicas o taxonómicas.
  * Prohibido adquirir retroactivamente la rareza de emisión o la población histórica (`population_at_issuance`).
* **Arquitectura Publicitaria Desacoplada (`AdService`):**
  * La infraestructura publicitaria opera en la capa externa desacoplada (`BannerPlacement`, `InterstitialPolicy`, `RewardedAdService`, `FrequencyPolicy`), sin contaminar el dominio biológico.
  * **Zonas Libres de Publicidad:** Visor de cámara, momento del disparo, carga de identificación, revelación y primer volteo de la carta, lectura de enciclopedia y combates.
  * **Rewarded Ads Voluntarios:** Anuncios por recompensa transparente con opción explícita y respetuosa de declinar (*"No, gracias"*).
* **Economía Ética y Tienda Cosmética (Shop):**
  * Moneda gratuita de juego obtenida mediante avistamientos, descubrimientos y completitud de grupos taxonómicos.
  * Tienda de personalización estética: marcos de cartas, fondos de Baúl, efectos visuales, temas, avatares y comodidades no competitivas de almacenamiento.
  * Posible moneda premium reservada para cosméticos en fases avanzadas, sin condicionar el lanzamiento temprano.
