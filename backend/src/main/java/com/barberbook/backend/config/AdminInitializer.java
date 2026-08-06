package com.barberbook.backend.config;

import java.util.Locale;

import com.barberbook.backend.entity.Role;
import com.barberbook.backend.entity.User;
import com.barberbook.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String name;
    private final String email;
    private final String password;

    public AdminInitializer(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.name}") String name,
        @Value("${app.admin.email}") String email,
        @Value("${app.admin.password}") String password
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String normalized = email
            .trim()
            .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalized)) {
            return;
        }

        if (password == null || password.length() < 8) {
            throw new IllegalStateException(
                "ADMIN_PASSWORD must have at least 8 characters."
            );
        }

        userRepository.save(
            new User(
                name.trim(),
                normalized,
                passwordEncoder.encode(password),
                Role.ADMIN
            )
        );
    }
}
