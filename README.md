# WHO Animal 🐾

> *"Descubre. Identifica. Colecciona."*

**WHO Animal** es una experiencia interactiva de descubrimiento, aprendizaje y colección de fauna. Combina la identificación visual de animales con un sistema de **cartas coleccionables de doble cara**, complementadas con información científica rigurosa y una Historia Personal escrita por el usuario (*Lore*), todo bajo un principio transversal e innegociable: **100% Pet Friendly**.

---

## 📌 Estado Actual del Proyecto

El proyecto se encuentra en la **Fase 0 — Fundación y Diseño de Producto**.

* El diseño conceptual, las mecánicas de colección y las reglas de contenido están siendo coordinados entre el Director Creativo (Usuario), el Project Manager (ChatGPT) y el Desarrollador Principal.
* La base arquitectónica inicial se ha diseñado en **Python 3.10+** bajo principios de código limpio y desacoplado, lista para evolucionar hacia servicios de visión por computadora, APIs y aplicaciones cliente.
* **No se han implementado mecánicas de juego definitivas, IA final ni bases de datos de producción todavía**, asegurando una base limpia y modular.
* El modelo de negocio inicial se ha formalizado formalmente (DEC-038): el núcleo de la aplicación es 100% gratuito (Free-to-Play) en todo el mundo, sustentado inicialmente en publicidad no intrusiva gestionada como infraestructura externa estricta, sin mecanismos prematuros de tienda o monedas in-app.
* Las extensiones futuras aprobadas (PVP desacoplado, intercambio/comercio de cartas y arte exclusivo con red de ilustradores) se encuentran formalmente registradas en la memoria técnica para garantizar una arquitectura evolutiva sin anticipar código innecesario.

---

## 🧭 Pilares de Producto

1. **INFORMACIÓN REAL:** Datos científicos rigurosos, verificables y educativos (hábitat, dieta, comportamiento, etc.).
2. **EXPERIENCIA:** Estética visual atractiva, mecánica de cartas coleccionables de doble cara e interacción fluida.
3. **LORE (Historia Personal):** Capa de contenido personal escrita por el usuario, asociada a una carta específica, que refleja su experiencia individual (sometida a reglas de edición y sin mezclarse jamás con la realidad biológica).
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
│   ├── ARCHITECTURE.md          # Especificación de capas y roadmap técnico
│   ├── CARD_SPEC.md             # Especificación técnica y campos de las cartas
│   ├── DECISIONS.md             # Registro formal de decisiones (ADR)
│   ├── DISCLAIMER.md            # Aviso de responsabilidad educativa y de seguridad
│   ├── GDD.md                   # Game Design Document (visión, cartas, core loop)
│   ├── GOVERNANCE.md            # Modelo de roles (Director, PM, Developer) y flujo
│   ├── PLANNING.md              # Plan Maestro de desarrollo y alineación de versión
│   ├── PRODUCT_RULES.md         # Reglas éticas y de separación Ciencia/Lore
│   ├── PROJECT_CONTEXT.md       # Memoria oficial y los 8 pilares del producto
│   ├── RELEASES.md              # Registro histórico de versiones y builds Android
│   ├── ROADMAP.md               # Planificación estratégica por fases y objetivos
│   └── VERSIONING.md            # Esquema semántico y control de versionCode
├── src/
│   └── whoanimal/
│       ├── core/                # Configuración global y excepciones base
│       ├── domain/              # Modelos de datos inmutables y enumeraciones
│       │   └── models/          # Entidades: AnimalProfile, Card, Lore
│       └── services/            # Protocolos e interfaces para IA, cartas y colección
├── tests/                       # Pruebas unitarias de contratos y separación de dominio
├── AGENTS.md                    # Manual normativo obligatorio para agentes de desarrollo
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
*(O con la biblioteca estándar: `python -m unittest discover -s tests -p "test_*.py"` configurando `PYTHONPATH=src`).*

---

## 📚 Documentación de Referencia

* **Gobernanza y Operación:**
  * [Manual para Agentes (AGENTS.md)](AGENTS.md)
  * [Plan Maestro de Desarrollo](docs/PLANNING.md)
  * [Modelo de Gobernanza](docs/GOVERNANCE.md)
  * [Flujo de Trabajo por Objetivos](docs/WORKFLOW.md)
  * [Registro de Decisiones (ADR)](docs/DECISIONS.md)
* **Producto y Diseño de Juego:**
  * [Memoria del Proyecto](docs/PROJECT_CONTEXT.md)
  * [Game Design Document (GDD)](docs/GDD.md)
  * [Especificación de la Carta](docs/CARD_SPEC.md)
  * [Reglas de Producto (100% Pet Friendly)](docs/PRODUCT_RULES.md)
  * [Aviso de Responsabilidad](docs/DISCLAIMER.md)
* **Arquitectura y Versionado:**
  * [Arquitectura Técnica](docs/ARCHITECTURE.md)
  * [Sistema de Versionado y Android versionCode](docs/VERSIONING.md)
  * [Registro de Releases](docs/RELEASES.md)
  * [Hoja de Ruta (Roadmap)](docs/ROADMAP.md)

---

## 🗺️ Próximos Pasos

1. **Aprobación del GDD v0.2:** Revisión conjunta con el Director y Project Manager.
2. **Definición de Esquemas de Validación:** Adopción de Pydantic para serialización JSON estricta de las cartas.
3. **Módulo de Ingesta Taxonómica:** Conectores de prueba con APIs biológicas abiertas (GBIF / iNaturalist).
4. **Prototipo de Visión Artificial:** Evaluación de modelos base de clasificación animal (MobileNet / ViT).
