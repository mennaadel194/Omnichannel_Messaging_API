
# Simplified Omnichannel Messaging API

This is a **RESTful API** for a **Simple Messaging Platform** where users can register, login, and send messages via different channels (email or SMS). The platform includes basic security measures, rate limiting, and message management functionalities.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
  - [User Authentication](#user-authentication)
  - [Message Management](#message-management)
- [Unit Tests](#unit-tests)
- [Rate Limiting](#rate-limiting)
- [Contributing](#contributing)

---

## Project Overview

This project implements a **RESTful API** built with **Spring Boot** for a **Messaging Platform** that supports:
- **User Authentication** using JWT (JSON Web Tokens).
- **Message Sending** via email or SMS.
- **Rate Limiting** to prevent spam (3 messages per user per minute).
- **Message Retrieval** with filtering by channel.
- Basic **security** through Spring Security and **input validation**.

---

## Features

- **User Registration**: Secure registration with hashed passwords.
- **User Login**: JWT-based login system for secure authentication.
- **Send Messages**: Users can send messages via email or SMS, with automatic message status set to **'Sent'**.
- **Message Retrieval**: Users can view a list of their sent messages, filtered by channel.
- **Rate Limiting**: Users are limited to 3 messages per minute to prevent spam.

---

## Technologies Used

- **Java 11+**
- **Spring Boot** (for building RESTful API)
- **Spring Security** (for JWT authentication)
- **Spring Data JPA** (for database interaction)
- **H2 Database** (in-memory database for testing)
- **JUnit** (for unit testing)
- **BCrypt** (for password hashing)

---

## Setup Instructions

### Prerequisites

- JDK 11 or higher
- Maven (for project dependencies)
- IDE (e.g., IntelliJ IDEA or Eclipse)

### Steps to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/mennaadel194/omnichannel-messaging-api.git
   cd omnichannel-messaging-api
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   The application should now be running locally on port `8080`.

4. **Test the API**:
   You can test the API using tools like [Postman](https://www.postman.com/) or `curl`.

---

## API Endpoints

### User Authentication

#### Register a User
- **POST** `/api/register`
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }
  ```

#### Login
- **POST** `/api/login`
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt-token-here"
  }
  ```

---

### Message Management

#### Send a Message
- **POST** `/api/messages`
- **Headers**:
  - `Authorization: Bearer <jwt-token>`
- **Request Body**:
  ```json
  {
    "channel": "email",
    "content": "Hello, this is a test message!"
  }
  ```

- **Response**:
  ```json
  {
    "message": "Message Sent",
    "status": "Sent"
  }
  ```

#### Get Sent Messages
- **GET** `/api/messages?channel=email`
- **Headers**:
  - `Authorization: Bearer <jwt-token>`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "channel": "email",
      "content": "Hello, this is a test message!",
      "status": "Sent",
      "timestamp": "2024-12-17T12:00:00"
    }
  ]
  ```

---

## Unit Tests

The project includes unit tests for the core functionalities, including:

- User registration and login.
- Message creation with rate limiting.
- Message retrieval with proper authorization.

To run the tests:

```bash
mvn test
```

---

## Rate Limiting

The API restricts each user to **3 messages per minute**. If a user attempts to send more than 3 messages in the same minute, they will receive a `429 Too Many Requests` response.

The rate limiting is implemented using an in-memory store that tracks the timestamps of each user's sent messages. If the user exceeds the limit, the request will be rejected.

---

## Contributing

1. **Fork** this repository.
2. **Clone** your forked repository.
3. Create a new **branch** for your feature or bug fix.
4. **Commit** your changes.
5. **Push** your changes to your fork.
6. Submit a **Pull Request**.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to modify this README as necessary, especially the links and specific instructions depending on how you host the repository.
