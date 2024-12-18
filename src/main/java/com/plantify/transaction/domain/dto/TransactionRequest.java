package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;

public record TransactionRequest(
        Long userId,
        Long sellerId,
        String orderId,
        String orderName,
        Long amount,
        String redirectUri
) {
    public Transaction toEntity() {
        return Transaction.builder()
                .userId(userId)
                .sellerId(sellerId)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .status(Status.PENDING)
                .redirectUri(redirectUri)
                .build();
    }
}