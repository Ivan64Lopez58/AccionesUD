package com.AccionesUD.AccionesUD.balance.infrastructure;


import com.AccionesUD.AccionesUD.balance.application.BalanceService;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceServiceImpl implements BalanceService {


    private final UserRepository userRepository;

    public BalanceServiceImpl (UserRepository userRepository) {
    
        this.userRepository = userRepository;}

     @Override
    public Double getBalance(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getBalance(); // Devuelve solo el balance
    }

    @Override
    @Transactional
    public User updateBalance(String userName, Double amount) {
        User user = userRepository.findByUsername(userName)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (user.getBalance() + amount < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        user.setBalance(user.getBalance() + amount);
        User updatedUser = userRepository.save(user);
        return updatedUser;
    }
}