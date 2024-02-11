# Movie REST API
It's a movie API created in Spring. There are 4 domain models. The main one - movie, and supporting: genre, actor and review.

Every domain has full CRUD implementation, some have a few business endpoints. There's also frontend-ready query endpoint for searching movies by title and other fields.  It's fully validated API, with error messages and status codes.

Also, there is a Swagger Open Api documentation. Every endpoint has a Postman request with tests in *postman.json*.
To run Postgres, there is docker-compose file.

## Technologies
- Spring
- Maven
- Java 17
- Spring Boot
- Postgres
- Lombok

## Links
- [Swagger docs](http://localhost:8080/swagger-ui/index.html),
- [Application Port](http://localhost:8080/)

## Run app
1. Enter main directory
```bash
cd Spring-movie-api
```
2. Run app using Maven
```
mvn spring-boot:run
```