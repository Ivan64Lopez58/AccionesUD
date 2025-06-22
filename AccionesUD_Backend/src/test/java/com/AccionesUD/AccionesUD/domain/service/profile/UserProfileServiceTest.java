package com.AccionesUD.AccionesUD.domain.service.profile;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    private UserRepository userRepository;
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userProfileService = new UserProfileService(userRepository);
    }

  @Test
void updateUser_UserExists_UpdatesAndSavesUser() {
    UpdateUserProfileRequest request = new UpdateUserProfileRequest();
    request.setId(123D);
    request.setFirstname("Juan");
    request.setLastname("Pérez");
    request.setPhone(1234567890D);
    request.setUsername("juan@example.com");
    request.setAddress("Calle Falsa 123");
    request.setOtpEnabled(true);
    request.setDailyOrderLimit(5);

    User user = new User();
    when(userRepository.findById(123D)).thenReturn(Optional.of(user));

    userProfileService.updateUser(request);

    assertEquals("Juan", user.getFirstname());
    assertEquals("Pérez", user.getLastname());
    assertEquals(1234567890D, user.getPhone()); // <--- Aquí está el fix
    assertEquals("juan@example.com", user.getUsername());
    assertEquals("Calle Falsa 123", user.getAddress());
    assertTrue(user.isOtpEnabled());
    assertEquals(5, user.getDailyOrderLimit());

    verify(userRepository).save(user);
}

   @Test
void updateUser_UserNotFound_ThrowsException() {
    UpdateUserProfileRequest request = new UpdateUserProfileRequest();
    request.setId(123D);

    when(userRepository.findById(123D)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
        userProfileService.updateUser(request);
    });

    assertEquals("Usuario no encontrado con cédula: " + String.valueOf(123D), ex.getMessage()); // <--- Aquí está el fix
    verify(userRepository, never()).save(any());
}

    @Test
    void getProfile_UserExists_ReturnsUser() {
        User user = new User();
        user.setUsername("jane@example.com");

        when(userRepository.findByUsername("jane@example.com")).thenReturn(Optional.of(user));

        User result = userProfileService.getProfile("jane@example.com");

        assertEquals("jane@example.com", result.getUsername());
    }

    @Test
    void getProfile_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("noexiste@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.getProfile("noexiste@example.com");
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }
}
