package com.plantify.transaction.domain.dto;

public record RefundRequest (
        Long userId,
        Long paymentId,
        String reason
)  {
}
