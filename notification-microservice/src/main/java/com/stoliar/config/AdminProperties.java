package com.stoliar.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Component
//@Validated
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {

    @NotEmpty(message = "Список email администраторов не может быть пустым")
    private List<@Email(message = "Некорректный email в списке администраторов") String> emails;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
