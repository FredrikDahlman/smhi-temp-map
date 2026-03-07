## Context

Build a web application that collects temperature data from SMHI's open data API and displays it on an interactive map of Sweden. Target users are developers learning full-stack development, with potential public access for viewing current Swedish temperatures.

**Technical Stack:**
- Backend: Quarkus (Java 25) with Maven
- Database: SQLite with Flyway migrations
- Testing: JUnit 5
- Frontend: React + Leaflet + OpenStreetMap
- Data Source: SMHI Open Data API

**SMHI API Endpoint:**
```
https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station-set/all/period/latest-hour/data.json
```
Returns JSON with station IDs, coordinates, and latest temperature observations.

## Goals / Non-Goals

**Goals:**
- Hourly automatic collection of SMHI temperature data
- Store all data indefinitely in SQLite
- Display current temperature as markers on Sweden map
- Click station marker to see temperature history graph
- Complete test coverage for backend services and APIs
- Simple frontend component tests

**Non-Goals:**
- User authentication / authorization
- Mobile app or PWA
- Weather forecast data (only current temperature)
- Push notifications or alerts
- Multi-language support (Swedish only for now)
- Date picker for historical view (out of scope per proposal)

## Decisions

### 1. Database: SQLite with Flyway migrations
**Chosen:** SQLite + Flyway for version-controlled schema
**Alternatives considered:**
- H2: Java-native but adds another dependency; harder to inspect data directly
- PostgreSQL: Overkill for single-user hobby project; requires separate server
- Manual SQL: No version control, harder to reproduce schema

**Rationale:** Flyway provides versioned migrations, easy to track schema changes, SQLite for data storage

### 2. Map: Leaflet + OpenStreetMap
**Chosen:** Leaflet with OSM tiles
**Alternatives considered:**
- MapLibre GL: Better rendering but more complex setup
- Mapbox/Stadia: Free tier has quotas; requires API key

**Rationale:** No API key, free unlimited usage, excellent Sweden detail, easy React integration via react-leaflet

### 3. Scheduler: Quarkus Scheduler
**Chosen:** `@Scheduled` annotation
**Alternatives considered:**
- External cron job calling REST endpoint: Adds complexity, extra moving parts
- Manual thread/scheduler: Reinventing the wheel

**Rationale:** Built into Quarkus, simple annotation-based configuration, integrated with application lifecycle

### 4. Chart Library: Recharts
**Chosen:** Recharts for React
**Alternatives considered:**
- Chart.js: More popular but less React-native
- D3: Too complex for simple line graph

**Rationale:** Simple React component, good default styling, easy to implement line graph

### 5. Test Strategy: JUnit 5 (backend), React Testing Library (frontend)
**Chosen:** JUnit 5 + Mockito + REST Assured for backend; Vitest + Testing Library for frontend
**Alternatives considered:**
- TestNG: Less common in Quarkus ecosystem
- Cypress: Overkill for this project; more for E2E

**Rationale:** Native Quarkus testing support, familiar patterns, sufficient for unit/integration tests

## Risks / Trade-offs

### Risk: SMHI API changes or becomes unavailable
→ **Mitigation:** Store last successful fetch timestamp; show "last updated" on frontend; add error logging

### Risk: Too many stations (~200+ in Sweden)
→ **Mitigation:** Cluster markers at zoom-out levels; load details on zoom-in

### Risk: SQLite concurrent writes during fetch
→ **Mitigation:** Single writer (scheduler runs hourly); minimal contention

### Risk: Frontend build complexity
→ **Mitigation:** Use Vite for fast dev experience; keep dependencies minimal

### Risk: Large database over time
→ **Mitigation:** Index on timestamp and station_id; consider partitioning or cleanup if needed (years away)

### Trade-off: Storing forever vs. storage management
→ Current decision: Store forever. Will need to revisit if database grows unexpectedly large (~1MB/year is negligible)

## Migration Plan

1. **Initial setup:** Create Quarkus project with Java 25, SQLite + Flyway extensions
2. **Database:** Create Flyway migration for stations and readings tables
3. **Backend:** Implement SMHI client, scheduler, REST endpoints
4. **Frontend:** Scaffold React app, add Leaflet map, implement station markers
5. **Testing:** Add JUnit tests for client, integration tests for endpoints
6. **Deploy:** Run locally to verify full flow
7. **GitHub:** Create new repository and push code
