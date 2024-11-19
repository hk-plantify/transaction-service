package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionRequestMessage;
import com.plantify.transaction.domain.entity.Status;

public interface TransactionService {

    void createTransaction(TransactionRequestMessage message);
    void updateTransactionStatus(Long transactionId, Status status);
}