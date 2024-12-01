package com.plantify.transaction.controller;

import com.plantify.transaction.domain.dto.TransactionRequest;
import com.plantify.transaction.domain.dto.PayTransactionResponse;
import com.plantify.transaction.domain.dto.TransactionResponse;
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

    // 페이 트랜잭션 생성
    @PostMapping
    public ApiResponse<PayTransactionResponse> createPayTransaction(@RequestBody TransactionRequest request) {
        PayTransactionResponse response = transactionService.createTransaction(request);
        return ApiResponse.ok(response);
    }

    // 트랜잭션 조회
    @GetMapping("/{transactionId}")
    public ApiResponse<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        TransactionResponse response = transactionService.getTransactionById(transactionId);
        return ApiResponse.ok(response);
    }

    // 트랜잭션 존재 여부 확인
    @GetMapping("/exist")
    public ApiResponse<Boolean> existTransaction(@RequestParam Long userId, @RequestParam List<Status> statusList) {
        boolean exists = transactionService.existTransaction(userId, statusList);
        return ApiResponse.ok(exists);
    }
}
