<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Banking Chatbot Virtual Assistant - Copilot Instructions

This is a Banking Chatbot & Virtual Assistant project built with Spring Boot backend and React frontend, designed for financial institutions.

## Project Context

This application provides:
- AI-powered banking assistance with NLP processing
- Voice integration capabilities
- Account inquiries, transaction support, and financial advice
- Appointment booking and loan services
- Real-time chat with WebSocket support
- Knowledge base integration

## Architecture Guidelines

### Backend (Spring Boot)
- Use Spring Boot 3.2.0 with Java 17
- Follow RESTful API design principles
- Implement proper error handling and validation
- Use DTOs for data transfer
- Apply banking-grade security practices
- Include comprehensive logging

### Frontend (React + TypeScript)
- Use React 18 with TypeScript for type safety
- Implement Material-UI for consistent design
- Follow React best practices and hooks
- Use proper state management
- Implement responsive design patterns
- Include accessibility features

## Coding Standards

### Java/Spring Boot
```java
// Use proper service layer architecture
@Service
public class ChatbotService {
    // Implement business logic here
}

// Use DTOs for API responses
public class ChatResponse {
    private String message;
    private String intent;
    // ... other fields
}
```

### React/TypeScript
```typescript
// Use functional components with hooks
const ChatInterface: React.FC = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    // Component logic
};

// Define proper interfaces
interface ChatMessage {
    message: string;
    type?: string;
    sessionId?: string;
}
```

## Banking Domain Knowledge

When working with banking features:
- Account balance inquiries
- Transaction history and processing
- Money transfers and payments
- Loan applications and information
- Investment portfolio management
- Appointment scheduling
- Financial advice and recommendations

## NLP and AI Integration

- Intent recognition for banking queries
- Entity extraction (amounts, dates, account types)
- Voice processing capabilities
- Knowledge base search and retrieval
- Conversation context management

## Security Considerations

- Implement JWT authentication
- Use proper input validation
- Apply rate limiting
- Encrypt sensitive data
- Follow banking security standards
- Include fraud detection capabilities

## Testing Approach

- Unit tests for service layers
- Integration tests for APIs
- Frontend component testing
- End-to-end testing for critical flows
- Security testing for vulnerabilities

## Performance Optimization

- Implement caching strategies
- Optimize database queries
- Use lazy loading for components
- Minimize bundle sizes
- Apply compression techniques

## Code Suggestions Priority

1. **Security**: Always prioritize banking-grade security
2. **Performance**: Optimize for real-time chat experience
3. **User Experience**: Focus on intuitive banking interactions
4. **Maintainability**: Write clean, documented code
5. **Scalability**: Design for enterprise-level usage

## Common Patterns

- Use builder pattern for complex DTOs
- Implement service layer abstraction
- Apply dependency injection consistently
- Use reactive programming where appropriate
- Implement proper exception handling

## Integration Guidelines

- RESTful APIs for synchronous operations
- WebSocket for real-time chat
- External banking APIs integration
- Voice processing services
- AI/ML model integration

Remember: This is a portfolio project showcasing modern banking technology capabilities suitable for financial institutions.
