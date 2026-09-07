# Memoria Oficial del Proyecto (Project Context) — WHO Animal

**Documento:** `docs/PROJECT_CONTEXT.md`  
**Propósito:** Memoria y contexto fundamental de WHO Animal para asegurar coherencia transversal a lo largo de todo el ciclo de vida del producto.  
**Última actualización:** 2026-09-07 (Formalización de Alpha 0.1, Golden Path y Horizontes Estratégicos WHO-013.1 / DEC-048)

---

## 1. ¿Qué es WHO Animal?

**WHO Animal** es una **aplicación móvil** centrada en el **descubrimiento, identificación, aprendizaje y coleccionismo de fauna mediante cartas coleccionables**.

Trasciende el concepto de una simple herramienta utilitaria de escaneo o identificación visual de especies. La identificación mediante cámara o imagen es una puerta de entrada al producto, no el producto completo.

El ciclo conceptual oficial se estructura como:

```text
IDENTIFICAR
    ↓
DESCUBRIR
    ↓
OBTENER CARTA
    ↓
EXPLORAR INFORMACIÓN
    ↓
ESCRIBIR LORE
    ↓
COLECCIONAR
    ↓
SEGUIR DESCUBRIENDO
```

---

## 2. Los Ocho Pilares de la Experiencia

1. **Identificación:** Tecnología de visión artificial que orienta al usuario reconociendo la fauna de su entorno o de archivos gráficos.
2. **Descubrimiento:** Sensación de revelación y asombro ante la riqueza de la biodiversidad planetaria.
3. **Educación:** Transmisión de conocimientos biológicos rigurosos, hábitos zoológicos, hábitats y ecología.
4. **Cartas:** Formato visual de dos caras (Frente estético y atrayente; Reverso estructurado y formativo) que materializa cada avistamiento.
5. **Colección:** Mecánica de progresión que permite organizar especímenes en álbumes temáticos, biomas o categorías taxonómicas.
6. **Exploración:** Invitación a prestar atención a la naturaleza circundante, desde aves urbanas e insectos hasta fauna silvestre protegida.
7. **Historia Personal (Lore):** Capa de contenido personal escrita por el usuario, asociada a una carta específica, que refleja su experiencia individual (hasta 300 caracteres, sujeta a reglas de edición controlada).
8. **Experiencia Visual:** Diseño de interfaces moderno, limpio, con micro-interacciones de alta fidelidad, dinámico y respetuoso de la fauna.

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
6. **Storage (Almacenamiento y Colección):**
   * Colección de cartas local y persistente.
   * Estructura organizada en **10 containers**.
   * Capacidad de **30 espacios por container** (capacidad total del sistema: 300 cartas).
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

## 13. Horizontes Estratégicos de Desarrollo

La planificación estratégica de WHO Animal distingue claramente entre objetivos ejecutables inmediatos y capacidades futuras, garantizando foco operativo sin restringir la evolución del producto:

```text
CORTO PLAZO
    ↓
ALPHA FUNCIONAL
    ↓
MEDIANO PLAZO
    ↓
BETA / RELEASE
    ↓
LARGO PLAZO
    ↓
ECOSYSTEM
    ↓
FUTURO ABIERTO
```

### Línea Temporal Única Oficial:
```text
FOUNDATION = COMPLETADA
ALPHA = PRÓXIMO PRODUCTO FUNCIONAL (WHO-014 → WHO-018)
BETA = SIGUIENTE HORIZONTE
RELEASE = LANZAMIENTO PÚBLICO
ECOSYSTEM = CAPACIDADES AVANZADAS
```

---

### 13.1. CORTO PLAZO — Alpha Funcional (Alpha 0.1)
* **Objetivo Rector:** `LOGIN → HOME → CAPTURE → IDENTIFY → CARD → STORAGE`.
* **Prioridad Absoluta:** Construir y entregar una aplicación Android funcional y demostrable que complete el ciclo de punta a punta.
* **Secuencia de Construcción:** Objetivos `WHO-014` a `WHO-018`.

---

### 13.2. MEDIANO PLAZO — Beta / Release
Documentado a nivel estratégico para guiar la evolución una vez cerrada la Alpha:
* Pruebas de usabilidad y feedback cualitativo con usuarios reales.
* Mejora continua y pulido de UX y micro-interacciones.
* Estabilidad del sistema y optimización de rendimiento.
* Calibración y afinamiento del motor de identificación de fauna.
* Expansión inicial controlada del catálogo zoológico.
* Testing exhaustivo en múltiples dispositivos Android.
* Preparación para distribución controlada (Google Play Internal Testing).
* Auditoría y cumplimiento legal, términos de servicio y políticas de privacidad.
* Publicación pública comercial en Google Play Store.

> *Nota de Gobernanza:* Estos puntos representan dirección táctica a mediano plazo y **no se convierten en objetivos atómicos ejecutables** hasta que la Alpha esté formalmente concluida y sean aprobados por el Director.

---

