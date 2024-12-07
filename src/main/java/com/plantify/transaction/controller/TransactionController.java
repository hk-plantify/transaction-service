package com.plantify.transaction.controller;

import com.plantify.transaction.domain.dto.*;
import com.plantify.transaction.domain.entity.Status;
import com.plantify.transaction.global.response.ApiResponse;
import com.plantify.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // 트랜잭션 조회
    @GetMapping("/{transactionId}")
    public ApiResponse<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        TransactionResponse response = transactionService.getTransactionById(transactionId);
        return ApiResponse.ok(response);
    }

    // 대기 트랜잭션 생성
    @PostMapping
    public ApiResponse<TransactionResponse> createPendingTransaction(@RequestBody PaymentRequest request) {
        TransactionResponse response = transactionService.createPendingTransaction(request);
        return ApiResponse.ok(response);
    }

    // 페이 트랜잭션 생성
    @PostMapping("/payments")
    public ApiResponse<TransactionResponse> createPayTransaction(@RequestBody PaymentRequest request) {
        TransactionResponse response = transactionService.createPayTransaction(request);
        return ApiResponse.ok(response);
    }

    // 트랜잭션 존재 여부 확인
    @GetMapping("/exist")
    public boolean existTransaction(@RequestParam Long userId, @RequestParam Long orderId, @RequestParam List<Status> statusList) {
        return transactionService.existTransaction(userId, orderId, statusList);
    }

    // 환불 트랜잭션 생성
    @PostMapping("/refunds")
    public ApiResponse<TransactionResponse> createRefundTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createRefundTransaction(request);
        return ApiResponse.ok(response);
    }
}
