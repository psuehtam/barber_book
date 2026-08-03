package com.barberbook.backend.domain.user;

import com.barberbook.backend.domain.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.barberbook.backend.domain.user.dto.RegisterUserResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRejectRegistrationWhenEmailAlreadyExists() {
        when(userRepository.existsByEmailIgnoreCase(
            "cliente@barberbook.com"
        )).thenReturn(true);

        UserService userService = new UserService(
            userRepository,
            passwordEncoder
        );

        RegisterUserRequest request = new RegisterUserRequest(
            "Cliente Teste",
            "CLIENTE@BARBERBOOK.COM",
            "Senha123"
        );

        assertThrows(
            EmailAlreadyRegisteredException.class,
            () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
    @Test
    void shouldRegisterCustomerWithNormalizedData() {
        when(passwordEncoder.encode("Senha123"))
            .thenReturn("senha-criptografada");

        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UserService userService = new UserService(
            userRepository,
            passwordEncoder
        );

        RegisterUserRequest request = new RegisterUserRequest(
            "  Cliente Teste  ",
            "  CLIENTE@BARBERBOOK.COM  ",
            "Senha123"
        );

        RegisterUserResponse response = userService.register(request);

        assertEquals("Cliente Teste", response.name());
        assertEquals("cliente@barberbook.com", response.email());
        assertEquals(UserRole.CUSTOMER, response.role());
        assertTrue(response.active());

        verify(userRepository)
            .existsByEmailIgnoreCase("cliente@barberbook.com");

        verify(passwordEncoder).encode("Senha123");
        verify(userRepository).save(any(User.class));
    }
}
