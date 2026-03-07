# Code Review: SMHI Temperature Mapping Application

## Executive Summary

This is a Quarkus-based application that fetches temperature data from the Swedish Meteorological and Hydrological Institute (SMHI) API and stores it for consumption via REST endpoints. The codebase has several areas that need attention, ranging from critical issues affecting data integrity and performance to medium/low concerns around maintainability and security.

---

## Critical Issues

### 1. N+1 Query Problem in TemperatureService

**Location:** `TemperatureService.java`, lines 103-120

**Issue:** The `getCurrentTemperatures()` method performs a separate database query for each station to fetch the latest reading, creating an N+1 query pattern that will cause severe performance degradation as the number of stations increases.

```java
for (Station station : stations) {
    Reading latestReading = Reading.findRecentByStationId(station.id, 1).getFirst();
    // ...
}
```

**Impact:** With 100 stations, this results in 101 queries (1 to list all stations + 100 to get readings). This will cause significant latency and database load.

**Recommendation:** Use a single query with a window function or a join to fetch the latest reading per station:

```java
public List<Reading> getCurrentTemperatures() {
    return getEntityManager()
        .createQuery("SELECT r FROM Reading r WHERE r.timestamp = " +
            "(SELECT MAX(r2.timestamp) FROM Reading r2 WHERE r2.station = r.station)", 
            Reading.class)
        .getResultList();
}
```

Or use Panache's native query support with a ROW_NUMBER() window function.

---

### 2. Type Mismatch in Database Schema

**Location:** `V1__initial_schema.sql`, line 12

**Issue:** The `readings` table has `station_id INTEGER` while the `stations` table uses `station_id TEXT`. The foreign key relationship attempts to link an INTEGER to a TEXT column, which will fail.

```sql
station_id TEXT NOT NULL UNIQUE,  -- In stations table
station_id INTEGER NOT NULL,      -- In readings table (MISMATCH!)
```

**Impact:** The foreign key constraint will fail, and data integrity cannot be enforced at the database level.

**Recommendation:** Change the `readings` table to use TEXT for station_id, or better yet, use the surrogate key (id) for the foreign key relationship:

```sql
CREATE TABLE readings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    station_id INTEGER NOT NULL,
    temperature REAL NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (station_id) REFERENCES stations(id)
);
```

Note: The current schema attempts this but the type mismatch needs fixing.

---

### 3. Missing Input Validation

**Location:** `TemperatureResource.java`, line 44

**Issue:** The `stationId` path parameter is not validated. If an invalid (non-existent) ID is passed, the code returns "Unknown" for the station name instead of returning a proper 404 error.

```java
@GET
@Path("/temperatures/{stationId}/history")
public TemperatureHistoryDto getTemperatureHistory(
        @PathParam("stationId") Long stationId) {
    Station station = Station.findById(stationId);
    String stationName = station != null ? station.name : "Unknown";  // Hides error
    return TemperatureHistoryDto.from(stationId, stationName, readings);
}
```

**Impact:** Clients cannot distinguish between a valid station with no history and a non-existent station. This leads to confusing API responses.

**Recommendation:** Add validation and proper error handling:

```java
@GET
@Path("/temperatures/{stationId}/history")
public Response getTemperatureHistory(
        @PathParam("stationId") Long stationId) {
    Station station = Station.findById(stationId);
    if (station == null) {
        return Response.status(Response.Status.NOT_FOUND)
            .entity("Station not found: " + stationId)
            .build();
    }
    List<Reading> readings = temperatureService.getTemperatureHistory(stationId, 24);
    return Response.ok(TemperatureHistoryDto.from(stationId, station.name, readings)).build();
}
```

---

## High Priority Issues

### 4. No HTTP Timeout Configuration

**Location:** `SmhiClient.java`, line 35

**Issue:** The HTTP request to the SMHI API has no timeout configured. If the API becomes unresponsive, the application thread will hang indefinitely.

```java
HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
// No timeout - can hang forever
```

**Impact:** The scheduled job could hang, blocking other scheduled tasks and potentially causing thread exhaustion.

**Recommendation:** Add timeout configuration:

```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(SMHI_API_URL))
    .timeout(Duration.ofSeconds(30))
    .GET()
    .build();
```

---

### 5. Hardcoded CORS Origin

**Location:** `CorsFilter.java`, line 14

**Issue:** The CORS origin is hardcoded to `http://localhost:3000`, which won't work in production or when the frontend runs on a different port.

```java
responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
```

**Impact:** Cross-origin requests from production frontend will be blocked, breaking the application in non-local environments.

**Recommendation:** Make the allowed origin configurable:

```java
@ConfigProperty(name = "cors.allowed-origin", defaultValue = "*")
String allowedOrigin;

@Override
public void filter(ContainerRequestContext requestContext, 
                   ContainerResponseContext responseContext) {
    responseContext.getHeaders().add("Access-Control-Allow-Origin", allowedOrigin);
    // ... rest of headers
}
```

