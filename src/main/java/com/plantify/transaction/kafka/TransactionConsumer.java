package com.plantify.transaction.kafka;

import com.plantify.transaction.domain.dto.TransactionStatusMessage;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.service.TransactionStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.plantify.transaction.domain.entity.Status.SUCCESS;
import static javax.management.remote.JMXConnectionNotification.FAILED;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final TransactionStatusService transactionStatusService;

    @KafkaListener(
            topics = "${spring.kafka.topic.transaction-status}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleTransactionStatus(TransactionStatusMessage message) {
        try {
            switch (message.status()) {
                case "SUCCESS" -> transactionStatusService.processSuccessfulTransaction(message);
                case "FAILED" -> transactionStatusService.processFailedTransaction(message);
                default -> log.warn("Unknown status: {}", message.status());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}