package com.barclays.bankingchatbot.service.nlp;

import com.barclays.bankingchatbot.dto.Intent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IntentRecognitionService {
    
    private final Map<String, List<String>> intentPatterns;
    private final Map<String, Pattern> entityPatterns;
    
    public IntentRecognitionService() {
        this.intentPatterns = initializeIntentPatterns();
        this.entityPatterns = initializeEntityPatterns();
    }
    
    public Intent recognizeIntent(String message) {
        String normalizedMessage = message.toLowerCase().trim();
        
        // Check for each intent pattern
        for (Map.Entry<String, List<String>> entry : intentPatterns.entrySet()) {
            String intentName = entry.getKey();
            List<String> patterns = entry.getValue();
            
            for (String pattern : patterns) {
                if (normalizedMessage.contains(pattern.toLowerCase())) {
                    Map<String, Object> entities = extractEntities(message);
                    double confidence = calculateConfidence(normalizedMessage, pattern);
                    
                    return new Intent(intentName, confidence, entities);
                }
            }
        }
        
        // Default to unknown intent
        return new Intent("unknown", 0.1, extractEntities(message));
    }
    
    private Map<String, List<String>> initializeIntentPatterns() {
        Map<String, List<String>> patterns = new HashMap<>();
        
        patterns.put("greeting", Arrays.asList(
            "hello", "hi", "hey", "good morning", "good afternoon", "good evening"
        ));
        
        patterns.put("goodbye", Arrays.asList(
            "bye", "goodbye", "see you", "thanks", "thank you", "exit", "quit"
        ));
        
        patterns.put("account_balance", Arrays.asList(
            "balance", "account balance", "how much money", "check balance", "current balance"
        ));
        
        patterns.put("transaction_history", Arrays.asList(
            "transaction", "transactions", "history", "recent transactions", "statement", "activity"
        ));
        
        patterns.put("transfer_money", Arrays.asList(
            "transfer", "send money", "pay", "payment", "wire transfer", "move money"
        ));
        
        patterns.put("book_appointment", Arrays.asList(
            "appointment", "book appointment", "schedule", "meeting", "visit branch"
        ));
        
        patterns.put("financial_advice", Arrays.asList(
            "advice", "financial advice", "investment", "savings", "budget", "financial planning"
        ));
        
        patterns.put("loan_inquiry", Arrays.asList(
            "loan", "mortgage", "credit", "borrow", "lending", "personal loan"
        ));
        
        patterns.put("card_services", Arrays.asList(
            "card", "credit card", "debit card", "block card", "card services", "new card"
        ));
        
        patterns.put("investment_info", Arrays.asList(
            "investment", "stocks", "bonds", "portfolio", "mutual funds", "trading"
        ));
        
        return patterns;
    }
    
    private Map<String, Pattern> initializeEntityPatterns() {
        Map<String, Pattern> patterns = new HashMap<>();
        
        patterns.put("amount", Pattern.compile("\\$?([0-9,]+\\.?[0-9]*)", Pattern.CASE_INSENSITIVE));
        patterns.put("account_number", Pattern.compile("\\b\\d{8,12}\\b"));
        patterns.put("date", Pattern.compile("\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b"));
        patterns.put("phone", Pattern.compile("\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b"));
        patterns.put("email", Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"));
        
        return patterns;
    }
    
    private Map<String, Object> extractEntities(String message) {
        Map<String, Object> entities = new HashMap<>();
        
        for (Map.Entry<String, Pattern> entry : entityPatterns.entrySet()) {
            String entityType = entry.getKey();
            Pattern pattern = entry.getValue();
            
            Matcher matcher = pattern.matcher(message);
            List<String> matches = new ArrayList<>();
            
            while (matcher.find()) {
                matches.add(matcher.group());
            }
            
            if (!matches.isEmpty()) {
                if (matches.size() == 1) {
                    entities.put(entityType, matches.get(0));
                } else {
                    entities.put(entityType, matches);
                }
            }
        }
        
        return entities;
    }
    
    private double calculateConfidence(String message, String pattern) {
        // Simple confidence calculation based on pattern match
        if (message.contains(pattern.toLowerCase())) {
            // Higher confidence for exact matches
            if (message.equals(pattern.toLowerCase())) {
                return 0.95;
            }
            // Medium confidence for partial matches
            return 0.7 + (pattern.length() / (double) message.length()) * 0.2;
        }
        return 0.1;
    }
    
    public List<Intent> getSupportedIntents() {
        List<Intent> intents = new ArrayList<>();
        
        for (String intentName : intentPatterns.keySet()) {
            Intent intent = new Intent(intentName, 1.0, new HashMap<>());
            intents.add(intent);
        }
        
        return intents;
    }
}
