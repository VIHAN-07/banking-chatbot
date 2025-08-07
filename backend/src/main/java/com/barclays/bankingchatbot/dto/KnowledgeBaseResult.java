package com.barclays.bankingchatbot.dto;

public class KnowledgeBaseResult {
    
    private String id;
    private String title;
    private String content;
    private String category;
    private Double relevanceScore;
    
    public KnowledgeBaseResult() {}
    
    public KnowledgeBaseResult(String id, String title, String content, String category, Double relevanceScore) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.relevanceScore = relevanceScore;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
}
