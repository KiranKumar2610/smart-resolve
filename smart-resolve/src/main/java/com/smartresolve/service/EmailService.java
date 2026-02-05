package com.smartresolve.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendStatusUpdateMail(String to, String title, String status) {

        System.out.println("📧 Sending email to: " + to);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Complaint Status Updated");
        msg.setText(
                "Your complaint \"" + title + "\" status is now: " + status
        );

        mailSender.send(msg);

        System.out.println("✅ Email sent successfully");
    }
}


