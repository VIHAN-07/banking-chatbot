package com.barclays.bankingchatbot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {
    
    private String message;
    private String intent;
    private Double confidence;
    private LocalDateTime timestamp;
    private List<String> suggestions;
    private String audioResponse; // base64 encoded audio for voice response
    private boolean requiresAction;
    private String actionType; // redirect, form, confirmation
    private Object actionData;
    
    // Constructors
    public ChatResponse() {}
    
    public ChatResponse(String message, String intent, Double confidence, LocalDateTime timestamp, 
                       List<String> suggestions, String audioResponse, boolean requiresAction, 
                       String actionType, Object actionData) {
        this.message = message;
        this.intent = intent;
        this.confidence = confidence;
        this.timestamp = timestamp;
        this.suggestions = suggestions;
        this.audioResponse = audioResponse;
        this.requiresAction = requiresAction;
        this.actionType = actionType;
        this.actionData = actionData;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    
    public String getAudioResponse() { return audioResponse; }
    public void setAudioResponse(String audioResponse) { this.audioResponse = audioResponse; }
    
    public boolean isRequiresAction() { return requiresAction; }
    public void setRequiresAction(boolean requiresAction) { this.requiresAction = requiresAction; }
    
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    
    public Object getActionData() { return actionData; }
    public void setActionData(Object actionData) { this.actionData = actionData; }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String message;
        private String intent;
        private Double confidence;
        private LocalDateTime timestamp;
        private List<String> suggestions;
        private String audioResponse;
        private boolean requiresAction;
        private String actionType;
        private Object actionData;
        
        public Builder message(String message) { this.message = message; return this; }
        public Builder intent(String intent) { this.intent = intent; return this; }
        public Builder confidence(Double confidence) { this.confidence = confidence; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder suggestions(List<String> suggestions) { this.suggestions = suggestions; return this; }
        public Builder audioResponse(String audioResponse) { this.audioResponse = audioResponse; return this; }
        public Builder requiresAction(boolean requiresAction) { this.requiresAction = requiresAction; return this; }
        public Builder actionType(String actionType) { this.actionType = actionType; return this; }
        public Builder actionData(Object actionData) { this.actionData = actionData; return this; }
        
        public ChatResponse build() {
            return new ChatResponse(message, intent, confidence, timestamp, suggestions, 
                                  audioResponse, requiresAction, actionType, actionData);
        }
    }
}
