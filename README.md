# Football Team DB Application

A Spring Boot and PostgreSQL application to manage football teams and players. The application provides a RESTful API to create, retrieve, update, and delete (CRUD) operations for clubs and players. This project uses Docker to simplify database setup and ensure a consistent environment.

It was created to learn and practice building a RESTful API with Spring Boot and PostgreSQL

## Table of Contents

- [Key Features](#key-features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Key Features

- REST API to manage football clubs and players.
- Full and partial updates for player and club details.
- Dockerized PostgreSQL setup for easy database management.
- Integration with ModelMapper to handle entity-DTO conversions.

## Technologies

- **Java 17**
- **Spring Boot 3.1**
- **PostgreSQL**
- **Docker**
- **ModelMapper** for entity-to-DTO conversion
- **JUnit 5** and **MockMvc** for testing

## Setup

**Prerequisites**
- Java 17 or later
- Maven
- Docker

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/football-team-db.git
   cd football-team-db
   ```

2. **Configure the Database**  
   The application uses PostgreSQL as the primary database. Configuration details are set in the application.properties file.

3. **Run Docker Compose**  
   Use docker-compose.yml to set up the PostgreSQL container. To start the database: 
   ```bash
   docker compose-up
   ```

4. **Build and Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```

## Running the Application  
Once the application is running, the API will be available at http://localhost:8080.

## API Endpoints

### Club Endpoints

- **POST /clubs** - Create a new club
- **GET /clubs/{id}** - Retrieve club details by ID
- **GET /clubs** - Retrieve all clubs
- **PUT /clubs/{id}** - Full update of club details
- **PATCH /clubs/{id}** - Partial update of club details
- **DELETE /clubs/{id}** - Delete club by ID

### Player Endpoints

- **POST /players** - Create a new player
- **GET /players/{id}** - Retrieve player details by ID
- **GET /players** - Retrieve all players
- **PUT /players/{id}** - Full update of player details
- **PATCH /players/{id}** - Partial update of player details
- **DELETE /players/{id}** - Delete player by ID

### Example Request
To create a new player, you might send the following JSON data to **POST /players**:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1995-08-15",
  "position": "Midfielder",
  "club": {
    "name": "Sample Club",
    "shortName": "SC",
    "foundingDate": "1992-10-12",
    "totalTrophies": 15
  }
}
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
