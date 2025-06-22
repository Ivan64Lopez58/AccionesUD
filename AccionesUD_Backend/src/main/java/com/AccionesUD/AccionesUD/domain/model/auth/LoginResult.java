package com.AccionesUD.AccionesUD.domain.model.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import lombok.*;

@Data
@AllArgsConstructor
public class LoginResult {
    private boolean otpRequired;
    private String otpMessage;
    private String jwt;
    private User user;
}