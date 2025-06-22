package com.AccionesUD.AccionesUD.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OtpRequest {
    private String username;
    private String otp;
}
