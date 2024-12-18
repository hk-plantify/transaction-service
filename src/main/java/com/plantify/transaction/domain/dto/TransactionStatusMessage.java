package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;

public record TransactionStatusMessage(
        Long transactionId,
        Long userId,
        String orderId,
        Long amount,          
        Status status
) {

    public static TransactionStatusMessage from(Transaction transaction) {
        return new TransactionStatusMessage(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getOrderId(),
                transaction.getAmount(),
                transaction.getStatus()
        );
    }
}