package com.duong.springaipractice.dto.request;

public record ChatRequest(
        String message,
        String conversationId
) {
}
