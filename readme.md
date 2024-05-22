# Travv-World Project

Welcome to the Travv-World Project! This project is built using Kotlin and Ktor with H2 Database for storage. The goal of this project is to create a backend service that supports user registration, login, and profile management.

## Table of Contents

- [Setup](#setup)
- [Endpoints](#endpoints)
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
    git clone https://github.com/yourusername/travv-world.git
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

## Endpoints

### User Registration and Login

- **Register**: `POST /register`
    - Request Body: `{ "username": "string", "email": "string", "password": "string" }`
    - Response: `201 Created`

- **Login**: `POST /login`
    - Request Body: `{ "email": "string", "password": "string" }`
    - Response: `200 OK` with a JWT token

### Profile Management

- **Get Profile**: `GET /profile/{id}`
    - Response: `200 OK` with user profile data

- **Update Profile**: `PUT /profile/{id}`
    - Request Body: `{ "username": "string", "email": "string" }`
    - Response: `200 OK`

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
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DatabaseFactory.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Result.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserLoginRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRegistrationReuqest.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂table
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
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRoutes.kt
 ┃ ┃ ┃ ┃ ┗ 📜Application.kt
 ┃ ┗ 📂resources
 ┃ ┃ ┗ 📜logback.xml
 ┗ 📂test
 ┃ ┗ 📂kotlin
 ┃ ┃ ┗ 📂dev
 ┃ ┃ ┃ ┗ 📂arjundev
 ┃ ┃ ┃ ┃ ┗ 📜ApplicationTest.kt
```

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.


Copyright (c) [2024] [Shreeman Arjun Sahu]

See the [LICENSE](LICENSE) file for details.

---

Thank you for checking out the Travv-World Project! If you have any questions or feedback, feel free to open an issue in the repository.