package com.barclays.bankingchatbot.controller;

import com.barclays.bankingchatbot.service.analytics.AnalyticsService;
import com.barclays.bankingchatbot.service.banking.FinancialInsightsService;
import com.barclays.bankingchatbot.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/advanced")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Advanced Banking Features", description = "Advanced banking and analytics features")
public class AdvancedFeaturesController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private FinancialInsightsService financialInsightsService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/analytics/report")
    @Operation(summary = "Get comprehensive analytics report")
    public ResponseEntity<?> getAnalyticsReport(Principal principal) {
        try {
            AnalyticsService.AnalyticsReport report = analyticsService.generateReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating analytics report: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/user-insights")
    @Operation(summary = "Get user-specific insights")
    public ResponseEntity<?> getUserInsights(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "anonymous";
            Map<String, Object> insights = analyticsService.getUserInsights(userId);
            return ResponseEntity.ok(insights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting user insights: " + e.getMessage());
        }
    }

    @GetMapping("/financial-insights")
    @Operation(summary = "Get personalized financial insights")
    public ResponseEntity<?> getFinancialInsights(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var insights = financialInsightsService.generatePersonalizedInsights(userId);
            return ResponseEntity.ok(insights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting financial insights: " + e.getMessage());
        }
    }

    @GetMapping("/spending-patterns")
    @Operation(summary = "Get spending patterns analysis")
    public ResponseEntity<?> getSpendingPatterns(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var patterns = financialInsightsService.getSpendingPatterns(userId);
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting spending patterns: " + e.getMessage());
        }
    }

    @GetMapping("/budget-recommendations")
    @Operation(summary = "Get personalized budget recommendations")
    public ResponseEntity<?> getBudgetRecommendations(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var recommendations = financialInsightsService.generateBudgetRecommendations(userId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting budget recommendations: " + e.getMessage());
        }
    }

    @GetMapping("/investment-suggestions")
    @Operation(summary = "Get personalized investment suggestions")
    public ResponseEntity<?> getInvestmentSuggestions(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var suggestions = financialInsightsService.getPersonalizedInvestmentSuggestions(userId);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting investment suggestions: " + e.getMessage());
        }
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<?> getNotifications(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting notifications: " + e.getMessage());
        }
    }

    @GetMapping("/notifications/unread")
    @Operation(summary = "Get unread notifications")
    public ResponseEntity<?> getUnreadNotifications(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            var notifications = notificationService.getUnreadNotifications(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("notifications", notifications);
            response.put("count", notifications.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting unread notifications: " + e.getMessage());
        }
    }

    @PostMapping("/notifications/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable String id) {
        try {
            notificationService.markAsRead(id);
            return ResponseEntity.ok("Notification marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking notification as read: " + e.getMessage());
        }
    }

    @PostMapping("/notifications/mark-all-read")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<?> markAllNotificationsAsRead(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking all notifications as read: " + e.getMessage());
        }
    }

    @PostMapping("/notifications/send-test")
    @Operation(summary = "Send test notification (demo purposes)")
    public ResponseEntity<?> sendTestNotification(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            notificationService.sendTransactionAlert(userId, 
                "Test transaction of $50.00 at Coffee Shop on " + java.time.LocalDate.now());
            return ResponseEntity.ok("Test notification sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending test notification: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard-summary")
    @Operation(summary = "Get comprehensive dashboard summary")
    public ResponseEntity<?> getDashboardSummary(Principal principal) {
        try {
            String userId = principal != null ? principal.getName() : "demo_user";
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("userInsights", analyticsService.getUserInsights(userId));
            summary.put("financialInsights", financialInsightsService.generatePersonalizedInsights(userId));
            summary.put("spendingPatterns", financialInsightsService.getSpendingPatterns(userId));
            summary.put("unreadNotifications", notificationService.getUnreadCount(userId));
            summary.put("lastUpdated", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting dashboard summary: " + e.getMessage());
        }
    }
}
