package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.domain.entity.TransactionType;

public record TransactionRequest(
        Long userId,
        Long sellerId,
        TransactionType transactionType,
        Long amount,
        String reason
) {
    public Transaction toEntity() {
        return Transaction.builder()
                .userId(userId())
                .sellerId(sellerId())
                .transactionType(transactionType())
                .amount(amount())
                .status(Status.PENDING)
                .reason(reason())
                .build();
    }
}