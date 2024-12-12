package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.domain.entity.TransactionType;

public record PaymentRequest(
        Long userId,
        String orderId,
        String orderName,
        Long sellerId,
        Long amount,
        String redirectUri
) {

    public Transaction toEntity() {
        return Transaction.builder()
                .userId(userId)
                .orderId(orderId)
                .orderName(orderName)
                .sellerId(sellerId)
                .amount(amount)
                .transactionType(TransactionType.PAYMENT)
                .status(Status.PENDING)
                .reason(null)
                .redirectUri(redirectUri)
                .build();
    }
}