### 13.3. LARGO PLAZO — Ecosystem (Capacidades Estratégicas Avanzadas)
Las siguientes 13 capacidades estratégicas representan la visión de largo alcance del ecosistema de WHO Animal. La arquitectura base deja previstos sus puntos de extensión, pero **ninguna de ellas compromete ni bloquea la entrega de Alpha o Beta**:

1. **Comercio de cartas (Trading):** Transferencia controlada de propiedad entre coleccionistas preservando la inmutabilidad histórica original (`card_id`, `specimen_number`, generación, rareza original, población de emisión y serial de autenticación).
2. **Sistema de Duelos PVP:** Enfrentamientos lúdicos entre cartas, estrictamente desacoplados del conocimiento taxonómico y del core loop.
3. **Cuentas avanzadas / multiusuario:** Autenticación remota, perfiles de usuario y gestión segura de sesiones.
4. **Infraestructura Cloud:** Backend distribuido y APIs escalables para soporte de red.
5. **Economía del ecosistema:** Sistema balanceado de progresión y recompensas in-app.
6. **Marketplace:** Mercado in-app para intercambio y adquisición controlada de cartas.
7. **Sistema completo de rarezas:** Implementación del algoritmo matemático y curvas de emisión dinámica (DEC-022-PENDING).
8. **Sistema / Red de ilustradores colaboradores:** Encargos artísticos personalizados gestionados in-app asociados a cartas específicas.
9. **Sincronización multidispositivo:** Persistencia remota y respaldo en la nube del inventario y álbumes.
10. **Expansión pública / global:** Despliegue internacional a gran escala y soporte multirregional.
11. **Expansión progresiva del catálogo zoológico hacia cobertura global:** Principio de crecimiento continuo y responsable de la base zoológica, sin prometer "todas las especies" de forma inmediata ni ficticia.
12. **Evolución continua del sistema de IA de identificación:** Mejora progresiva de precisión y modelos optimizados, sin falsas garantías de "IA perfecta".
13. **Gamificación completa:** Progresión profunda de maestría, medallas por biomas y dinámicas avanzadas de `rank` (DEC-037-PENDING).

---

### 13.4. FUTURO ABIERTO (Sandbox Conceptual)
Espacio documental de reserva para ideas, conceptos e iniciativas en gestación que **aún no tienen**:
* Alcance definido.
* Prioridad asignada.
* Dependencias técnicas resueltas.
* Versión objetivo formalizada.
* Criterios de aceptación estructurados.

**Regla de Oro:** Ninguna idea contenida en el Futuro Abierto se convertirá automáticamente en un objetivo ejecutable `WHO` sin pasar por el flujo formal de gobernanza: **Director → PM → Especificación → Aprobación**.

Entre las ideas del Futuro Abierto se exploran conceptualmente:
* Desafíos comunitarios de bioacústica o sonidos de fauna.
* Fichas de hábitat y ecosistemas como coleccionables complementarios.
* Integración con guías locales de reservas naturales y parques nacionales.
* Eventos estacionales basados en migraciones reales de fauna.
* Soporte para realidad aumentada (AR) en visualización de cartas y especímenes.

---

## 14. Modelo Inicial de Monetización (Gratuito y Publicidad)

En alineación formal con la decisión **DEC-038**, el modelo comercial inicial y su relación con la arquitectura del sistema se rigen por los siguientes principios:

* **Núcleo 100% Gratuito (Free-to-Play):** WHO Animal es un producto de alcance **internacional y global**. Las funcionalidades fundamentales (identificación, información zoológica, fichas, colección, descubrimiento y lore) serán accesibles sin coste.
* **Monetización Publicitaria:** La principal fuente de monetización en esta etapa inicial será la publicidad.
* **Desacoplamiento Arquitectónico Estricto:** Los proveedores de publicidad (como Google AdMob u otros) operan estrictamente en la capa de **Infraestructura Externa**. Ninguna entidad inmutable del Dominio (`Animal`, `Capture`, `Card`, `Lore`) debe depender ni conocer de SDKs publicitarios.
* **Prioridad UX: Rewarded Ads:** La publicidad no debe destruir el flujo de descubrimiento ni la experiencia interactiva mediante interrupciones constantes. Se prioriza conceptualmente el uso de **Anuncios Recompensados** (*Rewarded Ads*), donde el usuario interactúa de forma voluntaria a cambio de beneficios en la capa de experiencia.
* **Respeto Biológico e Identidad Histórica:** **Está terminantemente prohibido monetizar, falsificar o alterar información biológica real**. La monetización pertenece a la capa de experiencia de producto, jamás a la verdad zoológica. Igualmente, la identidad histórica de una carta (`card_id`, población en la emisión, rareza original) no puede comprarse ni modificarse retroactivamente.
* **Ausencia de Economía In-App:** Durante la fase actual de fundación, **no se implementarán** sistemas de economía interna, monedas, "WHO Coins", "Gems", tiendas, loot boxes, sistemas de pago directo, suscripciones ni funcionalidades de "pay-to-win".
