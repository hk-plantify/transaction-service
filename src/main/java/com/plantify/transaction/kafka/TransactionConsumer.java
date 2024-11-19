package com.plantify.transaction.kafka;

import com.plantify.transaction.domain.dto.TransactionRequestMessage;
import com.plantify.transaction.domain.dto.TransactionStatusUpdateMessage;
import com.plantify.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "transaction-request", groupId = "transaction-service-group")
    public void handleTransactionRequest(TransactionRequestMessage message) {
        transactionService.createTransaction(message);
    }

    @KafkaListener(topics = "transaction-status-update", groupId = "transaction-service-group")
    public void handleTransactionStatusUpdate(TransactionStatusUpdateMessage message) {
        transactionService.updateTransactionStatus(message.transactionId(), message.status());
    }
}