# COMMIT_PROTOCOL.md

## Mandatory Commit Workflow
```
IMPLEMENT
→ VERIFY
→ PM REVIEW
→ STAGE ONLY AUTHORIZED FILES
→ VERIFY STAGING
→ COMMIT
→ VERIFY COMMIT
→ WAIT FOR PUSH AUTHORIZATION
```

### Key Rules
- **Never** run `git add -A` for a task; only stage files explicitly authorized by the PM.
- **Always** inspect staging with:
  ```
  git diff --cached --stat
  git diff --cached --name-status
  git diff --cached --check
  ```
- Commits must be **task‑specific** and reflect a single logical change.
- No push without explicit PM permission.
- Do **not** mix documentation, code, or cleanup in the same commit unless the PM authorizes it.
- After committing, re‑run verification steps (build, tests) if required.
- Record the commit message format: `WHO-XXX — <Brief description>`.

---
*Adhering to this protocol guarantees clear audit trails and prevents accidental inclusion of unrelated changes.*
