# WHO Animal — Visión de Producto y Marco Estratégico (Product Vision)

**Documento:** `docs/PRODUCT_VISION.md`  
**Versión:** 1.0 (Rebaseline Oficial — WHO-DOC-001 / DEC-053)  
**Propósito:** Documento maestro, integral y legible para humanos sobre la visión global del producto, su arquitectura conceptual en capas, el diseño Free-to-Play, la experiencia de usuario y su evolución estratégica.  
**Última actualización:** 2026-09-07  

---

## 1. Declaración de Visión

> **WHO Animal = Identificación de Animales + Cartas Coleccionables + Enciclopedia Personal + Juego Ligero + Capa Social.**
> 
> *"Descubre. Identifica. Colecciona."*

**WHO Animal** trasciende el concepto de una utilidad técnica de identificación visual de fauna. Es una experiencia integral que conecta a las personas con la biodiversidad del planeta mediante el descubrimiento guiado, el rigor científico, el coleccionismo interactivo, la expresión personal a través de micro-relatos (*Lore*), dinámicas de juego ligeras y una comunidad respetuosa de la naturaleza.

WHO Animal es un producto **Free-to-Play (F2P) por diseño**. La publicidad respetuosa y una economía transparente forman parte de la visión comercial del producto, manteniéndose desacopladas del conocimiento zoológico y estructuradas para que el producto pueda escalar sin comprometer jamás su núcleo ético, pedagógico y biológico.

---

## 2. Los Tres Pilares de Arquitectura de Producto

Para garantizar escalabilidad, robustez conceptual y protección contra la degradación de la experiencia, WHO Animal se estructura en tres capas jerárquicas estrictamente delimitadas:

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

### 2.1. CAPA CORE (El Núcleo Irrenunciable)
Es la base fundacional de WHO Animal. Si se eliminan temporal o permanentemente las capas *Game* o *Social*, WHO Animal **sigue siendo una aplicación plenamente funcional, valiosa y educativa** de identificación, aprendizaje y colección.
* **Pipeline canónico:**
  $$\text{Capture} \rightarrow \text{Observation} \rightarrow \text{IdentificationResult} \rightarrow \text{IdentificationDecision} \rightarrow \text{Capture} \rightarrow \text{Card} \rightarrow \text{Persistence} \rightarrow \text{Collection}$$
* **Regla existencial:** Si se altera o destruye la secuencia `Identificar → Carta → Coleccionar`, la aplicación deja de ser WHO Animal.

### 2.2. CAPA GAME (Juego Ligero y Progresión)
Añade profundidad lúdica y rejugabilidad a la colección de cartas:
* Rareza de colección de las cartas (desacoplada de la rareza biológica real).
* Estadísticas de juego para dinámicas de rol livianas (HP, ATK, DEF, SPD, TYPE, SPECIAL).
* Sistema de duelos PvP con equipos de 10 cartas configurables semanalmente.
* Progresión de maestría del explorador, niveles de cartas (*rank*), logros y recompensas.
* Economía F2P con moneda gratuita obtenible jugando y tienda de personalización cosmética.
* Temporadas, misiones temáticas y eventos especiales de descubrimiento.
* **Regla estricta:** *Las estadísticas y dinámicas de juego jamás deben confundirse ni presentarse como información científica real de los animales.*

### 2.3. CAPA SOCIAL (Comunidad y Conexión)
Fomenta el intercambio y el aprendizaje compartido entre exploradores:
* Perfiles de explorador y listas de amigos.
* Envío de regalos diarios no invasivos.
* Intercambio controlado de cartas (*Trading*), preservando inmutable la identidad histórica original de la pieza (`card_id`, espécimen, población en emisión, serial de autenticación).
* Exhibición de colecciones públicas o vitrinas destacadas.
* Contenido generado por usuarios (UGC) en Historias Personales (*Lore*).
* Herramientas activas de moderación, prevención de toxicidad, reporte y bloqueo.

---

## 3. Experiencia de Entrada y Navegación Principal

### 3.1. Entrada y Bienvenida
El acceso a WHO Animal debe ser inmediato, acogedor y sin fricción técnica:
* **Creación de cuenta:** Registro sencillo de explorador (soporte para perfiles locales offline en etapas tempranas y cuentas en la nube en fases avanzadas).
* **Modo Invitado (Guest Mode):** Permite explorar inmediatamente el flujo principal sin barreras de autenticación obligatoria.
* **Selector de Idioma:** Configuración lingüística inicial con traducciones desacopladas de la taxonomía zoológica.
* **Preferencias:** Control de audio, vibración háptica, permisos de cámara y políticas de privacidad desde el primer instante.

