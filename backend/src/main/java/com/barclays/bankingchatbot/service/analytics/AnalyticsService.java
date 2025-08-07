package com.barclays.bankingchatbot.service.analytics;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalyticsService {

    private final Map<String, List<UserInteraction>> userInteractions = new ConcurrentHashMap<>();
    private final Map<String, Integer> intentPopularity = new ConcurrentHashMap<>();
    private final Map<String, Double> sentimentScores = new ConcurrentHashMap<>();

    @Data
    public static class UserInteraction {
        private String userId;
        private String intent;
        private String message;
        private LocalDateTime timestamp;
        private double responseTime;
        private double confidence;
        private String sentiment;
        private boolean successful;

        public UserInteraction(String userId, String intent, String message, double responseTime, 
                             double confidence, String sentiment, boolean successful) {
            this.userId = userId;
            this.intent = intent;
            this.message = message;
            this.timestamp = LocalDateTime.now();
            this.responseTime = responseTime;
            this.confidence = confidence;
            this.sentiment = sentiment;
            this.successful = successful;
        }
    }

    @Data
    public static class AnalyticsReport {
        private long totalInteractions;
        private double averageResponseTime;
        private double successRate;
        private Map<String, Integer> topIntents;
        private Map<String, Double> sentimentDistribution;
        private List<UserInteraction> recentInteractions;
        private double averageConfidence;
    }

    public void recordInteraction(String userId, String intent, String message, 
                                double responseTime, double confidence, boolean successful) {
        String sentiment = analyzeSentiment(message);
        
        UserInteraction interaction = new UserInteraction(
                userId, intent, message, responseTime, confidence, sentiment, successful
        );

        userInteractions.computeIfAbsent(userId, k -> new ArrayList<>()).add(interaction);
        intentPopularity.merge(intent, 1, Integer::sum);
        sentimentScores.put(userId + "_" + System.currentTimeMillis(), 
                          getSentimentScore(sentiment));

        log.info("Recorded interaction for user: {}, intent: {}, success: {}", 
                userId, intent, successful);
    }

    @Cacheable("analyticsReport")
    public AnalyticsReport generateReport() {
        List<UserInteraction> allInteractions = userInteractions.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        AnalyticsReport report = new AnalyticsReport();
        report.setTotalInteractions(allInteractions.size());
        
        if (!allInteractions.isEmpty()) {
            report.setAverageResponseTime(
                    allInteractions.stream()
                            .mapToDouble(UserInteraction::getResponseTime)
                            .average()
                            .orElse(0.0)
            );

            report.setSuccessRate(
                    allInteractions.stream()
                            .mapToDouble(i -> i.isSuccessful() ? 1.0 : 0.0)
                            .average()
                            .orElse(0.0)
            );

            report.setAverageConfidence(
                    allInteractions.stream()
                            .mapToDouble(UserInteraction::getConfidence)
                            .average()
                            .orElse(0.0)
            );

            // Top 5 intents
            report.setTopIntents(
                    intentPopularity.entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .limit(5)
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e1,
                                    LinkedHashMap::new
                            ))
            );

            // Sentiment distribution
            Map<String, Long> sentimentCounts = allInteractions.stream()
                    .collect(Collectors.groupingBy(
                            UserInteraction::getSentiment,
                            Collectors.counting()
                    ));

            report.setSentimentDistribution(
                    sentimentCounts.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> e.getValue().doubleValue() / allInteractions.size()
                            ))
            );

            // Recent interactions (last 10)
            report.setRecentInteractions(
                    allInteractions.stream()
                            .sorted(Comparator.comparing(UserInteraction::getTimestamp).reversed())
                            .limit(10)
                            .collect(Collectors.toList())
            );
        }

        return report;
    }

    public Map<String, Object> getUserInsights(String userId) {
        List<UserInteraction> userSpecificInteractions = userInteractions.get(userId);
        if (userSpecificInteractions == null || userSpecificInteractions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> insights = new HashMap<>();
        insights.put("totalInteractions", userSpecificInteractions.size());
        insights.put("averageConfidence", 
                userSpecificInteractions.stream()
                        .mapToDouble(UserInteraction::getConfidence)
                        .average()
                        .orElse(0.0));
        
        insights.put("mostUsedIntents", 
                userSpecificInteractions.stream()
                        .collect(Collectors.groupingBy(
                                UserInteraction::getIntent,
                                Collectors.counting()
                        )));

        insights.put("sentimentTrend", 
                userSpecificInteractions.stream()
                        .collect(Collectors.groupingBy(
                                UserInteraction::getSentiment,
                                Collectors.counting()
                        )));

        return insights;
    }

    private String analyzeSentiment(String message) {
        // Simple sentiment analysis - in production, use more sophisticated NLP
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("thank") || lowerMessage.contains("great") || 
            lowerMessage.contains("good") || lowerMessage.contains("excellent")) {
            return "positive";
        } else if (lowerMessage.contains("problem") || lowerMessage.contains("issue") || 
                  lowerMessage.contains("bad") || lowerMessage.contains("terrible")) {
            return "negative";
        } else {
            return "neutral";
        }
    }

    private double getSentimentScore(String sentiment) {
        switch (sentiment) {
            case "positive": return 1.0;
            case "negative": return -1.0;
            default: return 0.0;
        }
    }

    public void clearOldData() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        userInteractions.values().forEach(interactions -> 
                interactions.removeIf(interaction -> 
                        interaction.getTimestamp().isBefore(cutoff)));
        
        log.info("Cleared analytics data older than 30 days");
    }
}
