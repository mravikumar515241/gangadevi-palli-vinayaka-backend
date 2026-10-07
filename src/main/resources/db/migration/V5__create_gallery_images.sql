CREATE TABLE gallery_image (
    id BIGSERIAL PRIMARY KEY,
    festival_year_id BIGINT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    thumbnail_url VARCHAR(1000),
    caption VARCHAR(500),
    alt_text VARCHAR(500),
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_gallery_image_festival_year
        FOREIGN KEY (festival_year_id) REFERENCES festival_year(id) ON DELETE CASCADE,
    CONSTRAINT ck_gallery_image_sort_order_non_negative
        CHECK (sort_order >= 0)
);

CREATE INDEX idx_gallery_image_festival_year
    ON gallery_image (festival_year_id);

CREATE INDEX idx_gallery_image_sort_order
    ON gallery_image (festival_year_id, sort_order, id);
