package com.duong.springaipractice.dto.response;

import java.util.List;

public record CourseQuizResponse(
        String lessonTitle,
        List<QuizQuestionResponse> questions
) {
}
