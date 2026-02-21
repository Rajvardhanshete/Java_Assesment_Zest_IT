<think>
The user wants a README.md template for their GitHub repository. They want it in a simple format that they can directly copy and paste into their README.md file. Let me provide a clean, well-structured README.md template.
</think>+
- **Spring Boot**: 3.2.0
- **Spring Security**: 6.x
- **Spring Data JPA**: 3.2.0
- **PostgreSQL**: 15
- **JWT**: 0.12.3
- **Maven**: 3.9+
- **Docker**: Latest

---

## Features

- Full CRUD Operations on Products
- JWT Authentication with Refresh Token
- Role-Based Authorization (ADMIN, USER)
- Pagination Support
- Input Validation (Jakarta Validation)
- Swagger/OpenAPI Documentation
- Global Exception Handling
- Docker Support
- Unit Testing (JUnit 5 & Mockito)
- Integration Testing (Spring Boot Test)

---

## Project Structure

```
product-management-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/zest/productapi/product_management_api/
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   └── ProductController.java
│   │   │       ├── dto/
│   │   │       │   ├── AuthRequest.java
│   │   │       │   ├── AuthResponse.java
│   │   │       │   ├── ItemRequest.java
│   │   │       │   ├── ItemResponse.java
│   │   │       │   ├── ProductRequest.java
│   │   │       │   └── ProductResponse.java
│   │   │       ├── entity/
│   │   │       │   ├── Item.java
│   │   │       │   ├── Product.java
│   │   │       │   └── User.java
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── ResourceNotFoundException.java
│   │   │       ├── repository/
│   │   │       │   ├── ProductRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── security/
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── JwtService.java
│   │   │       ├── service/
│   │   │       │   ├── ProductService.java
│   │   │       │   └── UserService.java
│   │   │       └── ProductManagementApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       ├── java/
│       │   └── com/zest/productapi/product_management_api/
│       │       ├── controller/
│       │       │   └── ProductControllerTest.java
│       │       └── service/
│       │           └── ProductServiceTest.java
│       └── resources/
│           └── application.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Database Schema

### product Table

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT |
| product_name | VARCHAR(255) | NOT NULL |
| created_by | VARCHAR(100) | NOT NULL |
| created_on | TIMESTAMP | NOT NULL |
| modified_by | VARCHAR(100) | NULL |
| modified_on | TIMESTAMP | NULL |

### item Table

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT |
| product_id | INT | FOREIGN KEY, NOT NULL |
| quantity | INT | NOT NULL |

---

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/auth/login | Login to get JWT token |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/products | Get all products (Paginated) |
| GET | /api/v1/products/{id} | Get product by ID |
| POST | /api/v1/products | Create new product |
| PUT | /api/v1/products/{id} | Update product |
| DELETE | /api/v1/products/{id} | Delete product |
| GET | /api/v1/products/{id}/items | Get items by product ID |

---

## Setup Instructions

### Option 1: Docker

```bash
docker-compose up --build
```

### Option 2: Local Setup

1. Create database in PostgreSQL:

```sql
CREATE DATABASE product_db;
```

2. Update application.yml with your database credentials

3. Run the application:

```bash
mvn clean install
mvn spring-boot:run
```

---

## How to Use

### 1. Login

```
POST /api/v1/auth/login

{
  "username": "admin",
  "password": "admin123"
}
```

### 2. Access Protected Endpoints

```
Authorization: Bearer <JWT_TOKEN>
```

### 3. Create Product

```
POST /api/v1/products
Authorization: Bearer <JWT_TOKEN>

{
  "productName": "Laptop",
  "items": [
    {
      "quantity": 10
    }
  ]
}
```

### 4. Get All Products

```
GET /api/v1/products?page=0&size=10
Authorization: Bearer <JWT_TOKEN>
```

### 5. Get Product by ID

```
GET /api/v1/products/1
Authorization: Bearer <JWT_TOKEN>
```

### 6. Update Product

```
PUT /api/v1/products/1
Authorization: Bearer <JWT_TOKEN>

{
  "productName": "Gaming Laptop",
  "items": [
    {
      "quantity": 5
    }
  ]
}
```

### 7. Delete Product

```
DELETE /api/v1/products/1
Authorization: Bearer <JWT_TOKEN>
```

---

## Testing

```bash
mvn test
```

---

## Default User

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ROLE_ADMIN |

---

## Configuration

### application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/product_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update

app:
  jwt:
    secret: <BASE64_SECRET_KEY>
    expiration: 86400000
    refresh-expiration: 604800000
```

---

## Docker Commands

```bash
docker-compose up --build
docker-compose down
```

---

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

---

## License

This project is for evaluation purposes only.
