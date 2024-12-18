package com.plantify.transaction.domain.dto;

public record UpdateTransactionRequest(
        Long userId,
        String orderId,
        String reason
) {
}
