package com.stoliar.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}