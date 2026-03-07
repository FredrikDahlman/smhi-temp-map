## 1. Database Migration

- [x] 1.1 Create Flyway migration file V2__fix_schema_and_add_index.sql
- [x] 1.2 Change readings.station_id from INTEGER to TEXT
- [x] 1.3 Add composite unique index on (station_id, timestamp)
- [x] 1.4 Test migration runs successfully on dev database

## 2. N+1 Query Fix

- [x] 2.1 Refactor TemperatureService.getCurrentTemperatures() to use single JPQL subquery
- [ ] 2.2 Verify query returns correct latest reading per station
- [ ] 2.3 Test with multiple stations having readings

## 3. Null Check Fix

- [x] 3.1 Add station existence check in TemperatureResource.getTemperatureHistory()
- [x] 3.2 Return 404 NotFoundException for invalid stationId
- [ ] 3.3 Test endpoint returns 404 for non-existent station

## 4. Verification

- [x] 4.1 Run application and verify all endpoints work
- [x] 4.2 Verify N+1 query is fixed (check logs or use profiling)
- [x] 4.3 Verify foreign key constraint now works
- [ ] 4.4 Run existing tests to ensure no regressions
