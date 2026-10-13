package com.duong.springaipractice.controller;

import com.duong.springaipractice.dto.request.ChatRequest;
import com.duong.springaipractice.dto.request.TranslationRequest;
import com.duong.springaipractice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public String chat(@RequestBody ChatRequest request){
        return chatService.chat(request);
    }

    @PostMapping("/translation")
    public String translation(@RequestBody TranslationRequest request){
        return chatService.translation(request);
    }
}
