## 1. Project Setup

- [x] 1.1 Create Quarkus project with Java 25, Maven, SQLite + Flyway extensions
- [x] 1.2 Set up React project with Vite
- [x] 1.3 Add Leaflet and react-leaflet dependencies
- [x] 1.4 Add Recharts for history graph
- [x] 1.5 Verify dev servers run for both backend and frontend

## 2. Database

- [x] 2.1 Create Flyway migration for stations and readings tables
- [x] 2.2 Configure Quarkus to use SQLite datasource with Flyway
- [x] 2.3 Create JPA entities for Station and Reading
- [x] 2.4 Create repositories for both entities

## 3. SMHI Data Collection

- [x] 3.1 Implement SMHI API client to fetch temperature data
- [x] 3.2 Implement JSON parsing for SMHI response
- [x] 3.3 Add Quarkus scheduler for hourly fetches (@Scheduled)
- [x] 3.4 Implement station upsert logic
- [x] 3.5 Implement temperature reading storage
- [x] 3.6 Add error handling and logging

## 4. Backend API

- [x] 4.1 Create GET /api/stations endpoint
- [x] 4.2 Create GET /api/temperatures/current endpoint
- [x] 4.3 Create GET /api/temperatures/{stationId}/history endpoint
- [x] 4.4 Add request/response DTOs

## 5. Frontend - Map

- [x] 5.1 Set up React app with Leaflet map
- [x] 5.2 Configure map to center on Sweden
- [x] 5.3 Fetch stations and temperatures on load
- [x] 5.4 Display temperature markers on map
- [x] 5.5 Style markers with temperature labels

## 6. Frontend - History Graph

- [x] 6.1 Create history graph component using Recharts
- [x] 6.2 Add click handler to station markers
- [x] 6.3 Fetch history data when station is clicked
- [x] 6.4 Display graph in popup or side panel
- [x] 6.5 Handle loading and error states

## 7. Testing

- [x] 7.1 Add unit tests for SMHI API client
- [x] 7.2 Add unit tests for service layer
- [x] 7.3 Add integration tests for REST endpoints
- [ ] 7.4 Add basic React component tests for map
- [x] 7.5 Run full test suite and fix failures

## 8. GitHub Deployment

- [x] 8.1 Create new GitHub repository
- [x] 8.2 Initialize git and push code
- [x] 8.3 Verify repository structure
