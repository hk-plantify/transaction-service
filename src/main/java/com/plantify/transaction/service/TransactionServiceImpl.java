package com.plantify.transaction.service;

import com.plantify.transaction.domain.dto.TransactionRequestMessage;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import com.plantify.transaction.global.exception.ApplicationException;
import com.plantify.transaction.global.exception.errorcode.TransactionErrorCode;
import com.plantify.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void createTransaction(TransactionRequestMessage message) {
        Transaction transaction = Transaction.builder()
                .userId(message.userId())
                .sellerId(message.sellerId())
                .transactionType(message.transactionType())
                .status(Status.PENDING)
                .amount(message.amount())
                .reason(message.reason())
                .balanceAfter(message.balanceAfter())
                .build();
        transactionRepository.save(transaction);
    }

    @Override
    public void updateTransactionStatus(Long transactionId, Status status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

        transaction = transaction.toBuilder()
                .status(status)
                .build();
        transactionRepository.save(transaction);
    }
}