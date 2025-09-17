package com.stoliar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stoliar.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private User.Role role;
}