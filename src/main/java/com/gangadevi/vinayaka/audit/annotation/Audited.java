package com.gangadevi.vinayaka.audit.annotation;

import com.gangadevi.vinayaka.audit.entity.AuditAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audited {
    AuditAction action();
    String entityType();
    String description();
    /** Controller argument index containing the entity id. Use -1 when the id comes from the returned object. */
    int entityIdArgument() default -1;
    /** Extract the id from the successful method return value (record accessor id() or getId()). */
    boolean entityIdFromResult() default false;
}
