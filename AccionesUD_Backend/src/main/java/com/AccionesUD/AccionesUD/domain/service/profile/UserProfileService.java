package com.AccionesUD.AccionesUD.domain.service.profile;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.dto.profile.UpdateUserProfileRequest;
import com.AccionesUD.AccionesUD.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;

public void updateUser(UpdateUserProfileRequest request) {
    User user = userRepository.findById(request.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cédula: " + request.getId()));

    user.setFirstname(request.getFirstname());
    user.setLastname(request.getLastname());
    user.setPhone(request.getPhone());
    user.setUsername(request.getUsername());
    user.setAddress(request.getAddress());
    user.setOtpEnabled(request.isOtpEnabled());
    user.setDailyOrderLimit(request.getDailyOrderLimit());

    userRepository.save(user);
}

    public User getProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}