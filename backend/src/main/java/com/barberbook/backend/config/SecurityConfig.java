package com.barberbook.backend.config;

import java.util.Arrays;
import java.util.List;

import com.barberbook.backend.security.CustomUserDetailsService;
import com.barberbook.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final String allowedOrigins;

    public SecurityConfig(
        @Value("${app.cors.allowed-origins}") String allowedOrigins
    ) {
        this.allowedOrigins = allowedOrigins;
    }


    @Bean
    DaoAuthenticationProvider authenticationProvider(
        CustomUserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        var provider =
            new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtFilter,
        DaoAuthenticationProvider provider
    ) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(
                corsConfigurationSource(allowedOrigins)
            ))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(provider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/health",
                    "/api/auth/**",
                    "/api/barbearia",
                    "/api/barbeiros",
                    "/api/servicos",
                    "/api/barbeiros/*/disponibilidade",
                    "/api/webhooks/stripe",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                )
                .permitAll()
                .requestMatchers(
                    "/api/admin/**",
                    "/api/integracoes/cep/**"
                )
                .hasRole("ADMIN")
                .requestMatchers("/api/agendamentos/**")
                .hasRole("CLIENT")
                .anyRequest()
                .authenticated()
            )
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    private CorsConfigurationSource corsConfigurationSource(
        String originsValue
    ) {
        List<String> origins = Arrays
            .stream(originsValue.split(","))
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .toList();

        var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "OPTIONS")
        );
        configuration.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type",
                "Stripe-Signature"
            )
        );
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
