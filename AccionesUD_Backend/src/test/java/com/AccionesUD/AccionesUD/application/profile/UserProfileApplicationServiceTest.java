package com.AccionesUD.AccionesUD.application.profile;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.service.profile.UserProfileService;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileApplicationServiceTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileApplicationService applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUserProfile_shouldDelegateToService() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("usuario@correo.com");

        applicationService.updateUserProfile(request);

        verify(userProfileService, times(1)).updateUser(request);
    }

    @Test
    void getProfile_shouldReturnMappedResponse() {
        User mockUser = new User();
        mockUser.setId(1D);
        mockUser.setFirstname("Juan");
        mockUser.setLastname("Pérez");
        mockUser.setUsername("usuario@correo.com");
        mockUser.setPhone(3011234567D);
        mockUser.setAddress("Calle 123");
        mockUser.setOtpEnabled(true);
        mockUser.setDailyOrderLimit(5);

        when(userProfileService.getProfile("usuario@correo.com")).thenReturn(mockUser);

        UserProfileResponse response = applicationService.getProfile("usuario@correo.com");

        assertEquals("Juan", response.getFirstname());
        assertEquals("Pérez", response.getLastname());
        assertEquals("usuario@correo.com", response.getUsername());
        assertEquals(3011234567D, response.getPhone());
        assertEquals("Calle 123", response.getAddress());
        assertTrue(response.isOtpEnabled());
        assertEquals(5, response.getDailyOrderLimit());
    }
}
