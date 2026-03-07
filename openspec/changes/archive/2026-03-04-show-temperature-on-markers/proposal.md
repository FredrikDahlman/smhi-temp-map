## Why

The current implementation shows temperature only in the popup when clicking a marker. Users must click each station to see the temperature, which is not ideal for quickly viewing temperatures across Sweden. Displaying temperature directly on the marker will improve UX.

## What Changes

- Display temperature value directly on each map marker
- Update marker styling to show temperature as a label/icon
- Modify TemperatureMap component to render temperature on marker

## Capabilities

### New Capabilities
- (none - this is a frontend-only enhancement)

### Modified Capabilities
- `temperature-map`: Update requirement to display temperature directly on marker, not just in popup

## Impact

- Frontend: Modify TemperatureMap.tsx component
- No backend changes required
- No new dependencies
