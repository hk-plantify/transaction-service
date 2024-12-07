package com.plantify.transaction.client;

import com.plantify.transaction.domain.dto.PaymentRequest;
import com.plantify.transaction.domain.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentServiceClient {

    @PostMapping("/v1/payments/process")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);
}