package com.gangadevi.vinayaka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Fails fast for unsafe or missing production configuration.
 * Local development keeps the existing convenient defaults.
 */
@Component
@Profile("prod")
public class ProductionConfigurationValidator {

    public ProductionConfigurationValidator(
            @Value("${app.security.jwt-secret}") String jwtSecret,
            @Value("${app.security.admin-username}") String adminUsername,
            @Value("${app.security.admin-password}") String adminPassword,
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String cloudApiKey,
            @Value("${cloudinary.api-secret}") String cloudApiSecret,
            @Value("${app.cors.allowed-origins}") String corsOrigins) {

        require("JWT_SECRET", jwtSecret);
        if (jwtSecret.length() < 32 || jwtSecret.equalsIgnoreCase("change-this-local-secret-to-a-random-value-at-least-32-characters")) {
            throw new IllegalStateException("Production JWT_SECRET must be a strong secret of at least 32 characters");
        }

        require("ADMIN_USERNAME", adminUsername);
        require("ADMIN_PASSWORD", adminPassword);
        if (adminPassword.length() < 12 || adminPassword.equals("Admin@12345")) {
            throw new IllegalStateException("Production ADMIN_PASSWORD must be changed and contain at least 12 characters");
        }

        require("CLOUDINARY_CLOUD_NAME", cloudName);
        require("CLOUDINARY_API_KEY", cloudApiKey);
        require("CLOUDINARY_API_SECRET", cloudApiSecret);
        require("CORS_ALLOWED_ORIGINS", corsOrigins);

        if (Arrays.stream(corsOrigins.split(","))
                .map(String::trim)
                .anyMatch(origin -> origin.equals("*") || origin.isBlank())) {
            throw new IllegalStateException("Production CORS_ALLOWED_ORIGINS must contain explicit frontend origins and cannot be '*'");
        }
    }

    private static void require(String key, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required production configuration: " + key);
        }
    }
}
