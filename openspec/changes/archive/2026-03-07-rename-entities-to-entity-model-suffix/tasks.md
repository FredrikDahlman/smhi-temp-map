## 1. Rename Domain Models

- [ ] 1.1 Rename `domain/model/Reading.java` → `ReadingModel.java`
- [ ] 1.2 Update import in `adapters/persistence/ReadingRepository.java`
- [ ] 1.3 Update import in `domain/service/TemperatureQueryService.java`
- [ ] 1.4 Update import in `ports/ReadingPort.java`
- [ ] 1.5 Update import in `resource/TemperatureResource.java`
- [ ] 1.6 Update import in `dto/TemperatureHistoryDto.java`

## 2. Rename Station Domain Model

- [ ] 2.1 Rename `domain/model/Station.java` → `StationModel.java`
- [ ] 2.2 Update import in `domain/service/TemperatureQueryService.java`
- [ ] 2.3 Update import in `domain/service/TemperatureCommandService.java`
- [ ] 2.4 Update import in `ports/StationPort.java`
- [ ] 2.5 Update import in `resource/TemperatureResource.java`
- [ ] 2.6 Update import in `adapters/persistence/StationRepository.java`
- [ ] 2.7 Update import in `dto/StationDto.java`

## 3. Rename Entity Classes

- [ ] 3.1 Rename `entity/Reading.java` → `ReadingEntity.java`
- [ ] 3.2 Update references in `adapters/persistence/ReadingRepository.java`
- [ ] 3.3 Rename `entity/Station.java` → `StationEntity.java`
- [ ] 3.4 Update references in `adapters/persistence/ReadingRepository.java`
- [ ] 3.5 Update references in `adapters/persistence/StationRepository.java`

## 4. Simplify Conversion Methods

- [ ] 4.1 Simplify `toDomain()` in `ReadingEntity.java` (remove FQNs)
- [ ] 4.2 Simplify `toDomain()` in `StationEntity.java` (remove FQNs)

## 5. Verify

- [ ] 5.1 Run `mvn compile` to verify build passes
- [ ] 5.2 Run `mvn test` to verify tests pass