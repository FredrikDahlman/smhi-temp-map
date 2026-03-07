## ADDED Requirements

None - this change implements bug fixes and performance improvements without adding new capabilities.

## MODIFIED Requirements

None - the existing temperature-map capability requirements are unchanged. This change only addresses implementation-level issues:
- N+1 query performance fix
- Database schema type mismatch fix  
- Composite index for query optimization
- Proper 404 error handling

These are implementation details that do not change the spec-level behavior of the system.

## REMOVED Requirements

None.

## Notes

This change modifies implementation without changing system behavior. The existing spec at `openspec/specs/smhi-temperature-map/spec.md` remains the source of truth for requirements.
