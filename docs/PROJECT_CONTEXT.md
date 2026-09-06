# Memoria Oficial del Proyecto (Project Context) — WHO Animal

**Documento:** `docs/PROJECT_CONTEXT.md`  
**Propósito:** Memoria y contexto fundamental de WHO Animal para asegurar coherencia transversal a lo largo de todo el ciclo de vida del producto.  
**Última actualización:** 2026-09-06 (Consolidación de directrices WHO-005A)

---

## 1. ¿Qué es WHO Animal?

**WHO Animal** trasciende el concepto de una simple herramienta utilitaria de escaneo o identificación visual de especies. 

La visión central es construir una **experiencia integral de descubrimiento, coleccionismo y aprendizaje sobre el reino animal**, articulada a través de un sistema inmersivo de cartas coleccionables.

La identificación mediante cámara o imagen no es el destino final, sino la **puerta de entrada** a un ciclo continuo de asombro y conocimiento:

```mermaid
flowchart TD
    A[📷 IDENTIFICAR] --> B[✨ DESCUBRIR]
    B --> C[🃏 OBTENER CARTA]
    C --> D[🔬 EXPLORAR INFORMACIÓN]
    D --> E[💭 ESCRIBIR HISTORIA PERSONAL (LORE)]
    E --> F[📚 COLECCIONAR]
    F --> G[🗺️ SEGUIR DESCUBRIENDO]
    G --> A
```

---

## 2. Los Ocho Pilares de la Experiencia

1. **Identificación:** Tecnología de visión artificial que orienta al usuario reconociendo la fauna de su entorno o de archivos gráficos.
2. **Descubrimiento:** Sensación de revelación y asombro ante la riqueza de la biodiversidad planetaria.
3. **Educación:** Transmisión de conocimientos biológicos rigurosos, hábitos zoológicos, hábitats y ecología.
4. **Cartas:** Formato visual de dos caras (Frente estético y atrayente; Reverso estructurado y formativo) que materializa cada avistamiento.
5. **Colección:** Mecánica de progresión que permite organizar especímenes en álbumes temáticos, biomas o categorías taxonómicas.
6. **Exploración:** Invitación a prestar atención a la naturaleza circundante, desde aves urbanas e insectos hasta fauna silvestre protegida.
7. **Historia Personal (Lore):** Capa de contenido personal escrita por el usuario, asociada a una carta específica, que refleja su experiencia individual (hasta 300 caracteres).
8. **Experiencia Visual:** Diseño de interfaces moderno, limpio, con micro-interacciones de alta fidelidad, dinámico y respetuoso de la fauna.

---

## 3. Separación Ontológica Tripartita

Toda información asociada a un animal o a una carta en WHO Animal pertenece conceptualmente a uno de estos tres dominios estancos:

```text
┌────────────────────────────────────────────────────────────────────────┐
│                          WHO ANIMAL ECOSYSTEM                          │
├───────────────────┬───────────────────────────────┬────────────────────┤
│  INFORMACIÓN REAL │          EXPERIENCIA          │        LORE        │
│ • Curiosidades      emisión                       │ • Elementos mito-  │
│ • Protección      │ • Elementos de colección        lógicos            │
│ • Rareza natural  │ • Interfaz, álbum y volteo    │                    │
├───────────────────┼───────────────────────────────┼────────────────────┤
│  100% Verificable │     100% Estimulante          │   100% Ficticio    │
└───────────────────┴───────────────────────────────┴────────────────────┘
```

### Regla Absoluta contra Datos Científicos Inventados
* **La Información Real jamás se inventa.** No se admiten conjeturas en taxonomía, distribución, hábitats, medidas ni dietas.
* Si el sistema no dispone de un dato verídico contrastado, **debe consultarlo en una fuente válida o representarlo como desconocido (`null`)**, nunca rellenarlo con suposiciones.
* Si se desea incorporar contenido personal o narrativo sobre el encuentro, debe residir obligatoriamente en **EXPERIENCIA** o en **LORE** (Historia Personal asociada a la carta, escrita por el usuario).

---

## 4. Diferenciación Ontológica: Animal vs. Capture vs. Carta

> **Animal ≠ Capture ≠ Carta**

