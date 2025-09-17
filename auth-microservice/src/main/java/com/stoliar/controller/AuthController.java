package com.stoliar.controller;

import com.stoliar.dto.AuthResponse;
import com.stoliar.dto.LoginDto;
import com.stoliar.dto.RegisterDto;
import com.stoliar.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            AuthResponse response = authService.register(registerDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Enter email and password")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDto loginDto) {
        System.out.println(">>> AuthController: login called");

        try {
            return ResponseEntity.ok(authService.login(loginDto));
        } catch (Exception e) {
            System.out.println(">>> AUTH ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}