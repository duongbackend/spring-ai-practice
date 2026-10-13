package com.duong.springaipractice.dto.request;

public record TranslationRequest(
        String language,
        String text
) {
}
