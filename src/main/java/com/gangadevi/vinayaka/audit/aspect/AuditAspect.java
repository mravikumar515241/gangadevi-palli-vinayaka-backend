package com.gangadevi.vinayaka.audit.aspect;

import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditLogService auditLogService;
    private final HttpServletRequest request;

    @AfterReturning(pointcut = "@annotation(audited)", returning = "result")
    public void audit(JoinPoint joinPoint, Audited audited, Object result) {
        try {
            Long entityId = resolveEntityId(joinPoint, audited, result);
            auditLogService.recordCurrentUser(audited.action(), audited.entityType(), entityId,
                    audited.description(), request);
        } catch (Exception e) {
            // Audit failure must not turn a successful business operation into a failed API response.
            log.error("Unable to persist audit log for {}.{}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e);
        }
    }

    private Long resolveEntityId(JoinPoint joinPoint, Audited audited, Object result) {
        if (audited.entityIdArgument() >= 0) {
            Object[] args = joinPoint.getArgs();
            int index = audited.entityIdArgument();
            if (index < args.length && args[index] instanceof Number number) return number.longValue();
        }
        if (audited.entityIdFromResult() && result != null) {
            try {
                Method idMethod = result.getClass().getMethod("id");
                Object id = idMethod.invoke(result);
                if (id instanceof Number number) return number.longValue();
            } catch (ReflectiveOperationException ignored) {
                try {
                    Method idMethod = result.getClass().getMethod("getId");
                    Object id = idMethod.invoke(result);
                    if (id instanceof Number number) return number.longValue();
                } catch (ReflectiveOperationException ignoredAgain) {
                    // No id is available on the response.
                }
            }
        }
        return null;
    }
}
