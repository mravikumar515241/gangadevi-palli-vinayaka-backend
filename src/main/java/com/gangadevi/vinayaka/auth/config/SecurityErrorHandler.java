package com.gangadevi.vinayaka.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gangadevi.vinayaka.common.exception.ApiErrorResponse;
import com.gangadevi.vinayaka.common.exception.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        write(response, request, 401, "Unauthorized", "UNAUTHORIZED", "Authentication is required to access this resource");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        write(response, request, 403, "Forbidden", "FORBIDDEN", "You do not have permission to access this resource");
    }

    private void write(HttpServletResponse response, HttpServletRequest request, int status,
                       String error, String code, String message) throws IOException {
        String requestId = (String) request.getAttribute(RequestIdFilter.MDC_KEY);
        if (requestId == null) {
            requestId = request.getHeader(RequestIdFilter.HEADER);
        }
        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(), status, error, code, message,
                request.getRequestURI(), requestId, Map.of());
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
