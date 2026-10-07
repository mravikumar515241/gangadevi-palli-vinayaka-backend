package com.gangadevi.vinayaka.gallery.service;

import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageRequest;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageResponse;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageUploadResponse;
import com.gangadevi.vinayaka.gallery.entity.GalleryImage;
import com.gangadevi.vinayaka.gallery.repository.GalleryImageRepository;
import com.gangadevi.vinayaka.gallery.storage.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryImageService {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );

    private final GalleryImageRepository galleryImageRepository;
    private final FestivalYearRepository festivalYearRepository;
    private final ImageStorageService imageStorageService;

    @Value("${gallery.upload.max-file-size-bytes:10485760}")
    private long maxFileSizeBytes = 10 * 1024 * 1024;

    @Transactional(readOnly = true)
    public List<GalleryImageResponse> findByFestivalYear(Integer year) {
        getFestivalYear(year);
        return galleryImageRepository.findByFestivalYearYearOrderBySortOrderAscIdAsc(year)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GalleryImageResponse findById(Long id) {
        return toResponse(getGalleryImage(id));
    }

    public GalleryImageResponse create(Integer year, GalleryImageRequest request) {
        FestivalYear festivalYear = getFestivalYear(year);
        GalleryImage image = GalleryImage.builder()
                .festivalYear(festivalYear)
                .imageUrl(normalizeRequired(request.imageUrl()))
                .thumbnailUrl(normalize(request.thumbnailUrl()))
                .caption(normalize(request.caption()))
                .altText(normalize(request.altText()))
                .sortOrder(request.sortOrder() == null ? 0 : request.sortOrder())
                .build();
        return toResponse(galleryImageRepository.save(image));
    }

    public GalleryImageUploadResponse upload(
            Integer year,
            MultipartFile file,
            String caption,
            String altText,
            Integer sortOrder) {
        FestivalYear festivalYear = getFestivalYear(year);
        validateFile(file);
        validateMetadata(caption, altText, sortOrder);

        String folder = "gangadevi-palli/vinayaka/gallery/" + year;
        ImageStorageService.ImageUploadResult uploadResult = imageStorageService.upload(file, folder);
        GalleryImage image = GalleryImage.builder()
                .festivalYear(festivalYear)
                .imageUrl(uploadResult.secureUrl())
                .thumbnailUrl(uploadResult.thumbnailUrl())
                .cloudinaryPublicId(uploadResult.publicId())
                .caption(normalize(caption))
                .altText(normalize(altText))
                .sortOrder(sortOrder == null ? 0 : sortOrder)
                .build();

        try {
            GalleryImage saved = galleryImageRepository.save(image);
            return toUploadResponse(saved);
        } catch (RuntimeException e) {
            try {
                imageStorageService.delete(uploadResult.publicId());
            } catch (RuntimeException cleanupException) {
                e.addSuppressed(cleanupException);
            }
            throw e;
        }
    }

    public GalleryImageResponse update(Long id, GalleryImageRequest request) {
        GalleryImage image = getGalleryImage(id);
        image.setImageUrl(normalizeRequired(request.imageUrl()));
        image.setThumbnailUrl(normalize(request.thumbnailUrl()));
        image.setCaption(normalize(request.caption()));
        image.setAltText(normalize(request.altText()));
        image.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        return toResponse(galleryImageRepository.save(image));
    }

    public void delete(Long id) {
        GalleryImage image = getGalleryImage(id);
        if (image.getCloudinaryPublicId() != null && !image.getCloudinaryPublicId().isBlank()) {
            imageStorageService.delete(image.getCloudinaryPublicId());
        }
        galleryImageRepository.delete(image);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }
        if (file.getSize() > maxFileSizeBytes) {
            throw new IllegalArgumentException("Image file exceeds the maximum allowed size of "
                    + (maxFileSizeBytes / (1024 * 1024)) + " MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported image type. Allowed types: JPEG, PNG and WEBP");
        }
    }

    private void validateMetadata(String caption, String altText, Integer sortOrder) {
        if (caption != null && caption.length() > 500) {
            throw new IllegalArgumentException("Caption must not exceed 500 characters");
        }
        if (altText != null && altText.length() > 500) {
            throw new IllegalArgumentException("Alt text must not exceed 500 characters");
        }
        if (sortOrder != null && sortOrder < 0) {
            throw new IllegalArgumentException("Sort order must be greater than or equal to 0");
        }
    }

    private GalleryImage getGalleryImage(Long id) {
        return galleryImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery image not found: " + id));
    }

    private FestivalYear getFestivalYear(Integer year) {
        return festivalYearRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Festival year not found: " + year));
    }

    private GalleryImageResponse toResponse(GalleryImage image) {
        return new GalleryImageResponse(
                image.getId(),
                image.getFestivalYear().getYear(),
                image.getImageUrl(),
                image.getThumbnailUrl(),
                image.getCaption(),
                image.getAltText(),
                image.getSortOrder(),
                image.getCreatedAt(),
                image.getUpdatedAt());
    }

    private GalleryImageUploadResponse toUploadResponse(GalleryImage image) {
        return new GalleryImageUploadResponse(
                image.getId(),
                image.getFestivalYear().getYear(),
                image.getImageUrl(),
                image.getThumbnailUrl(),
                image.getCaption(),
                image.getAltText(),
                image.getSortOrder(),
                image.getCloudinaryPublicId(),
                image.getCreatedAt(),
                image.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private String normalizeRequired(String value) {
        return value.trim();
    }
}
