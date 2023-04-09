# Home Assignment


## Context
___
Bosch Thermotechnology plans to use REST Hooks to notify B2B clients about status events like system 
failures of connected heating appliances. Such an event-driven approach ensures sending the right 
notification to the right application at the time it occurs. Before a client can receive notifications, 
the client needs to register a new REST Hook. For this purpose, Bosch Thermotechnology plans to develop 
a service for the management of REST Hooks.


## Technologies
___
 - Java 17
 - [Spring Boot](https://spring.io/projects/spring-boot) as the application framework
 - [Spring WebFlux](https://spring.io/reactive) as the reactive web framework
 - [H2 Database](https://www.h2database.com/html/main.html) as the database
 - [Project Lombok](https://projectlombok.org/) library to reduce boilerplate code
 - [MapStruct](https://mapstruct.org/) as the object mapper converter
 - [OpenAPI 3.0](https://www.openapis.org/) as the API documentation


## Requirements
___
 - Java 17 installed


## Running the project
___

You can use the generated .jar file in the root folder or create a new one using the maven wrapper.
- [Download jar](https://github.com/joaozarate/heating-monitor/blob/main/heating-monitor-0.0.1-SNAPSHOT.jar)

### Packaging with maven wrapper

```
java -jar heating-monitor-0.0.1-SNAPSHOT.jar
```
### Running with maven wrapper

```
.\mvnw clean package
```

## Database configuration
___
Nothing needs to be done to set up the database. The project uses the H2 database.

Due to a potential issue between the H2 web interface and Spring WebFlux, the database cannot be accessed via the web interface.

The database will be created when the application is started in the same directory as the jar file.

## API documentation
___
The API documentation is available at the following file: https://github.com/joaozarate/heating-monitor/blob/main/endpoint-spec.yaml
