package com.duong.springaipractice.dto.response;

import java.util.List;

public record QuizQuestionResponse(
        String question,
        List<String> options,
        String correctAnswer,
        String explanation
) {
}
