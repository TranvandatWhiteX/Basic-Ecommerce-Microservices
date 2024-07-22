package com.dattran.notification_service.domain.services;

import com.dattran.notification_service.app.requests.EmailRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailConsumer {
    EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @KafkaListener(topics = "verification", groupId = "notification-group")
    public void listenerEmailRequest(EmailRequest emailRequest) {
        log.info("Log-kafka {}", emailRequest.getSubject());
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getTemplate(), emailRequest.getVariables());
    }
}
