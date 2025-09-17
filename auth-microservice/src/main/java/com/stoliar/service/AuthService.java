package com.stoliar.service;

import com.stoliar.dto.AuthResponse;
import com.stoliar.dto.LoginDto;
import com.stoliar.dto.RegisterDto;
import com.stoliar.entity.User;
import com.stoliar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotificationService notificationService;

    @Transactional
    public AuthResponse register(RegisterDto registerDto) {
        log.info("Регистрация нового пользователя: {}", registerDto.getEmail());

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String rawPassword = registerDto.getPassword();

        User user = User.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .role(User.Role.USER)
                .build();

        User createrUser = userRepository.save(user);

        notificationService.sendNotificationToAdmins("CREATED", createrUser, rawPassword);
        log.info("Уведомление о создании пользователя отправлено: {}", createrUser.getEmail());

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}