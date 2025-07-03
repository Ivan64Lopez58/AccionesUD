package com.AccionesUD.AccionesUD.domain.service.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

public void ensureUserExists(String email) {
    userRepository.findByUsername(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
}

public void updatePassword(String email, String newPassword) {
    User user = userRepository.findByUsername(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}

}