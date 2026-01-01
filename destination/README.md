# Destination Service

## Overview
The Destination Service is a microservice that manages destination information including names and geographic coordinates (latitude/longitude).

## Features
- **Create** new destinations
- **Read** destination by ID or list all destinations
- **Update** existing destinations
- **Delete** destinations

## API Endpoints

All endpoints are available through the API Gateway at `http://localhost:10000/api/destination/`

### Create Destination
- **POST** `/destinations`
- **Request Body:**
```json
{
  "name": "Sydney Opera House",
  "latitude": -33.8568,
  "longitude": 151.2153
}
```
- **Response:** `201 CREATED`
```json
{
  "id": "uuid",
  "name": "Sydney Opera House",
  "latitude": -33.8568,
  "longitude": 151.2153,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "message": "Destination created successfully"
}
```

### Get Destination by ID
- **GET** `/destinations/{id}`
- **Response:** `200 OK`

### Get All Destinations
- **GET** `/destinations`
- **Response:** `200 OK` - Returns array of destinations

### Update Destination
- **PUT** `/destinations/{id}`
- **Request Body:**
```json
{
  "name": "Updated Name",
  "latitude": -33.8568,
  "longitude": 151.2153
}
```
- **Response:** `200 OK`

### Delete Destination
- **DELETE** `/destinations/{id}`
- **Response:** `204 NO CONTENT`

## Architecture

### Structure
```
destination/
├── controller/
│   └── DestinationController.java    # REST API endpoints
├── dto/
│   ├── DestinationRequest.java       # Request payload
│   └── DestinationResponse.java      # Response payload
├── entity/
│   └── Destination.java              # JPA entity
├── repo/
│   └── DestinationRepository.java    # Data access layer
└── service/
    ├── DestinationService.java       # Service interface
    └── impl/
        └── DestinationServiceImpl.java # Service implementation
```

## Configuration

### Application Properties
- **Port:** 8083
- **Database:** PostgreSQL (destination_db)
- **Eureka:** Registered with service discovery

## Swagger Documentation

Access the API documentation at:
- **Direct:** http://localhost:8083/swagger-ui.html
- **Via Gateway:** http://localhost:10000/swagger-ui.html (select "Destination Service API")

## Running the Service

### Standalone
```bash
cd destination
mvn spring-boot:run
```

### With Docker Compose
```bash
docker-compose up destination
```

## Database Schema

### destinations table
| Column     | Type            | Constraints    |
|------------|-----------------|----------------|
| id         | UUID            | PRIMARY KEY    |
| name       | VARCHAR         | NOT NULL       |
| latitude   | DOUBLE          | NOT NULL       |
| longitude  | DOUBLE          | NOT NULL       |
| created_at | TIMESTAMP       |                |
| updated_at | TIMESTAMP       |                |

## Dependencies
- Spring Boot 3.5.9
- Spring Data JPA
- PostgreSQL Driver
- Spring Cloud Netflix Eureka Client
- Springdoc OpenAPI (Swagger)
- Lombok
- Bean Validation

## Error Handling
- **400 BAD REQUEST:** Invalid input or duplicate destination name
- **404 NOT FOUND:** Destination not found
- **204 NO CONTENT:** Successful deletion

