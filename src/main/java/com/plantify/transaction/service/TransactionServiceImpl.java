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
    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));
        return TransactionResponse.from(transaction);
    }

    @Override
    public boolean existTransaction(Long userId, String orderId, List<Status> statuses) {
        return transactionRepository.existsByUserIdAndOrderIdAndStatusIn(userId, orderId, statuses);
    }

    @Override
    @Transactional
    public TransactionResponse createPendingTransaction(TransactionRequest request) {
        String lockKey = String.format("transaction:%d", request.userId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            Transaction transaction = transactionRepository.save(request.toEntity());

            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse updateTransactionToSuccess(PayTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(request.transactionId())
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

        String lockKey = String.format("transaction:%d", transaction.getUserId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            PaymentRequest paymentRequest = new PaymentRequest(
                    transaction.getUserId(),
                    transaction.getTransactionId(),
                    transaction.getOrderId(),
                    transaction.getOrderName(),
                    transaction.getAmount()
            );
            ProcessResponse processResponse = paymentServiceClient.processPayment(paymentRequest).getData();
            transaction.updateStatus(processResponse.status()).updatePaymentId(processResponse.paymentId());
            transactionRepository.save(transaction);

            transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse updateTransactionToRefund(UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

        String lockKey = String.format("transaction:%d", transaction.getUserId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            RefundRequest refundRequest = new RefundRequest(
                    request.userId(),
                    transaction.getPaymentId(),
                    request.reason()
            );
            ProcessResponse processResponse = paymentServiceClient.processRefund(refundRequest).getData();
            transaction.updateReason(request.reason())
                    .updateStatus(processResponse.status());
            transactionRepository.save(transaction);

            transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    @Override
    public TransactionResponse updateTransactionToCancellation(UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findByOrderId(request.orderId())
                .orElseThrow(() -> new ApplicationException(TransactionErrorCode.TRANSACTION_NOT_FOUND));

        String lockKey = String.format("transaction:%d", transaction.getUserId());

        try {
            distributedLock.tryLockOrThrow(lockKey);

            if (transaction.getStatus() != Status.PENDING) {
                throw new ApplicationException(TransactionErrorCode.INVALID_TRANSACTION_STATUS);
            }

            transaction.updateReason(request.reason()).updateStatus(Status.CANCELLATION);

//            CancellationRequest cancellationRequest = new CancellationRequest(
//                    request.userId(),
//                    transaction.getPaymentId(),
//                    request.reason()
//            );
//            ProcessResponse processResponse = paymentServiceClient.processCancellation(cancellationRequest).getData();
//            transaction.updateReason(request.reason())
//                    .updateStatus(processResponse.status());
            transactionRepository.save(transaction);

            transactionProvider.sendTransactionStatusMessage(TransactionStatusMessage.from(transaction));
            return TransactionResponse.from(transaction);
        } finally {
            distributedLock.unlock(lockKey);
        }
    }
}