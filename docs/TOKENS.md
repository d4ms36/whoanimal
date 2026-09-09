# WHO Animal — Design Tokens & Materials

**Documento:** `docs/TOKENS.md`  
**Estado:** Canónico / Normalizado (Alineado con Design Lock WHO-003B-R1 y Compose WHO-003B-R2)  
**Fuente de Referencia Visual:** `docs/design-lab/styles.css` y `com.whoanimal.app.ui.card.RaritySystem.kt`

---

## 1. Regla de Oro de Rareza
**La rareza debe implementarse estrictamente como una especificación visual.** No deben generarse efectos invasivos en el código de producción que rompan la experiencia naturalista. Los materiales a continuación representan intenciones físicas aplicadas a marcos y acentos con gradientes y sombras especulares (*Highlights*).

---

## 2. Escala Canónica de Rarezas (6 Niveles)

La progresión de valor de la colección se dictamina por los siguientes 6 acabados y texturas de material, alineados estrictamente con el **Design Lock R1** y la implementación Jetpack Compose **R2**:

| Rareza | Denominación Material | Base (`--rarity-base`) | Highlight (`--rarity-highlight`) | Shadow (`--rarity-shadow`) | Intención Física |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **Common** | — | `#8C857B` | `#B0A99F` | `#59534C` | Metálico sobrio, textura fina, no reflectante. Solidez de archivo histórico. |
| **Uncommon** | — | `#4A6B53` | `#6E9479` | `#283D2E` | Acabado orgánico simulando tonos forestales ricos (hoja perenne) con acabado satinado. |
| **Rare** | — | `#A65E3A` | `#D98A62` | `#5C2F1A` | Metálico cálido bruñido, sutil oxidación, alto contraste de luces cálidas y sombras ricas. |
| **Epic** | — | `#36323B` | `#5B5563` | `#18161A` | Piedra volcánica profunda o cuarzo oscuro. Elegancia austera y bordes refractivos. |
| **Legendary** | — | `#CCA329` | `#FFE680` | `#664F00` | Reflejos metálicos dorados, alto valor lumínico, simulación de *foil stamping* editorial. |
| **Prisma** | — | `#A8A8A8` | `#FFFFFF` | `#555555` | Acabado espectral holográfico suave y controlado; preserva la legibilidad sin saturar. |

> 📌 **Nota Histórica de Normalización (WHO-DOC-002):**  
> Versiones preliminares exploratorias mencionaban 7 materiales (incluyendo *Azul Mineral* y *Amatista*). La escala oficial aprobada y congelada en el Design Lock R1 es estrictamente de 6 rarezas canónicas.

---

## 3. Tokens de Identidad Base (Explorer DNA)

El estilo **Explorer** es el ADN visual canónico y base de WHO Animal:

* **Superficie de Papel (`--theme-surface`):** `#F9F6F0` (Papel natural cálido tipo bitácora de campo)
* **Texto Primario (`--theme-text`):** `#2C251E` (Tinta oscura cálida de alta legibilidad)
* **Texto Secundario / Acento (`--theme-text-sec`):** `#8B3A2A` (Acento cobre / óxido cálido)
* **Divisores sutiles:** `#2C251E` con 10% de opacidad (`0x1A2C251E`)
* **Tipografía de Cabecera:** *Playfair Display* / Serif clásica
* **Tipografía de Datos / Cuerpo:** *Inter* / Sans-serif contemporánea
* **Tipografía Taxonómica Científica:** Monospace, máximo 2 líneas

---

## 4. Tokens Dimensionales y Anatómicos

* **Proporción de Fotografía Hero Photo:** `~60%` del área vertical total (fijo, inmutable, NO 75%).
* **Dimensiones Carta Estándar:** `380dp × 532dp` (Aspect Ratio ≈ 1:1.4)
* **Dimensiones Miniatura (Grid / Baúl):** `96dp × 134dp`
* **Radios de Esquina:**
  * Marco exterior estándar: `16dp` (Miniatura: `8dp`)
  * Contenedor interior estándar: `10dp` (Miniatura: `5dp`)
* **Paddings de Marco:**
  * Marco exterior estándar: `8dp` (Miniatura: `4dp`)

