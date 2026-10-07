package com.gangadevi.vinayaka.gallery.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.gallery.dto.GalleryImageRequest;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageResponse;
import com.gangadevi.vinayaka.gallery.dto.GalleryImageUploadResponse;
import com.gangadevi.vinayaka.gallery.service.GalleryImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GalleryImageController {
    private final GalleryImageService service;

    @GetMapping("/api/v1/festivals/{year}/gallery")
    public List<GalleryImageResponse> getByFestivalYear(@PathVariable Integer year) {
        return service.findByFestivalYear(year);
    }

    @PostMapping("/api/v1/festivals/{year}/gallery")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "GALLERY_IMAGE", description = "Created gallery image metadata", entityIdFromResult = true)
    public GalleryImageResponse create(
            @PathVariable Integer year,
            @Valid @RequestBody GalleryImageRequest request) {
        return service.create(year, request);
    }

    @PostMapping(value = "/api/v1/festivals/{year}/gallery/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "GALLERY_IMAGE", description = "Uploaded gallery image", entityIdFromResult = true)
    public GalleryImageUploadResponse upload(
            @PathVariable Integer year,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) @Size(max = 500) String caption,
            @RequestParam(required = false) @Size(max = 500) String altText,
            @RequestParam(required = false) @Min(0) Integer sortOrder) {
        return service.upload(year, file, caption, altText, sortOrder);
    }

    @GetMapping("/api/v1/gallery/{id}")
    public GalleryImageResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/gallery/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "GALLERY_IMAGE", description = "Updated gallery image metadata", entityIdArgument = 0)
    public GalleryImageResponse update(
            @PathVariable Long id,
            @Valid @RequestBody GalleryImageRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/gallery/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "GALLERY_IMAGE", description = "Deleted gallery image", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
