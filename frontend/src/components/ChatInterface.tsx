import React, { useState, useEffect, useRef } from 'react';
import {
  Box,
  Container,
  Paper,
  TextField,
  IconButton,
  Typography,
  List,
  ListItem,
  ListItemText,
  Avatar,
  Chip,
  Fab,
  CircularProgress,
  Alert,
  Button,
  Grid
} from '@mui/material';
import {
  Send as SendIcon,
  Mic as MicIcon,
  MicOff as MicOffIcon,
  SmartToy as BotIcon,
  Person as PersonIcon,
  VolumeUp as SpeakerIcon
} from '@mui/icons-material';
import { motion, AnimatePresence } from 'framer-motion';
import ChatService from '../services/ChatService';
import VoiceService from '../services/VoiceService';
import './ChatInterface.css';

interface Message {
  id: string;
  text: string;
  sender: 'user' | 'bot';
  timestamp: Date;
  suggestions?: string[];
  intent?: string;
  confidence?: number;
}

const ChatInterface: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isListening, setIsListening] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  
  // Simple speech synthesis using native Web Speech API
  const speak = (text: string) => {
    if ('speechSynthesis' in window) {
      const utterance = new SpeechSynthesisUtterance(text);
      utterance.rate = 0.8;
      utterance.pitch = 1;
      speechSynthesis.speak(utterance);
    }
  };
  
  const cancel = () => {
    if ('speechSynthesis' in window) {
      speechSynthesis.cancel();
    }
  };
  
  useEffect(() => {
    // Initial greeting
    const initialMessage: Message = {
      id: '1',
      text: "Hello! I'm your Banking Virtual Assistant. I can help you with account inquiries, transactions, appointments, and financial advice. How can I assist you today?",
      sender: 'bot',
      timestamp: new Date(),
      suggestions: [
        "Check my account balance",
        "View recent transactions", 
        "Book an appointment",
        "Get financial advice"
      ]
    };
    setMessages([initialMessage]);
  }, []);
  
  useEffect(() => {
    scrollToBottom();
  }, [messages]);
  
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };
  
  const sendMessage = async (text: string) => {
    if (!text.trim()) return;
    
    const userMessage: Message = {
      id: Date.now().toString(),
      text: text.trim(),
      sender: 'user',
      timestamp: new Date()
    };
    
    setMessages(prev => [...prev, userMessage]);
    setInputText('');
    setIsLoading(true);
    setError(null);
    
    try {
      const response = await ChatService.sendMessage(text);
      
      const botMessage: Message = {
        id: (Date.now() + 1).toString(),
        text: response.message,
        sender: 'bot',
        timestamp: new Date(),
        suggestions: response.suggestions,
        intent: response.intent,
        confidence: response.confidence
      };
      
      setMessages(prev => [...prev, botMessage]);
      
      // Text-to-speech for bot response
      if (response.message) {
        speak(response.message);
      }
      
    } catch (error) {
      console.error('Error sending message:', error);
      setError('Sorry, I encountered an error. Please try again.');
      
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        text: "I'm sorry, I encountered an error processing your request. Please try again.",
        sender: 'bot',
        timestamp: new Date()
      };
      
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };
  
  const handleVoiceInput = async () => {
    if (isListening) {
      VoiceService.stopListening();
      setIsListening(false);
      return;
    }
    
    try {
      setIsListening(true);
      const transcript = await VoiceService.startListening();
      
      if (transcript) {
        await sendMessage(transcript);
      }
    } catch (error) {
      console.error('Voice input error:', error);
      setError('Voice input failed. Please try typing instead.');
    } finally {
      setIsListening(false);
    }
  };
  
  const handleSuggestionClick = (suggestion: string) => {
    sendMessage(suggestion);
  };
  
  const handleKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      sendMessage(inputText);
    }
  };
  
  const speakMessage = (text: string) => {
    cancel(); // Stop any current speech
    speak(text);
  };
  
  return (
    <Container maxWidth="md" sx={{ height: '100vh', display: 'flex', flexDirection: 'column', py: 2 }}>
      <Paper elevation={3} sx={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
        {/* Header */}
        <Box sx={{ p: 2, borderBottom: 1, borderColor: 'divider', bgcolor: 'primary.main', color: 'white' }}>
          <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <BotIcon />
            Banking Virtual Assistant
          </Typography>
          <Typography variant="body2" sx={{ opacity: 0.8 }}>
            Powered by AI • Available 24/7
          </Typography>
        </Box>
        
        {/* Error Alert */}
        {error && (
          <Alert severity="error" onClose={() => setError(null)} sx={{ m: 1 }}>
            {error}
          </Alert>
        )}
        
        {/* Messages */}
        <Box sx={{ flex: 1, overflow: 'auto', p: 1 }}>
          <List>
            <AnimatePresence>
              {messages.map((message) => (
                <motion.div
                  key={message.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -20 }}
                  transition={{ duration: 0.3 }}
                >
                  <ListItem
                    sx={{
                      flexDirection: 'column',
                      alignItems: message.sender === 'user' ? 'flex-end' : 'flex-start',
                      mb: 1
                    }}
                  >
                    <Box
                      sx={{
                        display: 'flex',
                        alignItems: 'flex-start',
                        gap: 1,
                        maxWidth: '80%',
                        flexDirection: message.sender === 'user' ? 'row-reverse' : 'row'
                      }}
                    >
                      <Avatar
                        sx={{
                          bgcolor: message.sender === 'user' ? 'secondary.main' : 'primary.main',
                          width: 32,
                          height: 32
                        }}
                      >
                        {message.sender === 'user' ? <PersonIcon /> : <BotIcon />}
                      </Avatar>
                      
                      <Paper
                        elevation={1}
                        sx={{
                          p: 2,
                          bgcolor: message.sender === 'user' ? 'secondary.light' : 'grey.100',
                          borderRadius: 2,
                          position: 'relative'
                        }}
                      >
                        <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
                          {message.text}
                        </Typography>
                        
                        {message.sender === 'bot' && (
                          <IconButton
                            size="small"
                            onClick={() => speakMessage(message.text)}
                            sx={{ position: 'absolute', top: 4, right: 4 }}
                          >
                            <SpeakerIcon fontSize="small" />
                          </IconButton>
                        )}
                        
                        {/* Intent and Confidence */}
                        {message.intent && message.confidence && (
                          <Box sx={{ mt: 1 }}>
                            <Chip
                              label={`${message.intent} (${(message.confidence * 100).toFixed(0)}%)`}
                              size="small"
                              color={message.confidence > 0.7 ? 'success' : 'warning'}
                            />
                          </Box>
                        )}
                        
                        <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 0.5 }}>
                          {message.timestamp.toLocaleTimeString()}
                        </Typography>
                      </Paper>
                    </Box>
                    
                    {/* Suggestions */}
                    {message.suggestions && message.suggestions.length > 0 && (
                      <Box sx={{ mt: 1, maxWidth: '80%' }}>
                        <Typography variant="caption" color="text.secondary" sx={{ mb: 1, display: 'block' }}>
                          Quick suggestions:
                        </Typography>
                        <Grid container spacing={0.5}>
                          {message.suggestions.map((suggestion, index) => (
                            <Grid item key={index}>
                              <Chip
                                label={suggestion}
                                size="small"
                                onClick={() => handleSuggestionClick(suggestion)}
                                sx={{ cursor: 'pointer' }}
                                variant="outlined"
                              />
                            </Grid>
                          ))}
                        </Grid>
                      </Box>
                    )}
                  </ListItem>
                </motion.div>
              ))}
            </AnimatePresence>
            
            {/* Loading indicator */}
            {isLoading && (
              <ListItem sx={{ justifyContent: 'center' }}>
                <CircularProgress size={24} />
                <Typography variant="body2" sx={{ ml: 1 }}>
                  Assistant is typing...
                </Typography>
              </ListItem>
            )}
            
            <div ref={messagesEndRef} />
          </List>
        </Box>
        
        {/* Input */}
        <Box sx={{ p: 2, borderTop: 1, borderColor: 'divider' }}>
          <Box sx={{ display: 'flex', gap: 1, alignItems: 'flex-end' }}>
            <TextField
              fullWidth
              multiline
              maxRows={4}
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Type your message or ask about your account..."
              variant="outlined"
              size="small"
              disabled={isLoading}
            />
            
            <IconButton
              color="primary"
              onClick={() => sendMessage(inputText)}
              disabled={!inputText.trim() || isLoading}
            >
              <SendIcon />
            </IconButton>
            
            <Fab
              size="small"
              color={isListening ? "secondary" : "primary"}
              onClick={handleVoiceInput}
              disabled={isLoading}
            >
              {isListening ? <MicOffIcon /> : <MicIcon />}
            </Fab>
          </Box>
          
          {isListening && (
            <Typography variant="caption" color="secondary" sx={{ mt: 1, display: 'block', textAlign: 'center' }}>
              🎤 Listening... Click mic to stop
            </Typography>
          )}
        </Box>
      </Paper>
    </Container>
  );
};

export default ChatInterface;
