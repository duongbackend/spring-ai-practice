package com.duong.springaipractice.controller;

import com.duong.springaipractice.dto.response.MessageResponse;
import com.duong.springaipractice.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-history")
@RequiredArgsConstructor
public class ChatHistoryController {
    private final ChatHistoryService chatHistoryService;

    @GetMapping("/{conversationId}")
    public List<MessageResponse> getMessagesByConversationId(@PathVariable String conversationId){
        return chatHistoryService.getMessagesByConversationId(conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public void deleteConversationById(@PathVariable String conversationId){
        chatHistoryService.deleteConversationById(conversationId);
    }
}
