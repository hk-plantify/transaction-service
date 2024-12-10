package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.domain.entity.TransactionType;

public record TransactionRequest(
        Long userId,
        Long sellerId,
        String orderId,
        String orderName,
        Long amount,
        String transactionType,
        String reason
) {
    public Transaction toEntity() {
        return Transaction.builder()
                .userId(userId)
                .sellerId(sellerId)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .transactionType(TransactionType.valueOf(transactionType))
                .status(Status.PENDING)
                .reason(reason)
                .build();
    }
}