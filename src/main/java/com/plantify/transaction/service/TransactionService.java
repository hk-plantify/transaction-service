package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.*;
import com.plantify.transaction.domain.entity.Status;

import java.util.List;

public interface TransactionService {

    TransactionResponse getTransactionById(Long transactionId);
    boolean existTransaction(Long userId, String orderId, List<Status> statuses);
    TransactionResponse createPendingTransaction(TransactionRequest request);
    TransactionResponse updateTransactionToSuccess(PayTransactionRequest request);
    TransactionResponse updateTransactionToRefund(UpdateTransactionRequest request);
    TransactionResponse updateTransactionToCancellation(UpdateTransactionRequest request);
}