package com.AccionesUD.AccionesUD.balance.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.AccionesUD.AccionesUD.balance.application.BalanceService;
import com.AccionesUD.AccionesUD.domain.model.User;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping
    public Double getBalance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return balanceService.getBalance(username);
    }

    @PostMapping("/update")
    public User updateBalance(@RequestParam Double amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return balanceService.updateBalance(username, amount);
    }

}
