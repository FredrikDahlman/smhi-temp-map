## 1. Create Domain Models

- [x] 1.1 Create domain/model/Station.java (record)
- [x] 1.2 Create domain/model/Reading.java (record)
- [x] 1.3 Add toDomain() and fromDomain() methods to JPA entities

## 2. Create Port Interfaces

- [x] 2.1 Create ports/StationPort.java interface
- [x] 2.2 Create ports/ReadingPort.java interface
- [x] 2.3 Create ports/WeatherDataPort.java interface (rename from SmhiClient)

## 3. Create Repository Adapters

- [x] 3.1 Create adapters/persistence/StationRepository.java
- [x] 3.2 Create adapters/persistence/ReadingRepository.java
- [x] 3.3 Rename SmhiClient to SmhiClientAdapter, implement WeatherDataPort

## 4. Create Domain Services

- [x] 4.1 Create domain/service/TemperatureQueryService.java
- [x] 4.2 Create domain/service/TemperatureCommandService.java

## 5. Update REST Resource

- [x] 5.1 Update TemperatureResource to inject query and command services
- [x] 5.2 Update endpoint methods to use new services

## 6. Cleanup

- [x] 6.1 Delete old TemperatureService.java
- [x] 6.2 Remove unused imports from entities
- [x] 6.3 Run tests and verify all endpoints work
