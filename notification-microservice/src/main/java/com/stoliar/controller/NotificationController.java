package com.stoliar.controller;

import com.stoliar.dto.NotificationRequest;
import com.stoliar.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Уведомления", description = "Отправка email уведомлений")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @Operation(
            summary = "Отправка email уведомления",
            description = "Отправляет email всем администраторам после событий: создание, обновление или удаление пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление отправлено"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обработке уведомления")
    })
    @PostMapping
    public ResponseEntity<Void> notifyAdmins(@RequestBody NotificationRequest request) {
        log.info("Попытка отправить уведомление: {}", request);

        emailService.sendNotification(request);

        log.info("Получено уведомление: {}", request);

        return ResponseEntity.ok().build();
    }
}