### 3.2. Centro de Navegación (Home Hub)
La pantalla principal actúa como centro neurálgico despejado, visual y no sobrecargado:

```text
                                  HOME
                                   │
     ┌─────────────┬───────────────┼───────────────┬─────────────┐
     ▼             ▼               ▼               ▼             ▼
  CAPTURE     BAÚL / CORRAL     REWARDS          SHOP         PROFILE
 (Cámara)     (Colección)     (Recompensas)    (Tienda)     (Explorador)
     │                                                           │
     └───────────────────────┬───────────────────────────────────┘
                             ▼
                    SETTINGS / SOCIAL / PVP
```

* **Estética:** Moderna, inmersiva, con paleta cromática naturalista, micro-interacciones suaves y visualización destacada de la última carta descubierta o el estado de la colección.

---

## 4. Captura — Experiencia Principal (The Golden Experience)

La captura es el momento culminante de conexión entre el mundo real y el universo de WHO Animal. Debe transmitir entusiasmo, rigor y recompensa:

```text
HOME
 ↓
CAPTURE (Cámara en vivo + Guía de encuadre "Wildlife Reticle")
 ↓
PHOTO PREVIEW (Revisión rápida de la toma)
 ↓
IDENTIFICATION / LOADING (Procesamiento pedagógico con feedback visual)
 ↓
IDENTIFICATION RESULT (Especie sugerida, alternativas y confianza orientativa)
 ↓
CARD REVEAL (Animación de acuñación / revelación de la carta)
 ↓
CARD FRONT (Vista frontal: Arte, nombres, marco, rareza)
 ↓
CARD FLIP (Animación tridimensional de volteo táctil)
 ↓
CARD BACK (Vista posterior: Ciencia contrastada vs. Lore personal)
 ↓
REVIEW & ACTIONS
 ├── SAVE (Guardar en Baúl / Corral)
 ├── QUICK SAVE (Guardado ágil en primer slot libre)
 └── RELEASE / RE-CAPTURE (Liberar espécimen o reintentar fotografía)
```

### Protección de la Experiencia de Captura (Ad-Free Zone)
Queda estrictamente prohibida la inserción de publicidad intrusiva (banners, vídeos o intersticiales) durante:
* La apertura y uso de la cámara.
* El instante de encuadre y disparo.
* El procesamiento y carga de la identificación.
* La animación de revelación y primer volteo de la carta.

---

## 5. Anatomía de la Carta: Dos Caras, Dos Mundos

Cada carta emitida representa un espécimen observado y se divide en dos caras complementarias:

### 5.1. Cara Frontal (Card Front)
Diseñada para fascinar, reconocer y coleccionar:
* Fotografía o arte zoológico destacado del ejemplar.
* Nombre común en tipografía prominente (e.g., *Lince Ibérico*).
* Nombre científico en tipografía secundaria (e.g., *Lynx pardinus*).
* Categoría taxonómica / Icono biológico (Mamífero, Ave, Reptil, etc.).
* Identificador visual de colección y número de espécimen (e.g., `#0042`).
* Indicador visual de rareza de colección (marco, foil o brillos según corresponda).
* Atributos de juego ligero cuando estén activos en la Capa Game.

### 5.2. Cara Posterior (Card Back)
Estructurada con claridad tipográfica para educar y registrar la memoria del explorador, dividida estrictamente en dos mundos:

#### A. Mundo Científico (Información Real y Factual)
* **Taxonomía:** Reino, Filo, Clase, Orden, Familia, Género, Especie.
* **Biología y Ecología:** Hábitat, distribución geográfica, alimentación y comportamiento.
* **Morfología:** Tamaño y peso promedio representativo.
* **Conservación:** Estado poblacional, indicador sutil de protección o rareza natural.
* **Curiosidades:** Datos verídicos y asombrosos sobre su adaptación biológica.
* **Avisos Preventivos de Seguridad:** Indicadores de ⚠️ *Precaución* o ⚠️ *Peligro* objetivos y serenos (exclusivamente para especies con riesgo comprobado: ponzoña, veneno, mordedura).

