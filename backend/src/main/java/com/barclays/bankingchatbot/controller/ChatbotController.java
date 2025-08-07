package com.barclays.bankingchatbot.controller;

import com.barclays.bankingchatbot.dto.ChatMessage;
import com.barclays.bankingchatbot.dto.ChatResponse;
import com.barclays.bankingchatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatbotController {
    
    private final ChatbotService chatbotService;
    
    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }
    
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> processMessage(
            @Valid @RequestBody ChatMessage message,
            Principal principal) {
        
        System.out.println("Processing chat message from user: " + (principal != null ? principal.getName() : "anonymous"));
        
        ChatResponse response = chatbotService.processMessage(message, principal);
        return ResponseEntity.ok(response);
    }
    
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatResponse handleWebSocketMessage(ChatMessage message) {
        System.out.println("Processing WebSocket message: " + message.getMessage());
        return chatbotService.processMessage(message, null);
    }
    
    @GetMapping("/intents")
    public ResponseEntity<?> getSupportedIntents() {
        return ResponseEntity.ok(chatbotService.getSupportedIntents());
    }
    
    @PostMapping("/voice")
    public ResponseEntity<ChatResponse> processVoiceMessage(
            @RequestParam("audio") String audioData,
            Principal principal) {
        
        System.out.println("Processing voice message from user: " + (principal != null ? principal.getName() : "anonymous"));
        
        ChatResponse response = chatbotService.processVoiceMessage(audioData, principal);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/knowledge-base")
    public ResponseEntity<?> searchKnowledgeBase(@RequestParam String query) {
        return ResponseEntity.ok(chatbotService.searchKnowledgeBase(query));
    }
}
