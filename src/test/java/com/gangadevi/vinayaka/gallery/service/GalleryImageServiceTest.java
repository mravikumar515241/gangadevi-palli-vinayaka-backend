package com.gangadevi.vinayaka.gallery.service;

import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageRequest;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageResponse;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageUploadResponse;
import com.gangadevi.vinayaka.gallery.entity.GalleryImage;
import com.gangadevi.vinayaka.gallery.repository.GalleryImageRepository;
import com.gangadevi.vinayaka.gallery.storage.ImageStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GalleryImageServiceTest {
    @Mock
    private GalleryImageRepository galleryImageRepository;

    @Mock
    private FestivalYearRepository festivalYearRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private GalleryImageService service;

    @Test
    void create_defaultsSortOrderToZeroWhenNotProvided() {
        FestivalYear festivalYear = festivalYear();
        when(festivalYearRepository.findByYear(2026)).thenReturn(Optional.of(festivalYear));
        when(galleryImageRepository.save(any(GalleryImage.class))).thenAnswer(invocation -> {
            GalleryImage image = invocation.getArgument(0);
            image.setId(10L);
            return image;
        });

        GalleryImageResponse response = service.create(2026,
                new GalleryImageRequest("https://example.com/image.jpg", null, "Ganesh Idol", "Ganesh idol", null));

        assertEquals(10L, response.id());
        assertEquals(2026, response.festivalYear());
        assertEquals(0, response.sortOrder());
    }

    @Test
    void uploadStoresCloudinaryMetadataAndReturnsResponse() {
        FestivalYear festivalYear = festivalYear();
        MockMultipartFile file = new MockMultipartFile(
                "file", "ganesha.jpg", "image/jpeg", "image-bytes".getBytes());

        when(festivalYearRepository.findByYear(2026)).thenReturn(Optional.of(festivalYear));
        when(imageStorageService.upload(eq(file), eq("gangadevi-palli/vinayaka/gallery/2026")))
                .thenReturn(new ImageStorageService.ImageUploadResult(
                        "https://res.cloudinary.com/demo/image/upload/sample.jpg",
                        "gangadevi-palli/vinayaka/gallery/2026/sample",
                        "https://res.cloudinary.com/demo/image/upload/w_800/sample.jpg",
                        "jpg",
                        1200,
                        900));
        when(galleryImageRepository.save(any(GalleryImage.class))).thenAnswer(invocation -> {
            GalleryImage image = invocation.getArgument(0);
            image.setId(20L);
            return image;
        });

        GalleryImageUploadResponse response = service.upload(
                2026, file, "Vinayaka Chavithi", "Lord Ganesha idol", 1);

        assertEquals(20L, response.id());
        assertEquals(2026, response.festivalYear());
        assertEquals("https://res.cloudinary.com/demo/image/upload/sample.jpg", response.imageUrl());
        assertEquals("gangadevi-palli/vinayaka/gallery/2026/sample", response.cloudinaryPublicId());
        assertEquals(1, response.sortOrder());
    }

    @Test
    void uploadRejectsUnsupportedContentTypeBeforeStorageCall() {
        FestivalYear festivalYear = festivalYear();
        MockMultipartFile file = new MockMultipartFile(
                "file", "document.pdf", "application/pdf", "pdf".getBytes());
        when(festivalYearRepository.findByYear(2026)).thenReturn(Optional.of(festivalYear));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.upload(2026, file, null, null, null));

        assertEquals("Unsupported image type. Allowed types: JPEG, PNG and WEBP", exception.getMessage());
        verify(imageStorageService, never()).upload(any(), any());
    }

    @Test
    void uploadCleansCloudinaryAssetWhenDatabaseSaveFails() {
        FestivalYear festivalYear = festivalYear();
        MockMultipartFile file = new MockMultipartFile(
                "file", "ganesha.png", "image/png", "image-bytes".getBytes());
        ImageStorageService.ImageUploadResult uploadResult = new ImageStorageService.ImageUploadResult(
                "https://res.cloudinary.com/demo/image/upload/sample.png",
                "gangadevi-palli/vinayaka/gallery/2026/sample",
                "https://res.cloudinary.com/demo/image/upload/w_800/sample.png",
                "png",
                1000,
                800);

        when(festivalYearRepository.findByYear(2026)).thenReturn(Optional.of(festivalYear));
        when(imageStorageService.upload(any(), any())).thenReturn(uploadResult);
        when(galleryImageRepository.save(any(GalleryImage.class)))
                .thenThrow(new RuntimeException("database failure"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.upload(2026, file, null, null, null));

        assertEquals("database failure", exception.getMessage());
        verify(imageStorageService).delete(uploadResult.publicId());
    }

    @Test
    void deleteRemovesCloudinaryAssetForCloudinaryBackedImage() {
        GalleryImage image = GalleryImage.builder()
                .id(30L)
                .festivalYear(festivalYear())
                .imageUrl("https://res.cloudinary.com/demo/image/upload/sample.jpg")
                .cloudinaryPublicId("gangadevi-palli/vinayaka/gallery/2026/sample")
                .sortOrder(1)
                .build();

        when(galleryImageRepository.findById(30L)).thenReturn(Optional.of(image));

        service.delete(30L);

        verify(imageStorageService).delete(image.getCloudinaryPublicId());
        verify(galleryImageRepository).delete(image);
    }

    private FestivalYear festivalYear() {
        return FestivalYear.builder()
                .id(1L)
                .year(2026)
                .title("Vinayaka Chavithi 2026")
                .status(FestivalYear.FestivalStatus.ACTIVE)
                .build();
    }
}
