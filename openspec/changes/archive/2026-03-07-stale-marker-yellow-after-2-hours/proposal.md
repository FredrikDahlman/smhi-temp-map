## Why

Map marker color currently reflects only temperature sign (blue above 0C, red below 0C), which can make stale readings look fresh. Highlighting readings older than 2 hours improves data trust and helps users quickly spot outdated stations.

## What Changes

- Update marker color behavior so stations with last reading older than 2 hours are rendered in yellow.
- Keep existing temperature-based colors (blue/red) for non-stale readings.
- Ensure stale-state coloring applies consistently in marker rendering and legend/user-visible color meaning.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `smhi-temperature-map`: Modify frontend marker-color requirement to include stale-data coloring (yellow when reading age exceeds 2 hours).

## Impact

- Frontend marker color logic and any related color legend/text.
- Potential DTO/API usage of timestamps already returned for readings (no endpoint contract change expected).
- No database schema changes.
