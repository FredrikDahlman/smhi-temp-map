## ADDED Requirements

### Requirement: Fetch temperature data from SMHI API
The system SHALL fetch temperature data from the SMHI Open Data API endpoint at https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station-set/all/period/latest-hour/data.json

#### Scenario: Successful API fetch
- **WHEN** the scheduler triggers the fetch task
- **THEN** the system SHALL make an HTTP GET request to the SMHI API endpoint
- **AND** SHALL parse the JSON response
- **AND** SHALL store each temperature reading in the database

#### Scenario: API returns error
- **WHEN** the SMHI API returns a non-200 status code
- **THEN** the system SHALL log the error with details
- **AND** SHALL NOT crash or stop the scheduler
- **AND** SHALL retain the last successful fetch timestamp

#### Scenario: API is unreachable
- **WHEN** network error occurs when calling SMHI API
- **THEN** the system SHALL catch the exception
- **AND** SHALL log the error message
- **AND** SHALL continue normal operation for the next scheduled run

### Requirement: Run fetch scheduler hourly
The system SHALL automatically fetch temperature data once every hour using the Quarkus scheduler.

#### Scenario: Scheduler triggers on time
- **WHEN** one hour has passed since last fetch
- **THEN** the scheduler SHALL trigger the fetch task
- **AND** the task SHALL complete before the next hour

### Requirement: Store temperature readings with timestamp
The system SHALL store each temperature reading with the exact timestamp from the SMHI data.

#### Scenario: Store new reading
- **WHEN** a new temperature reading is received from SMHI
- **THEN** the system SHALL insert a record into the readings table
- **AND** the record SHALL include: station_id, temperature value, timestamp
