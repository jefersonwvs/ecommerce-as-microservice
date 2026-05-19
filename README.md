# Ecommerce Platform

This repository contains a Java-based microservice ecommerce platform with three Spring Boot services and a shared RabbitMQ broker.

## Services

- `ms-order` - Order service
- `ms-payment` - Payment service
- `ms-notification` - Notification service

## Architecture

- Each service is a Spring Boot application.
- RabbitMQ is used for messaging between services.
- `ms-order` and `ms-payment` use PostgreSQL as their relational datastore.
- `ms-notification` uses SMTP settings for sending email notifications.

## Ports

- RabbitMQ management: `http://localhost:15672`
- RabbitMQ AMQP: `5672`
- `ms-order`: `8080`
- `ms-payment`: `8081`
- `ms-notification`: `8082`

## Prerequisites

- Java 25
- Gradle (optional, each service includes Gradle wrapper)
- PostgreSQL databases:
  - `order_db` on port `5432`
  - `payment_db` on port `5433`
- Docker and Docker Compose (recommended for RabbitMQ)
- Optional SMTP testing tool such as MailHog on port `1025`

## RabbitMQ

The workspace root includes `docker-compose.yml` for RabbitMQ only.

Start RabbitMQ:

```bash
docker compose up -d
```

Then open the management console:

```text
http://localhost:15672
```

Credentials:

- user: `admin`
- pass: `admin`

## Running Microservices

Each microservice has its own Gradle wrapper and configuration.

### ms-order

```bash
cd ms-order
./gradlew bootRun
```

### ms-payment

```bash
cd ms-payment
./gradlew bootRun
```

### ms-notification

```bash
cd ms-notification
./gradlew bootRun
```

## Build

Build an individual service from its folder:

```bash
cd ms-order
./gradlew build
```

Or for `ms-payment` / `ms-notification`:

```bash
cd ms-payment
./gradlew build
```

```bash
cd ms-notification
./gradlew build
```

## Configuration

Each service has a `src/main/resources/application.yaml` file.

### ms-order

- `server.port: 8080`
- PostgreSQL: `jdbc:postgresql://localhost:5432/order_db`
- RabbitMQ: `localhost:5672`

### ms-payment

- `server.port: 8081`
- PostgreSQL: `jdbc:postgresql://localhost:5433/payment_db`
- RabbitMQ: `localhost:5672`

### ms-notification

- `server.port: 8082`
- SMTP host: `localhost:1025`
- RabbitMQ: `localhost:5672`

## Suggested local setup

1. Start RabbitMQ:
   ```bash
   docker compose up -d
   ```
2. Start PostgreSQL instances or create the required databases.
3. Start `ms-order`, `ms-payment`, and `ms-notification` in separate terminals.

## Troubleshooting

- If a service cannot connect to RabbitMQ, verify `localhost:5672` and credentials.
- If a service cannot connect to PostgreSQL, verify the database URL, username, and password.
- If email delivery does not work for `ms-notification`, verify the SMTP server on port `1025`.
