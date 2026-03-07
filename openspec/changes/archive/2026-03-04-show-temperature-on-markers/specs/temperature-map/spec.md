## MODIFIED Requirements

### Requirement: Display current temperatures on map
The system SHALL display temperature markers on an interactive map of Sweden showing the current temperature at each station.

#### Scenario: Map loads with station markers
- **WHEN** the frontend loads the map view
- **THEN** the system SHALL fetch current temperatures from /api/temperatures/current
- **AND** SHALL display a marker for each station on the map
- **AND** each marker SHALL show the current temperature directly on the marker

#### Scenario: Marker displays temperature
- **WHEN** a station marker is rendered
- **THEN** the marker SHALL display the temperature value in Celsius directly on the marker
- **AND** the marker color SHALL indicate temperature (blue for above 0°C, red for below)
- **AND** the marker SHALL be positioned at the station's latitude/longitude
- **AND** clicking the marker SHALL show additional details in a popup
