# Gangadevi Palli Vinayaka Chavithi Backend

Java 21 + Spring Boot backend for the Gangadevi Palli Vinayaka Chavithi website.

## Current modules
Festival Years, Donations, Activities, Games, Gallery, Laddu Auction, Dashboard, Authentication/Authorization, Cloudinary Gallery Upload, and Global Error Handling/Validation.

### Festival Year endpoints
- GET /api/v1/festivals
- GET /api/v1/festivals/{year}
- POST /api/v1/festivals
- PUT /api/v1/festivals/{year}

### Donor endpoints
- GET /api/v1/donors
- GET /api/v1/donors/{id}
- POST /api/v1/donors
- PUT /api/v1/donors/{id}
- DELETE /api/v1/donors/{id}

### Donation endpoints
- GET /api/v1/festivals/{year}/donations
- POST /api/v1/festivals/{year}/donations
- GET /api/v1/donations/{id}
- PUT /api/v1/donations/{id}
- DELETE /api/v1/donations/{id}

Pending donation is calculated by the backend as `plannedAmount - receivedAmount`.
Received amount cannot exceed planned amount.

Swagger: http://localhost:8080/swagger-ui.html

## Run
Create a PostgreSQL database named `vinayaka`, then:

```powershell
mvn spring-boot:run
```

Or configure environment variables:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/vinayaka"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-password"
mvn spring-boot:run
```

## Example POST

```json
{
  "year": 2026,
  "status": "ACTIVE",
  "title": "Gangadevi Palli Vinayaka Chavithi 2026",
  "festivalDate": "2026-08-28",
  "description": "Sri Maha Ganapathi Seva Samithi festival."
}
```

## Roadmap
1. ~~Donations~~
2. Activities + itemized expenses
3. Games + winners
4. Gallery
5. Dashboard
6. Admin authentication

## Module 3 - Activities & Itemized Expenses

This version adds activity budgeting and itemized expense tracking.

### Activity APIs
- `GET /api/v1/festivals/{year}/activities`
- `POST /api/v1/festivals/{year}/activities`
- `GET /api/v1/activities/{id}`
- `PUT /api/v1/activities/{id}`
- `DELETE /api/v1/activities/{id}`

### Activity Item APIs
- `GET /api/v1/activities/{activityId}/items`
- `POST /api/v1/activities/{activityId}/items`
- `GET /api/v1/activity-items/{id}`
- `PUT /api/v1/activity-items/{id}`
- `DELETE /api/v1/activity-items/{id}`

### Expense calculation
`item total = quantity * unit cost`

`activity actual spent = sum of all item totals`

`remaining budget = planned budget - actual spent`

Remaining budget can become negative when an activity exceeds its planned budget; this intentionally exposes overspending instead of hiding it.

### Flyway
Migration: `V3__create_activities_and_activity_items.sql`

## Module 4 — Games & Winners

Added Games & Winners support with Flyway migration `V4__create_games_and_game_winners.sql`.

### Game APIs
- `GET /api/v1/festivals/{year}/games`
- `POST /api/v1/festivals/{year}/games`
- `GET /api/v1/games/{id}`
- `PUT /api/v1/games/{id}`
- `DELETE /api/v1/games/{id}`

### Winner APIs
- `GET /api/v1/games/{gameId}/winners`
- `POST /api/v1/games/{gameId}/winners`
- `GET /api/v1/game-winners/{id}`
- `PUT /api/v1/game-winners/{id}`
- `DELETE /api/v1/game-winners/{id}`

Game categories: `SPORTS`, `TRADITIONAL`, `INDOOR`, `CHILDREN`, `CULTURAL`, `OTHER`.

Game statuses: `PLANNED`, `ONGOING`, `COMPLETED`, `CANCELLED`.

Winner positions: `FIRST`, `SECOND`, `THIRD`, `SPECIAL`. A game can have only one FIRST, SECOND, and THIRD winner; multiple SPECIAL winners are allowed.

## Module 5 — Gallery

Gallery metadata is stored in PostgreSQL while uploaded image files are stored in Cloudinary. The module supports both the existing URL-based metadata API and real multipart image uploads.

Endpoints:
- GET `/api/v1/festivals/{year}/gallery`
- POST `/api/v1/festivals/{year}/gallery`
- GET `/api/v1/gallery/{id}`
- PUT `/api/v1/gallery/{id}`
- DELETE `/api/v1/gallery/{id}`

Flyway migration: `V5__create_gallery_images.sql`

## Module 6 — Laddu Auction

Added Laddu Auction / Laddu Velam Paata support with payment tracking and interest slabs.

## Module 7 — Dashboard

Added year-wise dashboard statistics combining donations, activities, games, gallery and laddu auction data.

## Module 8 — Authentication & Authorization

Added JWT authentication, BCrypt password hashing, ADMIN role authorization and public GET/admin mutation separation.

Default local admin credentials:
- Username: `admin`
- Password: `Admin@12345`

Configure `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `JWT_SECRET` and `JWT_EXPIRATION_SECONDS` in production.

