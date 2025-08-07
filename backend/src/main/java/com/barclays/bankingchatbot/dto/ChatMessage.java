package com.barclays.bankingchatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ChatMessage {
    
    @NotBlank(message = "Message cannot be empty")
    @Size(max = 1000, message = "Message too long")
    private String message;
    
    private String type = "text"; // text, voice, image
    private String sessionId;
    private String userId;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    // Constructors
    public ChatMessage() {}
    
    public ChatMessage(String message, String type, String sessionId, String userId, LocalDateTime timestamp) {
        this.message = message;
        this.type = type;
        this.sessionId = sessionId;
        this.userId = userId;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
