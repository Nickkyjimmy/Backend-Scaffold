# Route Service

## Overview
The Route Service is a microservice that manages route information including route names, numbers, and associated destination IDs.

## Features
- **Create** new routes
- **Read** route by ID or list all routes
- **Update** existing routes
- **Delete** routes

## API Endpoints

All endpoints are available through the API Gateway at `http://localhost:10000/api/route/`

### Create Route
- **POST** `/routes`
- **Request Body:**
```json
{
  "name": "City Circle Route",
  "number": "96",
  "listOfDestinationIds": [
    "550e8400-e29b-41d4-a716-446655440001",
    "550e8400-e29b-41d4-a716-446655440002"
  ]
}
```
- **Response:** `201 CREATED`
```json
{
  "id": "uuid",
  "name": "City Circle Route",
  "number": "96",
  "listOfDestinationIds": [
    "550e8400-e29b-41d4-a716-446655440001",
    "550e8400-e29b-41d4-a716-446655440002"
  ],
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "message": "Route created successfully"
}
```

### Get Route by ID
- **GET** `/routes/{id}`
- **Response:** `200 OK`

### Get All Routes
- **GET** `/routes`
- **Response:** `200 OK` - Returns array of routes

### Update Route
- **PUT** `/routes/{id}`
- **Request Body:**
```json
{
  "name": "Updated Route Name",
  "number": "96",
  "listOfDestinationIds": [
    "550e8400-e29b-41d4-a716-446655440001"
  ]
}
```
- **Response:** `200 OK`

### Delete Route
- **DELETE** `/routes/{id}`
- **Response:** `204 NO CONTENT`

## Architecture

### Structure
```
route/
├── controller/
│   └── RouteController.java          # REST API endpoints
├── dto/
│   ├── RouteRequest.java             # Request payload
│   └── RouteResponse.java            # Response payload
├── entity/
│   └── Route.java                    # JPA entity
├── repo/
│   └── RouteRepository.java          # Data access layer
└── service/
    ├── RouteService.java             # Service interface
    └── impl/
        └── RouteServiceImpl.java     # Service implementation
```

## Configuration

### Application Properties
- **Port:** 8082
- **Database:** PostgreSQL (route_db)
- **Eureka:** Registered with service discovery

## Swagger Documentation

Access the API documentation at:
- **Direct:** http://localhost:8082/swagger-ui.html
- **Via Gateway:** http://localhost:10000/swagger-ui.html (select "Route Service API")

## Running the Service

### Standalone
```bash
cd route
mvn spring-boot:run
```

### With Docker Compose
```bash
docker-compose up route
```

## Database Schema

### routes table
| Column     | Type            | Constraints    |
|------------|-----------------|----------------|
| id         | UUID            | PRIMARY KEY    |
| name       | VARCHAR         | NOT NULL       |
| number     | VARCHAR         | NOT NULL       |
| created_at | TIMESTAMP       |                |
| updated_at | TIMESTAMP       |                |

### route_destinations table
| Column          | Type   | Constraints             |
|-----------------|--------|-------------------------|
| route_id        | UUID   | FOREIGN KEY (routes.id) |
| destination_id  | UUID   |                         |

## Dependencies
- Spring Boot 3.5.9
- Spring Data JPA
- PostgreSQL Driver
- Spring Cloud Netflix Eureka Client
- Springdoc OpenAPI (Swagger)
- Lombok
- Bean Validation

## Error Handling
- **400 BAD REQUEST:** Invalid input or duplicate route number
- **404 NOT FOUND:** Route not found
- **204 NO CONTENT:** Successful deletion

## Notes
- Route numbers must be unique
- `listOfDestinationIds` stores UUID references to destinations from the Destination Service
- The relationship is maintained as a simple list of UUIDs for loose coupling between services

