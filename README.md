# 📚 BookStore API

A RESTful API developed with Spring Boot for managing book loans in an online or in-person library. It allows operations with books, authors, clients, and orders. The project is modularized by domain and includes JWT authentication, OAuth2 with Google, multiple layers (controller, service, repository, validation, DTO, model...), and documentation with Swagger.

## 🚀 Technologies Used
<p align="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="40" height="40" alt="Java"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="40" height="40" alt="Spring Boot"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="40" height="40" alt="PostgreSQL"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="40" height="40" alt="Maven"/>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" width="40" height="40" alt="Git"/>
</p>

- Java 21
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring Security (JWT and OAuth2 with Google)
- PostgreSQL
- Docker
- Swagger (Springdoc OpenAPI)
- Lombok
- MapStruct

## 🏗️ Architecture

The API follows a layered architecture with domain-based modules, each with its own responsibilities:

Domains: `book`, `author`, `client`, `order`

- `model`: entity representation
- `controller`: handles HTTP requests
- `service`: business logic
- `repository`: database access with Spring Data JPA
- `dto`: input and output data
- `validator`: auxiliary validations
- `mapper`: mapping with MapStruct
- `security`: authentication and authorization
- `exception`: global error handling (`@ControllerAdvice`)
- `docs`, `config`, `util`, `common`: general support

## 🔐 Security

Authentication is done with JWT (JSON Web Token):

- Public endpoints:
  - `POST /auth/login`
  - `POST /client`
- Other endpoints require JWT token in the header:
  ```
  Authorization: Bearer <token>
  ```

Roles and permissions are managed using `@PreAuthorize` and Spring Security filters.

## 🌐 OAuth2 Authentication with Google

The API allows login with Google accounts:

- Endpoint:
  - `/oauth2/authorization/google`
- After authentication, the JWT is automatically generated.
- Configuration via `application.yml` with:
  ```yaml
  client-id: ${CLIENT_ID}
  client-secret: ${CLIENT_SECRET}
  ```

## 📌 API Documentation

Documentation available after starting the application:

👉 http://localhost:8081/swagger-ui/index.html#/

## 📁 Log

Logs are recorded in the `bookstore.log` file, as configured in `application.yml`.

---

## ▶️ How to Run

### ✅ Prerequisites
- Java 21
- Maven
- Docker (for PostgreSQL database)
- Configured Google OAuth2 account

### 📦 Start PostgreSQL with Docker (optional)
```bash
docker run --name postgres-bookstore -e POSTGRES_PASSWORD=senha -p 5432:5432 -d postgres
```

### 🧪 Configure `application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookstore
    username: postgres
    password: senha
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${CLIENT_ID}
            client-secret: ${CLIENT_SECRET}
```

### 1. Clone the repository
```bash
git clone https://github.com/CaiquePirs/bookstore.git
```

### 2. Configure `application.yml` (see above)

### 3. Run the application
```bash
./mvnw spring-boot:run
```

---

## 👨‍💻 Author

BookStore API was developed by me **Caique Pires**. Contributions are welcome!

## 📧 Contact

[![Gmail](https://img.shields.io/badge/Email-Gmail-red?style=flat&logo=gmail)](mailto:pirescaiq@gmail.com)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-LinkedIn-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/caique-pires-8843aa332)
