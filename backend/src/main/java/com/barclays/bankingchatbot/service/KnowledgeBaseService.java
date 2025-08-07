package com.barclays.bankingchatbot.service;

import com.barclays.bankingchatbot.dto.KnowledgeBaseResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KnowledgeBaseService {
    
    private final Map<String, KnowledgeBaseResult> knowledgeBase;
    
    public KnowledgeBaseService() {
        this.knowledgeBase = initializeKnowledgeBase();
    }
    
    public List<KnowledgeBaseResult> search(String query) {
        List<KnowledgeBaseResult> results = new ArrayList<>();
        String normalizedQuery = query.toLowerCase();
        
        for (KnowledgeBaseResult article : knowledgeBase.values()) {
            double relevanceScore = calculateRelevance(normalizedQuery, article);
            
            if (relevanceScore > 0.1) {
                article.setRelevanceScore(relevanceScore);
                results.add(article);
            }
        }
        
        // Sort by relevance score
        results.sort((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()));
        
        // Return top 5 results
        return results.subList(0, Math.min(5, results.size()));
    }
    
    private Map<String, KnowledgeBaseResult> initializeKnowledgeBase() {
        Map<String, KnowledgeBaseResult> kb = new HashMap<>();
        
        // Banking basics
        kb.put("account_types", new KnowledgeBaseResult(
            "account_types",
            "Types of Bank Accounts",
            "We offer several account types: Checking accounts for daily transactions, " +
            "Savings accounts for earning interest, Money Market accounts for higher yields, " +
            "and CD accounts for fixed-term investments.",
            "Banking Basics",
            0.0
        ));
        
        kb.put("online_banking", new KnowledgeBaseResult(
            "online_banking",
            "Online Banking Features",
            "Access your accounts 24/7 through Online Banking. Features include: " +
            "balance checking, bill pay, transfers, mobile check deposit, and account alerts.",
            "Digital Services",
            0.0
        ));
        
        kb.put("fees", new KnowledgeBaseResult(
            "fees",
            "Account Fees and Charges",
            "Our fee structure includes: Monthly maintenance fees (waived with minimum balance), " +
            "ATM fees for non-network usage, overdraft fees, and wire transfer charges. " +
            "Many fees can be avoided with proper account management.",
            "Fees and Charges",
            0.0
        ));
        
        kb.put("security", new KnowledgeBaseResult(
            "security",
            "Banking Security Measures",
            "Your security is our priority. We use multi-factor authentication, " +
            "encryption, fraud monitoring, and secure login protocols. " +
            "Never share your login credentials and always log out after banking sessions.",
            "Security",
            0.0
        ));
        
        kb.put("loans", new KnowledgeBaseResult(
            "loans",
            "Loan Products and Requirements",
            "We offer personal loans, auto loans, mortgages, and business loans. " +
            "Requirements typically include good credit score, stable income, " +
            "and debt-to-income ratio evaluation.",
            "Lending",
            0.0
        ));
        
        kb.put("credit_cards", new KnowledgeBaseResult(
            "credit_cards",
            "Credit Card Benefits",
            "Our credit cards offer cashback rewards, travel benefits, purchase protection, " +
            "and fraud protection. Choose from various cards based on your spending patterns " +
            "and reward preferences.",
            "Credit Cards",
            0.0
        ));
        
        kb.put("investments", new KnowledgeBaseResult(
            "investments",
            "Investment Services",
            "Our Wealth Management offers investment advisory services, " +
            "portfolio management, retirement planning, and access to stocks, bonds, " +
            "mutual funds, and ETFs.",
            "Investments",
            0.0
        ));
        
        kb.put("mobile_app", new KnowledgeBaseResult(
            "mobile_app",
            "Mobile Banking App",
            "Download our mobile banking app for convenient banking on-the-go. " +
            "Features include mobile check deposit, account management, bill pay, " +
            "and branch/ATM locator.",
            "Digital Services",
            0.0
        ));
        
        kb.put("customer_service", new KnowledgeBaseResult(
            "customer_service",
            "Customer Service Hours",
            "Customer service is available 24/7 at 1-800-BANKING. " +
            "Live chat is available during business hours. " +
            "Branch locations have varying hours - use our branch locator for specific times.",
            "Support",
            0.0
        ));
        
        kb.put("fraud_protection", new KnowledgeBaseResult(
            "fraud_protection",
            "Fraud Protection and Reporting",
            "If you suspect fraud, contact us immediately at 1-800-BANKING. " +
            "We monitor transactions 24/7 and will alert you of suspicious activity. " +
            "You're protected by zero liability for unauthorized transactions.",
            "Security",
            0.0
        ));
        
        return kb;
    }
    
    private double calculateRelevance(String query, KnowledgeBaseResult article) {
        double score = 0.0;
        
        String[] queryWords = query.split("\\s+");
        String articleText = (article.getTitle() + " " + article.getContent()).toLowerCase();
        
        for (String word : queryWords) {
            if (word.length() > 2) { // Ignore very short words
                if (articleText.contains(word)) {
                    score += 0.1;
                    
                    // Higher weight for title matches
                    if (article.getTitle().toLowerCase().contains(word)) {
                        score += 0.2;
                    }
                }
            }
        }
        
        // Boost score for category matches
        if (article.getCategory().toLowerCase().contains(query)) {
            score += 0.3;
        }
        
        return Math.min(1.0, score); // Cap at 1.0
    }
}
