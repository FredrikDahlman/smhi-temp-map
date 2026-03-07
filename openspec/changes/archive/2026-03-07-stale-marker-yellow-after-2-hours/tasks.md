## 1. Marker Color Logic

- [x] 1.1 Locate the frontend marker color function/component and centralize color selection in one helper if it is currently duplicated
- [x] 1.2 Implement stale-first color rule: return yellow when reading age is more than 2 hours
- [x] 1.3 Preserve existing temperature-based colors for non-stale readings (blue above 0C, red below 0C)
- [x] 1.4 Implement explicit boundary behavior so readings exactly 2 hours old are treated as non-stale

## 2. UI Consistency

- [x] 2.1 Update marker rendering to use the shared color helper consistently
- [x] 2.2 Update legend/label text to document yellow as stale-data marker color

## 3. Verification

- [x] 3.1 Add or update frontend tests for stale positive and stale negative cases (both yellow)
- [x] 3.2 Add or update frontend tests for fresh positive and fresh negative cases (blue/red)
- [x] 3.3 Add or update a boundary test for exactly 2 hours old reading (not stale)
- [x] 3.4 Manually verify on local app that marker colors match expected behavior for fresh vs stale data
