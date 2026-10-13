package com.duong.springaipractice.dto.request;

public record QuizRequest(
        String topic,
        String difficulty,
        int numberOfQuestions
) {
}
