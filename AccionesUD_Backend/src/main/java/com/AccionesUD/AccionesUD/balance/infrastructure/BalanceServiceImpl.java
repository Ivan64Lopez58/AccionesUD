package com.AccionesUD.AccionesUD.balance.infrastructure;


import com.AccionesUD.AccionesUD.application.orders.OrderService;
import com.AccionesUD.AccionesUD.balance.application.BalanceService;
import com.AccionesUD.AccionesUD.balance.dto.BalanceSummaryResponse;
import com.AccionesUD.AccionesUD.domain.model.User;
import com.AccionesUD.AccionesUD.repository.UserRepository;

import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceServiceImpl implements BalanceService {


    private final UserRepository userRepository;
    private final OrderService orderService;

    public BalanceServiceImpl (UserRepository userRepository, OrderService orderService) {
    
        this.userRepository = userRepository;
        this.orderService = orderService;}

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

    @Override
    public BalanceSummaryResponse getBalanceSummary(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Double saldo = user.getBalance();

        double totalPendientes = orderService.listarOrdenesPorUsuario(username).stream()
            .filter(o -> o.getStatus() == OrderStatus.PENDING)
            .map(order -> order.getMarketPrice().doubleValue() * order.getQuantity())
            .mapToDouble(Double::doubleValue)
            .sum();

        double saldoProyectado = saldo + totalPendientes; // O restar si así lo prefieres

        return BalanceSummaryResponse.builder()
            .availableBalance(saldo)
            .pendingBalance(totalPendientes)
            .totalBalance(saldoProyectado)
            .currency("COP") // Puedes cambiar la moneda si es necesario
            .build();
    }
}