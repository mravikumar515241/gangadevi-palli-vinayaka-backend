# Feature 9 — Cloudinary Gallery Upload

## Environment variables

Set these before starting the backend:

```text
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-cloudinary-api-secret
```

Never commit the real API secret.

Optional upload limits:

```text
GALLERY_MAX_FILE_SIZE_BYTES=10485760
GALLERY_MAX_FILE_SIZE=10MB
GALLERY_MAX_REQUEST_SIZE=12MB
```

## Upload endpoint

```text
POST /api/v1/festivals/{year}/gallery/upload
```

Authentication: `ADMIN` JWT required.

Content type: `multipart/form-data`

Fields:

- `file` — required image file
- `caption` — optional, max 500 characters
- `altText` — optional, max 500 characters
- `sortOrder` — optional, minimum 0

Allowed formats:

- JPEG
- PNG
- WEBP

Default maximum image size: 10 MB.

## Storage layout

Cloudinary folder:

```text
gangadevi-palli/vinayaka/gallery/{year}
```

PostgreSQL stores:

- Cloudinary secure URL
- Cloudinary public ID
- gallery metadata

The image binary is stored in Cloudinary, not PostgreSQL.

## Delete behavior

For Cloudinary-backed records, `DELETE /api/v1/gallery/{id}` removes the Cloudinary asset and then the database metadata. Older URL-only gallery records continue to work and are deleted from PostgreSQL only.
