package com.barberbook.backend.security;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import com.barberbook.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;
    private final Clock clock;

    public JwtService(
        @Value("${app.jwt.secret}") String base64Secret,
        @Value("${app.jwt.expiration-minutes}") long expirationMinutes,
        Clock clock
    ) {
        this.key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(base64Secret)
        );
        this.expirationMinutes = expirationMinutes;
        this.clock = clock;
    }

    public String generate(User user) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(
            expirationMinutes,
            ChronoUnit.MINUTES
        );

        return Jwts.builder()
            .subject(user.getEmail())
            .claim("role", user.getRole().name())
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiresAt))
            .signWith(key)
            .compact();
    }

    public String extractSubject(String token) {
        return claims(token).getSubject();
    }

    public boolean isValid(String token, String expectedEmail) {
        Claims claims = claims(token);
        return expectedEmail.equalsIgnoreCase(claims.getSubject())
            && claims.getExpiration().toInstant().isAfter(clock.instant());
    }

    private Claims claims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
