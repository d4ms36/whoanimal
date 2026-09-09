# PRODUCTO_FINAL_ESTABLE

## Visión del Producto
**WHO Animal** es una aplicación Android (Jetpack Compose) cuyo objetivo es proporcionar al usuario una experiencia de descubrimiento, identificación y colección de animales a través de un loop de juego intuitivo y premium.

## Loop de Experiencia Principal
1. **Explorar** – Navegar por entornos y observar fauna.
2. **Observar** – Focalizar el animal y activar la identificación.
3. **Identificar** – Sistema reconoce la *especie*.
4. **Capturar** – El usuario confirma la captura.
5. **Crear espécimen** – Cada captura genera un **espécimen único**.
6. **Obtener carta** – El espécimen se transforma en una **carta coleccionable**.
7. **Coleccionar** – La carta ocupa un espacio disponible en la colección del usuario.
8. **Progresar** – El usuario sigue explorando y repite el ciclo.

## Regla de Producto – Especie vs. Especimen
> Cada captura aceptada genera un **espécimen único** y la carta resultante debe conservar una **identidad única** (firma del espécimen) que permita diferenciarla de otras capturas de la misma especie.

* La especie y la captura no son lo mismo.
* La carta representa ese espécimen individual, no la especie genérica.
* La identidad única será definida técnicamente en fases posteriores.

## Capacidad de Colección
- **10 Cajas** por usuario.
- Cada caja contiene **30 espacios**.
- **Capacidad total:** 10 × 30 = **300** cartas/especímenes.
- Cada espacio representa un espécimen/carta.
- La colección permite visualizar y gestionar los especímenes dentro de estos límites.

## Carta Coleccionable – Características Aprobadas
- Representa un **espécimen concreto**.
- **Fotografía** es el elemento visual protagonista (≈ 60 % del protagonismo visual).
- Identidad visual **Explorer** (paleta, tipografía, geometría).
- **Standard Specimen** como presentación principal.
- **Hero Silhouette** solo si existe un recurso foreground transparente adecuado.
- Mantiene **información científica** (nombre común, nombre científico) y una **identidad RPG ligera**.
- No incluye estética medieval/fantasy (espadas, magia, runas).
- **Rarezas canónicas (6):** Common, Uncommon, Rare, Epic, Legendary, Prisma.  No hay Mythic ni séptima rareza.
- Prisma puede usar un tratamiento holográfico restringido como parte de su rareza.

## Identidad Visual
- **Explorer** como identidad visual principal.
- Lenguaje natural y elegante, fotografía como protagonista.
- Profundidad, materialidad y geometría Explorer.
- Estética moderna, apariencia premium, sensación de pieza de colección.
- Se prohíben temas globales Aurora/Eclipse, holografía generalizada, 75 % photo‑mode, fantasía medieval, RPG de combate y decoración arbitraria.

## Información de la Carta
- **Nombre de especie** (común).
- **Nombre científico** (máximo 2 líneas, con posible elipsis).
- **Datos de identidad** (rareza, firma del espécimen).
- **Estadísticas/Progresión** cuando corresponda (ligeras, RPG discreto).
- **Fotografía** del animal.
- **Elementos visuales de colección** (borde, marca Explorer).

## Thumbnail (Miniatura)
- Prioriza **animal**, **fotografía** y **rareza**.
- Evita saturación con estadísticas, rangos, metadata científica secundaria, elementos RPG complejos, siluetas complejas o información innecesaria.

## Experiencia de Usuario Completa (End‑to‑End)
1. El usuario abre la aplicación.
2. Accede a *Home*.
3. **Explora** entornos.
4. **Observa** un animal.
5. Inicia **identificación**.
6. Recibe el resultado de identificación.
7. Realiza la **captura**.
8. La captura genera un **espécimen único**.
9. El espécimen se transforma en una **carta**.
10. La carta se presenta al usuario.
11. La carta se incorpora a la **colección**.
12. El espécimen ocupa un **espacio disponible**.
13. El usuario puede **consultar su colección**.
14. El usuario continúa **explorando** y capturando.
15. El ciclo se repite hasta alcanzar el **límite de colección**.

## Colección – Sentimiento Personal
> “Estas son mis capturas. Estos son mis especímenes. Estas son mis cartas.”

- La colección es **personal** y muestra claramente que cada carta corresponde a una captura concreta.
- No es simplemente una enciclopedia; es una galería de los propios descubrimientos del usuario.

## Publicidad – Zonas Protegidas y Potenciales
### Zonas Protegidas (SIN PUBLICIDAD)
- **Observation**
- **Identification**
- **Inspection**
- **Capture** (resultado inmediato)
- **Revelación de la carta**
- **Presentación principal de la carta**

> La publicidad nunca debe interrumpir el momento de descubrimiento, identificación o adquisición del espécimen.

### Zonas Potenciales para Publicidad
- **Home**
- **Collection**
- Pantallas secundarias y espacios preparados (concepto **AdBannerSlot**).
- Experiencias recompensadas futuras, siempre bajo aprobación posterior del PM.

## Producto Usable – Definición
El producto se considera **usable** cuando cualquier persona puede completar el núcleo:
**Explorar → Identificar → Capturar → Obtener carta → Guardar → Coleccionar → Continuar explorando**.

No se declara que el proyecto esté finalizado; es la referencia *target* para futuras fases.

## Principios de Producto
1. La captura es el origen del espécimen.
2. El espécimen es individual.
3. La carta representa ese espécimen.
4. La colección pertenece al usuario.
5. La colección tiene capacidad limitada de 300 espacios.
6. La identidad científica debe mantenerse.
7. El RPG es ligero.
8. La experiencia de descubrimiento es prioritaria.
9. La publicidad nunca debe romper el momento emocional de captura.
10. La carta debe sentirse como una pieza coleccionable.
11. El producto debe ser comprensible a simple vista.
12. Las decisiones futuras deben respetar esta visión salvo nueva decisión explícita del PM/Director.

## Regla de Estabilidad del Producto
> Una vez revisado y aprobado por el PM, **PRODUCTO_FINAL_ESTABLE.md** será la referencia de producto para futuras fases.
>
> Toda tarea futura debe responder: *¿Esta tarea acerca el software al Producto Final Estable?*
>
> Si una tarea contradice esta visión, debe detenerse y elevarse al PM antes de implementarse.

---
*Este documento define la visión estable del producto y servirá como guía de referencia para todas las decisiones de desarrollo posteriores.*
