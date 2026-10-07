package com.gangadevi.vinayaka.audit.controller;

import com.gangadevi.vinayaka.audit.dto.AuditLogPageResponse;
import com.gangadevi.vinayaka.audit.entity.AuditAction;
import com.gangadevi.vinayaka.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService service;

    @GetMapping
    public AuditLogPageResponse search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) String entityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.search(username, action, entityType, page, size);
    }
}
