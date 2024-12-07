package com.plantify.transaction.service;

import com.plantify.transaction.client.PaymentServiceClient;
import com.plantify.transaction.domain.dto.*;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.exception.ApplicationException;
import com.plantify.transaction.global.exception.errorcode.TransactionErrorCode;
import com.plantify.transaction.global.util.DistributedLock;
import com.plantify.transaction.kafka.TransactionProvider;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentServiceClient paymentServiceClient;
    private final TransactionProvider transactionProvider;
    private final DistributedLock distributedLock;

    @Override
    public boolean existTransaction(Long userId, Long orderId, List<Status> statuses) {
        return transactionRepository.existsByUserIdAndOrderIdAndStatusIn(userId, orderId, statuses);
    }

    @Override
    @Transactional
    public TransactionResponse createPendingTransaction(PaymentRequest request) {
        String lockKey = String.format("transaction:%d", request.userId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            Transaction transaction = transactionRepository
                    .save(request.toEntity())
                    .updateStatus(Status.PENDING);
            transactionRepository.save(transaction);

            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse createPayTransaction(PaymentRequest request) {
        String lockKey = String.format("transaction:%d", request.userId());

        Transaction transaction = transactionRepository.save(request.toEntity());
        try {
            distributedLock.tryLockOrThrow(lockKey);

            PaymentResponse paymentResponse = paymentServiceClient.processPayment(request);
            transaction.updateStatus(Status.valueOf(paymentResponse.status()))
                    .updatePaymentId(paymentResponse.paymentId());

            transactionRepository.save(transaction);

            transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));
        return TransactionResponse.from(transaction);
    }

    @Override
    public TransactionResponse createRefundTransaction(TransactionRequest request) {
        String lockKey = String.format("transaction:%d", request.userId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            Transaction transaction = transactionRepository
                    .save(request.toEntity())
                    .updateStatus(Status.SUCCESS);

            transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }
}