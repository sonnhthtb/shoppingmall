package com.example.shoppingmall.service;

import com.example.shoppingmall.model.NotificationEmail;

public interface NotificationService {
    void sendMail(NotificationEmail notificationEmail);
}
