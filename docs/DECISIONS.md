# Registro Oficial de Decisiones de Arquitectura y Producto (ADR)

**Documento:** `docs/DECISIONS.md`  
**Propósito:** Registrar las decisiones formales tomadas a lo largo de la vida del proyecto para garantizar la trazabilidad del diseño y evitar la reapertura arbitraria de debates zanjados.

---

## Estructura Estándar de una Decisión

```text
[ID]: [Título Breve]
Tema: [Área de impacto]
Fecha: [AAAA-MM-DD]
Estado: [APPROVED | PENDING | DEPRECATED | SUPERSEDED]
Decisión: [Qué se decidió de forma concisa]
Motivo: [Por qué se tomó esta decisión / alternativas descartadas]
Impacto: [Consecuencias en arquitectura, producto o procesos]
Aprobado por: [Director Creativo / Project Manager / Consenso]
```

---

## Decisiones Aprobadas (Approved)

### DEC-001: WHO Animal como Experiencia Integral y no solo Identificador
* **Tema:** Visión de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** La identificación visual de fauna mediante IA no es el objetivo final, sino la puerta de entrada a una experiencia completa de descubrimiento, cartas coleccionables, aprendizaje científico y Lore.
* **Motivo:** Diferenciar el producto de utilidades genéricas de cámara, fomentando el compromiso a largo plazo y la apreciación de la naturaleza.
* **Impacto:** Todas las pantallas, interfaces y modelos de datos deben subordinarse a la narrativa de colección y descubrimiento.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-002: Anatomía de la Carta con Doble Cara (Frente y Reverso)
* **Tema:** Diseño de Cartas y UX
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Cada carta tendrá un Frente visualmente atractivo (imagen de calidad, nombre común, nombre científico secundario, categoría y códigos) y un Reverso estructurado con datos zoológicos, indicadores secundarios, curiosidades y Lore.
* **Motivo:** Evitar la sobrecarga cognitiva en la primera impresión visual manteniendo a su vez la profundidad formativa accesible al voltear.
* **Impacto:** Los modelos de datos desacoplan explícitamente `CardFront` y `CardBack`.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-003: Separación Estricta entre Información Real, Experiencia y Lore
* **Tema:** Reglas de Contenido y Ética Pedagógica
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los datos biológicos reales y la capa de narrativa mitológica/fantasía (Lore) deben vivir en contenedores independientes y portar etiquetas explícitas. Jamás se presentará el Lore como un hecho zoológico.
* **Motivo:** Preservar la función educativa y científica sin renunciar a la creatividad lúdica.
* **Impacto:** Modelos `ScientificInfo` y `LoreProfile` separados en el dominio. El Lore incorpora obligatoriamente un disclaimer de ficción.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-004: Indicadores de Protección y Rareza como Elementos Secundarios y Discretos
* **Tema:** UX y Formato de Datos
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los indicadores de si una especie está protegida o si es biológicamente rara se mostrarán mediante pequeños sellos discretos (`Sí/No`), sin transformar la carta en un formulario burocrático de conservación.
* **Motivo:** No convertir la aplicación en un catálogo sombrío o sobrecargado de leyes ambientales, manteniendo la experiencia estimulante y accesible.
* **Impacto:** Inclusión de booleanos en `ConservationIndicators` diseñados para insignias minimalistas.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-005: Avisos de Precaución y Peligro Exclusivamente Cuando Corresponda y Sin Exageración
* **Tema:** Seguridad Ciudadana y Ética de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Los avisos de ⚠️ *Precaución* o ⚠️ *Peligro* se activarán únicamente para especies con riesgos reales (veneno activo, mordedura, territorialismo salvaje). Queda prohibido el sensacionalismo o exagerar peligros para generar morbo.
* **Motivo:** Fomentar una convivencia responsable y prudente con la fauna sin estigmatizar a los animales.
* **Impacto:** Modelo `DangerAssessment` condicional que solo se renderiza cuando `has_warning` es verdadero.
* **Aprobado por:** Director Creativo / Project Manager

