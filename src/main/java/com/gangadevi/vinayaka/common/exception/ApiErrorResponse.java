package com.gangadevi.vinayaka.common.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        String requestId,
        Map<String, String> fieldErrors) {
}
