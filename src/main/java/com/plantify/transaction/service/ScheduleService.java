package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionStatusMessage;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.util.DistributedLock;
import com.plantify.transaction.kafka.TransactionProvider;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final TransactionRepository transactionRepository;
    private final TransactionProvider transactionProvider;
    private final DistributedLock distributedLock;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void markPendingTransactionsAsFailed() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<Transaction> expiredTransactions = transactionRepository.findAllByStatusAndCreatedAtBefore(Status.PENDING, expirationTime);

        for (Transaction transaction : expiredTransactions) {
            String lockKey = String.format("transaction:%d", transaction.getUserId());

            try {
                distributedLock.tryLockOrThrow(lockKey);

                transaction.updateStatus(Status.FAILED);
                transactionRepository.save(transaction);
                transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            } finally {
                distributedLock.unlock(lockKey);
            }
        }
    }
}
