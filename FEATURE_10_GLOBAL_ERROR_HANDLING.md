# Feature 10 — Global Error Handling + Validation

Feature 10 standardizes API errors across Features 1–9 without changing their public business APIs.

## Standard error response

Errors now use:

```json
{
  "timestamp": "2026-09-22T18:30:15+05:30",
  "status": 400,
  "error": "Bad Request",
  "code": "VALIDATION_FAILED",
  "message": "One or more fields are invalid",
  "path": "/api/v1/festivals/2026/donations",
  "requestId": "...",
  "fieldErrors": {
    "plannedAmount": "must be greater than or equal to 0"
  }
}
```

## Main mappings

- `400` — malformed/invalid request or business rule violation
- `401` — authentication required
- `403` — authenticated user lacks permission
- `404` — resource not found
- `409` — duplicate/conflicting data
- `413` — upload size is normally handled by multipart configuration; application-level file validation remains `400`
- `415` — unsupported media type
- `502` — Cloudinary/storage service failure
- `500` — unexpected server error

## Request IDs

Every request receives or preserves `X-Request-ID`. The value is returned in error responses and placed in the logging MDC so production logs can correlate failures.

## Security

The existing JWT authentication and ADMIN authorization rules remain unchanged. Security failures now use the same error-response shape.

## Validation

Existing DTO validation remains active. The global handler now converts validation, type conversion, malformed JSON, missing multipart parameters, oversized uploads, and database constraint failures into stable API responses.

## Security note

Do not commit Cloudinary API secrets, JWT secrets, passwords, or authorization tokens. Continue using environment variables for secrets.
