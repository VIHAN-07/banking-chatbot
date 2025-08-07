# Banking Chatbot Virtual Assistant

A comprehensive Banking Chatbot & Virtual Assistant built with **Spring Boot** backend and **React** frontend, designed to showcase modern banking technology solutions for financial institutions.

## 🚀 Features

### Core Banking Features
- **Account Inquiries**: Check balances, account information, and statements
- **Transaction Support**: View transaction history, process transfers, and payments
- **Financial Advice**: Personalized financial recommendations and tips
- **Appointment Booking**: Schedule meetings with financial advisors
- **Loan Services**: Loan inquiries, applications, and information
- **Investment Information**: Portfolio management and investment advice

### Advanced AI Capabilities
- **Natural Language Processing (NLP)**: Intent recognition and entity extraction
- **Voice Integration**: Speech-to-text and text-to-speech capabilities
- **Knowledge Base**: Intelligent search and FAQ responses
- **Multi-turn Conversations**: Context-aware dialogue management
- **Sentiment Analysis**: Customer emotion detection and response adaptation

### Technical Features
- **Real-time Chat**: WebSocket-based messaging
- **Voice Commands**: Browser-based speech recognition
- **Responsive Design**: Mobile-first UI with Material-UI
- **Security**: JWT authentication and banking-grade security
- **API Documentation**: Swagger/OpenAPI integration
- **Monitoring**: Actuator endpoints and health checks

## 🏗️ Architecture

### Backend (Spring Boot)
```
backend/
├── src/main/java/com/barclays/bankingchatbot/
│   ├── controller/          # REST API controllers
│   ├── service/            # Business logic services
│   │   ├── banking/        # Banking-specific services
│   │   └── nlp/           # NLP processing services
│   ├── dto/               # Data Transfer Objects
│   └── BankingChatbotApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

### Frontend (React + TypeScript)
```
frontend/
├── src/
│   ├── components/        # React components
│   ├── services/         # API service layers
│   ├── App.tsx          # Main application component
│   └── index.tsx        # Application entry point
├── public/
└── package.json
```

## 🛠️ Technology Stack

### Backend Technologies
- **Spring Boot 3.2.0** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Spring WebSocket** - Real-time communication
- **H2/PostgreSQL** - Database options
- **Stanford CoreNLP** - Natural language processing
- **Spring AI** - AI integration capabilities
- **Maven** - Dependency management

### Frontend Technologies
- **React 18** - UI framework
- **TypeScript** - Type safety
- **Material-UI (MUI)** - Component library
- **Framer Motion** - Animations
- **Axios** - HTTP client
- **Socket.IO** - Real-time communication
- **React Speech Kit** - Voice capabilities

## 📋 Prerequisites

- **Java 17+**
- **Node.js 16+**
- **Maven 3.6+**
- **npm or yarn**

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd banking-chatbot-virtual-assistant
```

### 2. Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup
```bash
cd frontend
npm install
npm start
```

The frontend will start on `http://localhost:3000`

### 4. Access the Application
- **Chatbot Interface**: http://localhost:3000
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

## 🔧 Configuration

### Environment Variables
Create a `.env` file in the backend directory:
```properties
OPENAI_API_KEY=your-openai-api-key
DATABASE_URL=jdbc:postgresql://localhost:5432/bankingchatbot
DATABASE_USERNAME=username
DATABASE_PASSWORD=password
```

### Frontend Environment
Create a `.env` file in the frontend directory:
```properties
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_WS_URL=ws://localhost:8080/ws
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📝 API Documentation

### Chat Endpoints
- `POST /api/chatbot/chat` - Send text message
- `POST /api/chatbot/voice` - Send voice message
- `GET /api/chatbot/intents` - Get supported intents
- `GET /api/chatbot/knowledge-base` - Search knowledge base

### Banking Endpoints
- `GET /api/banking/accounts` - Get account information
- `GET /api/banking/transactions` - Get transaction history
- `POST /api/banking/transfer` - Process money transfer
- `POST /api/banking/appointments` - Book appointment

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **CORS Configuration**: Cross-origin resource sharing setup
- **Input Validation**: Comprehensive request validation
- **Rate Limiting**: API rate limiting for protection
- **Encryption**: Data encryption at rest and in transit

## 🎯 Banking Use Cases

### 1. Account Management
```
User: "What's my account balance?"
Bot: "Your checking account balance is $2,847.50. Your savings account has $15,230.75."
```

### 2. Transaction Inquiry
```
User: "Show me transactions from last week"
Bot: "Here are your transactions from last week: [transaction list]"
```

### 3. Money Transfer
```
User: "Transfer $500 to my savings account"
Bot: "I can help you transfer $500 to your savings account. Please confirm this transaction."
```

### 4. Appointment Booking
```
User: "Book an appointment with a financial advisor"
Bot: "I can schedule that for you. What type of consultation do you need?"
```

## 🌟 Advanced Features

### Voice Integration
- Speech-to-text conversion
- Text-to-speech responses
- Voice command processing
- Multi-language support

### NLP Capabilities
- Intent recognition (greeting, balance_inquiry, transfer_money, etc.)
- Entity extraction (amounts, dates, account types)
- Sentiment analysis
- Context management

### Knowledge Base
- Banking FAQ integration
- Intelligent search capabilities
- Content relevance scoring
- Dynamic response generation

## 🔧 Customization

### Adding New Intents
1. Update `IntentRecognitionService.java`
2. Add intent patterns
3. Implement handler in `ChatbotService.java`
4. Test the new functionality

### Extending Banking Services
1. Create new service class in `banking/` package
2. Implement business logic
3. Add API endpoints in controller
4. Update frontend service calls

## 📊 Monitoring & Analytics

- **Health Checks**: Application health monitoring
- **Metrics**: Performance and usage metrics
- **Logging**: Comprehensive application logging
- **Error Tracking**: Error monitoring and alerting

## 🚢 Deployment

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up --build
```

### Cloud Deployment
- **Backend**: Deploy to AWS/Azure/GCP
- **Frontend**: Deploy to Netlify/Vercel
- **Database**: Use cloud database services

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

For questions and support:
- **Email**: support@example.com
- **Documentation**: [Wiki](wiki-link)
- **Issues**: [GitHub Issues](issues-link)

## 🎯 Banking Industry Relevance

This project demonstrates:
- **Digital Transformation**: Modern banking interfaces
- **Customer Experience**: 24/7 AI-powered assistance
- **Operational Efficiency**: Automated customer service
- **Innovation**: Voice and NLP integration
- **Security**: Banking-grade security measures
- **Scalability**: Enterprise-ready architecture

Perfect for demonstrating modern banking technology expertise for roles at financial institutions.

---

**Built with ❤️ for the future of banking technology**
