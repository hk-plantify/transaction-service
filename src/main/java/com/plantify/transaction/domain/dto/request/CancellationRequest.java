package com.plantify.transaction.domain.dto.request;

public record CancellationRequest(
        Long paymentId,
        String reason
) {
}
