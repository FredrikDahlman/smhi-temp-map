## Why

The codebase has `Reading` and `Station` classes in both `entity` and `domain/model` packages. This naming collision makes the code harder to read and requires fully qualified class names in conversion methods (e.g., `com.github.fredrikdahlman.tempmap.domain.model.Reading`). Using distinct suffixes (`Entity` for persistence, `Model` for domain) improves clarity and eliminates the need for FQNs.

## What Changes

- Rename `entity/Reading.java` → `entity/ReadingEntity.java`
- Rename `entity/Station.java` → `entity/StationEntity.java`
- Rename `domain/model/Reading.java` → `domain/model/ReadingModel.java`
- Rename `domain/model/Station.java` → `domain/model/StationModel.java`
- Update all imports and references in dependent files
- Simplify `toDomain()` conversion methods (no longer need FQNs)

## Capabilities

### New Capabilities
(None - this is a refactoring task)

### Modified Capabilities
(None - no requirement changes)

## Impact

**Affected files (16 total):**

| File | Action |
|------|--------|
| `entity/Reading.java` | Rename to ReadingEntity.java |
| `entity/Station.java` | Rename to StationEntity.java |
| `domain/model/Reading.java` | Rename to ReadingModel.java |
| `domain/model/Station.java` | Rename to StationModel.java |
| `adapters/persistence/ReadingRepository.java` | Update imports |
| `adapters/persistence/StationRepository.java` | Update imports |
| `domain/service/TemperatureQueryService.java` | Update imports |
| `domain/service/TemperatureCommandService.java` | Update imports |
| `ports/ReadingPort.java` | Update imports |
| `ports/StationPort.java` | Update imports |
| `resource/TemperatureResource.java` | Update imports |
| `dto/TemperatureHistoryDto.java` | Update imports |
| `dto/StationDto.java` | Update imports |

No database schema changes - table names remain unchanged (`readings`, `stations`).
