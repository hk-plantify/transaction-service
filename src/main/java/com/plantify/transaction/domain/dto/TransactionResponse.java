package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;

import java.time.LocalDateTime;

public record TransactionResponse(
        Long transactionId,
        Long userId,
        Long sellerId,
        Status status,
        String errorCode,
        Long retryCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getSellerId(),
                transaction.getStatus(),
                transaction.getErrorCode(),
                transaction.getRetryCount(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
