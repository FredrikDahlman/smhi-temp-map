## ADDED Requirements

### Requirement: Store station metadata
The system SHALL maintain a registry of SMHI weather stations with their metadata including station ID, name, latitude, and longitude.

#### Scenario: New station discovered
- **WHEN** a temperature reading is received for an unknown station
- **THEN** the system SHALL create a new station record with the station ID, name, and coordinates from the SMHI response

#### Scenario: Station already exists
- **WHEN** a temperature reading is received for an existing station
- **THEN** the system SHALL use the existing station record
- **AND** SHALL NOT create a duplicate

### Requirement: Retrieve all stations
The system SHALL provide an API endpoint to return all registered stations.

#### Scenario: Request all stations
- **WHEN** a GET request is made to /api/stations
- **THEN** the system SHALL return a JSON array of all stations
- **AND** each station SHALL include: id, stationId, name, latitude, longitude
