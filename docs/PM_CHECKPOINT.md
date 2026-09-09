# PM_CHECKPOINT.md

## Project Snapshot
- **Project**: WHO Animal
- **Stack**: Android/Kotlin/Jetpack Compose + Python/domain components
- **Main branch**: `main`
- **Current architecture**: Clean Architecture with `domain`, `services`, `core` layers; UI built with Jetpack Compose and Material3 theming.
- **WHO‑XXX conventions**: Prefix task IDs with `WHO-`, maintain isolated commits per task, never mix tasks.
- **Task states**: Pending → In‑Progress → Completed → Verified.
- **Design Lock**: WHO‑003B‑R2 (geometry, navigation, Room, datasets locked).
- **Current task**: WHO‑028 – Beta UI Visual Redesign & Semantic Theming (Completed).
- **Last relevant commits**:
  - `9a9a795` – WHO‑028 — Beta UI Visual Redesign & Semantic Theming
  - `d3e6fa4` – WHO‑003B‑R2 (Design Lock)
- **Rules**:
  - No push without explicit PM approval.
  - Repository is the source of truth.
  - Do not mix tasks.
  - No refactoring during a specific WHO‑XXX task.

## CURRENT TASK
- **ID**: WHO‑000 – PM Continuity Framework (in progress)
- **Goal**: Establish documentation for continuity, recovery, and commit protocols.

## NEXT ACTION
- Create documentation files as defined in the PM request.

## KNOWN RISKS
- Over‑documentation may drift from reality if not kept up‑to‑date.
- Future changes to conventions may require updates to this checkpoint.
