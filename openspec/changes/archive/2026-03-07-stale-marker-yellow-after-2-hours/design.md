## Context

The map currently colors station markers only by temperature sign (blue above zero, red below zero). Readings can be old while still shown with a "normal" temperature color, which may mislead users about freshness. The backend already exposes timestamps for readings, so stale-state evaluation can be done in the frontend without API or schema changes.

## Goals / Non-Goals

**Goals:**
- Add deterministic stale-data coloring: marker is yellow when the latest reading is older than 2 hours.
- Preserve existing temperature colors for non-stale readings.
- Keep implementation aligned with existing frontend marker rendering and legend behavior.
- Avoid backend/API contract changes.

**Non-Goals:**
- No change to data collection frequency.
- No historical analytics or "staleness trend" feature.
- No database migration or backend persistence changes.

## Decisions

### 1) Compute staleness in frontend using reading timestamp
- **Decision:** Determine stale state in UI by comparing current time to each marker's reading timestamp.
- **Rationale:** Timestamp is already available in responses; this avoids introducing backend branching and keeps presentation concerns in frontend.
- **Alternatives considered:**
  - Add `isStale` field from backend: rejected as unnecessary API coupling for a simple visual rule.
  - Infer staleness from fetch cycle only: rejected because station-level timestamps can differ.

### 2) Color precedence: stale overrides temperature color
- **Decision:** Apply yellow first when reading age exceeds 2 hours; otherwise apply blue/red by temperature sign.
- **Rationale:** Freshness is the higher-priority signal because outdated values should be clearly distinguished regardless of value.
- **Alternatives considered:**
  - Dual encoding (border + fill): rejected for now to keep UI simple.
  - Keep temperature color and only mark stale in popup: rejected because users need immediate map-level visibility.

### 3) Use one shared marker-color helper
- **Decision:** Centralize color selection in a single function used by marker rendering (and legend text, if applicable).
- **Rationale:** Prevents divergence where marker logic and legend descriptions drift.
- **Alternatives considered:**
  - Inline conditionals in multiple components: rejected due to duplication risk.

## Risks / Trade-offs

- [Client clock skew] -> Use a simple tolerance in tests and rely on ISO timestamp parsing with UTC semantics.
- [Boundary behavior at exactly 2 hours] -> Define rule explicitly as "older than 2 hours" (`>` threshold), not `>=`.
- [Visual ambiguity for users] -> Update legend/label wording so yellow clearly means stale data.

## Migration Plan

1. Introduce/update shared marker-color helper with stale-first precedence.
2. Update marker rendering to use helper output.
3. Update legend or marker color explanation text to include yellow stale meaning.
4. Add/adjust frontend tests for color selection (fresh positive/negative, stale positive/negative, boundary case).
5. Validate manually on local map with mocked/stubbed timestamps.

Rollback:
- Revert marker-color helper and legend updates in one commit; no data migration required.

## Open Questions

- Should stale threshold remain fixed at 2 hours or be configurable later (env/UI setting)?
- Do we want an additional stale indicator in popup text (for accessibility beyond color)?
