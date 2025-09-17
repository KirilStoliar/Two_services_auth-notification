package com.stoliar.service;

import com.stoliar.dto.NotificationRequest;
import com.stoliar.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;

    public void sendNotificationToAdmins(String action, User targetUser, String encodePassword) {
            NotificationRequest request = new NotificationRequest();
            request.setAction(action);
            request.setUsername(targetUser.getUsername());
            request.setPassword(encodePassword);
            request.setEmail(targetUser.getEmail());

            sendNotification(request);
            log.info("Отправлено уведомление в notification-service о действии {} над пользователем {}",
                action, targetUser.getEmail());
    }

    private void sendNotification(NotificationRequest request) {
        log.info("Отправка уведомления в notification-сервис для пользователя: {}",
                request.getUsername());
        restTemplate.postForObject("http://notification-service:8081/api/notifications",
                request, Void.class);
    }
}
