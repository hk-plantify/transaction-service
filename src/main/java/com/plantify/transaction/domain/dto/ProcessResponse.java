package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;

import java.time.LocalDateTime;

public record ProcessResponse(
        Long paymentId,
        Long userId,
        Long transactionId,
        String orderId,
        String orderName,
        Long amount,
        Status status,
        String method,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}