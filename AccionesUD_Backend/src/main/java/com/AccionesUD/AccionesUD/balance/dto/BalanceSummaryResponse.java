package com.AccionesUD.AccionesUD.balance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceSummaryResponse {
    private Double availableBalance;
    private Double pendingBalance;
    private Double totalBalance;
    private String currency;
}

