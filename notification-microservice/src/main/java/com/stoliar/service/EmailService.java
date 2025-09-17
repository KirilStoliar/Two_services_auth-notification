package com.stoliar.service;

import com.stoliar.config.AdminProperties;
import com.stoliar.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final AdminProperties adminProperties;

    public void sendNotification(NotificationRequest request) {
        List<String> adminEmails = adminProperties.getEmails();

        String subject = request.getAction() + " пользователь " + request.getUsername();
        String text = "";

        if (request.getAction().equals("DELETE")) {
            text = String.format("Удалён пользователь %s и почтой - %s.",
                    request.getUsername(), request.getEmail());
        } else if (request.getAction().equals("UPDATE")) {
            text = String.format("Изменён пользователь %s с паролем - %s и почтой - %s.",
                    request.getUsername(), request.getPassword(), request.getEmail());
        } else if (request.getAction().equals("CREATED")) {
            text = String.format("Создан пользователь %s с паролем - %s и почтой - %s.",
                    request.getUsername(), request.getPassword(), request.getEmail());
        }

        for (String to : adminEmails) {
            if (to == null || to.isBlank()) {
                log.warn("Email администратора отсутствует, уведомление не отправлено");
                continue;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
        }
    }
}
