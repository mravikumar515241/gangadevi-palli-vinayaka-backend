package com.gangadevi.vinayaka.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse notFound(ResourceNotFoundException e, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "Not Found", "RESOURCE_NOT_FOUND", e.getMessage(), request, Map.of());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse conflict(ConflictException e, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "Conflict", "CONFLICT", e.getMessage(), request, Map.of());
    }

    @ExceptionHandler(ImageStorageException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiErrorResponse imageStorage(ImageStorageException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_GATEWAY, "Bad Gateway", "STORAGE_SERVICE_ERROR", e.getMessage(), request, Map.of());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse business(BusinessException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "BUSINESS_RULE_VIOLATION", e.getMessage(), request, Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse badRequest(IllegalArgumentException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "INVALID_REQUEST", e.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fields.putIfAbsent(fieldError.getField(), Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid value"));
        }
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "VALIDATION_FAILED",
                "One or more fields are invalid", request, fields);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse constraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        e.getConstraintViolations().forEach(v -> fields.put(v.getPropertyPath().toString(), v.getMessage()));
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "VALIDATION_FAILED",
                "One or more request parameters are invalid", request, fields);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse typeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String message = "Invalid value for parameter '" + e.getName() + "'";
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "INVALID_PARAMETER", message, request, Map.of(e.getName(), message));
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse missingParameter(Exception e, HttpServletRequest request) {
        String message = e instanceof MissingServletRequestPartException
                ? "Required multipart part is missing"
                : "Required request parameter is missing";
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "MISSING_PARAMETER", message, request, Map.of());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse maxUploadSize(MaxUploadSizeExceededException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "FILE_TOO_LARGE",
                "Uploaded file exceeds the maximum allowed size", request, Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse unreadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", "MALFORMED_REQUEST",
                "Request body is missing or malformed", request, Map.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiErrorResponse unsupportedMediaType(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type", "UNSUPPORTED_MEDIA_TYPE",
                "The request content type is not supported", request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse dataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "Conflict", "DATA_CONFLICT",
                "The request conflicts with existing data", request, Map.of());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse authentication(AuthenticationException e, HttpServletRequest request) {
        String message = e instanceof BadCredentialsException
                ? "Username or password is incorrect"
                : "Authentication failed";
        return error(HttpStatus.UNAUTHORIZED, "Unauthorized", "UNAUTHORIZED", message, request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unexpected(Exception e, HttpServletRequest request) {
        log.error("Unexpected API error requestId={} method={} path={}",
                request.getAttribute(RequestIdFilter.MDC_KEY), request.getMethod(), request.getRequestURI(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred", request, Map.of());
    }

    private ApiErrorResponse error(HttpStatus status, String error, String code, String message,
                                   HttpServletRequest request, Map<String, String> fieldErrors) {
        String requestId = request.getAttribute(RequestIdFilter.MDC_KEY) instanceof String id
                ? id
                : request.getHeader(RequestIdFilter.HEADER);
        return new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                error,
                code,
                message == null || message.isBlank() ? status.getReasonPhrase() : message,
                request.getRequestURI(),
                requestId,
                fieldErrors);
    }
}
