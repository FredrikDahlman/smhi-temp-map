## Context

The temperature map currently displays markers without temperature labels. Users must click each marker to see the temperature in the popup. This requires unnecessary interaction to get basic temperature information.

## Goals / Non-Goals

**Goals:**
- Display temperature value directly on each marker
- Make temperature visible at a glance without clicking
- Maintain marker readability with clear temperature labels

**Non-Goals:**
- Change backend APIs (already returning temperature data)
- Add new data sources
- Redesign the overall map layout

## Decisions

### Approach: Custom DivIcon with temperature label

**Chosen:** Use Leaflet's DivIcon to render temperature as HTML inside the marker
**Alternatives considered:**
- Default marker with popup only: Doesn't solve the visibility problem
- Marker tooltips: Too small/imprecise for permanent display
- Custom CSS marker with ::after pseudo-element: Works but less flexible

**Rationale:** DivIcon allows full HTML/CSS control over marker appearance, enabling temperature text directly on the marker

### Design: Temperature as badge/label

**Chosen:** Show temperature as a centered label on a colored background
- Blue background for above 0°C
- Red background for below 0°C
- White text for readability
- Rounded corners for modern look

**Rationale:** Color coding matches the existing marker color scheme; clear visibility at various zoom levels

## Risks / Trade-offs

### Risk: Overcrowded markers at default zoom
→ **Mitigation:** Keep labels compact; users can zoom in for detail

### Risk: Different screen sizes
→ **Mitigation:** Test at multiple zoom levels; labels scale with map zoom

## Migration Plan

1. Update TemperatureMap.tsx to use DivIcon
2. Add CSS styling for temperature labels
3. Test at various zoom levels
4. Deploy to production

No rollback needed - change is purely additive to frontend.