## Module 9 — Real Gallery Image Upload & Cloudinary

Gallery now supports real image uploads to Cloudinary while PostgreSQL stores only image metadata and the Cloudinary public ID.

### Cloudinary configuration

Set these environment variables before using the upload endpoint:

```powershell
$env:CLOUDINARY_CLOUD_NAME="your-cloud-name"
$env:CLOUDINARY_API_KEY="your-api-key"
$env:CLOUDINARY_API_SECRET="your-cloudinary-api-secret"
```

Optional upload limits:

```powershell
$env:GALLERY_MAX_FILE_SIZE_BYTES="10485760"
$env:GALLERY_MAX_FILE_SIZE="10MB"
$env:GALLERY_MAX_REQUEST_SIZE="12MB"
```

### Gallery APIs

Public:
- `GET /api/v1/festivals/{year}/gallery`
- `GET /api/v1/gallery/{id}`

ADMIN:
- `POST /api/v1/festivals/{year}/gallery/upload` — multipart image upload
- `POST /api/v1/festivals/{year}/gallery` — existing URL-based metadata API
- `PUT /api/v1/gallery/{id}`
- `DELETE /api/v1/gallery/{id}`

Allowed upload types: JPEG, PNG and WEBP.
Maximum default image size: 10 MB.

Cloudinary folder structure:
`gangadevi-palli/vinayaka/gallery/{year}`

Flyway migration: `V8__add_cloudinary_gallery_support.sql`


## Module 10 — Global Error Handling + Validation

Feature 10 standardizes API error responses, validation failures, business conflicts, resource-not-found responses, security errors, Cloudinary/storage failures, malformed requests, and database constraint conflicts.

### Standard error fields
- `timestamp`
- `status`
- `error`
- `code`
- `message`
- `path`
- `requestId`
- `fieldErrors`

Every request receives or preserves an `X-Request-ID` response header. The request ID is also added to the logging MDC.

Flyway migrations are unchanged by Feature 10; no database migration is required.

## Feature 12 — Production Configuration & Hardening

The backend supports separate `local` and `prod` Spring profiles while preserving the existing F1–F11 APIs and Flyway migrations.

### Local

The default profile is `local`, so the existing developer workflow remains convenient.

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
mvn spring-boot:run
```

Swagger remains available locally at:

`http://localhost:8080/swagger-ui.html`

Health check:

`http://localhost:8080/actuator/health`

### Production

Set `SPRING_PROFILES_ACTIVE=prod` and provide all required environment variables. Production does not use the local JWT secret or default admin password.

Required production configuration:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`
- `CORS_ALLOWED_ORIGINS`
- `CLOUDINARY_CLOUD_NAME`
- `CLOUDINARY_API_KEY`
- `CLOUDINARY_API_SECRET`

Optional:

- `SERVER_PORT`
- `SWAGGER_ENABLED`
- Hikari pool variables
- Gallery upload limits

`CORS_ALLOWED_ORIGINS` must contain explicit frontend origins and must not be `*` in production.

### Actuator

Only the health endpoint is exposed:

`GET /actuator/health`

Sensitive actuator endpoints are not exposed over the web.

### Docker

Build and run with environment variables supplied externally. Do not put real secrets into `docker-compose.yml`, `.env.example`, or Git.

```powershell
docker compose up --build
```

For production, replace all placeholder/local values with deployment secrets.

### Security

- JWT secret is environment-configured.
- Production configuration fails fast when required secrets are missing or unsafe.
- CORS is allowlisted.
- Authorization headers are not logged.
- Error responses do not expose stack traces or internal exception details.
- Security headers are enabled without disabling Swagger locally.
- Existing public GET APIs and protected admin mutations are preserved.
