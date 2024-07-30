package com.himanshu.stackoverflow.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text, String imageUrl) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String base64Image = imageUrl.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                helper.addInline("answerImage", new ByteArrayResource(imageBytes), "image/png");
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
