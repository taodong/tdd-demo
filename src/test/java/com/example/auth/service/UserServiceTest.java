package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.exception.AccountLockedException;
import com.example.auth.exception.EmailAlreadyExistsException;
import com.example.auth.exception.InvalidCredentialsException;
import com.example.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .passwordHash("$hashed$")
                .locked(false)
                .failedAttempts(0)
                .build();
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$hashed$");
        when(userRepository.save(any())).thenReturn(existingUser);

        User result = userService.register("Alice", "alice@example.com", "password123");

        assertThat(result.getName()).isEqualTo("Alice");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register("Alice", "alice@example.com", "password123"))
                .isInstanceOf(EmailAlreadyExistsException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success_resetsFailedAttempts() {
        existingUser.setFailedAttempts(1);
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "$hashed$")).thenReturn(true);

        User result = userService.login("alice@example.com", "password123");

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(existingUser.getFailedAttempts()).isEqualTo(0);
        verify(userRepository).save(existingUser);
    }

    @Test
    void login_wrongEmail_throws() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("unknown@example.com", "password123"))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_wrongPassword_incrementsFailedAttempts() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.login("alice@example.com", "wrongpass"))
                .isInstanceOf(InvalidCredentialsException.class);

        assertThat(existingUser.getFailedAttempts()).isEqualTo(1);
        assertThat(existingUser.isLocked()).isFalse();
        verify(userRepository).save(existingUser);
    }

    @Test
    void login_threeWrongPasswords_locksAccount() {
        existingUser.setFailedAttempts(2);
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.login("alice@example.com", "wrongpass"))
                .isInstanceOf(InvalidCredentialsException.class);

        assertThat(existingUser.isLocked()).isTrue();
        assertThat(existingUser.getFailedAttempts()).isEqualTo(3);
    }

    @Test
    void login_lockedAccount_throwsAccountLocked() {
        existingUser.setLocked(true);
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.login("alice@example.com", "password123"))
                .isInstanceOf(AccountLockedException.class)
                .hasMessage("This account has been locked.");
    }
}
