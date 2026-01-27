# Airline Tickets

An application that automatically monitors Ryanair flight prices for user-defined alerts and sends email notifications when round-trip prices drop below set limits.

## Table of Contents

- [Description](#description)
- [Tech Stack](#tech-stack)
- [Getting Started Locally](#getting-started-locally)
- [Available Scripts](#available-scripts)
- [Project Scope](#project-scope)
- [Project Status](#project-status)
- [License](#license)

## Description

Airline Tickets is a flight price monitoring application designed to help users find affordable flights without having to manually check prices daily. The system automatically checks Ryanair flight prices once per day and notifies users via email when prices meet their criteria.

### Key Features

- **User Account Management**: Registration, email verification, login, and password reset functionality
- **Alert Management**: Create, edit, and manage up to 5 active price alerts per user
- **Automated Price Monitoring**: Daily automatic checks of Ryanair direct flight prices
- **Email Notifications**: Receive emails with the top 3 cheapest options when prices drop below your limit
- **Price History**: Complete audit trail of historical flight prices
- **Flexible Date Selection**: Define date windows (up to 31 days) and stay duration ranges (up to 5 days spread)

### MVP Scope

- Monitors Ryanair direct flights only
- Single origin airport (system-configured)
- User selects destination by IATA code
- Price limits in PLN
- Daily job runs once per day in Europe/Warsaw timezone
- Email notifications without deduplication (daily alerts while conditions are met)

## Tech Stack

### Backend

- **Language**: Scala 3.7.4 on JVM 21
- **HTTP API**: Tapir + Netty with OpenAPI/Swagger UI
- **HTTP Client**: sttp client4 with OpenTelemetry integration
- **JSON**: jsoniter-scala
- **Database**: PostgreSQL 17.5 with JDBC + HikariCP connection pooling
- **Database Client**: Magnum (type-safe DB client)
- **Database Migrations**: Flyway
- **Concurrency**: Ox (structured concurrency)
- **Configuration**: pureconfig
- **Logging**: SLF4J + Logback
- **Observability**: OpenTelemetry (metrics, tracing, logs via OTLP)
- **Security**: password4j for password hashing, API key authorization (Bearer tokens)
- **Email**: SMTP or Mailgun integration
- **Dependency Injection**: MacWire (compile-time DI)
- **Testing**: ScalaTest with embedded PostgreSQL

### Frontend

- **Language**: TypeScript
- **Framework**: React 19
- **Routing**: react-router v7
- **Data Fetching**: @tanstack/react-query
- **API Client**: Auto-generated from OpenAPI using openapi-codegen
- **Forms**: react-hook-form + zod validation
- **UI Components**: Radix UI with shadcn-style components
- **Styling**: Tailwind CSS
- **Build Tool**: Vite with SWC plugin
- **Testing**: Vitest + Testing Library with jsdom
- **Code Quality**: ESLint + Prettier
- **Node Version**: >= 22

### Infrastructure

- **Container Orchestration**: Docker Compose (backend + PostgreSQL + Grafana LGTM stack)
- **Kubernetes**: Helm chart with Bitnami PostgreSQL dependency
- **Observability Stack**: Grafana LGTM (Grafana + Loki + Tempo + Prometheus)

## Getting Started Locally

### Prerequisites

- JVM 21 or higher
- [SBT](https://www.scala-sbt.org) (Scala Build Tool)
- [Yarn](https://yarnpkg.com) package manager
- Node.js 22 or higher
- Docker (for PostgreSQL)

### Run with Docker Compose

The fastest way to see the project in action is using Docker Compose, which starts the backend, PostgreSQL, and Grafana LGTM observability stack:

```bash
docker-compose up
```

### Run Locally for Development

#### 1. Database

Start a PostgreSQL database using Docker:

```bash
# Use "airline" as the password
docker run --name airline-postgres -p 5432:5432 -e POSTGRES_PASSWORD=airline -e POSTGRES_DB=airline -d postgres
```

#### 2. Backend

Start the backend server (requires JVM 21+ and SBT):

```bash
SQL_PASSWORD=airline ./backend-start.sh
```

The backend will start on [`http://localhost:8080`](http://localhost:8080).

**API Documentation**: Explore the API using Swagger UI at [`http://localhost:8080/api/v1/docs`](http://localhost:8080/api/v1/docs).

**Auto-reload**: The backend automatically restarts when source files change. When endpoint definitions are modified, the OpenAPI description is regenerated, which the frontend uses to generate service stubs.

**Note**: By default, OpenTelemetry is disabled in development mode to avoid telemetry export exceptions. Edit the startup script if you have a collector running.

#### 3. Frontend

First, install Yarn if you haven't already:

```bash
curl -o- -L https://yarnpkg.com/install.sh | bash
```

Create a `ui/.env` file using `ui/.env.example` as a template. The default backend URL (`http://localhost:8080`) should work unless you changed the backend port.

Start the frontend development server:

```bash
./frontend-start.sh
```

Open [`http://localhost:8081`](http://localhost:8081) in your browser.

**Auto-reload**: The frontend automatically reloads when source files change.

## Available Scripts

### Backend

- `./backend-start.sh` - Start the backend development server with auto-reload
- `./backend-start.bat` - Windows version of the backend start script

### Frontend

- `./frontend-start.sh` - Start the frontend development server with hot reload

### Docker

- `docker-compose up` - Start the complete application stack (backend, database, observability)
- `docker-compose down` - Stop and remove all containers

## Project Scope

### In Scope (MVP)

1. **Flight Monitoring**
   - Ryanair direct flights only
   - Single origin airport (system-configured, e.g., Wrocław - WRO)
   - User-selected destination by IATA code
   - Date window: up to 31 calendar days
   - Stay duration: configurable min/max days with max 5-day spread

2. **User Management**
   - User registration with email and password
   - Email verification (24-hour token validity)
   - Login with email/password
   - Password reset (1-hour token validity)
   - Account deletion

3. **Alert Management**
   - Create, read, update, delete alerts
   - Maximum 5 active alerts per user
   - Price limit in PLN (per passenger, base fare only)
   - Activate/deactivate alerts
   - Alert status tracking

4. **Price Monitoring**
   - Daily job execution (once per day in Europe/Warsaw timezone)
   - Round-trip (RT) price calculation: outbound + inbound minimum prices
   - Top 3 cheapest options in email notifications
   - Total count of qualifying options
   - No deduplication between days (daily alerts when conditions are met)

5. **Notifications**
   - Email alerts when prices drop below limit
   - One aggregated email per day per user (all qualifying alerts in one message)
   - Error notification emails when checks fail
   - No email sent when no qualifying options exist

6. **Price Audit**
   - Historical price data storage
   - Independent of user accounts
   - Preserved after account deletion

### Out of Scope (Post-MVP)

1. Flights with connections/layovers
2. Multiple origin airports selectable by users
3. Multi-airport cities (e.g., "London" with multiple airports)
4. Day-of-week filters
5. Price checks more than once per day
6. Additional fees (baggage, seat selection, priority boarding)
7. Multi-currency support
8. Advanced performance optimizations beyond daily deduplication

## Project Status

This project is currently in active development, implementing the MVP features as defined in the Product Requirements Document (PRD). The application architecture follows a modern full-stack approach with Scala 3 backend and React frontend, built on the Airline scaffolding template.

### Current Focus

- Core alert management functionality
- Ryanair API integration
- Daily price monitoring job
- Email notification system
- User authentication and authorization

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
