## Why

The backend has two critical data integrity and performance issues that need immediate attention. The N+1 query problem in `getCurrentTemperatures()` causes severe performance degradation as the number of stations grows - with 100 stations, it executes 101 database queries. Additionally, the database schema has a type mismatch where `stations.station_id` is TEXT but `readings.station_id` is INTEGER, breaking the foreign key constraint. These issues prevent the application from scaling and maintaining data integrity.

## What Changes

- Refactor `TemperatureService.getCurrentTemperatures()` to use a single query with subquery instead of N+1 queries
- Fix database schema type mismatch: change `readings.station_id` from INTEGER to TEXT to match `stations.station_id`
- Add composite unique index on `(station_id, timestamp)` to prevent duplicate readings and optimize queries
- Fix missing null check in `TemperatureResource.getTemperatureHistory()` to return proper 404 for invalid station IDs

## Capabilities

### New Capabilities
- None - these are bug fixes and performance improvements, not new features

### Modified Capabilities
- None - the existing temperature-map capability requirements are unchanged; these are implementation fixes

## Impact

- **Affected Code**:
  - `smhi-temp-map/src/main/java/com/smhi/tempmap/service/TemperatureService.java` - N+1 query fix
  - `smhi-temp-map/src/main/resources/db/migration/V1__initial_schema.sql` - schema type fix and new index
  - `smhi-temp-map/src/main/java/com/smhi/tempmap/resource/TemperatureResource.java` - null check fix
- **Database**: Schema migration needed to fix type mismatch and add composite index
- **No API changes**: Existing endpoints maintain same contract
