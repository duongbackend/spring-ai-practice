package com.duong.springaipractice.service;

import com.duong.springaipractice.dto.request.ChatRequest;
import com.duong.springaipractice.dto.request.QuizRequest;
import com.duong.springaipractice.dto.request.TranslationRequest;
import com.duong.springaipractice.dto.response.CourseQuizResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j(topic = "CHAT-SERVICE")
public class ChatService {
    @Value("classpath:prompts/system-message.st")
    private Resource systemResource;

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    public String chat(ChatRequest request){
        String conversationId = "1";
        SystemMessage systemMessage = new SystemMessage("Bạn là AI Assistant của BackendService, trả lời dựa trên ngôn ngữ mà người dùng sử dụng");
        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public String translation(TranslationRequest request){
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder()
                        .startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .template("Hãy dịch đoạn văn bản sau sang <language>: <text>")
                .build();

        String prompt = promptTemplate.render(
                Map.of(
                       "language", request.language(),
                       "text", request.text()
                )
        );
        return chatClient.prompt(prompt)
                .system(systemResource)
                .call()
                .content();
    }

    public CourseQuizResponse generateCourseQuiz(QuizRequest request){
        String conversationId = "2";
        String template = """
                Bạn là một chuyên gia giáo dục. Hãy tạo ra một bộ câu hỏi trắc nghiệm (quiz) chất lượng cao bằng tiếng Việt về chủ đề: "<topic>".
                Yêu cầu:
                - Độ khó mong muốn: <difficulty>
                - Số lượng câu hỏi: <numberOfQuestions> câu hỏi.
                - Đối với mỗi câu hỏi, hãy cung cấp đúng 4 phương án lựa chọn (A, B, C, D), xác định một đáp án đúng
                  duy nhất (chỉ trả về một trong các chữ cái viết hoa "A", "B", "C" hoặc "D") và phần giải thích (explanation)
                  lý do tại sao đáp án đó đúng một cách cặn kẽ.
                - Tự động đặt một tiêu đề ngắn gọn, phù hợp cho bài học (lessonTitle) dựa trên chủ đề được cung cấp.
                """;
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder()
                        .startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .template(template)
                .build();

        String prompt = promptTemplate.render(Map.of(
                "topic", request.topic(),
                "difficulty", request.difficulty(),
                "numberOfQuestions", request.numberOfQuestions()
        ));

        return chatClient.prompt(prompt)
                .system(systemResource)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(CourseQuizResponse.class);
    }
}
