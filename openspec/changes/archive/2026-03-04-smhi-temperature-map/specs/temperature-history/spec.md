## ADDED Requirements

### Requirement: Show temperature history graph on station click
The system SHALL display a line graph showing the temperature history when a user clicks on a station marker.

#### Scenario: User clicks station marker
- **WHEN** a user clicks on a station marker on the map
- **THEN** the system SHALL fetch temperature history from /api/temperatures/{stationId}/history
- **AND** SHALL display a graph showing temperature over time
- **AND** the graph SHALL include the last 24 hours of data by default

#### Scenario: History graph displays data
- **WHEN** the history graph renders
- **THEN** the graph SHALL show temperature (Y-axis) vs time (X-axis)
- **AND** each data point SHALL be labeled with the exact temperature and timestamp

### Requirement: API returns station temperature history
The system SHALL provide an API endpoint that returns historical temperature readings for a specific station.

#### Scenario: Request station history
- **WHEN** a GET request is made to /api/temperatures/{stationId}/history
- **THEN** the system SHALL return a JSON array of temperature readings
- **AND** each reading SHALL include: temperature value, timestamp
- **AND** the results SHALL be limited to the last 24 hours
