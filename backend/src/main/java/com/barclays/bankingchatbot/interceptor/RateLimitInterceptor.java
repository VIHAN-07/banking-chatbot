package com.barclays.bankingchatbot.interceptor;

import com.barclays.bankingchatbot.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Principal;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    @Autowired
    private RateLimitConfig rateLimitConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientId = getClientIdentifier(request);
        String requestType = determineRequestType(request);
        String bucketKey = requestType + "_" + clientId;

        Bucket bucket = rateLimitConfig.resolveBucket(bucketKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            
            log.warn("Rate limit exceeded for client: {} on endpoint: {}", clientId, request.getRequestURI());
            return false;
        }
    }

    private String getClientIdentifier(HttpServletRequest request) {
        // Try to get user ID from Principal
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return principal.getName();
        }
        
        // Fall back to IP address
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        return request.getRemoteAddr();
    }

    private String determineRequestType(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        if (uri.contains("/voice")) {
            return "voice";
        } else if (uri.contains("/transfer")) {
            return "transfer";
        } else if (uri.contains("/chat")) {
            return "chat";
        } else {
            return "general";
        }
    }
}