* **Animal (`AnimalProfile`):** Representa la entidad biológica y taxonómica objetiva de la especie en la base de conocimiento de WHO Animal. Describe rasgos universales de la especie (taxonomía, hábitat, dieta, distribución). Puede existir en el sistema sin necesidad de haber sido emitido aún en una carta para ningún usuario. **No contiene atributos particulares de individuos observados (ej. `sex ∉ Animal`)**.
* **Captura / Espécimen (`Capture / Specimen`):** Representa el evento de observación y registro de un individuo físico concreto en el mundo real (`capture_id`, `animal_id`, telemetría de campo, timestamp). **Aquí reside el sexo biológico del ejemplar observado (`sex ∈ Capture`, valores: `MALE`, `FEMALE`, `UNKNOWN`; ver DEC-036). Se trata de un dato opcional y nullable sin valor por defecto (DEC-039), diferenciando la imposibilidad de identificación (`UNKNOWN`) de la ausencia de dato (`null`)**.
* **Carta (`AnimalCard`):** Representa un ejemplar coleccionable individual, acuñado y emitido en un momento histórico concreto para el álbum de un jugador, vinculado a un espécimen animal pero dotado de propiedades de colección, generación, rareza y autenticación propias. Puede proyectar datos de la captura (como el sexo del individuo observado) en su visualización, pero la fuente primaria de verdad es la captura.

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

## 13. Capacidades Futuras Aprobadas (Extensiones de Fases Posteriores)

Las siguientes capacidades han sido aprobadas a nivel de diseño conceptual para que **la arquitectura actual no cierre las puertas a su evolución**, pero **permanecen estrictamente fuera de la implementación en la Fase 0**:

### A. Sistema de Enfrentamientos PVP
* Enfrentamientos lúdicos o duelos de cartas entre usuarios.
* Concebido como un módulo independiente desacoplado de los perfiles taxonómicos (`Animal`), de la identificación por visión, de la información científica real y del Lore.

### B. Intercambio y Comercio de Cartas (Trading / Transferencia Controlada)
* Capacidad de traspasar cartas entre usuarios manteniendo intacta e inmutable la identidad histórica (`card_id`, `specimen_number`, generación, rareza original, población de emisión y serial de autenticación).

### C. Cartas con Artwork Único y Red de Ilustradores
* Los usuarios podrán solicitar un diseño artístico personalizado para una carta específica interactuando directamente desde la app con ilustradores colaboradores asociados.
* El artwork constituye una capa estética y creativa adicional que **no sustituye ni deforma**: la identidad del animal, la información científica ni los metadatos históricos de la carta.

---

## 14. Modelo Inicial de Monetización (Gratuito y Publicidad)

En alineación formal con la decisión **DEC-038**, el modelo comercial inicial y su relación con la arquitectura del sistema se rigen por los siguientes principios:

* **Núcleo 100% Gratuito (Free-to-Play):** WHO Animal es un producto de alcance **internacional y global**. Las funcionalidades fundamentales (identificación, información zoológica, fichas, colección, descubrimiento y lore) serán accesibles sin coste.
* **Monetización Publicitaria:** La principal fuente de monetización en esta etapa inicial será la publicidad.
* **Desacoplamiento Arquitectónico Estricto:** Los proveedores de publicidad (como Google AdMob u otros) operan estrictamente en la capa de **Infraestructura Externa**. Ninguna entidad inmutable del Dominio (`Animal`, `Capture`, `Card`, `Lore`) debe depender ni conocer de SDKs publicitarios.
* **Prioridad UX: Rewarded Ads:** La publicidad no debe destruir el flujo de descubrimiento ni la experiencia interactiva mediante interrupciones constantes. Se prioriza conceptualmente el uso de **Anuncios Recompensados** (*Rewarded Ads*), donde el usuario interactúa de forma voluntaria a cambio de beneficios en la capa de experiencia.
* **Respeto Biológico e Identidad Histórica:** **Está terminantemente prohibido monetizar, falsificar o alterar información biológica real**. La monetización pertenece a la capa de experiencia de producto, jamás a la verdad zoológica. Igualmente, la identidad histórica de una carta (`card_id`, población en la emisión, rareza original) no puede comprarse ni modificarse retroactivamente.
* **Ausencia de Economía In-App:** Durante la fase actual de fundación, **no se implementarán** sistemas de economía interna, monedas, "WHO Coins", "Gems", tiendas, loot boxes, sistemas de pago directo, suscripciones ni funcionalidades de "pay-to-win".
