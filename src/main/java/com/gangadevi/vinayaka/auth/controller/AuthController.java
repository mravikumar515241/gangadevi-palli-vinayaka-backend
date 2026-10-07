package com.gangadevi.vinayaka.auth.controller;

import com.gangadevi.vinayaka.auth.dto.CurrentUserResponse;
import com.gangadevi.vinayaka.auth.dto.LoginRequest;
import com.gangadevi.vinayaka.auth.dto.LoginResponse;
import com.gangadevi.vinayaka.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                .orElse("UNKNOWN");
        return new CurrentUserResponse(authentication.getName(), role);
    }
}
