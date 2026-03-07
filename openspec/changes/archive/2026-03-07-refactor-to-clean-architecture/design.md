## Context

**Current State:**
- Single `TemperatureService` handles both queries and commands
- Direct Panache entity usage in service code (hard to test)
- No clear separation between domain and persistence
- Business logic coupled with data access

**Constraints:**
- Quarkus with Panache ORM
- SQLite database
- Existing REST API must remain unchanged
- Scheduled job for hourly data fetch
- Minimal dependencies (keep it simple)

## Goals / Non-Goals

**Goals:**
- Extract data access to repository interfaces
- Create domain model POJOs separate from JPA entities
- Split services into query vs command handlers
- Define ports (interfaces) for layer boundaries
- Improve testability

**Non-Goals:**
- Full hexagonal architecture
- Adding new external dependencies
- Changing REST API contracts
- Database migration (schema stays the same)
- Adding use case classes (out of scope)

## Decisions

### D1: Keep JPA Entities for Persistence

**Decision:** Keep `Station.java` and `Reading.java` entities with Panache for persistence. Create separate domain models.

**Rationale:**
- Panache's active record pattern is convenient for persistence
- Entities can stay as-is with minimal changes
- Domain models give us testable, stable interfaces

**Alternative:** Remove entities entirely, use repositories only
- Rejected: Too much refactoring, loses Panache benefits

### D2: Use Records for Domain Models

**Decision:** Use Java records for immutable domain models.

**Rationale:**
- Less boilerplate than classes
- Immutable by default
- Clear representation of "data" vs "behavior"

```java
public record Station(Long id, String stationId, String name, Double latitude, Double longitude) {}
```

### D3: Split Service by CQRS-lite

**Decision:** Split into `TemperatureQueryService` and `TemperatureCommandService`.

**Rationale:**
- Clear mental model: reads vs writes
- Different scaling strategies possible
- Simpler to reason about each service

**Alternative:** Keep single service with separate methods
- Rejected: Doesn't solve the coupling problem

### D4: Ports Live in Domain Layer

**Decision:** Port interfaces (`StationPort`, `ReadingPort`) live in domain layer, implementations in adapters.

**Rationale:**
- Domain defines what it needs, adapters provide how
- Easy to mock ports in tests
- Clear dependency direction: domain → ports ← adapters

### D5: Rename SmhiClient to Adapter

**Decision:** Rename `SmhiClient` → `SmhiClientAdapter`, create `WeatherDataPort` interface.

**Rationale:**
- Consistent naming: everything external is an "adapter"
- Port interface makes it swappable
- Aligns with clean architecture terminology

## Risks / Trade-offs

[Risk: More files to navigate] → Mitigation: Clear package structure makes location obvious

[Risk: Mapping code between layers] → Mitigation: Entity classes have `toDomain()` and `fromDomain()` methods

[Risk: Breaking something during refactor] → Mitigation: Test each endpoint after changes, can revert easily

[Risk: Over-engineering for app size] → Mitigation: This is "lightweight" clean architecture - not full hexagonal

## Migration Plan

1. Create new packages: `domain/model`, `domain/service`, `ports`, `adapters/persistence`, `adapters/external`
2. Create domain model records
3. Create port interfaces
4. Create repository adapters (implement ports)
5. Create domain services (query + command)
6. Update REST resource to use new services
7. Delete old `TemperatureService`
8. Run tests and verify endpoints
9. Commit

**Rollback:** Single revert commit if something breaks - no schema changes.

## Open Questions

- Should we keep `SmhiStationData` record in domain or move to adapter?
- Should `TemperatureQueryService` return domain models or DTOs?
