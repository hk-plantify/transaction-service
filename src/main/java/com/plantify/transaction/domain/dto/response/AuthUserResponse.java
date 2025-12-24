package com.plantify.transaction.domain.dto.response;

public record AuthUserResponse(
        Long userId,
        String role
) {
}
