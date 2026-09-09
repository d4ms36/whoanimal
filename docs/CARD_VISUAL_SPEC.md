# WHO Animal — Card Specification

Este documento establece la estructura anatómica inamovible de las cartas coleccionables y las especificaciones del laboratorio de pruebas visuales.

## 1. Anatomía Oficial de la Carta

Todas las cartas coleccionables en la interfaz, sin importar su rareza o estilo, deben cumplir de manera estricta la siguiente anatomía:

* **Hero Photo (≈60%):** La fotografía del espécimen debe abarcar aproximadamente el 60% del espacio total de la carta, estableciéndose como el absoluto protagonista visual.
* **Logo Brújula + Planta:** El isotipo oficial del proyecto (brújula fusionada con planta) debe estar integrado en la composición superior o posterior de la carta, validando su estatus oficial.
* **Marco Inteligente:** El borde o contenedor de la carta debe comportarse como un contenedor con profundidad y textura, capaz de reaccionar a diferentes rarezas sin cambiar su forma base.
* **Rank Hexagonal:** El rango numérico o nivel de rareza se enmarca dentro de un contenedor geométrico de forma hexagonal.
* **Elemento Triangular:** Empleo de decoraciones u orientadores geométricos triangulares como firma de identidad secundaria (e.g., en esquinas o apuntando a la especie).
* **Stats Anatómicos:** Datos biológicos clave (peso, longitud, hábitat, etc.) presentados con alta legibilidad tipográfica.
* **Skills Minimalistas:** Los "Skills" o rasgos de comportamiento se mostrarán mediante iconografía minimalista, no invasiva.
* **Restricción Fotográfica Absoluta:** **Sin texto sobre la fotografía**. Todo dato, título o decoración debe colocarse fuera del *Hero Photo* para no ensuciar la imagen del espécimen y respetar el naturalismo de la fotografía.

---

## 2. Card Laboratory Foundation

Para asegurar que el diseño de las cartas sea robusto, escalable y no se rompa con casos extremos, se define la especificación del **Laboratorio de Comparación de Cartas**. 

Este laboratorio (que se construirá en código posteriormente) debe contemplar obligatoriamente los siguientes 5 especímenes de prueba para garantizar la adaptación a diferentes biotipos:

1. **Ballena** (Prueba de proporciones masivas, azules y hábitat acuático).
2. **Lobo** (Prueba de pelaje, tonos neutros/oscuros, carnívoro de bosque).
3. **Perro** (Prueba de domesticidad, familiaridad, variabilidad de raza).
4. **Águila** (Prueba de hábitat aéreo, aves, plumas y contrastes altos).
5. **Rana** (Prueba de espécimen pequeño, anfibio, saturación de colores vibrantes y macros).

### Dimensiones del Laboratorio

Para cada uno de los especímenes, el sistema de diseño deberá permitir probar visualmente:

* **Tamaños de fotografía:** Comportamiento del *crop* (recorte) al escalar la carta.
* **Marcos:** Interacción del color predominante del animal con el borde de la carta.
* **Badges:** Adaptabilidad de insignias largas y cortas de nombres taxonómicos.
* **Rarezas:** Cambio total de material sobre el mismo animal.
* **Miniaturas:** Reducción del layout (mini-cards para galería) sin perder legibilidad ni jerarquía.
