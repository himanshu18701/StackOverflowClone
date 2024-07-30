package com.himanshu.stackoverflow.service;

public interface NotificationService {
    void sendEmail(String to, String subject, String text,String imageUrl);
}
