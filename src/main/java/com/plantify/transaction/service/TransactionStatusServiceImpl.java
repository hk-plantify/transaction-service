package com.plantify.transaction.service;

import com.plantify.transaction.config.RedisLock;
import com.plantify.transaction.domain.dto.TransactionStatusMessage;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.exception.ApplicationException;
import com.plantify.transaction.global.exception.errorcode.TransactionErrorCode;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionStatusServiceImpl implements TransactionStatusService {

    private final TransactionRepository transactionRepository;
    private final RedisLock redisLock;

    private static final int LOCK_TIMEOUT_MS = 3000;

    // 성공
    @Override
    public void processSuccessfulTransaction(TransactionStatusMessage message) {
        String lockKey = String.format("transaction:%d", message.userId());

        try {
            if (!redisLock.tryLock(lockKey, LOCK_TIMEOUT_MS)) {
                throw new ApplicationException(TransactionErrorCode.CONCURRENT_UPDATE);
            }

            Transaction transaction = transactionRepository.findByTransactionId(message.transactionId())
                    .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

            transaction = transaction.toBuilder()
                    .status(Status.SUCCESS)
                    .balanceAfter(message.amount())
                    .build();
            transactionRepository.save(transaction);
        } finally {
            redisLock.unlock(lockKey);
        }
    }

    // 실패
    @Override
    public void processFailedTransaction(TransactionStatusMessage message) {
        String lockKey = String.format("transaction:%d", message.userId());

        try {
            if (!redisLock.tryLock(lockKey, LOCK_TIMEOUT_MS)) {
                throw new ApplicationException(TransactionErrorCode.CONCURRENT_UPDATE);
            }

            Transaction transaction = transactionRepository.findByTransactionId(message.transactionId())
                    .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

            transaction = transaction.toBuilder()
                    .status(Status.FAILED)
                    .build();
            transactionRepository.save(transaction);
        } finally {
            redisLock.unlock(lockKey);
        }
    }
}