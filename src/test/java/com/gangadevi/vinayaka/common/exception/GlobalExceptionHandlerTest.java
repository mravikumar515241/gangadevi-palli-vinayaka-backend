package com.gangadevi.vinayaka.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundUsesStandardErrorContract() {
        MockHttpServletRequest request = request("GET", "/api/v1/festivals/2030");
        request.setAttribute(RequestIdFilter.MDC_KEY, "request-123");

        ApiErrorResponse response = handler.notFound(
                new ResourceNotFoundException("Festival year not found: 2030"), request);

        assertEquals(404, response.status());
        assertEquals("RESOURCE_NOT_FOUND", response.code());
        assertEquals("/api/v1/festivals/2030", response.path());
        assertEquals("request-123", response.requestId());
        assertTrue(response.fieldErrors().isEmpty());
    }

    @Test
    void conflictUses409() {
        ApiErrorResponse response = handler.conflict(
                new ConflictException("Festival year already exists: 2026"), request("POST", "/api/v1/festivals"));

        assertEquals(409, response.status());
        assertEquals("CONFLICT", response.code());
    }

    @Test
    void validationIncludesFieldErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "must not be blank"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ApiErrorResponse response = handler.validation(exception, request("POST", "/api/v1/festivals"));

        assertEquals(400, response.status());
        assertEquals("VALIDATION_FAILED", response.code());
        assertEquals(Map.of("name", "must not be blank"), response.fieldErrors());
    }

    @Test
    void unexpectedErrorHidesInternalDetails() {
        ApiErrorResponse response = handler.unexpected(
                new RuntimeException("database password should never be returned"),
                request("GET", "/api/v1/festivals/2026"));

        assertEquals(500, response.status());
        assertEquals("INTERNAL_SERVER_ERROR", response.code());
        assertEquals("An unexpected error occurred", response.message());
    }

    private MockHttpServletRequest request(String method, String path) {
        return new MockHttpServletRequest(method, path);
    }
}
