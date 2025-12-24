package com.plantify.transaction.domain.dto.request;

public record RefundRequest (
        Long paymentId,
        String reason
)  {
}
