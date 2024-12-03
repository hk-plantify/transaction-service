package com.plantify.transaction.domain.dto;

public record TransactionStatusMessage(
        Long transactionId,
        Long userId,          
        Long amount,          
        String status         
) {}