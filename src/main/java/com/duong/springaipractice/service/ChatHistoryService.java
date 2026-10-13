package com.duong.springaipractice.service;

import com.duong.springaipractice.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j(topic = "CHAT-HISTORY-SERVICE")
public class ChatHistoryService {
    private final ChatMemory chatMemory;

    public ChatHistoryService(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    public List<MessageResponse> getMessagesByConversationId(String conversationId){
        List<Message> messages = chatMemory.get(conversationId);

        return messages.stream().map(message -> new MessageResponse(
                message.getMessageType().getValue(),
                message.getText(),
                (Instant) message.getMetadata().get(JdbcChatMemoryRepository.CONVERSATION_TS)
        )).toList();
    }

    public void deleteConversationById(String conversationId){
        chatMemory.clear(conversationId);
    }
}
