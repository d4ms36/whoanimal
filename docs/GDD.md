# WHO ANIMAL — GAME DESIGN DOCUMENT (GDD)

**Versión:** 0.2 (Fundación Conceptual y Arquitectura)  
**Estado:** En definición colaborativa (Director Creativo: Usuario / Project Manager: ChatGPT / Dev Principal: Antigravity)  
**Tecnología Base:** Python (Núcleo y Servicios)

---

## 1. Visión del Producto

> **"Descubre. Identifica. Colecciona."**

**WHO Animal** no es una simple herramienta utilitaria de identificación de especies. Es una **experiencia interactiva de descubrimiento, aprendizaje y colección de fauna** articulada alrededor de un sistema visual de cartas coleccionables con profundidad narrativa (*Lore*) y base científica rigurosa.

### El Bucle Conceptual de Experiencia (Core Loop)

```mermaid
flowchart LR
    A[📷 Identificar] --> B[✨ Descubrir]
    B --> C[🃏 Obtener Carta]
    C --> D[🔬 Explorar Info Real]
    D --> E[📖 Descubrir Lore]
    E --> F[📚 Coleccionar y Preservar]
```

1. **Identificar:** El usuario toma o sube una fotografía de un animal.
2. **Descubrir:** El sistema reconoce el animal y presenta la revelación.
3. **Obtener Carta:** Se genera y entrega la carta correspondiente con arte y atributos iniciales.
4. **Explorar Información:** El usuario gira la carta para descubrir datos biológicos, hábitat, dieta y curiosidades.
5. **Descubrir Lore:** Se desbloquea una narrativa de fantasía o leyenda ligada al animal dentro del universo Who Animal.
6. **Coleccionar:** El espécimen se archiva en el álbum/inventario personal de avistamientos para su consulta y futura evolución.

---

## 2. Los Tres Pilares Fundamentales

Todo el diseño y la arquitectura de datos de Who Animal respetan esta separación ontológica:

1. **INFORMACIÓN REAL:** Datos taxonómicos, biológicos, morfológicos y de conservación verídicos, contrastables y educativos.
2. **EXPERIENCIA:** Diseño de interfaz, estética de cartas, sensaciones de recompensa, descubrimiento y colección.
3. **LORE:** Capa narrativa y de ficción que da personalidad mitológica o de aventura a los animales sin ser jamás confundida con hechos científicos.

---

## 3. Especificación de la Carta

Cada animal avistado e identificado genera una carta con dos caras:

### Frente de la Carta (Atraer + Reconocer + Coleccionar)
* **Finalidad:** Puramente visual, limpia y atractiva. No sobrecargar con párrafos informativos.
* **Componentes:**
  * Fotografía o ilustración de alta calidad del animal.
  * Nombre común destacado (e.g., *Zorro Rojo*).
  * Nombre científico en tipografía secundaria (e.g., *Vulpes vulpes*).
  * Categoría taxonómica / Tipo (Mamífero, Ave, Reptil, etc.).
  * Elementos visuales del marco (tema visual, distintivo estético).
  * Metadatos de colección (código único de carta, identificador de serie).

### Reverso de la Carta (Aprender + Asombrarse + Profundizar)
* **Finalidad:** Organización clara y tipográficamente confortable de la información.
* **Componentes:**
  * **Datos Factuales:** Hábitat, distribución geográfica, alimentación, tamaño y peso típico (cuando aplique), comportamiento.
  * **Indicadores Secundarios:**
    * *Protegido:* Sí / No (sutil, sin convertir la carta en un formulario técnico).
    * *Especie rara:* Sí / No (referido a rareza biológica real, nunca como gamificación confusa).
  * **Sección de Seguridad (Condicional):**
    * ⚠️ **Precaución** o ⚠️ **Peligro**: Se activa exclusivamente para especies con riesgos reales comprobados (veneno, mordedura, agresividad territorial).
    * Enfoque: Responsable, preventivo, sin sensacionalismo ni estigmatización.
  * **Curiosidades:** Datos llamativos y asombrosos sobre su biología o hábitos.
  * **Lore (Capa Narrativa Independiente):** Micro-relato o título de leyenda (e.g., *"El Susurro del Viento Helado"*), claramente demarcado como ficción lúdica.

---

## 4. Filosofía y Sistema Pet Friendly

**Who Animal es 100% Pet Friendly.**  
* Toda dinámica premia la **observación respetuosa y la preservación**.
* Queda excluida cualquier mecánica que recompense la perturbación, acoso o captura física de animales en la vida real.
* La fauna silvestre se respeta en su hábitat y a distancia segura.

---

## 5. Colección y Progresión

* **Álbum de Avistamientos:** Organización por biomas, categorías taxonómicas o regiones.
* **Marcadores de Completitud:** Motivación por completar grupos sin crear urgencias nocivas.
* **Variantes de Cartas (Propuesta futura):** Posibles ediciones especiales por estación, iluminación o región, siempre supeditadas a la aprobación de diseño.

---

## 6. Posibles Mecánicas Futuras (Propuestas no definitivas)

Estas mecánicas están en análisis conceptual y **NO deben implementarse todavía**:
1. **Sistema RPG / Duelos de Sabiduría:** Enfrentamientos de conocimiento animal o estadísticas lúdicas ficticias, alejadas de cualquier violencia o combate físico animal.
2. **Retos de Exploración / Badges:** Insignias por registrar diversidad de aves urbanas, insectos del jardín, etc.
3. **Economía interna ética:** Moneda del juego obtenida por hitos educativos, nunca ligada a mecánicas abusivas.

---

## 7. Decisiones Pendientes (Requieren Aprobación del Director/PM)

* Formato final del arte de cartas (generación procedural, plantillas vectoriales o renderizado dinámico).
* Sistema definitivo de identificación: Modelo on-device vs. API híbrida de taxonomía (iNaturalist / GBIF / Modelo custom).
* Convención formal de nomenclatura para los identificadores de cartas (`WA-MAM-001`, etc.).
* Criterio editorial específico para la redacción de Lore por bioma.
