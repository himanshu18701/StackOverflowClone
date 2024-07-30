package com.himanshu.stackoverflow.eventListener;

import com.himanshu.stackoverflow.event.AnswerAddedEvent;
import com.himanshu.stackoverflow.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnswerEventListener {

    private final NotificationService notificationService;

    @Autowired
    public AnswerEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onAnswerAdded(AnswerAddedEvent event) {
        String to = event.getQuestion().getUser().getEmail();
        String subject = "New Answer Added to your Question";
        String imageUrl = extractImageUrl(event.getAnswer().getContent());
        String text = formatEmailContent(event, imageUrl);
        notificationService.sendEmail(to, subject, text, imageUrl);
    }

    private String formatEmailContent(AnswerAddedEvent event, String imageUrl) {
        return String.format(
                "<html>" +
                        "<body>" +
                        "<p>Hello,</p>" +
                        "<p>An answer has been added to your question by <strong>%s</strong>.</p>" +
                        "<p>Answer:</p>" +
                        (imageUrl != null ? "<p><img src='cid:answerImage'></p>" : "") +
                        "<p>%s</p>" +
                        "<p>Best regards,</p>" +
                        "<p>Your StackOverflow Team</p>" +
                        "</body>" +
                        "</html>",
                event.getAnswer().getUser().getName(),
                event.getAnswer().getContent().replaceAll("<p><img[^>]+></p>", "")
        );
    }

    private String extractImageUrl(String content) {
        String[] parts = content.split("src=\"");
        if (parts.length > 1) {
            String imageUrl = parts[1].split("\"")[0];
            if (imageUrl.startsWith("data:image/")) {
                return imageUrl;
            }
        }
        return null;
    }
}
