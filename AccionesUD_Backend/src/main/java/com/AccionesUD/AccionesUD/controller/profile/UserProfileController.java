package com.AccionesUD.AccionesUD.controller.profile;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.AccionesUD.AccionesUD.application.profile.UserProfileApplicationService;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserProfileController {

    private final UserProfileApplicationService appService;

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserProfile(@RequestBody UpdateUserProfileRequest request) {
        appService.updateUserProfile(request);
        return ResponseEntity.ok(Map.of("message", "Perfil actualizado exitosamente"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(appService.getProfile(auth.getName()));
    }
}
