package com.plantify.transaction.client;

import com.plantify.transaction.domain.dto.PaymentResponse;
import com.plantify.transaction.domain.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentServiceClient {

    @PostMapping("/payments/process")
    PaymentResponse processPayment(@RequestBody TransactionRequest request);
}