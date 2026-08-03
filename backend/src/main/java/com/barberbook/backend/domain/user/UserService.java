package com.barberbook.backend.domain.user;

import com.barberbook.backend.domain.user.dto.RegisterUserRequest;
import com.barberbook.backend.domain.user.dto.RegisterUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        String normalizedEmail = request.email()
            .trim()
            .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException();
        }

        User user = new User(
            request.name().trim(),
            normalizedEmail,
            passwordEncoder.encode(request.password()),
            UserRole.CUSTOMER
        );

        User savedUser = userRepository.save(user);

        return RegisterUserResponse.from(savedUser);
    }
}
