package com.plantify.transaction.domain.dto.request;

public record UpdateTransactionRequest(
        String orderId,
        String reason
) {
}
