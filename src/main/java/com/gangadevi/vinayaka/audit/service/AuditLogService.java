package com.gangadevi.vinayaka.audit.service;

import com.gangadevi.vinayaka.audit.dto.AuditLogPageResponse;
import com.gangadevi.vinayaka.audit.dto.AuditLogResponse;
import com.gangadevi.vinayaka.audit.entity.AuditAction;
import com.gangadevi.vinayaka.audit.entity.AuditLog;
import com.gangadevi.vinayaka.audit.repository.AuditLogRepository;
import com.gangadevi.vinayaka.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String username, AuditAction action, String entityType, Long entityId,
                       String description, String requestId, String ipAddress) {
        repository.save(AuditLog.builder()
                .username(username == null || username.isBlank() ? "SYSTEM" : username)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .requestId(requestId)
                .ipAddress(ipAddress)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordCurrentUser(AuditAction action, String entityType, Long entityId,
                                  String description, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.isAuthenticated()
                ? authentication.getName() : "SYSTEM";
        String requestId = request == null ? null : request.getAttribute("requestId") instanceof String id ? id : request.getHeader("X-Request-ID");
        String ip = request == null ? null : resolveClientIp(request);
        record(username, action, entityType, entityId, description, requestId, ip);
    }

    @Transactional(readOnly = true)
    public AuditLogPageResponse search(String username, AuditAction action, String entityType, int page, int size) {
        if (page < 0) throw new BusinessException("Page must be greater than or equal to 0");
        if (size < 1 || size > 100) throw new BusinessException("Size must be between 1 and 100");
        String normalizedUsername = normalize(username);
        String normalizedEntityType = normalize(entityType);

        Specification<AuditLog> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (normalizedUsername != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + normalizedUsername.toLowerCase(Locale.ROOT) + "%"));
            }

            if (action != null) {
                predicates.add(criteriaBuilder.equal(root.get("action"), action));
            }

            if (normalizedEntityType != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("entityType")),
                        normalizedEntityType.toLowerCase(Locale.ROOT)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "createdAt")
                        .and(org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC, "id")));

        Page<AuditLog> result = repository.findAll(specification, pageRequest);
        return new AuditLogPageResponse(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages(),
                result.isFirst(), result.isLast());
    }

    private AuditLogResponse toResponse(AuditLog a) {
        return new AuditLogResponse(a.getId(), a.getUsername(), a.getAction(), a.getEntityType(),
                a.getEntityId(), a.getDescription(), a.getRequestId(), a.getIpAddress(), a.getCreatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
