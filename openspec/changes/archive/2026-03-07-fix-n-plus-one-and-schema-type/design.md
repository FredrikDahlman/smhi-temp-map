## Context

The SMHI Temperature Map backend has critical performance and data integrity issues:

**Current State:**
- `TemperatureService.getCurrentTemperatures()` iterates through all stations and executes a separate query for each to get the latest reading (N+1 pattern)
- Database schema has type mismatch: `stations.station_id` is TEXT, `readings.station_id` is INTEGER, breaking foreign key constraint
- No composite index on (station_id, timestamp) causing slow queries
- `TemperatureResource.getTemperatureHistory()` returns "Unknown" for invalid stationId instead of 404

**Constraints:**
- Must maintain existing API contract (no breaking changes)
- Quarkus with Panache ORM and SQLite database
- Scheduled job runs hourly to fetch SMHI data

## Goals / Non-Goals

**Goals:**
- Fix N+1 query to use single database query with subquery/window function
- Fix schema type mismatch to enable foreign key constraints
- Add composite unique index to prevent duplicates and optimize queries
- Add proper 404 response for invalid station IDs

**Non-Goals:**
- API contract changes
- Adding new features or capabilities
- Migrating to different database technology
- Implementing retry logic or advanced error handling (out of scope for this fix)

## Decisions

### D1: N+1 Query Fix - JPQL Subquery

**Decision:** Use JPQL with correlated subquery to fetch latest reading per station in a single query.

**Rationale:**
- Window functions (ROW_NUMBER) require native query in Panache
- Subquery is portable across JPA providers
- Single query eliminates N+1 problem entirely

**Alternative Considered:** Fetch all readings and group in Java
- Rejected: Loads all readings into memory, worse performance for large datasets

```java
public List<Reading> getCurrentTemperatures() {
    return getEntityManager()
        .createQuery("SELECT r FROM Reading r WHERE r.timestamp = " +
            "(SELECT MAX(r2.timestamp) FROM Reading r2 WHERE r2.station = r.station)", 
            Reading.class)
        .getResultList();
}
```

### D2: Schema Type Fix - Change to TEXT

**Decision:** Change `readings.station_id` from INTEGER to TEXT to match `stations.station_id`.

**Rationale:**
- The foreign key currently can't work due to type mismatch
- TEXT matches the string station_id from SMHI API
- Minimal migration impact

**Alternative Considered:** Change stations to use INTEGER surrogate key
- Rejected: Would require updating all references to station_id in code and queries

### D3: Composite Index Design

**Decision:** Add composite unique index on (station_id, timestamp).

**Rationale:**
- Prevents duplicate readings for same station at same time
- Optimizes ORDER BY timestamp DESC queries filtered by station
- Covers both uniqueness constraint and query performance

### D4: 404 for Invalid Station

**Decision:** Check station existence and throw NotFoundException for invalid IDs.

**Rationale:**
- Clear error semantics for API consumers
- Consistent with REST best practices
- Panache makes it easy to check existence

## Risks / Trade-offs

[Risk: Migration may fail if readings table has existing data with invalid station references] → Mitigation: First migration should validate data, provide clear error message if issues found

[Risk: N+1 fix query may return duplicate readings if timestamps are identical] → Mitigation: Add DISTINCT or use ID in subquery tiebreaker

[Risk: Changing station_id to TEXT increases storage slightly] → Negligible for this use case

## Migration Plan

1. Create new Flyway migration file `V2__fix_schema_and_add_index.sql`
2. Verify no existing duplicate (station_id, timestamp) pairs before adding unique constraint
3. Run migration
4. Deploy updated application code
5. No rollback needed - forward-only migration

## Open Questions

- Should we also add HTTP timeout to SmhiClient? (mentioned in code review but not in proposal)
- Should we make CORS origin configurable? (mentioned in code review but not in proposal)

These are separate concerns that can be addressed in future changes.
