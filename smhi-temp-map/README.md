# smhi-temp-map

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

---

## Architecture

This project follows a **clean architecture** pattern with clear separation between layers.

### Package Structure

```
src/main/java/com/smhi/tempmap/
├── domain/              # Business logic (pure Java, no frameworks)
│   ├── model/          # Domain entities (Station, Reading)
│   └── service/        # Domain services (Query + Command)
├── ports/              # Interfaces defining boundaries
│   ├── StationPort.java
│   ├── ReadingPort.java
│   └── WeatherDataPort.java
├── adapters/           # Implementations of ports
│   └── persistence/    # Repository implementations
├── resource/           # REST API endpoints
├── dto/                # Data transfer objects
└── entity/             # JPA entities (persistence only)
```

### Layer Rules

| Layer | Contains | Knows About |
|-------|----------|-------------|
| **domain/model** | Pure domain models (records) | Nothing else |
| **domain/service** | Business logic | Ports (interfaces) |
| **ports** | Interface definitions | Domain models |
| **adapters** | Implementations | Ports, entities |
| **resource** | HTTP handlers | Domain services |
| **dto** | API representations | Nothing specific |
| **entity** | JPA persistence | Nothing else |

### Dependency Flow

```
resource ──────▶ domain/service ──────▶ ports (interfaces)
                                      │
                                      ▼
                               adapters (implement)
                                      │
                                      ▼
                                  entities
```

---

## Code Navigation

### Adding a New Feature

1. **Domain Model** - Does it need a new domain object?
   - Create in `domain/model/`

2. **Ports** - What does the domain need from outside?
   - Create interface in `ports/`

3. **Adapters** - How do we implement the port?
   - Create in `adapters/persistence/` or `adapters/external/`

4. **Service** - What business logic operates on this?
   - Add to existing service in `domain/service/` or create new one

5. **Resource** - How is it exposed via HTTP?
   - Add endpoint in `resource/`

### Finding Code

| To find... | Look in... |
|------------|------------|
| REST endpoint | `resource/TemperatureResource.java` |
| Query logic | `domain/service/TemperatureQueryService.java` |
| Save logic | `domain/service/TemperatureCommandService.java` |
| Database access | `adapters/persistence/*Repository.java` |
| External API | `client/SmhiClient.java` |
| API response | `dto/` |

---

## Thinking in Clean Architecture

### When Adding New Code

Ask yourself:

1. **What layer does this belong in?**
   - Business logic → domain/service
   - Data access → ports + adapters
   - HTTP handling → resource

2. **What does the domain need?**
   - Define a port (interface) first
   - Keep domain pure - it should know about interfaces, not implementations

3. **Which direction do dependencies flow?**
   - Always inward toward domain
   - Domain owns the contracts (ports), adapters fulfill them

4. **Is this a read or a write?**
   - Reads → TemperatureQueryService
   - Writes → TemperatureCommandService
   - Helps keep concerns separated

### Common Patterns

**Adding a new query:**
```
1. Service: Add method to TemperatureQueryService
2. Port: Add method to relevant port interface (if needed)
3. Adapter: Implement in repository
4. Resource: Call service, map to DTO, return
```

**Adding a new command:**
```
1. Service: Add method to TemperatureCommandService
2. Port: Add method to relevant port interface (if needed)
3. Adapter: Implement in repository
4. Resource: Call service, return response
```

**Adding external API:**
```
1. Port: Create WeatherDataPort-style interface
2. Adapter: Implement in adapters/external/
3. Inject via port interface in service
```

---

## Database

### Schema

- **stations**: id, station_id, name, latitude, longitude, created_at
- **readings**: id, station_id, temperature, timestamp, created_at

### Migrations

Database migrations are in `src/main/resources/db/migration/`. Do not modify existing migration files - create new ones for changes.

### Viewing Data

```bash
# List tables
sqlite3 smhi-temp-map.db ".tables"

# View data
sqlite3 smhi-temp-map.db "SELECT * FROM readings LIMIT 10;"
sqlite3 smhi-temp-map.db "SELECT * FROM stations LIMIT 10;"
```

---

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it's not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/smhi-temp-map-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Scheduler ([guide](https://quarkus.io/guides/scheduler)): Schedule jobs and tasks
- Flyway ([guide](https://quarkus.io/guides/flyway)): Handle your database schema migrations
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it