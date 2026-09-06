# Arquitectura Técnica — Who Animal

**Versión:** 0.1 (Fase de Fundación)  
**Tecnología Principal:** Python 3.10+  
**Paradigma:** Clean Architecture / Domain-Driven Design (DDD) modular y desacoplado

---

## 1. Visión y Principios Arquitectónicos

La arquitectura de Who Animal está concebida para crecer de forma orgánica sin acoplamientos prematuros. Sus principios rectores son:

1. **Aislamiento del Dominio (`domain`):** La lógica de negocio, las reglas de las cartas y la separación entre *Ciencia* y *Lore* no dependen de ningún framework web, base de datos ni motor de IA específico.
2. **Interfaces y Protocolos (`services`):** La identificación visual, la generación de cartas y la persistencia se definen mediante abstracciones (`typing.Protocol`). Esto permite sustituir implementaciones simuladas (mocks) por modelos de Machine Learning (ONNX, PyTorch) o APIs externas (iNaturalist, GBIF) sin alterar el resto del sistema.
3. **Cero Dependencias Pesadas Iniciales:** En esta fase de fundación, el núcleo funciona íntegramente sobre la biblioteca estándar de Python (dataclasses, typing, enum), garantizando portabilidad y velocidad de testeo.
4. **Preparado para Clientes Múltiples:** La capa de servicios podrá exponerse fácilmente vía FastAPI / REST a clientes móviles (Flutter / iOS / Android) o aplicaciones web.

---

## 2. Mapa de Capas del Sistema

```text
┌────────────────────────────────────────────────────────┐
│               CAPA DE APLICACIÓN / APIS                │
│    (FastAPI / CLI / SDK de Integración con Clientes)   │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                  CAPA DE SERVICIOS                     │
│  • IdentificationService (Protocolo de Visión / IA)    │
│  • CardService (Ensamble de cartas y metadatos)        │
│  • CollectionService (Inventario y gestión de álbum)   │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                   CAPA DE DOMINIO                      │
│  • Modelos: AnimalProfile, ScientificInfo, Card, Lore  │
│  • Enums: AnimalCategory, DangerLevel, ProtectionStatus│
│  • Reglas: Validación de Lore vs. Hechos Factuales     │
└────────────────────────────────────────────────────────┘
```

---

## 3. Modelo de Datos del Dominio

* **`ScientificInfo`:** Contenedor inmutable de datos biológicos (nombre común, nombre científico, hábitat, distribución, dieta, comportamiento, morfología, fuentes).
* **`ConservationIndicators`:** `is_protected: bool`, `is_rare_species: bool`.
* **`DangerAssessment`:** Nivel de peligro preventivo (`NONE`, `PRECAUTION`, `DANGER`), factores de riesgo y advertencia pedagógica.
* **`LoreProfile`:** Ficción y narrativa independiente, rotulada explícitamente.
* **`AnimalCard`:** Estructura bidireccional compuesta por `CardFront`, `CardBack` y `CardMetadata`.

---

## 4. Hoja de Ruta de Integración Técnica

| Fase | Enfoque | Tecnologías Previstas |
| :--- | :--- | :--- |
| **Fase 0 (Actual)** | Fundación, modelos de dominio, documentación, setup Git/GitHub | Python stdlib, pytest |
| **Fase 1** | Validación robusta de esquemas y conectores a APIs biológicas | Pydantic v2, HTTPX, Pillow |
| **Fase 2** | Pipeline de identificación visual por imagen | ONNX Runtime / PyTorch, OpenCV |
| **Fase 3** | Exposición de backend e inventario persistente | FastAPI, SQLModel / SQLAlchemy, SQLite/PostgreSQL |
| **Fase 4** | Integración con cliente de experiencia visual | API REST / WebSockets hacia app cliente |
