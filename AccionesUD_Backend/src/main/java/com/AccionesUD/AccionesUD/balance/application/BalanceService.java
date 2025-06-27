package com.AccionesUD.AccionesUD.balance.application;

import com.AccionesUD.AccionesUD.balance.dto.BalanceSummaryResponse;
import com.AccionesUD.AccionesUD.domain.model.User;

public interface BalanceService {
    Double getBalance(String userName);
    User updateBalance(String userName, Double amount);
    BalanceSummaryResponse getBalanceSummary(String username);
}