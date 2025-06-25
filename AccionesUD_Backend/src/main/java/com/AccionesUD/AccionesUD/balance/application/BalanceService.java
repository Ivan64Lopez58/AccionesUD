package com.AccionesUD.AccionesUD.balance.application;

import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.dto.profile.UserProfileResponse;

public interface BalanceService {
    Double getBalance(Double userId);
    User updateBalance(Double userId, Double amount);
}