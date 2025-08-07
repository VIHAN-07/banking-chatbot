package com.barclays.bankingchatbot.service.banking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FinancialInsightsService {

    @Data
    public static class FinancialInsight {
        private String category;
        private String insight;
        private String recommendation;
        private Double impact;
        private String priority; // high, medium, low
        private LocalDateTime timestamp;

        public FinancialInsight(String category, String insight, String recommendation, 
                              Double impact, String priority) {
            this.category = category;
            this.insight = insight;
            this.recommendation = recommendation;
            this.impact = impact;
            this.priority = priority;
            this.timestamp = LocalDateTime.now();
        }
    }

    @Data
    public static class SpendingPattern {
        private String category;
        private BigDecimal amount;
        private Double percentage;
        private String trend; // increasing, decreasing, stable
        private String comparison; // above_average, below_average, average
    }

    @Data
    public static class BudgetRecommendation {
        private String category;
        private BigDecimal suggestedLimit;
        private BigDecimal currentSpending;
        private String reasoning;
        private Double potentialSavings;
    }

    @Data
    public static class InvestmentSuggestion {
        private String investmentType;
        private String riskLevel;
        private Double expectedReturn;
        private BigDecimal minimumAmount;
        private String description;
        private List<String> pros;
        private List<String> cons;
    }

    @Cacheable("financialInsights")
    public List<FinancialInsight> generatePersonalizedInsights(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();

        // Spending Analysis
        insights.addAll(analyzeSpendingPatterns(userId));
        
        // Savings Opportunities
        insights.addAll(identifySavingsOpportunities(userId));
        
        // Investment Recommendations
        insights.addAll(generateInvestmentInsights(userId));
        
        // Credit Score Insights
        insights.addAll(analyzeCreditProfile(userId));
        
        // Budget Optimization
        insights.addAll(optimizeBudget(userId));

        return insights.stream()
                .sorted((i1, i2) -> {
                    // Sort by priority: high -> medium -> low
                    Map<String, Integer> priorityOrder = Map.of(
                        "high", 3, "medium", 2, "low", 1
                    );
                    return priorityOrder.get(i2.getPriority()).compareTo(
                           priorityOrder.get(i1.getPriority()));
                })
                .collect(Collectors.toList());
    }

    private List<FinancialInsight> analyzeSpendingPatterns(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();
        
        // Simulate spending analysis
        Map<String, BigDecimal> categorySpending = getSpendingByCategory(userId);
        
        // Find highest spending category
        String topCategory = categorySpending.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("dining");

        BigDecimal topAmount = categorySpending.get(topCategory);
        
        insights.add(new FinancialInsight(
                "spending",
                String.format("Your highest spending category is %s ($%.2f this month)", 
                             topCategory, topAmount),
                String.format("Consider setting a monthly limit for %s to control expenses", topCategory),
                topAmount.doubleValue() * 0.15, // 15% potential savings
                "high"
        ));

        // Identify unusual spending
        if (topAmount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            insights.add(new FinancialInsight(
                    "spending",
                    "Your spending has increased by 25% compared to last month",
                    "Review recent transactions and identify unnecessary expenses",
                    topAmount.doubleValue() * 0.25,
                    "medium"
            ));
        }

        return insights;
    }

    private List<FinancialInsight> identifySavingsOpportunities(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();
        
        insights.add(new FinancialInsight(
                "savings",
                "You have $2,500 in low-yield checking account",
                "Move excess funds to high-yield savings account (2.5% APY vs 0.1%)",
                60.0, // Annual interest difference
                "medium"
        ));

        insights.add(new FinancialInsight(
                "savings",
                "You're not maximizing your 401(k) employer match",
                "Increase contribution to get full employer match - free money!",
                1200.0, // Annual employer match potential
                "high"
        ));

        return insights;
    }

    private List<FinancialInsight> generateInvestmentInsights(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();
        
        // Based on user profile, suggest appropriate investments
        insights.add(new FinancialInsight(
                "investment",
                "You have good emergency fund and stable income",
                "Consider diversified index fund portfolio for long-term growth",
                5000.0, // Potential annual growth
                "medium"
        ));

        insights.add(new FinancialInsight(
                "investment",
                "You're eligible for Roth IRA contributions",
                "Max out Roth IRA ($6,500/year) for tax-free retirement growth",
                325.0, // Annual tax savings
                "high"
        ));

        return insights;
    }

    private List<FinancialInsight> analyzeCreditProfile(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();
        
        insights.add(new FinancialInsight(
                "credit",
                "Your credit utilization is 35% (recommended: under 30%)",
                "Pay down credit card balances or request credit limit increase",
                0.0, // Credit score improvement
                "medium"
        ));

        insights.add(new FinancialInsight(
                "credit",
                "You have excellent payment history (100% on-time payments)",
                "Continue maintaining perfect payment history to keep strong credit score",
                0.0,
                "low"
        ));

        return insights;
    }

    private List<FinancialInsight> optimizeBudget(String userId) {
        List<FinancialInsight> insights = new ArrayList<>();
        
        insights.add(new FinancialInsight(
                "budget",
                "You're following the 50/30/20 rule well",
                "Consider increasing savings rate to 25% for faster financial goals",
                500.0, // Additional monthly savings
                "low"
        ));

        return insights;
    }

    @Cacheable("spendingPatterns")
    public List<SpendingPattern> getSpendingPatterns(String userId) {
        Map<String, BigDecimal> categorySpending = getSpendingByCategory(userId);
        BigDecimal totalSpending = categorySpending.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return categorySpending.entrySet().stream()
                .map(entry -> {
                    SpendingPattern pattern = new SpendingPattern();
                    pattern.setCategory(entry.getKey());
                    pattern.setAmount(entry.getValue());
                    pattern.setPercentage(entry.getValue()
                            .divide(totalSpending, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue());
                    pattern.setTrend(determineSpendingTrend(entry.getKey(), userId));
                    pattern.setComparison(compareToAverage(entry.getKey(), entry.getValue()));
                    return pattern;
                })
                .sorted((p1, p2) -> p2.getAmount().compareTo(p1.getAmount()))
                .collect(Collectors.toList());
    }

    public List<BudgetRecommendation> generateBudgetRecommendations(String userId) {
        List<BudgetRecommendation> recommendations = new ArrayList<>();
        Map<String, BigDecimal> currentSpending = getSpendingByCategory(userId);

        currentSpending.forEach((category, amount) -> {
            BudgetRecommendation rec = new BudgetRecommendation();
            rec.setCategory(category);
            rec.setCurrentSpending(amount);
            rec.setSuggestedLimit(calculateSuggestedLimit(category, amount));
            rec.setReasoning(generateBudgetReasoning(category, amount));
            rec.setPotentialSavings(amount.subtract(rec.getSuggestedLimit()).doubleValue());
            recommendations.add(rec);
        });

        return recommendations;
    }

    public List<InvestmentSuggestion> getPersonalizedInvestmentSuggestions(String userId) {
        List<InvestmentSuggestion> suggestions = new ArrayList<>();
        
        // Conservative suggestion
        InvestmentSuggestion conservative = new InvestmentSuggestion();
        conservative.setInvestmentType("High-Yield Savings Account");
        conservative.setRiskLevel("Very Low");
        conservative.setExpectedReturn(2.5);
        conservative.setMinimumAmount(BigDecimal.valueOf(1000));
        conservative.setDescription("FDIC-insured savings with competitive interest rate");
        conservative.setPros(Arrays.asList("FDIC insured", "Liquid", "Stable returns"));
        conservative.setCons(Arrays.asList("Low returns", "Inflation risk"));
        suggestions.add(conservative);

        // Moderate suggestion
        InvestmentSuggestion moderate = new InvestmentSuggestion();
        moderate.setInvestmentType("S&P 500 Index Fund");
        moderate.setRiskLevel("Moderate");
        moderate.setExpectedReturn(7.0);
        moderate.setMinimumAmount(BigDecimal.valueOf(3000));
        moderate.setDescription("Diversified fund tracking the S&P 500 index");
        moderate.setPros(Arrays.asList("Diversified", "Low fees", "Historical growth"));
        moderate.setCons(Arrays.asList("Market volatility", "No guarantee"));
        suggestions.add(moderate);

        // Aggressive suggestion
        InvestmentSuggestion aggressive = new InvestmentSuggestion();
        aggressive.setInvestmentType("Technology Sector ETF");
        aggressive.setRiskLevel("High");
        aggressive.setExpectedReturn(12.0);
        aggressive.setMinimumAmount(BigDecimal.valueOf(5000));
        aggressive.setDescription("Exchange-traded fund focused on technology companies");
        aggressive.setPros(Arrays.asList("High growth potential", "Innovation exposure"));
        aggressive.setCons(Arrays.asList("High volatility", "Sector concentration risk"));
        suggestions.add(aggressive);

        return suggestions;
    }

    private Map<String, BigDecimal> getSpendingByCategory(String userId) {
        // Simulate user spending data
        Map<String, BigDecimal> spending = new HashMap<>();
        spending.put("dining", BigDecimal.valueOf(850.50));
        spending.put("groceries", BigDecimal.valueOf(650.25));
        spending.put("transportation", BigDecimal.valueOf(420.75));
        spending.put("entertainment", BigDecimal.valueOf(380.00));
        spending.put("utilities", BigDecimal.valueOf(320.50));
        spending.put("shopping", BigDecimal.valueOf(580.30));
        spending.put("healthcare", BigDecimal.valueOf(180.00));
        return spending;
    }

    private String determineSpendingTrend(String category, String userId) {
        // Simulate trend analysis
        return new Random().nextBoolean() ? "increasing" : "stable";
    }

    private String compareToAverage(String category, BigDecimal amount) {
        // Simulate comparison to national averages
        Map<String, BigDecimal> averages = Map.of(
            "dining", BigDecimal.valueOf(600),
            "groceries", BigDecimal.valueOf(500),
            "transportation", BigDecimal.valueOf(400)
        );
        
        BigDecimal average = averages.getOrDefault(category, BigDecimal.valueOf(300));
        if (amount.compareTo(average.multiply(BigDecimal.valueOf(1.2))) > 0) {
            return "above_average";
        } else if (amount.compareTo(average.multiply(BigDecimal.valueOf(0.8))) < 0) {
            return "below_average";
        }
        return "average";
    }

    private BigDecimal calculateSuggestedLimit(String category, BigDecimal currentAmount) {
        // Suggest 10-20% reduction for over-spending categories
        return currentAmount.multiply(BigDecimal.valueOf(0.85));
    }

    private String generateBudgetReasoning(String category, BigDecimal amount) {
        return String.format("Based on your income and financial goals, reducing %s spending could improve your savings rate", category);
    }
}
