package com.plantify.transaction.repository;

import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByUserIdAndOrderIdAndStatusIn(Long userId, Long orderId, List<Status> statuses);
}