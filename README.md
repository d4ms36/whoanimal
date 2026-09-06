# WHO Animal 🐾

> *"Descubre. Identifica. Colecciona."*

**WHO Animal** es una experiencia interactiva de descubrimiento, aprendizaje y colección de fauna. Combina la identificación visual de animales con un sistema de **cartas coleccionables de doble cara**, complementadas con información científica rigurosa y una capa narrativa de fantasía (*Lore*), todo bajo un principio transversal e innegociable: **100% Pet Friendly**.

---

## 📌 Estado Actual del Proyecto

El proyecto se encuentra en la **Fase 0 — Fundación y Diseño de Producto**.

* El diseño conceptual, las mecánicas de colección y las reglas de contenido están siendo coordinados entre el Director Creativo (Usuario), el Project Manager (ChatGPT) y el Desarrollador Principal.
* La base arquitectónica inicial se ha diseñado en **Python 3.10+** bajo principios de código limpio y desacoplado, lista para evolucionar hacia servicios de visión por computadora, APIs y aplicaciones cliente.
* **No se han implementado mecánicas de juego definitivas, IA final ni bases de datos de producción todavía**, asegurando una base limpia y modular.

---

## 🧭 Pilares de Producto

1. **INFORMACIÓN REAL:** Datos científicos rigurosos, verificables y educativos (hábitat, dieta, comportamiento, etc.).
2. **EXPERIENCIA:** Estética visual atractiva, mecánica de cartas coleccionables de doble cara e interacción fluida.
3. **LORE:** Capa narrativa y mitológica independiente que convierte a los animales en personajes dentro del universo Who Animal, sin mezclarse jamás con la realidad biológica.
4. **100% PET FRIENDLY:** Todo el producto promueve la protección, respeto y observación responsable de los animales y la naturaleza, prohibiendo mecánicas de acoso, captura física o maltrato.

---

## 🛠️ Tecnología y Arquitectura

* **Lenguaje Principal:** Python 3.10+ (probado con Python 3.14).
* **Diseño Arquitectónico:** Clean Architecture modular (`domain`, `core`, `services`).
* **Dependencias Actuales:** 0 dependencias externas en runtime (uso puro de la biblioteca estándar para máxima portabilidad).
* **Testing:** `pytest` para verificación de modelos y contratos.

Para más detalles, consulta [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

---

## 📂 Estructura del Proyecto

```text
whoanimal/
├── .github/
│   ├── workflows/
│   │   └── ci.yml               # Pipeline de Integración Continua (GitHub Actions)
│   └── PULL_REQUEST_TEMPLATE.md # Guía y checklist de contribución
├── docs/
│   ├── GDD.md                   # Game Design Document (visión, cartas, core loop)
│   ├── CARD_SPEC.md             # Especificación técnica y campos de las cartas
│   ├── PRODUCT_RULES.md         # Reglas éticas y de separación Ciencia/Lore
│   ├── DISCLAIMER.md            # Aviso de responsabilidad educativa y de seguridad
│   └── ARCHITECTURE.md          # Especificación de capas y roadmap técnico
├── src/
│   └── whoanimal/
│       ├── core/                # Configuración global y excepciones base
│       ├── domain/              # Modelos de datos inmutables y enumeraciones
│       │   └── models/          # Entidades: AnimalProfile, Card, Lore
│       └── services/            # Protocolos e interfaces para IA, cartas y colección
├── tests/                       # Pruebas unitarias de contratos y separación de dominio
├── .gitignore                   # Exclusiones estándar para Python y sistemas operativos
├── pyproject.toml               # Configuración estándar PEP 621 y dependencias por fases
├── requirements.txt             # Dependencias de producción (fase actual: stdlib)
├── requirements-dev.txt         # Dependencias de desarrollo y test
└── README.md                    # Documento raíz de presentación
```

---

## 🚀 Preparación del Entorno de Desarrollo

### 1. Prerrequisitos
* Python 3.10 o superior instalado en el sistema.
* Git instalado.

### 2. Crear y activar el entorno virtual
En Windows (PowerShell):
```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
```

En macOS / Linux:
```bash
python3 -m venv .venv
source .venv/bin/activate
```

### 3. Instalar herramientas de desarrollo
```bash
pip install -r requirements-dev.txt
```

---

## 🧪 Ejecutar Pruebas

Para comprobar la integridad de los modelos de dominio y las especificaciones de separación de datos:

```bash
pytest
```

---

## 📚 Documentación de Referencia

* [Game Design Document (GDD)](docs/GDD.md)
* [Especificación de la Carta](docs/CARD_SPEC.md)
* [Reglas de Producto (100% Pet Friendly)](docs/PRODUCT_RULES.md)
* [Aviso de Responsabilidad](docs/DISCLAIMER.md)
* [Arquitectura Técnica](docs/ARCHITECTURE.md)

---

## 🗺️ Próximos Pasos

1. **Aprobación del GDD v0.2:** Revisión conjunta con el Director y Project Manager.
2. **Definición de Esquemas de Validación:** Adopción de Pydantic para serialización JSON estricta de las cartas.
3. **Módulo de Ingesta Taxonómica:** Conectores de prueba con APIs biológicas abiertas (GBIF / iNaturalist).
4. **Prototipo de Visión Artificial:** Evaluación de modelos base de clasificación animal (MobileNet / ViT).
