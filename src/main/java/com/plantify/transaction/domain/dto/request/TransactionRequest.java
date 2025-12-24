package com.plantify.transaction.domain.dto.request;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;

public record TransactionRequest(
        Long sellerId,
        String orderId,
        String orderName,
        Long amount,
        String redirectUri
) {
    public Transaction toEntity(Long userId) {
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