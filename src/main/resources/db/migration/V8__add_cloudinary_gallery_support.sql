ALTER TABLE gallery_image
    ADD COLUMN cloudinary_public_id VARCHAR(500);

ALTER TABLE gallery_image
    ADD CONSTRAINT uk_gallery_image_cloudinary_public_id
        UNIQUE (cloudinary_public_id);
