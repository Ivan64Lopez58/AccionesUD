package com.AccionesUD.AccionesUD.domain.model.auth;

import com.AccionesUD.AccionesUD.domain.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationResult {
    private String jwt;
    private User user;
}