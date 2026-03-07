# SMHI Temperature Map

## Overview

A web application that collects temperature data from SMHI's open data API and presents it on an interactive map of Sweden.

## System Requirements

### Data Collection

- SHALL fetch temperature data from SMHI Open Data API every hour
- SHALL store station metadata (id, name, lat, lon) permanently
- SHALL store temperature readings with timestamps indefinitely
- SHALL handle API errors gracefully without crashing

### API Endpoints

- `GET /api/stations` - Returns all stations with their coordinates
- `GET /api/temperatures/current` - Returns current temperature for each station
- `GET /api/temperatures/{stationId}/history` - Returns temperature history for a station (last 24 hours)

### Frontend

- SHALL display Sweden on an interactive map centered at approximately 60°N, 15°E
- SHALL show temperature markers for each station
- SHALL display temperature value directly on each marker (blue for above 0°C, red for below)
- SHALL display temperature value in Celsius on marker popup when clicked
- SHALL show history graph when user clicks on a station marker
- SHALL support manual refresh of data

### Technical Constraints

- SHALL use Java 25 with Quarkus backend
- SHALL use SQLite database with Flyway migrations
- SHALL use React with Leaflet for frontend mapping
- SHALL use JUnit 5 for backend testing
- SHALL enable CORS for frontend on localhost:3000

### Architecture

The backend follows a **clean architecture** pattern with clear layer separation:

#### Layer Structure

| Layer | Location | Purpose |
|-------|----------|---------|
| **domain/model** | `domain/model/*.java` | Pure domain objects (records) - no framework dependencies |
| **domain/service** | `domain/service/*Service.java` | Business logic - uses ports (interfaces) |
| **ports** | `ports/*Port.java` | Interface definitions - contracts between layers |
| **adapters** | `adapters/persistence/*.java` | Implementations of ports |
| **resource** | `resource/*Resource.java` | REST API handlers |
| **dto** | `dto/*.java` | API request/response objects |
| **entity** | `entity/*.java` | JPA entities - persistence only |

#### Design Principles

- **Dependency Flow**: Always points inward toward domain. Domain owns contracts (ports), adapters fulfill them.
- **Ports First**: When adding features, define the port interface before implementation
- **Query vs Command**: Separate read operations (QueryService) from write operations (CommandService)
- **Domain Purity**: Domain models and services know nothing about persistence or HTTP

#### Adding New Code

When adding a new feature:

1. Create domain model in `domain/model/` if needed
2. Define port interface in `ports/`
3. Implement adapter in `adapters/persistence/`
4. Add business logic to appropriate service in `domain/service/`
5. Expose via REST in `resource/`

## Data Model

### Station
- `id` (Long, PK)
- `stationId` (String, unique)
- `name` (String)
- `latitude` (Double)
- `longitude` (Double)

### Reading
- `id` (Long, PK)
- `station` (Station, FK)
- `temperature` (Double, celsius)
- `timestamp` (Instant)