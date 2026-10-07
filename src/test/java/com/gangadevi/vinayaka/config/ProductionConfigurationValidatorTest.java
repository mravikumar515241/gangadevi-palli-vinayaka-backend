package com.gangadevi.vinayaka.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductionConfigurationValidatorTest {

    @Test
    void acceptsStrongProductionConfiguration() {
        assertDoesNotThrow(() -> new ProductionConfigurationValidator(
                "a-strong-production-jwt-secret-that-is-long-enough-123456",
                "admin",
                "A-strong-admin-password-123",
                "cloud-name",
                "api-key",
                "api-secret",
                "https://example.com"));
    }

    @Test
    void rejectsWeakJwtSecret() {
        assertThrows(IllegalStateException.class, () -> new ProductionConfigurationValidator(
                "too-short",
                "admin",
                "A-strong-admin-password-123",
                "cloud-name",
                "api-key",
                "api-secret",
                "https://example.com"));
    }

    @Test
    void rejectsWildcardProductionCors() {
        assertThrows(IllegalStateException.class, () -> new ProductionConfigurationValidator(
                "a-strong-production-jwt-secret-that-is-long-enough-123456",
                "admin",
                "A-strong-admin-password-123",
                "cloud-name",
                "api-key",
                "api-secret",
                "*"));
    }
}
