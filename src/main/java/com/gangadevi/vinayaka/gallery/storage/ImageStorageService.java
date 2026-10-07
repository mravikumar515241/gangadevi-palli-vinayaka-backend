package com.gangadevi.vinayaka.gallery.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    ImageUploadResult upload(MultipartFile file, String folder);

    void delete(String publicId);

    record ImageUploadResult(
            String secureUrl,
            String publicId,
            String thumbnailUrl,
            String format,
            Integer width,
            Integer height
    ) {
    }
}
