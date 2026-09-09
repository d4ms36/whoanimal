# WHO Animal — Card Test Protocol

El siguiente protocolo define los cinco tests oficiales y estandarizados que debe pasar cualquier diseño de carta antes de ser validado y enviado a desarrollo en Jetpack Compose. Esto asegura escalabilidad y legibilidad.

## Metodología de Ejecución

Cada diseño o prototipo visual deberá someterse a estos tests, reportando "PASA" o "FALLA".

### 1. Flash Test
**Objetivo:** Medir la capacidad de reconocimiento inmediato de la jerarquía visual.
**Ejecución:** Se muestra la carta en pantalla durante exactamente **1.5 segundos** y luego se oculta. Se le pregunta al observador: *¿Qué animal era? ¿Cuál es su rareza?*. 
**Aprobación:** Pasa si el observador logra identificar sin dudar el espécimen protagonista y si notó el nivel de rareza, comprobando que la fotografía y los *badges* no quedan eclipsados por la UI.

### 2. Thumbnail Test
**Objetivo:** Asegurar que el diseño es escalable para vistas de colección (Grid).
**Ejecución:** Escalar la carta a un tamaño de *100x140 píxeles* (tamaño típico de miniatura en un teléfono).
**Aprobación:** Pasa si el marco sigue viéndose como un contenedor físico en lugar de un borde ruidoso, y si el nombre del animal (común) sigue siendo razonablemente discernible (o si la estructura de la miniatura funciona de forma autónoma).

### 3. Grayscale Test
**Objetivo:** Verificar contraste lumínico y accesibilidad para personas daltónicas.
**Ejecución:** Aplicar un filtro de 100% saturación nula (Blanco y Negro puro) a la carta entera, incluyendo fotografía y marcos.
**Aprobación:** Pasa si las jerarquías de rareza siguen siendo evidentes (ej. por contraste, brillo, texturas) y si todo el texto es 100% legible contra su fondo sin depender de la diferenciación por color (e.g., rojo vs verde).

### 4. One-Hand Test
**Objetivo:** Validar la ergonomía visual de los datos al interactuar físicamente con un dispositivo móvil.
**Ejecución:** Renderizar la UI en el tercio superior/medio de la pantalla simulada de un teléfono, asumiendo que el pulgar del usuario estará en la mitad inferior obstruyendo la visión y swipeando.
**Aprobación:** Pasa si la información clave taxonómica y el elemento central del arte no quedan bloqueados por la posición de descanso natural del pulgar en un uso a una sola mano.

### 5. Gallery Test
**Objetivo:** Evaluar la armonía o el ruido del diseño al multiplicarlo.
**Ejecución:** Replicar la misma carta (con 5 especies distintas y diferentes fondos/rarezas) en un *Grid* de 2x3 o 3x4 uno al lado del otro.
**Aprobación:** Pasa si la galería se siente como una "colección unificada" y ordenada (álbum). Falla si la multiplicidad de gradientes, bordes extraños o tipografías densas causan saturación cognitiva o fatiga visual extrema.
