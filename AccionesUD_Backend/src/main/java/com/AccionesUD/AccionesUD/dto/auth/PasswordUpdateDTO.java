package com.AccionesUD.AccionesUD.dto.auth;

import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private String token;
    private String newPassword;
}