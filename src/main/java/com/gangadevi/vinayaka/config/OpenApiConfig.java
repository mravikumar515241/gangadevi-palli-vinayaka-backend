package com.gangadevi.vinayaka.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI vinayakaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gangadevi Palli Vinayaka Chavithi API")
                        .version("v1")
                        .description("Backend API for the festival website."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter the JWT returned by POST /api/v1/auth/login.")));
    }

    @Bean
    public OperationCustomizer secureWriteOperations() {
        return (operation, handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();
            boolean isLogin = handlerMethod.getBeanType().getPackageName().equals("com.gangadevi.vinayaka.auth.controller")
                    && "login".equals(methodName);
            boolean isGet = handlerMethod.getMethod().isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class);
            boolean isAuthMe = handlerMethod.getBeanType().getPackageName().equals("com.gangadevi.vinayaka.auth.controller")
                    && "me".equals(methodName);
            boolean isAuditLog = handlerMethod.getBeanType().getPackageName().equals("com.gangadevi.vinayaka.audit.controller");
            if (!isLogin && (!isGet || isAuthMe || isAuditLog)) {
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            }
            return operation;
        };
    }
}