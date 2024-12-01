package com.plantify.transaction.service;

import com.plantify.transaction.client.PaymentServiceClient;
import com.plantify.transaction.config.RedisLock;
import com.plantify.transaction.domain.dto.*;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.exception.ApplicationException;
import com.plantify.transaction.global.exception.errorcode.TransactionErrorCode;
import com.plantify.transaction.kafka.TransactionProvider;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentServiceClient paymentServiceClient;
    private final TransactionProvider transactionProvider;
    private final RedisLock redisLock;

    private static final int LOCK_TIMEOUT_MS = 3000;

    @Override
    public PayTransactionResponse createTransaction(TransactionRequest request) {
        String lockKey = String.format("transaction:%d", request.userId());

        try {
            if (!redisLock.tryLock(lockKey, LOCK_TIMEOUT_MS)) {
                throw new ApplicationException(TransactionErrorCode.CONCURRENT_UPDATE);
            }

            Transaction transaction = transactionRepository.save(request.toEntity());
            PaymentResponse paymentResponse = paymentServiceClient.processPayment(request);

            TransactionStatusMessage statusMessage = new TransactionStatusMessage(
                    transaction.getTransactionId(),
                    transaction.getUserId(),
                    paymentResponse.amount(),
                    paymentResponse.status()
            );
            transactionProvider.sendTransactionStatusMessage(statusMessage);

            return PayTransactionResponse.from(transaction, paymentResponse);
        } finally {
            redisLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));
        return TransactionResponse.from(transaction);
    }

    @Override
    public boolean existTransaction(Long userId, List<Status> statuses) {
        return transactionRepository.existsByUserIdAndStatusIn(userId, statuses);
    }
}