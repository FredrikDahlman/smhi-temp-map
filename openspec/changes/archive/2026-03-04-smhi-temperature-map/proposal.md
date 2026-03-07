## Why

Build an educational web application that collects temperature data from SMHI's open data API and presents it visually on an interactive map of Sweden. This provides a hands-on project for learning modern web development with Quarkus, React, and real-world data integration.

## What Changes

- Create a Quarkus backend with Maven, hourly scheduler to fetch temperature data from SMHI API
- Implement SQLite database to store station metadata and temperature readings indefinitely
- Build React frontend with Leaflet/OpenStreetMap showing current temperatures on map
- Add click interaction on stations to display historical temperature graph
- Include test strategy covering backend services, API endpoints, and frontend components

## Capabilities

### New Capabilities
- `smhi-data-collection`: Fetch and store temperature observations from SMHI station API
- `station-management`: Maintain registry of SMHI weather stations with coordinates
- `temperature-map`: Display current temperatures on interactive Sweden map
- `temperature-history`: Show historical temperature data as graph when station is clicked

### Modified Capabilities
- (none)

## Impact

- Backend: Quarkus (Java 25) with Maven, REST API with scheduled data fetching
- Database: SQLite with Flyway migrations, stations and readings tables
- Testing: JUnit 5 for backend unit/integration tests
- Frontend: React SPA with Leaflet map integration
- Dependency: SMHI Open Data API (external)
