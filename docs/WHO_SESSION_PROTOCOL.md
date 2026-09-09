# WHO_SESSION_PROTOCOL.md

## Standard Operational Cycle
```
PM PLAN
→ AGENT READ
→ AGENT IMPLEMENT
→ AGENT VERIFY
→ PM REVIEW
→ COMMIT
→ CHECKPOINT
→ NEXT WHO
```

### Roles & Responsibilities
#### Product Manager (PM)
- **Direction**: Define scope, objectives, and acceptance criteria.
- **Approval**: Authorize implementations, reviews, and commits.
- **Review**: Verify deliverables against specifications.
- **Authorization**: Explicitly grant permission for pushes.
- **Task Isolation**: A `WHO‑XXX` task does **not** automatically authorize any other task.

#### Antigravity Agent
- **Implementation**: Execute the work strictly within the approved scope.
- **Testing**: Run required builds, unit‑tests, and verification steps.
- **Reporting**: Communicate results, status, and any blockers.
- **Scope Discipline**: Do **not** expand the scope without PM consent.
- **Block on Issues**: Halt work when encountering unapproved changes, missing resources, or blockers.

### Interaction Flow
1. **PM PLAN** – PM provides a detailed plan or request.
2. **AGENT READ** – Agent reviews specifications, codebase, and context.
3. **AGENT IMPLEMENT** – Agent makes the required changes.
4. **AGENT VERIFY** – Agent runs builds, tests, and validation checks.
5. **PM REVIEW** – PM evaluates the outcome and either approves or requests adjustments.
6. **COMMIT** – Agent stages *only* the authorized files and creates a task‑specific commit.
7. **CHECKPOINT** – Documentation of the state is recorded (e.g., `PM_CHECKPOINT.md`).
8. **NEXT WHO** – The cycle repeats for the next `WHO‑XXX` task.

*The cycle must be completed for each task before moving to the next.*
