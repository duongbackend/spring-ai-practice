package com.duong.springaipractice.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResponse(
        String role,
        String content,
        Instant createdAt
) {
}
