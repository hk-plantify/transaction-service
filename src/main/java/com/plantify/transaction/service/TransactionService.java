package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionRequest;
import com.plantify.transaction.domain.dto.PayTransactionResponse;
import com.plantify.transaction.domain.dto.TransactionResponse;
import com.plantify.transaction.domain.entity.Status;

import java.util.List;

public interface TransactionService {

    PayTransactionResponse createTransaction(TransactionRequest request);
    TransactionResponse getTransactionById(Long transactionId);
    boolean existTransaction(Long userId, List<Status> statuses);
}