---

### 6. Silent Failure in Scheduled Job

**Location:** `TemperatureService.java`, lines 79-81

**Issue:** The outer catch block silently logs the exception without re-throwing or taking any recovery action. Scheduled job failures go unnoticed in production monitoring.

```java
} catch (Exception e) {
    LOG.error("Failed to fetch temperature data", e);
}
// No re-throw, no alerting
```

**Impact:** If the SMHI API is down or there's a configuration issue, the job fails silently. Operations team won't be alerted to the failure.

**Recommendation:** Either re-throw the exception to let Quarkus handle it, or implement proper alerting:

```java
} catch (Exception e) {
    LOG.error("Failed to fetch temperature data", e);
    throw new RuntimeException("Temperature fetch failed", e);
}
```

Or use Quarkus's scheduler metrics integration to track job failures.

---

### 7. Silently Swallowing InterruptedException

**Location:** `SmhiClient.java`, lines 42-45

**Issue:** When an `InterruptedException` occurs, the interrupt flag is set but the method returns an empty list as if nothing happened.

```java
} catch (IOException | InterruptedException e) {
    LOG.error("Failed to fetch SMHI data", e);
    Thread.currentThread().interrupt();  // Sets flag but continues
}
return results;  // Returns empty list, hiding the interruption
```

**Impact:** Calling code has no way to know if the failure was due to an interruption. The thread interrupt flag should be respected.

**Recommendation:** Re-throw or handle appropriately:

```java
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    LOG.error("SMHI request interrupted", e);
    throw new RuntimeException("Request interrupted", e);
} catch (IOException e) {
    LOG.error("Failed to fetch SMHI data", e);
}
```

---

## Medium Priority Issues

### 8. Hardcoded API URL

**Location:** `SmhiClient.java`, line 21

**Issue:** The SMHI API URL is hardcoded in the code, making it difficult to change endpoints for different environments (development, staging, production) without code changes.

```java
private static final String SMHI_API_URL = "https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station-set/all/period/latest-hour/data.json";
```

**Impact:** Not following the twelve-factor app methodology; configuration should be externalized.

**Recommendation:** Move to configuration:

```java
@ConfigProperty(name = "smhi.api.url")
String smhiApiUrl;
```

---

### 9. Hardcoded History Limit

**Location:** `TemperatureResource.java`, line 45

**Issue:** The temperature history query uses a hardcoded 24-hour limit. Clients cannot request different time ranges.

```java
List<Reading> readings = temperatureService.getTemperatureHistory(stationId, 24);
```

**Impact:** API lacks flexibility; clients can't get historical data for different periods.

**Recommendation:** Add a query parameter:

```java
@GET
@Path("/temperatures/{stationId}/history")
public TemperatureHistoryDto getTemperatureHistory(
        @PathParam("stationId") Long stationId,
        @QueryParam("hours") @DefaultValue("24") int hours) {
    List<Reading> readings = temperatureService.getTemperatureHistory(stationId, hours);
    // ...
}
```

---

### 10. Default Values Mask Data Quality Issues

**Location:** `TemperatureService.java`, lines 53-60

**Issue:** When latitude/longitude are missing from the SMHI API, the code defaults to 0.0, which is a valid coordinate (in the Gulf of Guinea) but represents invalid/missing data.

```java
station.latitude = smhiData.latitude() != null ? smhiData.latitude() : 0.0;
station.longitude = smhiData.longitude() != null ? smhiData.longitude() : 0.0;
```

**Impact:** Invalid data is stored in the database, potentially appearing on maps as locations in the Atlantic Ocean. This masks data quality issues and makes debugging harder.

**Recommendation:** Either skip storing stations with missing coordinates, or use `null` with appropriate database constraints:

```java
if (smhiData.latitude() == null || smhiData.longitude() == null) {
    LOG.warnf("Missing coordinates for station %s, skipping", smhiData.stationId());
    return;  // Or continue with null and handle in queries
}
station.latitude = smhiData.latitude();
station.longitude = smhiData.longitude();
```

---

### 11. Public Entity Fields

**Location:** `Station.java`, lines 18-30 and `Reading.java`, lines 20-32

**Issue:** Entity fields are declared as public, breaking encapsulation. This is an anti-pattern that prevents adding validation, lazy loading, or future changes without breaking consumers.

```java
public Long id;
public String stationId;
public String name;
public Double latitude;
```

**Impact:** Code that directly accesses these fields cannot benefit from encapsulation. Adding validation or computed fields later would be a breaking change.

**Recommendation:** Use private fields with getters/setters:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "station_id", unique = true, nullable = false)
private String stationId;

