package com.barberbook.backend.domain.user;

import com.barberbook.backend.domain.user.dto.RegisterUserRequest;
import com.barberbook.backend.domain.user.dto.RegisterUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoInteractions;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldReturnCreatedWhenRegistrationIsValid() throws Exception {
        RegisterUserResponse response = new RegisterUserResponse(
            1L,
            "Cliente Teste",
            "cliente@barberbook.com",
            UserRole.CUSTOMER,
            true
        );

        when(userService.register(any(RegisterUserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                  "name": "Cliente Teste",
                                  "email": "cliente@barberbook.com",
                                  "password": "Senha123"
                                }
                                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Cliente Teste"))
            .andExpect(jsonPath("$.email")
                .value("cliente@barberbook.com"))
            .andExpect(jsonPath("$.role").value("CUSTOMER"))
            .andExpect(jsonPath("$.active").value(true));
    }
    @Test
    void shouldReturnBadRequestWhenRegistrationDataIsInvalid()
        throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "name": "",
                              "email": "email-invalido",
                              "password": "123"
                            }
                            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code")
                .value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.path")
                .value("/api/users"))
            .andExpect(jsonPath("$.fieldErrors.name")
                .value("O nome é obrigatório"))
            .andExpect(jsonPath("$.fieldErrors.email")
                .value("Informe um e-mail válido"))
            .andExpect(jsonPath("$.fieldErrors.password")
                .value("A senha deve possuir entre 8 e 72 caracteres"));

        verifyNoInteractions(userService);
    }
    @Test
    void shouldReturnConflictWhenEmailIsAlreadyRegistered()
        throws Exception {

        when(userService.register(any(RegisterUserRequest.class)))
            .thenThrow(new EmailAlreadyRegisteredException());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "name": "Cliente Teste",
                              "email": "cliente@barberbook.com",
                              "password": "Senha123"
                            }
                            """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.code")
                .value("EMAIL_ALREADY_REGISTERED"))
            .andExpect(jsonPath("$.message")
                .value("Este e-mail já está cadastrado"))
            .andExpect(jsonPath("$.path")
                .value("/api/users"))
            .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }
    @Test
    void shouldReturnBadRequestWhenJsonIsMalformed()
        throws Exception {

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "name": "JSON Incorreto",
                              "email":
                            }
                            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code")
                .value("MALFORMED_JSON"))
            .andExpect(jsonPath("$.message")
                .value("O JSON enviado está inválido."))
            .andExpect(jsonPath("$.path")
                .value("/api/users"))
            .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verifyNoInteractions(userService);
    }
}
