package com.barclays.bankingchatbot.dto;

import java.util.Map;

public class Intent {
    
    private String name;
    private Double confidence;
    private Map<String, Object> entities;
    
    public Intent() {}
    
    public Intent(String name, Double confidence, Map<String, Object> entities) {
        this.name = name;
        this.confidence = confidence;
        this.entities = entities;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    
    public Map<String, Object> getEntities() { return entities; }
    public void setEntities(Map<String, Object> entities) { this.entities = entities; }
}
