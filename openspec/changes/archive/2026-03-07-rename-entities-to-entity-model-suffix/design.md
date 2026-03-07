## Context

The application uses Clean Architecture with separate `entity` and `domain/model` packages. Currently, both packages contain classes with identical names (`Reading`, `Station`), creating naming collisions that require fully qualified class names in conversion methods.

## Goals / Non-Goals

**Goals:**
- Rename entity classes to use `Entity` suffix
- Rename domain model classes to use `Model` suffix
- Update all dependent files to use new names
- Simplify conversion code (remove FQN usage)

**Non-Goals:**
- No database schema changes
- No new functionality
- No changes to API contracts or DTOs beyond import updates
- No migration of existing data required

## Decisions

### 1. Naming Convention
**Decision:** Use `ReadingEntity`/`StationEntity` for JPA entities and `ReadingModel`/`StationModel` for domain models.

**Rationale:** `Entity` clearly signals a persistence layer concern. `Model` is commonly used for domain representations in Clean Architecture. The suffix approach is explicit and avoids ambiguity without requiring package context to understand class purpose.

**Alternatives considered:**
- `ReadingEntity`/`ReadingRecord` - Using record for domain model was considered but `Model` is more universally understood
- `ReadingData`/`ReadingDomain` - Less common terminology in this architecture style

### 2. Migration Order
**Decision:** Rename domain models first, then entities.

**Rationale:** This minimizes the window where imports are inconsistent. The domain model is referenced by more files (ports, services, DTOs) than the entity, so completing those updates first reduces the need for cross-file coordination.

### 3. Simplification of Conversion Methods
**Decision:** After renaming, refactor `toDomain()` methods to use simple class names instead of FQNs.

**Rationale:** The current FQNs exist solely due to the name collision. Once resolved, the conversion code becomes cleaner.

## Risks / Trade-offs

| Risk | Mitigation |
|------|-----------|
| Missing a reference during rename | Use IDE refactor (rename) or systematic grep check after changes |
| Build breakage during partial update | Complete all renames and updates in single commit, verify build passes |
| Accidental table name change | Entity `@Table` annotations remain unchanged - only class name changes |

## Migration Plan

1. Rename `domain/model/Reading.java` → `ReadingModel.java`
2. Update all imports referencing `domain.model.Reading`
3. Rename `domain/model/Station.java` → `StationModel.java`
4. Update all imports referencing `domain.model.Station`
5. Rename `entity/Reading.java` → `ReadingEntity.java`
6. Update entity references in repositories
7. Rename `entity/Station.java` → `StationEntity.java`
8. Update entity references in repositories
9. Simplify `toDomain()` conversion methods
10. Verify build passes: `mvn compile`
11. Run tests: `mvn test`

**Rollback:** Git revert the change directory and restore original files if issues arise.

## Open Questions

None - the scope is well-defined and straightforward.
