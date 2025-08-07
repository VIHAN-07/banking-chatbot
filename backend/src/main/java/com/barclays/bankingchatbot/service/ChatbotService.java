package com.barclays.bankingchatbot.service;

import com.barclays.bankingchatbot.dto.ChatMessage;
import com.barclays.bankingchatbot.dto.ChatResponse;
import com.barclays.bankingchatbot.dto.Intent;
import com.barclays.bankingchatbot.dto.KnowledgeBaseResult;
import com.barclays.bankingchatbot.service.banking.AccountService;
import com.barclays.bankingchatbot.service.banking.TransactionService;
import com.barclays.bankingchatbot.service.banking.AppointmentService;
import com.barclays.bankingchatbot.service.nlp.IntentRecognitionService;
import com.barclays.bankingchatbot.service.nlp.VoiceProcessingService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {
    
    private final IntentRecognitionService intentRecognitionService;
    private final VoiceProcessingService voiceProcessingService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AppointmentService appointmentService;
    private final KnowledgeBaseService knowledgeBaseService;
    
    public ChatbotService(IntentRecognitionService intentRecognitionService,
                         VoiceProcessingService voiceProcessingService,
                         AccountService accountService,
                         TransactionService transactionService,
                         AppointmentService appointmentService,
                         KnowledgeBaseService knowledgeBaseService) {
        this.intentRecognitionService = intentRecognitionService;
        this.voiceProcessingService = voiceProcessingService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.appointmentService = appointmentService;
        this.knowledgeBaseService = knowledgeBaseService;
    }
    
    public ChatResponse processMessage(ChatMessage message, Principal principal) {
        try {
            // Extract intent from the message
            Intent intent = intentRecognitionService.recognizeIntent(message.getMessage());
            
            // Process based on intent
            String response = processIntent(intent, message, principal);
            
            return ChatResponse.builder()
                    .message(response)
                    .intent(intent.getName())
                    .confidence(intent.getConfidence())
                    .timestamp(LocalDateTime.now())
                    .suggestions(generateSuggestions(intent))
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            return ChatResponse.builder()
                    .message("I'm sorry, I encountered an error processing your request. Please try again.")
                    .intent("error")
                    .confidence(0.0)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
    
    public ChatResponse processVoiceMessage(String audioData, Principal principal) {
        try {
            // Convert speech to text
            String text = voiceProcessingService.speechToText(audioData);
            
            // Process as regular text message
            ChatMessage message = new ChatMessage();
            message.setMessage(text);
            message.setType("voice");
            
            ChatResponse response = processMessage(message, principal);
            
            // Add voice response
            String audioResponse = voiceProcessingService.textToSpeech(response.getMessage());
            response.setAudioResponse(audioResponse);
            
            return response;
            
        } catch (Exception e) {
            System.err.println("Error processing voice message: " + e.getMessage());
            return ChatResponse.builder()
                    .message("I'm sorry, I couldn't process your voice message. Please try typing instead.")
                    .intent("error")
                    .confidence(0.0)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
    
    private String processIntent(Intent intent, ChatMessage message, Principal principal) {
        String userId = principal != null ? principal.getName() : "anonymous";
        
        switch (intent.getName().toLowerCase()) {
            case "account_balance":
                return accountService.getAccountBalance(userId, intent.getEntities());
                
            case "transaction_history":
                return transactionService.getTransactionHistory(userId, intent.getEntities());
                
            case "transfer_money":
                return transactionService.processTransfer(userId, intent.getEntities());
                
            case "book_appointment":
                return appointmentService.bookAppointment(userId, intent.getEntities());
                
            case "financial_advice":
                return provideFinancialAdvice(intent.getEntities());
                
            case "loan_inquiry":
                return accountService.getLoanInformation(userId, intent.getEntities());
                
            case "card_services":
                return accountService.getCardServices(userId, intent.getEntities());
                
            case "investment_info":
                return accountService.getInvestmentInfo(userId, intent.getEntities());
                
            case "greeting":
                return generateGreeting(userId);
                
            case "goodbye":
                return "Thank you for using Banking Virtual Assistant. Have a great day!";
                
            default:
                return handleUnknownIntent(message.getMessage());
        }
    }
    
    private String generateGreeting(String userId) {
        return String.format("Hello! I'm your Banking Virtual Assistant. " +
                "I can help you with account inquiries, transactions, appointments, " +
                "and financial advice. How can I assist you today?");
    }
    
    private String handleUnknownIntent(String message) {
        List<KnowledgeBaseResult> results = knowledgeBaseService.search(message);
        
        if (!results.isEmpty()) {
            return "I found some information that might help: " + results.get(0).getContent() +
                   "\n\nIs this what you were looking for?";
        }
        
        return "I'm not sure I understand. Could you please rephrase your question? " +
               "I can help with account balance, transactions, appointments, and financial advice.";
    }
    
    private String provideFinancialAdvice(Map<String, Object> entities) {
        String topic = (String) entities.get("topic");
        
        if (topic == null) {
            return "I can provide financial advice on various topics including savings, " +
                   "investments, loans, and budgeting. What specific area would you like advice on?";
        }
        
        switch (topic.toLowerCase()) {
            case "savings":
                return "Here are some savings tips: 1) Set up automatic transfers to savings, " +
                       "2) Consider high-yield savings accounts, 3) Review and reduce unnecessary expenses.";
                       
            case "investment":
                return "Investment basics: 1) Diversify your portfolio, 2) Consider your risk tolerance, " +
                       "3) Think long-term, 4) Consider consulting with our investment advisors.";
                       
            case "budgeting":
                return "Budgeting tips: 1) Track your income and expenses, 2) Use the 50/30/20 rule, " +
                       "3) Set realistic financial goals, 4) Review and adjust monthly.";
                       
            default:
                return "I can provide advice on savings, investments, budgeting, loans, and more. " +
                       "What specific financial topic interests you?";
        }
    }
    
    private List<String> generateSuggestions(Intent intent) {
        switch (intent.getName().toLowerCase()) {
            case "greeting":
                return List.of(
                    "Check my account balance",
                    "View recent transactions",
                    "Book an appointment",
                    "Get financial advice"
                );
                
            case "account_balance":
                return List.of(
                    "Show transaction history",
                    "Transfer money",
                    "View savings account",
                    "Check credit card balance"
                );
                
            case "transaction_history":
                return List.of(
                    "Filter by date range",
                    "Export transactions",
                    "Dispute a transaction",
                    "Set up alerts"
                );
                
            default:
                return List.of(
                    "How can I help you?",
                    "What else would you like to know?",
                    "Check account balance",
                    "Book appointment"
                );
        }
    }
    
    public List<Intent> getSupportedIntents() {
        return intentRecognitionService.getSupportedIntents();
    }
    
    public List<KnowledgeBaseResult> searchKnowledgeBase(String query) {
        return knowledgeBaseService.search(query);
    }
}
