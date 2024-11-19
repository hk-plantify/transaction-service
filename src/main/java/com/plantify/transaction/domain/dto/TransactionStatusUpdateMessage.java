package com.plantify.transaction.domain.dto;

import com.plantify.transaction.domain.entity.Status;

public record TransactionStatusUpdateMessage(
        Long transactionId,
        Status status
) {}