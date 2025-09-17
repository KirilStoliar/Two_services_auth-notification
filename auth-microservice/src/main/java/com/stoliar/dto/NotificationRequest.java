package com.stoliar.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NotificationRequest {
    private String action;     // CREATED, UPDATED, DELETED
    private String username;
    private String password;
    private String email;
}


