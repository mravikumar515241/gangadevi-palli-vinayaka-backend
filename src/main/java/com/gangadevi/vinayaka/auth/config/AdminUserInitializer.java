package com.gangadevi.vinayaka.auth.config;

import com.gangadevi.vinayaka.auth.entity.AdminUser;
import com.gangadevi.vinayaka.auth.entity.Role;
import com.gangadevi.vinayaka.auth.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {
    private final AdminUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.admin-username:admin}")
    private String username;

    @Value("${app.security.admin-password:Admin@12345}")
    private String password;

    @Override
    public void run(String... args) {
        if (!repository.existsByUsername(username)) {
            repository.save(AdminUser.builder()
                    .username(username)
                    .passwordHash(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build());
        }
    }
}
