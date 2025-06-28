package com.tracking.notificationservice.listener;

import com.tracking.commonservice.event.RegistrationEvent;
import com.tracking.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final EmailService emailService;

    @Bean
    public Consumer<RegistrationEvent> handleRegistrationEvent() {
        return this::sendGreetingEmail;
    }

    private void sendGreetingEmail(RegistrationEvent registerEvent) {
        String content = "<html>" +
                "<body>" +
                "<h1>Welcome to our service!</h1>" +
                "<p>Dear " + registerEvent.getName() + ",</p>" +
                "<p>We are excited to have you on board" +
                "<p>Best regards,<br/>The Team</p>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(registerEvent.getEmail(), "Greeting!", content, true);
    }
}
