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