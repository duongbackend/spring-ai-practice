package com.duong.springaipractice.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(
        Long id,
        String orderCode,
        String productName,
        BigDecimal amount,
        String status,
        String shippingAddress,
        LocalDateTime createdAt
) {
}
