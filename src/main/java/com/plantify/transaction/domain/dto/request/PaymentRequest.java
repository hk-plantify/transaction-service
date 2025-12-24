package com.plantify.transaction.domain.dto.request;

public record PaymentRequest(
        Long transactionId,
        String orderId,
        String orderName,
        Long amount
) {
}
