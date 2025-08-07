import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export interface ChatMessage {
  message: string;
  type?: string;
  sessionId?: string;
  userId?: string;
}

export interface ChatResponse {
  message: string;
  intent: string;
  confidence: number;
  timestamp: string;
  suggestions: string[];
  audioResponse?: string;
  requiresAction?: boolean;
  actionType?: string;
  actionData?: any;
}

class ChatService {
  private static sessionId: string = this.generateSessionId();
  
  private static generateSessionId(): string {
    return Date.now().toString() + Math.random().toString(36).substr(2, 9);
  }
  
  static async sendMessage(message: string): Promise<ChatResponse> {
    try {
      const chatMessage: ChatMessage = {
        message,
        type: 'text',
        sessionId: this.sessionId,
        userId: 'demo_user' // In real app, this would come from authentication
      };
      
      const response = await axios.post(`${API_BASE_URL}/chatbot/chat`, chatMessage, {
        headers: {
          'Content-Type': 'application/json',
        },
        timeout: 10000 // 10 second timeout
      });
      
      return response.data;
    } catch (error) {
      console.error('Error sending message:', error);
      throw new Error('Failed to send message to chatbot');
    }
  }
  
  static async sendVoiceMessage(audioData: string): Promise<ChatResponse> {
    try {
      const formData = new FormData();
      formData.append('audio', audioData);
      
      const response = await axios.post(`${API_BASE_URL}/chatbot/voice`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        timeout: 15000 // 15 second timeout for voice processing
      });
      
      return response.data;
    } catch (error) {
      console.error('Error sending voice message:', error);
      throw new Error('Failed to process voice message');
    }
  }
  
  static async getSupportedIntents(): Promise<any[]> {
    try {
      const response = await axios.get(`${API_BASE_URL}/chatbot/intents`);
      return response.data;
    } catch (error) {
      console.error('Error fetching supported intents:', error);
      return [];
    }
  }
  
  static async searchKnowledgeBase(query: string): Promise<any[]> {
    try {
      const response = await axios.get(`${API_BASE_URL}/chatbot/knowledge-base`, {
        params: { query }
      });
      return response.data;
    } catch (error) {
      console.error('Error searching knowledge base:', error);
      return [];
    }
  }
}

export default ChatService;