#### B. Mundo Personal (Memoria del Explorador)
* **Fecha y Momento:** Timestamp de la observación.
* **Ubicación Generalizada:** `display_location` (e.g., *Serranía de Ronda, España*), protegiendo rigurosamente las coordenadas exactas para salvaguardar a la fauna contra la caza furtiva o perturbación humana.
* **Historia Personal (Lore):** Micro-relato personal del usuario sobre el encuentro (hasta 300 caracteres, sujeto a reglas de edición controlada).

> **Aviso de Gobernanza:** *La información científica jamás se confunde con el Lore personal. La ciencia es inmutable y factual; el Lore es la voz del observador.*

---

## 6. Baúl / Corral (La Experiencia de Colección)

El **Baúl o Corral** es el inventario central donde vive la colección del usuario:
* **Visualización de Contenedores:** Organización por cajas, hábitats o biomas.
* **Inspección de Cartas:** Apertura fluida en pantalla completa con soporte de giro 3D táctil (*flip*).
* **Edición Permitida:** Gestión de la Historia Personal (*Lore*) dentro de los límites de cuenta establecidos.
* **Eliminación Segura:** Liberación confirmada de cartas con doble verificación para evitar pérdidas accidentales.
* **Capacidad Alpha vs. Capacidad Futura:**
  * La persistencia actual de **10 containers × 30 espacios = 300 cartas** constituye la capacidad oficial de la fase *Alpha / Foundation*.
  * Esta capacidad técnica responde a optimizaciones locales tempranas y **no es una limitación conceptual permanente del producto final**. Las fases posteriores permitirán ampliar contenedores mediante progresión y utilidades del juego.

---

## 7. Principios Fundamentales Free-to-Play (F2P)

> **"WHO Animal es Free-to-Play por diseño."**

El acceso al conocimiento, la apreciación de la naturaleza y el coleccionismo básico son derechos inalienables del usuario en la plataforma:

1. **Acceso Universal Gratuito:** Identificar animales, aprender sobre su biología, acuñar cartas y almacenarlas en la colección base es y será siempre 100% gratuito.
2. **Cero Pay-to-Win (No P2W):** Ningún pago con dinero real podrá otorgar ventajas biológicas ficticias ni superioridad competitiva injusta en duelos.
3. **Prohibición Absoluta de Venta de Verdad Científica:**
   * Jamás se venderá mayor precisión o "mejor identificación" zoológica.
   * Jamás se permitirá comprar modificaciones a datos taxonómicos, hábitats o dietas.
   * La rareza histórica de emisión de una carta (`population_at_issuance`) no puede comprarse retroactivamente.
4. **Respeto Económico al Usuario:** Ausencia de mecánicas depredadoras, precios opacos o barreras artificiales diseñadas para extorsionar la progresión del explorador.

---

## 8. Arquitectura Publicitaria Desacoplada (Advertising Experience)

La publicidad es una vía legítima y permanente para sustentar el desarrollo continuo de WHO Animal, implementada bajo una arquitectura limpia y desacoplada del dominio zoológico:

```text
┌─────────────────────────────────────────────────────────────┐
│                    INFRAESTRUCTURA PUBLICITARIA             │
├─────────────────────────────────────────────────────────────┤
│  AdService                                                  │
│  ├── BannerPlacement      (Puntos de montaje no intrusivos) │
│  ├── InterstitialPolicy   (Reglas de frecuencia controlada) │
│  ├── RewardedAdService    (Recompensas voluntarias opt-in)  │
│  └── FrequencyPolicy      (Límites y enfriamientos globales)│
└─────────────────────────────────────────────────────────────┘
```

### 8.1. Banner Ads (Publicidad Estática Discreta)
* **Permitidos en:** Home, Baúl / Corral, Shop, Profile, Rewards, pantallas de configuración.
* **Prohibidos en:** Cámara en vivo, pantalla de captura, visor de identificación, revelación de carta, lectura de enciclopedia zoológica y combates PvP.

### 8.2. Interstitial Ads (Anuncios a Pantalla Completa en Transiciones Naturales)
* Se ejecutan únicamente en pausas lógicas (e.g., al salir de la colección hacia el Home).
* Su cadencia se rige por una **política de frecuencia configurable** (`FrequencyPolicy`), evitando apariciones consecutivas o molestas.
* Nunca se muestran durante momentos de decisión crítica o captura.

### 8.3. Rewarded Ads (Anuncios Voluntarios por Recompensa)
* El usuario decide voluntariamente visualizar un anuncio a cambio de un beneficio transparente:
  * Oportunidad de una edición adicional de Historia Personal (*Lore*).
  * Moneda gratuita de juego (*Free Currency*).
  * Recompensas cosméticas temporales o multiplicadores de experiencia de exploración.
