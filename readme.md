# Travv-World Project

Welcome to the Travv-World Project! This project is built using Kotlin and Ktor with H2 Database for storage. The goal of this project is to create a backend service that supports user registration, login, and profile management.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=for-the-badge&logo=Ktor&logoColor=violet)](https://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/Ktor-2.3.11-white.svg?style=for-the-badge&logo=Ktor&logoColor=violet)](https://kotlinlang.org)

![GitHub](https://img.shields.io/github/license/Shreemanarjun/travv-world)
![GitHub Repo stars](https://img.shields.io/github/stars/Shreemanarjun/travv-world)
![GitHub all releases](https://img.shields.io/github/downloads/Shreemanarjun/travv-world/total)
![GitHub repo size](https://img.shields.io/github/repo-size/Shreemanarjun/travv-world)
![GitHub issues](https://img.shields.io/github/issues/Shreemanarjun/travv-world)
![GitHub pull requests](https://img.shields.io/github/issues-pr/Shreemanarjun/travv-world)



## Table of Contents

- [Travv-World Project](#travv-world-project)
  - [Table of Contents](#table-of-contents)
  - [Setup](#setup)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
  - [Endpoints](#endpoints)
    - [User Registration and Login](#user-registration-and-login)
    - [Profile Management](#profile-management)
  - [Data Storage](#data-storage)
  - [Testing](#testing)
  - [Documentation](#documentation)
  - [Project Structure](#project-structure)
  - [Contributing](#contributing)
  - [License](#license)

## Setup

### Prerequisites

- [Kotlin](https://kotlinlang.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (or your preferred IDE)
- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Shreemanarjun/travv-world.git
    cd travv-world
    ```

2. Open the project in your IDE.

3. Build the project:
    ```sh
    ./gradlew build
    ```

4. Run the application:
    ```sh
    ./gradlew run
    ```

The server will start on `http://localhost:8080`.

# User Management API

This API provides endpoints for user management, including user registration, login, profile retrieval, and profile updates.

## Endpoints

### User Registration and Login

- **Register**: `POST /api/v1/user/signup`
  - **Request Body:**
    ```json
    {
      "username": "string",
      "email": "string",
      "password": "string"
    }
    ```
  - **Response:**
    - `201 Created`
      ```json
      {
        "data": "User successfully created"
      }
      ```

- **Login**: `POST /api/v1/user/login`
  - **Request Body:**
    ```json
    {
      "email": "string",
      "password": "string"
    }
    ```
  - **Response:**
    - `200 OK` with a JWT token
      ```json
      {
        "data": {
          "accessToken": "string",
          "refreshToken": "string"
        }
      }
      ```

### Profile Management

- **Get Profile**: `GET /api/v1/user/profile`
  - **Response:**
    - `200 OK` with user profile data
      ```json
      {
        "data": {
          "userID": "string",
          "username": "string",
          "email": "string"
        }
      }
      ```

- **Update Profile**: `PUT /api/v1/user/profile`
  - **Request Body:**
    ```json
    {
      "username": "string",
      "email": "string"
    }
    ```
  - **Response:**
    - `200 OK`
      ```json
      {
        "data": {
          "profile": {
            "userID": "string",
            "username": "string",
            "email": "string"
          },
          "updateMessage": "Profile updated successfully"
        }
      }
      ```

## Security

This API uses JWT for authentication. To access secured endpoints like `/api/v1/user/profile`, include the JWT in the `Authorization` header as a Bearer token.

Example:
``` 
Authorization: Bearer <your_jwt_token>

 ```


## Data Storage

User data is stored in an H2 Database. The database schema is auto-generated based on the entity definitions in the code.

## Testing

Unit tests are written using Kotlin's test framework. To run the tests:

```sh
./gradlew test
```

## Documentation

API documentation is generated using Swagger. Once the application is running, you can access the Swagger UI at `http://localhost:8080/swagger`.

## Project Structure

```
travv-world/
📦src
 ┣ 📂main
 ┃ ┣ 📂kotlin
 ┃ ┃ ┗ 📂dev
 ┃ ┃ ┃ ┗ 📂arjundev
 ┃ ┃ ┃ ┃ ┣ 📂data
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dao
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂token
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenDao.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenDaoFacade.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜IUserDao.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserDao.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DatabaseFactory.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MyToken.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Profile.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProfileUpdateRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProfileUpdateResponse.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Result.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserLoginRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRegistrationRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂table
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenTable.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserTable.kt
 ┃ ┃ ┃ ┃ ┣ 📂plugins
 ┃ ┃ ┃ ┃ ┃ ┣ 📜HTTP.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜RequestValidation.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Routing.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Security.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Serialization.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Swagger.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Templating.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UsersSchema.kt
 ┃ ┃ ┃ ┃ ┣ 📂routes
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProfileRoutes.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRoutes.kt
 ┃ ┃ ┃ ┃ ┗ 📜Application.kt
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📜application.conf
 ┃ ┃ ┗ 📜logback.xml
 ┗ 📂test
 ┃ ┗ 📂kotlin
 ┃ ┃ ┗ 📂dev
 ┃ ┃ ┃ ┗ 📂arjundev
 ┃ ┃ ┃ ┃ ┣ 📂plugins
 ┃ ┃ ┃ ┃ ┣ 📂routes
 ┃ ┃ ┃ ┃ ┣ 📜ApplicationTest.kt
 ┃ ┃ ┃ ┃ ┗ 📜ProfileTest.kt
```

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.


Copyright (c) [2024] [Shreeman Arjun Sahu]

See the [LICENSE](LICENSE) file for details.

---

Thank you for checking out the Travv-World Project! If you have any questions or feedback, feel free to open an issue in the repository.