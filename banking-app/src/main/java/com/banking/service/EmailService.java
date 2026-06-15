package com.banking.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String email, String fullName) {
        send(email, "Welcome to Secure Online Banking", "Hello " + fullName + ", your account has been created.");
    }

    public void sendTransferEmail(String email, BigDecimal amount, String receiverAccount) {
        send(email, "Transfer successful", "Amount " + amount + " transferred to account " + receiverAccount + ".");
    }

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Email delivery failed for {}: {}", to, ex.getMessage());
        }
    }
}