* **Regla de Oro:** Siempre debe existir la opción explícita y respetuosa de declinar:
  > *"No, gracias."*
* Los anuncios recompensados jamás son obligatorios para progresar.

---

## 9. Sistema de Economía y Tienda (Shop)

La economía de WHO Animal se despliega progresivamente con una estructura sencilla, ética y orientada a la personalización visual:

```text
RECOMPENSAS / GAMEPLAY / DESCUBRIMIENTO
                 ↓
      MONEDA GRATUITA (Coins)
                 ↓
             TIENDA (Shop)
  (Temas, Marcos, Fondos, Cosméticos, Avatares)
```

* **Moneda Gratuita:** Se obtiene exclusivamente jugando, descubriendo nuevas especies, completando familias biológicas y cumpliendo metas de observación.
* **Catálogo de Tienda:**
  * Temas visuales para las cartas (marcos artísticos, estilos vintage, modernos o neón).
  * Fondos de exhibición para el Baúl/Corral.
  * Efectos holográficos cosméticos para cartas favoritas.
  * Avatares y títulos de explorador.
  * Utilidades no competitivas (slots adicionales de contenedor).
* **Moneda Premium:** Podrá evaluarse en fases posteriores para comodidades cosméticas, pero **no se asume ni se implementa de forma inmediata**.

---

## 10. Sistema de Recompensas y Logros

Diseñado para incentivar la curiosidad científica sin generar ansiedad ni fatiga:
* **Hitos de Descubrimiento:** Bonificación por la primera identificación, descubrimiento de una nueva especie o registro de especies amenazadas.
* **Logros Taxonómicos:** Reconocimiento al completar órdenes o familias zoológicas (e.g., *Familia Canidae*, *Aves Rapaces*).
* **Logros Biogeográficos:** Reconocimiento por registrar fauna en biomas específicos (Bosque Mediterráneo, Humedales, Desierto, Selva Tropical).
* **Recompensas Diarias Suaves:** Incentivos por conexión o avistamiento sin penalizaciones agresivas por días de ausencia.

---

## 11. Sistema de Enfrentamientos PvP (Juego Ligero de Cartas)

El sistema de duelos pertenece a la **Capa Game** y se incorporará tras la estabilización del Core y el lanzamiento 1.0:
* **Dinámica:** Batallas de cartas ágiles, por turnos y accesibles, inspiradas en mecánicas RPG livianas.
* **Equipo de Exploración:**
  * Configuración de un **equipo activo de 10 cartas**.
  * El equipo se modifica exclusivamente dentro de una **ventana estratégica semanal**, fomentando la planificación y evitando el cambio reactivo permanente.
* **Estadísticas Ficticias de Juego (RPG Stats):**
  ```text
  HP (Puntos de Vida)      ATK (Ataque)
  DEF (Defensa)            SPD (Velocidad)
  TYPE (Elemento/Bioma)    SPECIAL (Habilidad de soporte)
  ```
* **Separación Taxonómica Innegociable:** Los atributos de combate responden exclusivamente a balance de juego lúdico. No representan fuerza, ferocidad ni superioridad de las especies reales en la naturaleza.
* **Ética 100% Pet Friendly:** Sin representaciones de violencia gráfica, sangre ni crueldad animal.

---

## 12. Capa Social y de Comunidad

Evolución planificada para conectar a la comunidad global de naturalistas:
* **Perfiles Públicos:** Vitrina personal donde exhibir cartas destacadas y estadísticas de avistamiento.
* **Amigos y Regalos:** Red de contactos para compartir regalos diarios de exploración.
* **Intercambio (Trading):** Sistema seguro de intercambio de cartas entre jugadores. Al transferirse una carta, se actualiza el `owner_id` pero **permanecen estrictamente congelados los metadatos de emisión histórica** (`specimen_number`, `issued_at`, rareza de emisión, población histórica y serial).
* **Contenido Generado por Usuarios (UGC):** Historias Personales compartidas públicamente en perfiles comunitarios.
* **Seguridad y Moderación:** Sistemas obligatorios de reporte de abusos, bloqueo de usuarios, filtros de lenguaje y cumplimiento estricto de protección de menores.

---

## 13. Ajustes y Configuración (Settings)

