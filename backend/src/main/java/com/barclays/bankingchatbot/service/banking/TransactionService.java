package com.barclays.bankingchatbot.service.banking;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class TransactionService {
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    public String getTransactionHistory(String userId, Map<String, Object> entities) {
        try {
            String period = extractTimePeriod(entities);
            return generateTransactionHistory(userId, period);
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't retrieve your transaction history at the moment. Please try again later.";
        }
    }
    
    public String processTransfer(String userId, Map<String, Object> entities) {
        try {
            String amount = extractAmount(entities);
            String recipient = extractRecipient(entities);
            
            if (amount == null) {
                return "How much would you like to transfer? Please specify the amount.";
            }
            
            if (recipient == null) {
                return String.format("I can help you transfer %s. Who would you like to send money to? " +
                                   "You can transfer to your other accounts or to external recipients.", amount);
            }
            
            return processTransferRequest(userId, amount, recipient);
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't process the transfer request at the moment. " +
                   "For immediate assistance, please visit our website or call customer service.";
        }
    }
    
    private String extractTimePeriod(Map<String, Object> entities) {
        if (entities.containsKey("time_period")) {
            return (String) entities.get("time_period");
        }
        return "recent"; // default to recent transactions
    }
    
    private String extractAmount(Map<String, Object> entities) {
        if (entities.containsKey("amount")) {
            Object amount = entities.get("amount");
            if (amount instanceof String) {
                return (String) amount;
            }
        }
        return null;
    }
    
    private String extractRecipient(Map<String, Object> entities) {
        if (entities.containsKey("recipient")) {
            return (String) entities.get("recipient");
        }
        return null;
    }
    
    private String generateTransactionHistory(String userId, String period) {
        StringBuilder history = new StringBuilder();
        
        switch (period.toLowerCase()) {
            case "today":
                history.append("Today's transactions:\n");
                history.append("• Starbucks Coffee - $4.25 (10:30 AM)\n");
                history.append("• Gas Station - $45.00 (2:15 PM)\n");
                history.append("• Online Transfer to Savings - $200.00 (4:00 PM)\n");
                break;
                
            case "week":
            case "weekly":
                history.append("This week's transactions:\n");
                history.append("• Grocery Store - $127.83 (").append(LocalDate.now().minusDays(2).format(dateFormatter)).append(")\n");
                history.append("• Electric Bill - $89.50 (").append(LocalDate.now().minusDays(3).format(dateFormatter)).append(")\n");
                history.append("• Restaurant - $56.40 (").append(LocalDate.now().minusDays(1).format(dateFormatter)).append(")\n");
                history.append("• ATM Withdrawal - $100.00 (").append(LocalDate.now().format(dateFormatter)).append(")\n");
                break;
                
            default: // recent
                history.append("Recent transactions:\n");
                history.append("• Starbucks Coffee - $4.25 (Today 10:30 AM)\n");
                history.append("• Online Purchase - $79.99 (Yesterday)\n");
                history.append("• Grocery Store - $127.83 (").append(LocalDate.now().minusDays(2).format(dateFormatter)).append(")\n");
                history.append("• Salary Deposit - $3,500.00 (").append(LocalDate.now().minusDays(3).format(dateFormatter)).append(")\n");
                history.append("• Rent Payment - $1,200.00 (").append(LocalDate.now().minusDays(4).format(dateFormatter)).append(")\n");
        }
        
        history.append("\nWould you like to see more details or filter by category?");
        return history.toString();
    }
    
    private String processTransferRequest(String userId, String amount, String recipient) {
        // In a real implementation, this would:
        // 1. Validate the transfer amount
        // 2. Check account balance
        // 3. Verify recipient details
        // 4. Process the transfer
        // 5. Send confirmation
        
        return String.format("Transfer request summary:\n" +
                           "• Amount: %s\n" +
                           "• To: %s\n" +
                           "• From: Your checking account\n\n" +
                           "For security reasons, please confirm this transfer through our mobile app " +
                           "or by calling our secure line. You'll receive a confirmation code to complete the transaction.",
                           amount, recipient);
    }
}
