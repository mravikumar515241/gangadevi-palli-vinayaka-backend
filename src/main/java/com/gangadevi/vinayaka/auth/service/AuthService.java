package com.gangadevi.vinayaka.auth.service;

import com.gangadevi.vinayaka.auth.dto.LoginRequest;
import com.gangadevi.vinayaka.auth.dto.LoginResponse;
import com.gangadevi.vinayaka.auth.entity.AdminUser;
import com.gangadevi.vinayaka.audit.entity.AuditAction;
import com.gangadevi.vinayaka.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AdminUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        AdminUser user = userDetailsService.loadAdminUser(request.username());
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        auditLogService.record(user.getUsername(), AuditAction.LOGIN, "AUTHENTICATION", null,
                "Successful admin login", null, null);
        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds(), user.getUsername(), user.getRole().name());
    }
}
