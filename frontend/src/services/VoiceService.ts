class VoiceService {
  private recognition: any = null;
  private synthesis: SpeechSynthesis = window.speechSynthesis;
  
  constructor() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
      const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
      this.recognition = new SpeechRecognition();
      
      this.recognition.continuous = false;
      this.recognition.interimResults = false;
      this.recognition.lang = 'en-US';
    }
  }
  
  async startListening(): Promise<string> {
    return new Promise((resolve, reject) => {
      if (!this.recognition) {
        reject(new Error('Speech recognition not supported'));
        return;
      }
      
      this.recognition.onresult = (event: any) => {
        const transcript = event.results[0][0].transcript;
        resolve(transcript);
      };
      
      this.recognition.onerror = (event: any) => {
        reject(new Error(`Speech recognition error: ${event.error}`));
      };
      
      this.recognition.onend = () => {
        // Recognition ended
      };
      
      try {
        this.recognition.start();
      } catch (error) {
        reject(error);
      }
    });
  }
  
  stopListening(): void {
    if (this.recognition) {
      this.recognition.stop();
    }
  }
  
  speak(text: string, voice?: SpeechSynthesisVoice): void {
    if (!this.synthesis) {
      console.warn('Speech synthesis not supported');
      return;
    }
    
    // Cancel any ongoing speech
    this.synthesis.cancel();
    
    const utterance = new SpeechSynthesisUtterance(text);
    
    if (voice) {
      utterance.voice = voice;
    } else {
      // Try to use a female voice by default
      const voices = this.synthesis.getVoices();
      const femaleVoice = voices.find(voice => 
        voice.name.toLowerCase().includes('female') || 
        voice.name.toLowerCase().includes('samantha') ||
        voice.name.toLowerCase().includes('serena')
      );
      if (femaleVoice) {
        utterance.voice = femaleVoice;
      }
    }
    
    utterance.rate = 0.9; // Slightly slower for better comprehension
    utterance.pitch = 1.0;
    utterance.volume = 0.8;
    
    this.synthesis.speak(utterance);
  }
  
  getVoices(): SpeechSynthesisVoice[] {
    return this.synthesis ? this.synthesis.getVoices() : [];
  }
  
  isSupported(): boolean {
    return !!(this.recognition && this.synthesis);
  }
  
  // Convert audio blob to base64 for sending to backend
  async audioToBase64(audioBlob: Blob): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const base64 = (reader.result as string).split(',')[1];
        resolve(base64);
      };
      reader.onerror = reject;
      reader.readAsDataURL(audioBlob);
    });
  }
  
  // Record audio for sending to backend speech-to-text service
  async recordAudio(duration: number = 5000): Promise<string> {
    return new Promise(async (resolve, reject) => {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        const mediaRecorder = new MediaRecorder(stream);
        const audioChunks: Blob[] = [];
        
        mediaRecorder.addEventListener('dataavailable', (event) => {
          audioChunks.push(event.data);
        });
        
        mediaRecorder.addEventListener('stop', async () => {
          const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
          const base64 = await this.audioToBase64(audioBlob);
          
          // Stop all tracks
          stream.getTracks().forEach(track => track.stop());
          
          resolve(base64);
        });
        
        mediaRecorder.start();
        
        // Auto-stop after duration
        setTimeout(() => {
          if (mediaRecorder.state === 'recording') {
            mediaRecorder.stop();
          }
        }, duration);
        
      } catch (error) {
        reject(error);
      }
    });
  }
}

export default new VoiceService();
