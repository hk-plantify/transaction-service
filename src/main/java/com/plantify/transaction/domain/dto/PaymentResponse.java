package com.plantify.transaction.domain.dto;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long transactionId,
        Long userId,
        Long paymentId,
        Long orderId,
        String orderName,
        Long sellerId,
        String method,
        String amount,
        String transactionType,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}