package com.gangadevi.vinayaka.gallery.dto;

import java.time.OffsetDateTime;

public record GalleryImageResponse(
        Long id,
        Integer festivalYear,
        String imageUrl,
        String thumbnailUrl,
        String caption,
        String altText,
        Integer sortOrder,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
