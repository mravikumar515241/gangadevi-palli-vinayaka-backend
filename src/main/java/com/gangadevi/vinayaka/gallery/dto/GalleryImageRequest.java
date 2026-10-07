package com.gangadevi.vinayaka.gallery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GalleryImageRequest(
        @NotBlank @Size(max = 1000) String imageUrl,
        @Size(max = 1000) String thumbnailUrl,
        @Size(max = 500) String caption,
        @Size(max = 500) String altText,
        @Min(0) Integer sortOrder
) {
}
