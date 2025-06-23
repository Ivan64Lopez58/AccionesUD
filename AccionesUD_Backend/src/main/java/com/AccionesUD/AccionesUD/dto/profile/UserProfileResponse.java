package com.AccionesUD.AccionesUD.dto.profile;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Double id;
    private String firstname;
    private String lastname;
    private Double phone;
    private String username;
    private String address;
    private boolean otpEnabled;
    private Integer dailyOrderLimit;
    private Integer Balance;
    private String idiomaPreferido;
}
