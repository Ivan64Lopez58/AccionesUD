package com.AccionesUD.AccionesUD.application.profile;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.domain.service.profile.UserProfileService;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileApplicationService {
    private final UserProfileService userProfileService;

public void updateUserProfile(UpdateUserProfileRequest request) {
    userProfileService.updateUser(request);
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
            .build();
}

}