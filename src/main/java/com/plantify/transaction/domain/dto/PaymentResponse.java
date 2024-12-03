package com.plantify.transaction.domain.dto;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long userId,
        Long payId,
        Long sellerId,
        Long orderId,
        String orderName,
        String status,
        Long amount,
        String method,
        String failureReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}