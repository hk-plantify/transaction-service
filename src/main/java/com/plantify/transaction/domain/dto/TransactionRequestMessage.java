package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.TransactionType;

public record TransactionRequestMessage(
        Long userId,
        Long sellerId,
        TransactionType transactionType,
        Long amount,
        String reason,
        Long balanceAfter
) {}