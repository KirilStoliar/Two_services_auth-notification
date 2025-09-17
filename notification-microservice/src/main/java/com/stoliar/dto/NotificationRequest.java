package com.stoliar.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String action;     // CREATED, UPDATED, DELETED
    private String username;
    private String password;
    private String email;
}
