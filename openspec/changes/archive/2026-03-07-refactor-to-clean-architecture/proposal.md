## Why

The current codebase mixes business logic with data access in `TemperatureService`, making it hard to test and reason about. The service handles everything from fetching external API data to persisting entities, creating tight coupling. Refactoring to clean architecture will improve testability, maintainability, and create clear boundaries between layers - without the full complexity of hexagonal architecture.

## What Changes

- Extract data access to dedicated repository interfaces and implementations
- Create domain model POJOs separate from JPA entities
- Split `TemperatureService` into `TemperatureQueryService` (reads) and `TemperatureCommandService` (writes)
- Create ports (interfaces) to define boundaries between layers
- Rename `SmhiClient` to `SmhiClientAdapter` and create `WeatherDataPort` interface
- Add mappers between domain models and JPA entities
- Update REST resource to use new services

**BREAKING**: REST API endpoints remain unchanged - this is purely internal refactoring.

## Capabilities

### New Capabilities
- `clean-architecture`: Internal code structure refactor without spec-level changes

### Modified Capabilities
- None - existing temperature-map capability requirements unchanged

## Impact

- **Code Structure**: New packages for domain, ports, adapters
- **Services**: `TemperatureQueryService`, `TemperatureCommandService`
- **Persistence**: New repositories implementing ports
- **No API changes**: All existing endpoints work identically
- **Dependencies**: No new external dependencies
