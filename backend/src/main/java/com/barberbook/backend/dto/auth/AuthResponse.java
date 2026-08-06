package com.barberbook.backend.dto.auth;

import com.barberbook.backend.entity.Role;

public record AuthResponse(
    String token,
    Long userId,
    String name,
    String email,
    Role role
) {
}
