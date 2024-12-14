package com.plantify.transaction.domain.dto;

public record PaymentRequest(
        Long userId,
        Long transactionId,
        String orderId,
        String orderName,
        Long amount
) {

}
