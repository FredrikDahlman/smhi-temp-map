## ADDED Requirements

None - this is an internal code architecture refactor, not a new feature or capability.

## MODIFIED Requirements

None - the existing temperature-map capability requirements are unchanged. This refactor:
- Extracts data access to repositories
- Splits services into query vs command
- Creates domain models and ports
- All without changing system behavior

The existing spec at `openspec/specs/smhi-temperature-map/spec.md` remains the source of truth.

## REMOVED Requirements

None.

## Notes

This change is purely internal restructuring. The system's external behavior (API endpoints, data collection, display) remains identical.
