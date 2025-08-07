package com.barclays.bankingchatbot.service.banking;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class AppointmentService {
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
    
    public String bookAppointment(String userId, Map<String, Object> entities) {
        try {
            String appointmentType = extractAppointmentType(entities);
            String preferredDate = extractDate(entities);
            String preferredTime = extractTime(entities);
            
            if (appointmentType == null) {
                return "What type of appointment would you like to book?\n" +
                       "• Financial consultation\n" +
                       "• Loan application\n" +
                       "• Investment planning\n" +
                       "• Account services\n" +
                       "• Mortgage consultation";
            }
            
            return processAppointmentBooking(userId, appointmentType, preferredDate, preferredTime);
            
        } catch (Exception e) {
            return "I'm sorry, I couldn't book your appointment at the moment. " +
                   "Please call our customer service or visit our website to schedule.";
        }
    }
    
    private String extractAppointmentType(Map<String, Object> entities) {
        if (entities.containsKey("appointment_type")) {
            return (String) entities.get("appointment_type");
        }
        // Try to infer from other entities
        if (entities.containsKey("loan") || entities.containsKey("mortgage")) {
            return "loan consultation";
        }
        if (entities.containsKey("investment")) {
            return "investment planning";
        }
        return null;
    }
    
    private String extractDate(Map<String, Object> entities) {
        if (entities.containsKey("date")) {
            return (String) entities.get("date");
        }
        return null;
    }
    
    private String extractTime(Map<String, Object> entities) {
        if (entities.containsKey("time")) {
            return (String) entities.get("time");
        }
        return null;
    }
    
    private String processAppointmentBooking(String userId, String appointmentType, 
                                           String preferredDate, String preferredTime) {
        
        // Generate available time slots
        String availableSlots = generateAvailableSlots(preferredDate);
        
        if (preferredDate == null && preferredTime == null) {
            return String.format("I'd be happy to help you book a %s appointment. " +
                               "Here are the available time slots for this week:\n\n%s\n\n" +
                               "Which time works best for you?", 
                               appointmentType, availableSlots);
        }
        
        if (preferredDate != null && preferredTime != null) {
            return confirmAppointment(userId, appointmentType, preferredDate, preferredTime);
        }
        
        if (preferredDate != null) {
            return String.format("Great! For %s, here are the available times:\n\n%s\n\n" +
                               "Which time slot would you prefer?", 
                               preferredDate, generateSlotsForDate(preferredDate));
        }
        
        return String.format("I can book your %s appointment. When would you like to schedule it?\n\n%s", 
                           appointmentType, availableSlots);
    }
    
    private String generateAvailableSlots(String preferredDate) {
        StringBuilder slots = new StringBuilder();
        LocalDate startDate = LocalDate.now().plusDays(1);
        
        for (int i = 0; i < 5; i++) {
            LocalDate date = startDate.plusDays(i);
            slots.append("• ").append(date.format(dateFormatter)).append(":\n");
            slots.append("  - 9:00 AM, 11:00 AM, 2:00 PM, 4:00 PM\n");
        }
        
        return slots.toString();
    }
    
    private String generateSlotsForDate(String date) {
        return "Available times for " + date + ":\n" +
               "• 9:00 AM\n" +
               "• 11:00 AM\n" +
               "• 2:00 PM\n" +
               "• 4:00 PM";
    }
    
    private String confirmAppointment(String userId, String appointmentType, 
                                    String date, String time) {
        
        return String.format("Perfect! I've scheduled your appointment:\n\n" +
                           "📅 Appointment Details:\n" +
                           "• Type: %s\n" +
                           "• Date: %s\n" +
                           "• Time: %s\n" +
                           "• Location: Main Branch\n" +
                           "• Address: 123 Financial St, Downtown\n\n" +
                           "📧 A confirmation email will be sent to your registered email address.\n" +
                           "📱 You'll receive a reminder 24 hours before your appointment.\n\n" +
                           "Please bring a valid ID and any relevant documents. " +
                           "If you need to reschedule, you can do so up to 2 hours before your appointment.",
                           appointmentType, date, time);
    }
}
