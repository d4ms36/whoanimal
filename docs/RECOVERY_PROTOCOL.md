# RECOVERY_PROTOCOL.md

## Recovery Procedure for Agent Interruptions

The following steps must be executed **exactly** when the Antigravity agent encounters a disruption such as:
- HTTP 503 Service Unavailable
- `MODEL_CAPACITY_EXHAUSTED`
- Agent restart or crash
- Loss of session context
- New session initialization

### Recovery Mode (No file modifications)
1. **Do NOT edit any repository files**.
2. Run:
   ```powershell
   git status -sb
   ```
   to obtain the current repository state.
3. Open and read `docs/PM_CHECKPOINT.md` – this file holds the latest global snapshot.
4. Open the checkpoint file for the **active task** (e.g., `docs/checkpoints/WHO-028.md`).
5. Open the corresponding section in `docs/PLANNING.md` that describes the task’s plan.
6. Summarize the gathered information and report back to the PM, indicating:
   - Current branch and ahead/behind status.
   - Any staged or modified files.
   - The last known checkpoint description.
7. **Await explicit instructions** from the PM before proceeding with any further actions (e.g., re‑running builds, applying patches, or creating new commits).

### Important Constraints
- The agent must **never automatically continue** work without a clear, confirmed state.
- No `git add`, `git commit`, or file edits are permitted during recovery unless the PM explicitly authorizes them.
- If the state is ambiguous (e.g., unexpected staged files), the agent must stop and request clarification.

---
*This protocol ensures the continuity of work across interruptions while preserving repository integrity.*
