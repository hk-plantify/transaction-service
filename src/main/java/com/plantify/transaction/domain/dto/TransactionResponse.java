package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Transaction;

import java.time.LocalDateTime;

public record TransactionResponse(
        Long transactionId,
        Long userId,
        Long paymentId,
        String orderId,
        String orderName,
        Long amount,
        Long sellerId,
        String transactionType,
        String status,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getPaymentId(),
                transaction.getOrderId(),
                transaction.getOrderName(),
                transaction.getAmount(),
                transaction.getSellerId(),
                transaction.getTransactionType().name(),
                transaction.getStatus().name(),
                transaction.getReason(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
