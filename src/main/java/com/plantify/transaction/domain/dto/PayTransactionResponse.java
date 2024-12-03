package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.domain.entity.TransactionType;

import java.time.LocalDateTime;

public record PayTransactionResponse(
        Long transactionId,
        Long userId,
        Long sellerId,
        Long orderId,
        String orderName,
        TransactionType transactionType,
        String status,
        Long amount,
        Long balanceAfter,
        String method,
        String failureReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PayTransactionResponse from(Transaction transaction, PaymentResponse paymentResponse) {
        return new PayTransactionResponse(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getSellerId(),
                paymentResponse.orderId(),
                paymentResponse.orderName(),
                transaction.getTransactionType(),
                transaction.getStatus().name(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                paymentResponse.method(),
                paymentResponse.failureReason(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
