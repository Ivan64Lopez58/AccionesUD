package com.AccionesUD.AccionesUD.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    private Double id; //Cedula de identidad o pasaporte
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Double phone;
    private String address;
    private boolean otpEnabled;
    private int dailyOrderLimit;
    private Double balance = 0.0; // Balance inicial
}
