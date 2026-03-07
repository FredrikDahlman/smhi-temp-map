## ADDED Requirements

### Requirement: Display current temperatures on map
The system SHALL display temperature markers on an interactive map of Sweden showing the current temperature at each station.

#### Scenario: Map loads with station markers
- **WHEN** the frontend loads the map view
- **THEN** the system SHALL fetch current temperatures from /api/temperatures/current
- **AND** SHALL display a marker for each station on the map
- **AND** each marker SHALL show the current temperature

#### Scenario: Marker displays temperature
- **WHEN** a station marker is rendered
- **THEN** the marker SHALL display the temperature value in Celsius
- **AND** the marker SHALL be positioned at the station's latitude/longitude

### Requirement: Center map on Sweden
The system SHALL initially display the map centered on Sweden with appropriate zoom level to show the entire country.

#### Scenario: Initial map view
- **WHEN** the map component first renders
- **THEN** the map SHALL be centered at coordinates approximately 60.0°N, 15.0°E
- **AND** the zoom level SHALL be set to show all of Sweden (approximately zoom 5)
