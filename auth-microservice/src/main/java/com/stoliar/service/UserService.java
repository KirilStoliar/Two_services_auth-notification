package com.stoliar.service;

import com.stoliar.dto.UserDto;
import com.stoliar.entity.User;
import com.stoliar.repository.UserRepository;
import com.stoliar.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
//    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long id) {
        User user = getUserById(id);
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        UserDetailsImpl currentUserDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long currentId = currentUserDetails.getId();
        String currentRole = currentUserDetails.getRole();

        Long targetId = currentRole.equals("ADMIN") ? id : currentId;

        log.info("Updating user with id: " + targetId);

        User user = getUserById(targetId);

        String originalPassword = null;
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            originalPassword = userDto.getPassword();
            user.setPassword(passwordEncoder.encode(originalPassword));
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());

            if (targetId.equals(currentId)) {
                // Если меняем email текущему пользователю — обновляем SecurityContext
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        new UserDetailsImpl(user),
                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        if (currentRole.equals("ADMIN") && userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }

        User updatedUser = userRepository.save(user);

//        if (updatedUser.getRole() == User.Role.USER) {
//            notificationService.sendNotificationToAdmins("UPDATE", updatedUser, originalPassword);
//        }
        notificationService.sendNotificationToAdmins("UPDATE", updatedUser, originalPassword);
        log.info("Данные успешно изменены для пользователя " + updatedUser.getFirstName() + " с почтой " + updatedUser.getEmail());

        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        User user = getUserById(id);
        userRepository.deleteById(id);

        notificationService.sendNotificationToAdmins("DELETE", user, user.getPassword());
    }

    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}