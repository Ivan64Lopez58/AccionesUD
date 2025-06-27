package com.AccionesUD.AccionesUD.application.profile;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.application.NotificationApplicationService;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.model.notification.NotificationType;
import com.AccionesUD.AccionesUD.domain.service.profile.UserProfileService;
import com.AccionesUD.AccionesUD.dto.notification.NotificationRequest;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileApplicationService {
    private final UserProfileService userProfileService;
    private final NotificationApplicationService notificationService;


public void updateUserProfile(UpdateUserProfileRequest request) {
    userProfileService.updateUser(request);

   
    notificationService.processNotification(
        new NotificationRequest(
            NotificationType.PROFILE_UPDATE,
            "Perfil actualizado",
            "Has actualizado tu perfil de usuario"
        ),
        request.getUsername()
    );
}


public UserProfileResponse getProfile(String username) {
    User user = userProfileService.getProfile(username);

    return UserProfileResponse.builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .username(user.getUsername())
            .phone(user.getPhone())
            .address(user.getAddress())
            .otpEnabled(user.isOtpEnabled())
            .dailyOrderLimit(user.getDailyOrderLimit())
            .Balance(user.getBalance())
            .build();
}

}