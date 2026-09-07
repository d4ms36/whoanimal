# Reglas de Producto — WHO Animal

**Versión:** 1.0 (Rebaseline Oficial — WHO-DOC-001 / DEC-053)  
**Propósito:** Establecer los principios normativos de diseño, ética, experiencia de usuario y arquitectura de negocio que rigen transversalmente todo el ecosistema de WHO Animal.

---

## 1. Principio Fundamental Rector

> **"WHO Animal es 100% Pet Friendly."**

Este principio rige de manera absoluta el diseño de producto, la arquitectura de información, la experiencia de usuario (UX), los contenidos narrativos, las dinámicas de juego y los esquemas de monetización. Si surge un conflicto entre una mecánica de juego atractiva y el bienestar de los animales reales, **prevalece siempre el bienestar animal**.

---

## 2. Reglas Cardinales de Información y Contenido

### Regla 1: Separación Estricta entre Información Real, Experiencia y Lore
* **Información Científica Factual:** Basada exclusivamente en taxonomía y datos zoológicos contrastables.
* **Experiencia:** Vehículo lúdico de descubrimiento, colección, estética de cartas y disfrute visual.
* **Lore (Historia Personal):** Relato personal escrito por el usuario sobre su encuentro.
* **Prohibición de mezcla:** El Lore **NUNCA** debe presentarse como un hecho científico ni crear confusión en el aprendizaje biológico del usuario. Porta obligatoriamente su propio contenedor y disclaimer.

### Regla 2: Prohibición de Inventar Datos Científicos
* Queda terminantemente prohibido inventar hábitats, dietas, tamaños, comportamientos o estatus de conservación de animales reales.
* Si un dato zoológico es desconocido o impreciso para una especie, se indicará claramente o se representará como nulo (`null`), nunca formulando conjeturas.

### Regla 3: Indicadores de Protección y Rareza como Elementos Secundarios
* Los indicadores `Protegido: Sí/No` y `Especie rara: Sí/No` deben ser sutiles y secundarios en la carta.
* **WHO Animal no es un catálogo burocrático:** Estos datos complementan la ficha de manera rápida sin sobrecargar la experiencia visual ni entristecer el disfrute del usuario.
* La rareza biológica real de una especie silvestre no debe confundirse con la rareza de colección lúdica de una carta en el juego.

### Regla 4: Responsabilidad y No Exageración en Avisos de Peligro
* Los indicadores de **⚠️ Precaución** o **⚠️ Peligro** se muestran **únicamente** cuando sean pertinentes para la especie (veneno activo, ponzoña, riesgo por mordedura, agresividad territorial).
* Queda prohibido exagerar el peligro de un animal para generar sensacionalismo, miedo o morbo. La información de seguridad debe ser serena, formativa y preventiva.

### Regla 5: La Experiencia Nunca Sacrifica la Precisión
* Las animaciones, temas visuales y efectos tridimensionales no deben deformar las proporciones, características anatómicas esenciales ni la veracidad de la especie representada.

---

## 3. Reglas Éticas de Interacción y Colección

1. **Observación Responsable:** La aplicación alienta a observar la fauna desde una distancia prudente que no altere su comportamiento natural.
2. **Cero Perturbación:** Ninguna misión, logro o incentivo de la app recompensará perseguir, atrapar físicamente, acorralar, tocar o perturbar a ningún animal en su hábitat.
3. **Protección de Ubicación Silvestre:** La carta expone públicamente solo una ubicación generalizada (`display_location`), manteniendo ocultas las coordenadas GPS exactas para proteger a las especies de cazadores furtivos o visitas masivas dañinas.

---

## 4. Reglas de Modelo de Negocio y Free-to-Play (F2P)

### Regla 6: Free-to-Play por Diseño
* El núcleo de WHO Animal (identificar, aprender, descubrir y coleccionar en el Baúl) es y será **100% gratuito de por vida**.

### Regla 7: Cero Pay-to-Win (No P2W)
* Ningún pago con dinero real podrá otorgar ventajas biológicas ficticias ni superioridad competitiva injusta en duelos.
* **Prohibiciones comerciales absolutas:**
  * Prohibido vender mejor precisión o mayor "puntería" en el motor de identificación zoológica.
  * Prohibido vender modificaciones a datos taxonómicos, hábitats o dietas.
  * Prohibido comprar retroactivamente la rareza de emisión o la población histórica (`population_at_issuance`) de una carta.

### Regla 8: Desacoplamiento y Ética Publicitaria (`AdService`)
* Los proveedores publicitarios operan estrictamente en la capa de infraestructura externa, sin acoplarse al dominio biológico.
* **Zonas Libres de Anuncios:** Queda prohibida la publicidad intrusiva durante el uso de la cámara, la toma fotográfica, el procesamiento de identificación, la revelación y volteo de la carta, la lectura científica y los combates.
* **Consentimiento en Rewarded Ads:** Los anuncios por recompensa son estrictamente voluntarios y deben incluir siempre la opción explícita de rechazar (*"No, gracias"*).

### Regla 9: Economía Transparente y Tienda Cosmética
* La moneda principal del juego es gratuita y se obtiene jugando y descubriendo fauna.
* La tienda se centra en elementos cosméticos de personalización (marcos, temas, fondos de Baúl, avatares, efectos visuales) y utilidades no competitivas de almacenamiento.

---

## 5. Reglas de la Capa Game y Duelos PvP

### Regla 10: Estadísticas de Juego Desacopladas de la Biología Real
* Los atributos numéricos de combate (HP, ATK, DEF, SPD, TYPE, SPECIAL) son parámetros puramente lúdicos para balance de duelos.
* Jamás deben presentarse como medida de fuerza, superioridad, agresividad ni dominancia de los animales reales en la naturaleza.
* Las batallas son representaciones ágiles de cartas sin violencia gráfica, sangre ni maltrato.

### Regla 11: Equipos de 10 Cartas y Ventana Semanal
* El equipo de exploración activo se compone de 10 cartas.
* Para fomentar la estrategia y evitar el cambio compulsivo reactivo, el equipo activo solo puede modificarse dentro de la ventana semanal programada.

---

## 6. Reglas de la Capa Social y de Comunidad

### Regla 12: Inmutabilidad Histórica en el Intercambio (Trading)
* La transferencia de cartas entre usuarios actualiza la titularidad (`owner_id`), pero **mantiene rigurosamente congelada la identidad histórica** original (`card_id`, espécimen, rareza original, población en emisión, timestamp de captura y serial de autenticación).

### Regla 13: Moderación Activa y Protección de Menores
* Las Historias Personales (*Lore*) compartidas públicamente están sujetas a filtros de lenguaje, reportes de usuario y moderación proactiva contra contenidos inapropiados.
* Se dispondrá de herramientas inmediatas de bloqueo de usuarios y reporte de abusos.

---

## 7. Reglas de Internacionalización Transversal (i18n)

### Regla 14: Separación Lingüística Tripartita
$$\text{Traducciones de UI} \neq \text{Contenido Científico} \neq \text{Lore Personal}$$
* Los textos de interfaz deben estar completamente parametrizados para traducción dinámica.
* Los nombres científicos zoológicos en latín son invariantes y universales en todos los idiomas.
* El Lore personal escrito por el usuario se preserva inalterado en su idioma original.
