package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.domain.entity.TransactionType;

import java.time.LocalDateTime;

public record TransactionResponse(
        Long transactionId,
        Long userId,
        Long sellerId,
        TransactionType transactionType,
        String status,
        Long amount,
        Long balanceAfter,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getSellerId(),
                transaction.getTransactionType(),
                transaction.getStatus().name(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}