---

### DEC-006: Arquitectura Limpia y Desacoplada (Clean Architecture / Domain-Driven Design)
* **Tema:** Arquitectura de Software
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** El núcleo de la aplicación se estructurará en `domain` (modelos inmutables), `services` (interfaces/protocolos abstractos) y `core` (configuración y excepciones), desacoplado de interfaces gráficas o motores de IA concretos.
* **Motivo:** Permitir que el sistema evolucione sin rehacer código de negocio al conectar modelos locales de ML, APIs remotas o clientes móviles (Flutter/Web).
* **Impacto:** Implementación de protocolos `typing.Protocol` y aislamiento absoluto de las dependencias externas.
* **Aprobado por:** Project Manager / Developer

---

### DEC-007: Cero Dependencias Innecesarias en Fase de Fundación
* **Tema:** Gestión de Dependencias
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** Mantener cero dependencias externas de producción durante la Fase 0, utilizando exclusivamente la biblioteca estándar de Python (`dataclasses`, `enum`, `typing`).
* **Motivo:** Evitar la instalación prematura de librerías pesadas (PyTorch, TensorFlow, frameworks web) sin justificación técnica inmediata, garantizando builds ligeras y reproducibles.
* **Impacto:** `requirements.txt` limpio; dependencias categorizadas en `pyproject.toml` como previstas y futuras.
* **Aprobado por:** Project Manager / Developer

---

### DEC-008: Principio Fundamental 100% Pet Friendly
* **Tema:** Filosofía de Producto
* **Fecha:** 2026-09-06
* **Estado:** `APPROVED`
* **Decisión:** El bienestar, la protección y la preservación animal tienen prioridad absoluta sobre cualquier mecánica lúdica o esquema de monetización. Prohibida toda mecánica que premie la persecución, captura física o daño a animales.
* **Motivo:** Compromiso ético inquebrantable con la conservación y el respeto ambiental.
* **Impacto:** Criterio rector para la aprobación de cualquier futura propuesta de juego o monetización.
* **Aprobado por:** Director Creativo

---

## Decisiones Pendientes de Aprobación (Pending)

### DEC-009-PENDING: Motor Definitivo de Identificación Visual
* **Tema:** Tecnología de Visión / IA
* **Estado:** `PENDING`
* **Opciones en evaluación:**
  1. Modelo ligero local on-device (MobileNet / EfficientNet optimizado con ONNX).
  2. Integración híbrida con APIs públicas de taxonomía (iNaturalist / GBIF).
  3. Modelo multimodal de visión en la nube vía backend propio.
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-010-PENDING: Estilo y Universo Mitológico del Lore
* **Tema:** Diseño Narrativo
* **Estado:** `PENDING`
* **Opciones en evaluación:**
  1. Universo mitológico unificado de fantasía (ej. "Los Guardianes del Equilibrio").
  2. Adaptación de fábulas y leyendas folclóricas reales según el bioma y región del animal.
* **Decisión requerida de:** Director Creativo

---

### DEC-011-PENDING: Formato Definitivo de Códigos de Carta
* **Tema:** Identificadores de Colección
* **Estado:** `PENDING`
* **Propuesta actual:** Formato `WA-[CATEGORÍA]-[NÚMERO]` (ej. `WA-MAM-0001` para mamíferos, `WA-AV-0001` para aves).
* **Decisión requerida de:** Director Creativo / Project Manager

---

### DEC-012-PENDING: Tecnología Cliente Definitiva para la App de Usuario
* **Tema:** Frontend Móvil
* **Estado:** `PENDING`
* **Propuesta inicial:** Flutter para aplicación móvil multiplataforma (Android / iOS) consumiendo los servicios y modelos definidos en Python.
* **Decisión requerida de:** Director Creativo / Project Manager
