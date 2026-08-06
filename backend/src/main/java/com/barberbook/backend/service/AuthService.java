package com.barberbook.backend.service;

import java.util.Locale;

import com.barberbook.backend.dto.auth.AuthResponse;
import com.barberbook.backend.dto.auth.LoginRequest;
import com.barberbook.backend.dto.auth.RegisterRequest;
import com.barberbook.backend.entity.Role;
import com.barberbook.backend.entity.User;
import com.barberbook.backend.exception.ConflictException;
import com.barberbook.backend.exception.UnauthorizedException;
import com.barberbook.backend.repository.UserRepository;
import com.barberbook.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException(
                "EMAIL_ALREADY_USED",
                "Já existe uma conta com esse e-mail."
            );
        }

        User user = new User(
            request.name().trim(),
            email,
            passwordEncoder.encode(request.password()),
            Role.CLIENT
        );

        userRepository.save(user);

        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email,
                    request.password()
                )
            );
        } catch (AuthenticationException exception) {
            throw new UnauthorizedException(
                "INVALID_CREDENTIALS",
                "E-mail ou senha inválidos."
            );
        }

        User user = userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() ->
                new UnauthorizedException(
                    "INVALID_CREDENTIALS",
                    "E-mail ou senha inválidos."
                )
            );

        return response(user);
    }

    private AuthResponse response(User user) {
        return new AuthResponse(
            jwtService.generate(user),
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

    private String normalizeEmail(String email) {
        return email
            .trim()
            .toLowerCase(Locale.ROOT);
    }
}