El centro de control de usuario incluirá:
* Gestión de perfil y cuenta de explorador.
* Selector de idioma de la interfaz.
* Configuración de sonido, música de ambientación y efectos hápticos.
* Gestión de notificaciones push (eventos, avistamientos).
* Opciones de privacidad, telemetría y preferencias de anuncios.
* Gestión de almacenamiento local y caché de fotografías.
* Centro de ayuda, aviso legal ([DISCLAIMER.md](DISCLAIMER.md)), términos de servicio y eliminación definitiva de cuenta.

---

## 14. Internacionalización Transversal (i18n)

La accesibilidad idiomática es un principio de diseño transversal. Se prohibe el texto hardcodeado en la interfaz y se respeta la separación tripartita lingüística:

```text
TRADUCCIONES DE UI
        ≠
CONTENIDO CIENTÍFICO (Taxonomía, descripciones zoológicas multilingües)
        ≠
LORE PERSONAL (Idioma original del explorador, inmutable por traducción automática)
```

* Los nombres científicos en latín (*Vulpes vulpes*) permanecen universales e invariantes en cualquier idioma.
* Los nombres comunes y fichas pedagógicas se adaptan al idioma o región seleccionada.
* El Lore personal escrito por el usuario se conserva fielmente en su lengua de origen.

---

## 15. Hoja de Ruta Estratégica Oficial (Roadmap Rebaseline)

El ciclo de desarrollo de WHO Animal se organiza en 7 fases consecutivas y bien delimitadas:

```text
1. FOUNDATION (Completada)
       ↓
2. ALPHA (Core Loop Funcional — En Curso)
       ↓
3. BETA (Estabilidad, UX y Dispositivos Reales)
       ↓
4. RELEASE 1.0 (Lanzamiento Público Comercial)
       ↓
5. RELEASE 1.x — GAME (PvP, Equipos de 10, Temporadas y Logros)
       ↓
6. RELEASE 2.x — SOCIAL (Amigos, Trading, Vitrinas y Moderación)
       ↓
7. RELEASE 3.x — WORLD (Biomas Globales, Multidispositivo y Cloud)
```

* **1. FOUNDATION:** Modelos de dominio inmutables, catálogo zoológico inicial de 28 especies validadas, arquitectura limpia, tooling de pruebas y base técnica Android.
* **2. ALPHA:** Cierre del Golden Path: *Login local → Home → Capture (CameraX) → Observation → Identification → Decision → Capture → Card → Flip → Persistence (Room 10x30=300) → Storage*.
* **3. BETA:** Pulido de UX, compatibilidad con múltiples terminales Android, accesibilidad, internacionalización básica, rendimiento, auditoría legal y preparación de infraestructura publicitaria.
* **4. RELEASE 1.0:** Lanzamiento público oficial en Google Play Store con el Core Loop completo, enciclopedia zoológica, Baúl personal, personalización básica, moneda gratuita, tienda cosmética y publicidad desacoplada. *(El PvP no bloquea Release 1.0).*
* **5. RELEASE 1.x (GAME):** Introducción de la Capa Game: duelos PvP con equipos de 10 cartas, rankings de exploradores, temporadas estacionales, misiones y logros.
* **6. RELEASE 2.x (SOCIAL):** Introducción de la Capa Social: amigos, intercambio seguro de cartas (*trading*), vitrinas públicas comunitarias, moderación y herramientas de seguridad.
* **7. RELEASE 3.x (WORLD):** Expansión del catálogo a escala mundial, sincronización multidispositivo en la nube, eventos biogeográficos globales y experiencias de exploración avanzada.

---

## 16. Realidad Tecnológica del Proyecto

WHO Animal reconoce y formaliza la coexistencia sinérgica de sus dos entornos tecnológicos:

```text
PYTHON (stdlib + pytest)
• Dominio zoológico de referencia, servicios taxonómicos y validación de datasets.
• Especificación de contratos inmutables de Card y catálogo zoológico JSON.
• Tooling de testing y verificación matemática de modelos.

ANDROID + KOTLIN (Kotlin 2.0 + Jetpack Compose + Material 3 + Room + CameraX)
• Aplicación móvil interactiva nativa para el usuario final.
• Captura visual nativa con CameraX y control de ciclo de vida.
• Interfaz declarativa, animaciones tridimensionales de volteo y navegación de flujo.
• Persistencia local de alto rendimiento mediante Room / SQLite.
```

Ambos entornos trabajan de forma armónica para asegurar rigor científico en el dominio y excelencia sensorial en la experiencia móvil.
