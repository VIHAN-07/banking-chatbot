package com.barclays.bankingchatbot.service.banking;

import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Service
public class AccountService {
    
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final Random random = new Random();
    
    public String getAccountBalance(String userId, Map<String, Object> entities) {
        // In a real implementation, this would query the database
        // For demo purposes, we'll simulate account data
        
        try {
            String accountType = extractAccountType(entities);
            double balance = simulateAccountBalance(userId, accountType);
            
            if ("all".equals(accountType)) {
                return getAllAccountBalances(userId);
            }
            
            return String.format("Your %s account balance is %s. Is there anything else I can help you with?", 
                               accountType, currencyFormat.format(balance));
                               
        } catch (Exception e) {
            return "I'm sorry, I couldn't retrieve your account balance at the moment. Please try again later.";
        }
    }
    
    public String getLoanInformation(String userId, Map<String, Object> entities) {
        try {
            String loanType = entities.containsKey("loan_type") ? 
                            (String) entities.get("loan_type") : "personal";
            
            switch (loanType.toLowerCase()) {
                case "mortgage":
                case "home":
                    return getMortgageInfo(userId);
                case "personal":
                    return getPersonalLoanInfo(userId);
                case "auto":
                case "car":
                    return getAutoLoanInfo(userId);
                default:
                    return getLoanOptions();
            }
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't retrieve loan information at the moment. Please contact our loan department.";
        }
    }
    
    public String getCardServices(String userId, Map<String, Object> entities) {
        try {
            String serviceType = entities.containsKey("service") ? 
                               (String) entities.get("service") : "info";
            
            switch (serviceType.toLowerCase()) {
                case "block":
                case "freeze":
                    return "I can help you temporarily freeze your card. For security reasons, " +
                           "please call our 24/7 hotline at 1-800-BANKING to block your card immediately.";
                           
                case "new":
                case "replacement":
                    return "I can help you order a replacement card. It typically takes 5-7 business days " +
                           "for delivery. Would you like me to start the process?";
                           
                case "limit":
                    return getCardLimits(userId);
                    
                default:
                    return getCardInfo(userId);
            }
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't access card services at the moment. Please try again later.";
        }
    }
    
    public String getInvestmentInfo(String userId, Map<String, Object> entities) {
        try {
            String investmentType = entities.containsKey("investment_type") ? 
                                  (String) entities.get("investment_type") : "portfolio";
            
            switch (investmentType.toLowerCase()) {
                case "portfolio":
                    return getPortfolioSummary(userId);
                case "stocks":
                    return "Your current stock holdings show a 12.5% gain this year. " +
                           "Would you like to see detailed performance or explore new investment opportunities?";
                case "bonds":
                    return "Your bond portfolio is performing well with a 4.2% yield. " +
                           "Current allocation is 30% government bonds and 70% corporate bonds.";
                default:
                    return "I can provide information about your investments, market updates, " +
                           "or help you explore new investment opportunities. What would you like to know?";
            }
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't retrieve investment information at the moment. " +
                   "Please contact our investment services team.";
        }
    }
    
    private String extractAccountType(Map<String, Object> entities) {
        if (entities.containsKey("account_type")) {
            return (String) entities.get("account_type");
        }
        return "checking"; // default
    }
    
    private double simulateAccountBalance(String userId, String accountType) {
        // Simulate different balances based on account type
        switch (accountType.toLowerCase()) {
            case "savings":
                return 5000 + (random.nextDouble() * 15000);
            case "checking":
                return 1000 + (random.nextDouble() * 5000);
            case "credit":
                return -(random.nextDouble() * 2000); // negative for credit balance
            default:
                return 2500 + (random.nextDouble() * 7500);
        }
    }
    
    private String getAllAccountBalances(String userId) {
        double checking = simulateAccountBalance(userId, "checking");
        double savings = simulateAccountBalance(userId, "savings");
        double credit = Math.abs(simulateAccountBalance(userId, "credit"));
        
        return String.format("Here are your account balances:\n" +
                           "• Checking: %s\n" +
                           "• Savings: %s\n" +
                           "• Credit Card: %s available credit\n" +
                           "Total assets: %s",
                           currencyFormat.format(checking),
                           currencyFormat.format(savings),
                           currencyFormat.format(credit),
                           currencyFormat.format(checking + savings));
    }
    
    private String getMortgageInfo(String userId) {
        return "Your current mortgage details:\n" +
               "• Remaining balance: $284,567\n" +
               "• Monthly payment: $1,892\n" +
               "• Interest rate: 3.25%\n" +
               "• Next payment due: December 15th\n" +
               "Would you like to explore refinancing options?";
    }
    
    private String getPersonalLoanInfo(String userId) {
        return "Personal loan options available:\n" +
               "• Amount: $5,000 - $50,000\n" +
               "• APR: Starting at 5.99%\n" +
               "• Terms: 2-7 years\n" +
               "• Quick approval process\n" +
               "Would you like to apply or get a personalized quote?";
    }
    
    private String getAutoLoanInfo(String userId) {
        return "Auto loan options:\n" +
               "• New cars: APR starting at 2.99%\n" +
               "• Used cars: APR starting at 3.49%\n" +
               "• Terms: Up to 84 months\n" +
               "• Pre-approval available\n" +
               "Would you like to get pre-approved?";
    }
    
    private String getLoanOptions() {
        return "Available loan products:\n" +
               "• Personal loans\n" +
               "• Mortgages\n" +
               "• Auto loans\n" +
               "• Home equity loans\n" +
               "Which type interests you?";
    }
    
    private String getCardLimits(String userId) {
        return "Your card limits:\n" +
               "• Credit limit: $15,000\n" +
               "• Available credit: $12,340\n" +
               "• Daily ATM limit: $500\n" +
               "• Daily purchase limit: $5,000\n" +
               "Would you like to request a limit increase?";
    }
    
    private String getCardInfo(String userId) {
        return "Your Premium Banking Card:\n" +
               "• Card ending in 4567\n" +
               "• 2% cashback on all purchases\n" +
               "• No annual fee\n" +
               "• Fraud protection enabled\n" +
               "What can I help you with regarding your card?";
    }
    
    private String getPortfolioSummary(String userId) {
        return "Your investment portfolio summary:\n" +
               "• Total value: $125,430\n" +
               "• YTD return: +8.7%\n" +
               "• Asset allocation: 60% stocks, 30% bonds, 10% cash\n" +
               "• Top performer: Technology sector (+15.2%)\n" +
               "Would you like detailed analysis or rebalancing recommendations?";
    }
}
