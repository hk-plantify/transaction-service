package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;

public record TransactionRequest(
        Long userId,
        Long sellerId,
        Status status,
        String errorCode,
        Long retryCount
) {

    public Transaction toEntity() {
        return Transaction.builder()
                .userId(userId)
                .sellerId(sellerId)
                .status(status)
                .errorCode(errorCode)
                .retryCount(retryCount)
                .build();
    }
}
