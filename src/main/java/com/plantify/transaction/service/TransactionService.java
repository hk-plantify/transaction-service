package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.*;
import com.plantify.transaction.domain.entity.Status;

import java.util.List;

public interface TransactionService {

    TransactionResponse getTransactionById(Long transactionId);
    TransactionResponse createPendingTransaction(PaymentRequest request);
    TransactionResponse createPayTransaction(PaymentRequest request);
    boolean existTransaction(Long userId, Long orderId, List<Status> statuses);
    TransactionResponse createRefundTransaction(TransactionRequest request);
}