package com.plantify.transaction.domain.dto;

public record CancellationRequest(
        Long userId,
        Long paymentId,
        String reason
) {
}
