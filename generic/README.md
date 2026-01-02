# Generic Microservice Template

This is a generic microservice template based on the route-service structure, following the alpha microservice pattern. It provides a complete foundation with:

- ✅ Full CRUD operations
- ✅ Kafka integration (producer and consumer)
- ✅ Swagger/OpenAPI documentation
- ✅ Dockerfile for containerization
- ✅ Multi-module Maven structure (generic_api and generic_service)
- ✅ Eureka service discovery integration
- ✅ PostgreSQL database integration

## Structure

```
generic/
├── generic_api/          # API module (DTOs, interfaces)
│   └── src/main/java/com/rmit/generic/generic_api/
│       ├── internal/     # Internal DTOs and services
│       └── external/     # External DTOs and services (Kafka)
├── generic_service/      # Service module (implementations)
│   └── src/main/java/com/rmit/generic/generic_service/
│       ├── generic/      # Domain logic (controller, model, repo, service)
│       └── common/       # Shared components (config, kafka, http)
├── Dockerfile
├── Makefile
└── pom.xml
```

## Usage

### 1. Replace Generic with Your Entity Name

To customize this template for your specific use case, replace all occurrences of:
- `Generic` / `generic` with your entity name (e.g., `Product` / `product`)
- Update package names accordingly
- Update table names in entity models
- Update field names in DTOs and models to match your requirements

### 2. Configure Application

Update `generic_service/src/main/resources/application.yml`:
- Database connection settings
- Kafka bootstrap servers
- Eureka server URL
- Application name and port

### 3. Build and Run

```bash
# Build
make build

# Run
make run

# Or use Maven directly
./mvnw clean package -DskipTests
java -jar generic_service/target/generic_service-0.0.1-SNAPSHOT.jar
```

### 4. Docker Build

```bash
docker build --build-arg MY_MODULE=generic -t generic-service .
docker run -p 8080:8080 generic-service
```

### 5. API Documentation

Once running, access Swagger UI at:
- Direct service: http://localhost:8080/swagger-ui.html
- Via gateway: http://gateway-url/api/generic/swagger-ui.html

## API Endpoints

- `POST /generic` - Create a new entity
- `GET /generic/{id}` - Get entity by ID
- `GET /generic` - List all entities
- `PUT /generic/{id}` - Update entity
- `DELETE /generic/{id}` - Delete entity

## Kafka Topics

Default topics (configured in `GenericTopicRegistry`):
- `generic.test_request_reply_req` / `generic.test_request_reply_res`
- `generic.list_req` / `generic.list_res`

## Data Generation

The service includes a `DataGeneratorConfig` that can generate mock data on startup. To enable data generation:

### Option 1: Using Environment Variable (Recommended)
Set `SEED_DATA_ENABLED=true` in your environment or docker-compose.yml

### Option 2: Using Seed Key
Set `SEED_DATA_KEY=seed_db` in your environment or application.yml

**Note**: Data generation only runs if:
- Seeding is enabled AND
- The database is empty (no existing entities)

The generator creates 5 sample entities by default. Modify `generateMockData()` method in `DataGeneratorConfig` to customize the generated data.

## Notes

- All placeholder fields in DTOs and models should be replaced with your specific fields
- Add custom repository methods as needed
- Extend Kafka consumers/producers for your specific messaging needs
- Customize mock data generation in `DataGeneratorConfig.generateMockData()`

