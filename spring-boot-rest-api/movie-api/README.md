## Test-drive development Practice with Spring Boot, Spring Security, JWT, JPA, And Rest-API

Build Restful CRUD API with Unit test, Integration test, Database integration test, and Mocking, etc.

## Basic Requirements

-   `Spring Boot v2.0+`
-   `JDK v1.8+`
-   `Maven 3+`

---

## Test

1. Clone the repository

```bash
git clone https://github.com/kkk12a9/Test-Driven-devevelopment-practice-study-using-java-spring-boot.git
```

-   change the directory to rest-api project

```bash
cd spring-boot-rest-api/movie-api
```

2. Update database properties as yours

-   open `src/main/resources/application.properties`
-   change `spring.datasource.username` and `spring.datasource.password` values to fit MySQL installation properties in your system.

3. Run application with Maven CLI

```bash
./mvnw clean spring-boot:run
```

The default port is `2000`, so the application will start running at http://localhost:2000

---

## Summary

### Database architecture

![database schema](https://user-images.githubusercontent.com/48620639/215303060-0d9579d5-fd51-4b9c-9ab1-f684bd7e9316.png)

### RestController Endpoints

| Rest         | Url                    | Description              |
| ------------ | ---------------------- | ------------------------ |
| Auth         | /auth                  | Authentication endpoints |
| User         | /api/users             | Application users        |
| Movie        | /api/movies            | Movie endpoints          |
| Movie Review | /api/{movieId}/reviews | Movie reviews endpoints  |
| Tag          | /api/tags              | Movie tag endpoints      |

---

### Inspired Project

https://github.com/ivangfr/springboot-react-social-login