public Long getId() { return id; }
public void setId(Long id) { this.id = id; }
// ... etc
```

Note: If using Panache, you can use the `public` pattern but should be aware of the tradeoffs.

---

### 12. Missing equals() and hashCode()

**Location:** `Station.java` and `Reading.java`

**Issue:** Neither entity class implements `equals()` and `hashCode()`. This can cause issues when entities are used in collections, sets, or as map keys.

**Impact:** Unpredictable behavior when comparing entities, potential memory leaks in collections, and difficulty in testing.

**Recommendation:** Implement these methods based on the business key (stationId for Station) or use Panache's built-in support:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Station station = (Station) o;
    return Objects.equals(stationId, station.stationId);
}

@Override
public int hashCode() {
    return Objects.hash(stationId);
}
```

---

### 13. Missing Composite Index

**Location:** `V1__initial_schema.sql`

**Issue:** There's no composite index on (station_id, timestamp) for the common query pattern of "get latest readings by station" or "get readings for station within time range".

**Impact:** Queries filtering by both station and time will perform full table scans on larger datasets.

**Recommendation:** Add a composite index:

```sql
CREATE INDEX idx_readings_station_timestamp ON readings(station_id, timestamp DESC);
```

---

## Low Priority Issues

### 14. Redundant Wrapper Method

**Location:** `Station.java`, lines 36-38

**Issue:** The `listAllStations()` method simply calls `listAll()` without adding any value.

```java
public static List<Station> listAllStations() {
    return listAll();
}
```

**Impact:** Unnecessary code that adds no benefit.

**Recommendation:** Either remove this method and call `Station.listAll()` directly, or add additional logic if needed.

---

### 15. Unused Field

**Location:** `StationDto.java`, line 7

**Issue:** The `id` field in `StationDto` is public and mutable, allowing modification after creation.

**Recommendation:** Use private final fields with getters, or use Java records for immutable DTOs:

```java
public record StationDto(
    Long id,
    String stationId,
    String name,
    Double latitude,
    Double longitude,
    Double temperature,
    String timestamp
) {
    public static StationDto from(Station station) {
        return new StationDto(
            station.id,
            station.stationId,
            station.name,
            station.latitude,
            station.longitude,
            null,
            null
        );
    }
}
```

---

### 16. DTO Field Mutability

**Location:** `StationDto.java` and `TemperatureHistoryDto.java`

**Issue:** DTO classes use mutable public fields instead of immutable records or proper encapsulation.

**Impact:** DTOs can be accidentally modified after creation, leading to unpredictable behavior.

**Recommendation:** Convert to Java records (available since Java 16/17):

```java
public record StationDto(
    Long id,
    String stationId,
    String name,
    Double latitude,
    Double longitude,
    Double temperature,
    String timestamp
) {}
```

---

### 17. Missing NOT NULL Constraints

**Location:** `V1__initial_schema.sql`, lines 13-14

**Issue:** The `readings` table lacks explicit NOT NULL constraints on `temperature` and `timestamp` columns.

```sql
temperature REAL NOT NULL,  -- Present
timestamp TIMESTAMP NOT NULL,  -- Present
```

Note: These actually have NOT NULL in the schema. This issue can be disregarded.

---

### 18. Inconsistent Logging Style

**Location:** `TemperatureService.java`, lines 20-24

**Issue:** Uses both constructor-style and method-style logging:
- Constructor: `Logger.getLogger(TemperatureService.class)`
- Should be: `@Inject Logger log` in Quarkus

**Recommendation:** Use proper CDI injection:

```java
@Inject
Logger log;
```

---

## Security Considerations

### 19. CORS Credentials with Wildcard

**Location:** `CorsFilter.java`, lines 14-15

**Issue:** The filter sets `Access-Control-Allow-Credentials` to true while using a specific origin. This is correct, but if the origin were changed to `*` (wildcard), it would be a security vulnerability as browsers don't allow credentials with wildcard origins.

**Current code is secure**, but future changes could introduce vulnerabilities.

---

### 20. No Rate Limiting

**Location:** `TemperatureResource.java`

**Issue:** The API endpoints have no rate limiting, making them vulnerable to abuse.

**Recommendation:** Consider adding rate limiting using Quarkus extensions or a filter:

```java
@ServerRateLimited
@GET
@Path("/stations")
public List<StationDto> getAllStations() { ... }
```

---

## Summary of Findings

| Severity | Count |
|----------|-------|
| Critical | 3 |
| High | 4 |
| Medium | 6 |
| Low | 5 |

### Priority Actions

1. **Immediate (Critical):** Fix the database schema type mismatch (issue #2)
2. **Immediate (Critical):** Fix the N+1 query problem (issue #1)
3. **Immediate (Critical):** Add input validation with proper 404 responses (issue #3)
4. **Soon (High):** Add HTTP timeout (issue #4)
5. **Soon (High):** Make CORS origin configurable (issue #5)
6. **Soon (High):** Handle scheduled job failures properly (issue #6)

The application has a solid foundation but needs attention to these critical and high-priority issues before production deployment, particularly around data integrity, performance, and operational monitoring.
