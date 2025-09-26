package com.example.chatbot.controller;

import com.example.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String answer = chatbotService.getAnswer(prompt);
        return Map.of("reply", answer);
    }
}
