# Contributing to Banking Chatbot Virtual Assistant

Thank you for your interest in contributing! This document provides guidelines for contributing to this project.

## 🛠️ Development Setup

### Prerequisites
- Java 21+
- Node.js 16+
- Maven 3.6+
- Git

### Local Development
1. Fork and clone the repository
2. Set up backend: `cd backend && mvn spring-boot:run`
3. Set up frontend: `cd frontend && npm install && npm start`

## 📋 Contribution Guidelines

### Code Style
- Follow existing code formatting
- Write meaningful commit messages
- Add tests for new features
- Update documentation

### Pull Request Process
1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Update documentation
5. Submit PR with clear description

### Issue Reporting
- Use clear, descriptive titles
- Provide steps to reproduce
- Include environment details
- Add relevant labels

## 🔄 Development Workflow

### Backend Changes
```bash
# Run tests
mvn test

# Check code quality
mvn checkstyle:check

# Build application
mvn clean package
```

### Frontend Changes
```bash
# Run tests
npm test

# Lint code
npm run lint

# Build application
npm run build
```

## 📚 Areas for Contribution

### High Priority
- [ ] Mobile responsiveness improvements
- [ ] Additional banking services
- [ ] Performance optimizations
- [ ] Security enhancements

### Medium Priority
- [ ] UI/UX improvements
- [ ] Additional language support
- [ ] Integration with external APIs
- [ ] Advanced analytics

### Documentation
- [ ] API documentation improvements
- [ ] Code examples
- [ ] Tutorial content
- [ ] Architecture diagrams

## 🤝 Community

- Be respectful and inclusive
- Help others learn and grow
- Share knowledge and experiences
- Follow our Code of Conduct

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

**Happy Contributing! 🚀**
