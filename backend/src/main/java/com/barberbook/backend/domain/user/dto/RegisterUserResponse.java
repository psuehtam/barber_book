package com.barberbook.backend.domain.user.dto;

import com.barberbook.backend.domain.user.User;
import com.barberbook.backend.domain.user.UserRole;

public record RegisterUserResponse(
    Long id,
    String name,
    String email,
    UserRole role,
    boolean active
) {

    public static RegisterUserResponse from(User user) {
        return new RegisterUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.isActive()
        );
    }
}
