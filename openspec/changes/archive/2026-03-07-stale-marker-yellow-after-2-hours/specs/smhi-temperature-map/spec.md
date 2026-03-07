## ADDED Requirements

### Requirement: Stale readings use distinct marker color
The system SHALL render station markers in yellow when the latest reading for that station is older than 2 hours.

#### Scenario: Reading older than 2 hours is stale
- **WHEN** a station marker is rendered and the latest reading timestamp is more than 2 hours older than current time
- **THEN** the marker color is yellow

#### Scenario: Fresh positive reading keeps temperature color
- **WHEN** a station marker is rendered and the latest reading is 2 hours old or newer and temperature is above 0C
- **THEN** the marker color is blue

#### Scenario: Fresh negative reading keeps temperature color
- **WHEN** a station marker is rendered and the latest reading is 2 hours old or newer and temperature is below 0C
- **THEN** the marker color is red
