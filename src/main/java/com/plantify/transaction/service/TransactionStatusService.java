package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionStatusMessage;

public interface TransactionStatusService {

    void processSuccessfulTransaction(TransactionStatusMessage message);
    void processFailedTransaction(TransactionStatusMessage message);
}
