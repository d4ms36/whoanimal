# WHO Animal – Command Center

**Descubre. Identifica. Colecciona.**

---

## Current Snapshot

| Área               | Estado      |
|--------------------|------------|
| Milestone actual   | **WHO‑DOC‑004** |
| Último Freeze      | **WHO‑003B‑R2** |
| Diseño             | **Explorer** |
| APK                | **Disponible** |
| Cleanup            | **Pendiente** |
| Git                | **Sin commit** |

---

## Project Tree (relevant)

```
WHO-001
WHO-002
WHO-003B
├── R1 ❄️
└── R2 ❄️

WHO-DOC
├── DOC‑001 ✅
├── DOC‑002 🔄
├── DOC‑003 ⏳
└── DOC‑004 ⏳
```

---

## Frozen Decisions

- Explorer es la identidad principal.
- Hero Image ≈60%.
- Standard Specimen.
- Hero Silhouette condicional.
- 6 rarezas oficiales.
- RPG ligero.
- Thumbnail simplificada.

---

## Current Milestone

**WHO‑DOC‑004 – Documentation Freeze & Continuity Checkpoint**

- **Objetivo:** Formalizar el cierre de la documentación y establecer el checkpoint de continuidad.
- **Alcance:** Actualizar PROJECT_STATUS, registrar decisiones congeladas, definir próximo task autorizado, establecer protocolo de continuidad.
- **Restricciones:** No modificar código, no tocar Design Lab, no hacer commits, no eliminar archivos.

---

## Next Checkpoint

- **WHO‑CLEAN‑001 – Architecture / File Audit**
- **WHO‑AUDIT‑001 – Technical Debt Audit**

---

## Documentation Freeze Checkpoint

**Milestone:** Documentation Backbone

**Current Phase:** Documentation Frozen

**Last Completed:** WHO‑DOC‑004

**Frozen Decisions:**
- Explorer visual identity
- Hero Image ≈ 60%
- Standard Specimen
- Conditional Hero Silhouette
- 6 canonical rarities
- Light RPG
- Simplified Thumbnail
- Species → Capture → Card

**Next Authorized Task:** WHO‑CLEAN‑001 — PROJECT CLEANUP

**Do Not Touch:**
- WHO‑003B‑R1
- WHO‑003B‑R2
- Design Lab
- Card implementation
- Python/domain
- Architecture
- Room
- Navigation

**Deferred Work:**
- Human visual validation
- Project cleanup
- Architecture/File audit
- Legacy file classification

**Continuity Protocol:**
`PROJECT_STATUS.md → AGENTS.md → ROADMAP.md → PLANNING.md → DECISIONS.md → ARCHITECTURE.md → task‑specific documentation`

**Documentation Status:** **FROZEN**
## Navigation Hub

| Acción                     | Documento |
|----------------------------|-----------|
| Entender el producto       | [PRODUCT_VISION](docs/PRODUCT_VISION.md) |
| Ver reglas                 | [PRODUCT_RULES](docs/PRODUCT_RULES.md) |
| Arquitectura               | [ARCHITECTURE](docs/ARCHITECTURE.md) |
| Roadmap                    | [ROADMAP](docs/ROADMAP.md) |
| Plan operativo             | [PLANNING](docs/PLANNING.md) |
| Decisiones                 | [DECISIONS](docs/DECISIONS.md) |
| Workflow                   | [WORKFLOW](docs/WORKFLOW.md) |
| Diseño                     | [DESIGN_SYSTEM](docs/DESIGN_SYSTEM.md) |
| Card visual spec           | [CARD_VISUAL_SPEC](docs/CARD_VISUAL_SPEC.md) |
| Card domain spec           | [CARD_DOMAIN_SPEC](docs/CARD_DOMAIN_SPEC.md) |
| Testing protocol           | [CARD_TEST_PROTOCOL](docs/CARD_TEST_PROTOCOL.md) |

---

## Agent Rules (short)

1. Leer **PROJECT_STATUS.md** antes de cualquier acción.
2. Respetar **AGENTS.md** y **DECISIONS.md**.
3. No modificar artefactos marcados como **FROZEN**.
4. No tocar código ni Design Lab.
5. No ejecutar commits o pushes.
6. No iniciar el siguiente lote sin autorización del PM.

---

## Technical Snapshot

- **Compose** (Android UI)
- **Python** (data pipelines)
- **Android** (app platform)
- **Build** (Gradle)
- **Tests** (instrumented & unit)
- **APK** (available for review)

---

## Deferred Work

- Validación visual humana (aplazada).
- Cleanup de arquitectura y archivos.
- Auditoría de deuda técnica.
- Clasificación legacy.

---

*Este documento sirve como punto de referencia rápido para cualquier agente futuro.*
