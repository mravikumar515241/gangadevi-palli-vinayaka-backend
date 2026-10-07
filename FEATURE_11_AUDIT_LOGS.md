# Feature 11 — Audit Logs

Feature 11 adds centralized admin audit logging without changing the existing business APIs.

## What is audited

Successful mutation operations are recorded for:

- Festival years
- Donors
- Donations
- Activities
- Activity items
- Games
- Game winners
- Gallery images, including Cloudinary uploads
- Laddu auctions
- Laddu payments
- Successful admin login

Actions are `CREATE`, `UPDATE`, `DELETE`, and `LOGIN`.

## Database

Flyway migration `V9__create_audit_logs.sql` creates `audit_log` and indexes for timestamp, username, entity and action.

## API

`GET /api/v1/audit-logs`

Admin only. Optional filters:

- `username`
- `action`: CREATE, UPDATE, DELETE, LOGIN
- `entityType`
- `page` (default 0)
- `size` (default 20, maximum 100)

Example:

`GET /api/v1/audit-logs?action=DELETE&entityType=GALLERY_IMAGE&page=0&size=20`

## Stored fields

- username
- action
- entityType
- entityId
- description
- requestId
- ipAddress
- createdAt

The API never stores passwords, JWTs, Cloudinary secrets, or request Authorization headers.

## Security

All audit-log reads require the existing `ADMIN` role. Existing public GET endpoints remain public; audit logs are explicitly excluded from the public GET rule.

## Behavior

Audit persistence runs in a separate transaction. An audit persistence failure is logged and does not turn an otherwise successful business operation into a failed API response.
