package com.gangadevi.vinayaka.audit.dto;

import com.gangadevi.vinayaka.audit.entity.AuditAction;

import java.time.OffsetDateTime;

public record AuditLogResponse(
        Long id,
        String username,
        AuditAction action,
        String entityType,
        Long entityId,
        String description,
        String requestId,
        String ipAddress,
        OffsetDateTime createdAt) {
}
