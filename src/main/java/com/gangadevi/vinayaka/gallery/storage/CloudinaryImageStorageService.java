package com.gangadevi.vinayaka.gallery.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.gangadevi.vinayaka.common.exception.ImageStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStorageService implements ImageStorageService {

    private final Cloudinary cloudinary;

    @Override
    public ImageUploadResult upload(MultipartFile file, String folder) {
        try {
            String publicId = UUID.randomUUID().toString();
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", folder,
                            "public_id", publicId,
                            "use_filename", false,
                            "unique_filename", false,
                            "overwrite", false
                    ));

            String secureUrl = String.valueOf(result.get("secure_url"));
            String uploadedPublicId = String.valueOf(result.get("public_id"));
            String thumbnailUrl = cloudinary.url()
                    .secure(true)
                    .transformation(new Transformation()
                            .width(800)
                            .height(800)
                            .crop("limit")
                            .quality("auto")
                            .fetchFormat("auto"))
                    .generate(uploadedPublicId);

            return new ImageUploadResult(
                    secureUrl,
                    uploadedPublicId,
                    thumbnailUrl,
                    result.get("format") == null ? null : String.valueOf(result.get("format")),
                    toInteger(result.get("width")),
                    toInteger(result.get("height"))
            );
        } catch (IOException | RuntimeException e) {
            throw new ImageStorageException("Unable to upload image to Cloudinary", e);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "type", "upload",
                            "invalidate", true
                    ));
            String status = result.get("result") == null ? null : String.valueOf(result.get("result"));
            if (!"ok".equals(status) && !"not found".equals(status)) {
                throw new ImageStorageException("Cloudinary did not delete image: " + publicId);
            }
        } catch (IOException | RuntimeException e) {
            if (e instanceof ImageStorageException imageStorageException) {
                throw imageStorageException;
            }
            throw new ImageStorageException("Unable to delete image from Cloudinary", e);
        }
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
