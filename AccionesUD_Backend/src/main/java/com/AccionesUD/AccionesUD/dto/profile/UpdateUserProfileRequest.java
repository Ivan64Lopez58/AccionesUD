package com.AccionesUD.AccionesUD.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private Double id;
    private String firstname;
    private String lastname;
    private Double phone;
    private String username;
    private String address;
    private boolean otpEnabled;
    private Integer dailyOrderLimit;
    private Double Balance;
}
