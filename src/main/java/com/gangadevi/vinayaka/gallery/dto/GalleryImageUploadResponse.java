package com.gangadevi.vinayaka.gallery.dto;

import java.time.OffsetDateTime;

public record GalleryImageUploadResponse(
        Long id,
        Integer festivalYear,
        String imageUrl,
        String thumbnailUrl,
        String caption,
        String altText,
        Integer sortOrder,
        String cloudinaryPublicId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
