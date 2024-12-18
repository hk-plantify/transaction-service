package com.plantify.transaction.repository;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByUserIdAndOrderIdAndStatusIn(Long userId, String orderId, List<Status> statuses);
    Optional<Transaction> findByOrderId(String orderId);
    List<Transaction> findAllByStatusAndCreatedAtBefore(Status status, LocalDateTime expirationTime);
}