package com.dattran.identity_service.domain.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
//            Pass variables to html template
            Context context = new Context();
            context.setVariables(variables);
            String htmlTemplate = templateEngine.process(template, context);
//            Send
            messageHelper.setFrom("contact@tranvandat.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(htmlTemplate, true);
            mailSender.send(message);
            future.complete(null); // Indicate that the email sending is successful
        } catch (MessagingException e) {
            log.warn(e.getMessage());
            future.completeExceptionally(e); // Indicate that the email sending failed
        }
        return future;
    }
}