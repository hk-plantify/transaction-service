package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionStatusMessage;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.exception.ApplicationException;
import com.plantify.transaction.global.exception.errorcode.TransactionErrorCode;
import com.plantify.transaction.global.util.DistributedLock;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionStatusServiceImpl implements TransactionStatusService {

    private final TransactionRepository transactionRepository;
    private final DistributedLock distributedLock;

    // 성공
    @Override
    public void processSuccessfulTransaction(TransactionStatusMessage message) {
        String lockKey = String.format("transaction:result:%d", message.userId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            Transaction transaction = transactionRepository.findById(message.transactionId())
                    .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

            transaction.updateStatus(message.status());
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    // 실패
    @Override
    public void processFailedTransaction(TransactionStatusMessage message) {
        String lockKey = String.format("transaction:result:%d", message.userId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            Transaction transaction = transactionRepository.findById(message.transactionId())
                    .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

            transaction.updateStatus(Status.FAILED);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }
}