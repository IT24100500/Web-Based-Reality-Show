# Web-Based Reality Show Voting System

A Spring Boot application for managing voting in reality television shows, allowing viewers to vote for their favorite contestants.

## Overview

This system provides a web-based platform for conducting polls and managing votes in reality TV shows. It includes features for:
- Contestant management
- Real-time voting
- Vote tracking and analytics
- Administrative controls

## Technology Stack

- **Backend**: Spring Boot 3.5.4
- **Database**: H2 (in-memory for development)
- **ORM**: Hibernate/JPA
- **Frontend**: Thymeleaf templates
- **Build Tool**: Maven
- **Java Version**: 17

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use the included wrapper)

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
./mvnw spring-boot:run
```

The application will start on http://localhost:8080

### Database Access

The application uses an in-memory H2 database for development. You can access the H2 console at:
http://localhost:8080/h2-console

**Connection details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Running Tests

```bash
./mvnw test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   └── WebBasedRealityShowVotingSystemApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/demo/
        └── WebBasedRealityShowVotingSystemApplicationTests.java
```

## Development

This is currently a basic Spring Boot project setup. The following features are planned:
- Contestant entity and management
- Voting functionality
- User authentication
- Real-time vote counting
- Administrative dashboard
- Responsive web interface

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests to ensure everything works
5. Submit a pull request

## License

This project is developed for educational purposes.