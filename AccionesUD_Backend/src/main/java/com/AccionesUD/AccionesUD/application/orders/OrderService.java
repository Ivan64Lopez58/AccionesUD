package com.AccionesUD.AccionesUD.application.orders;


import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    OrderResponseDTO ejecutarOrden(Long orderId);
    OrderResponseDTO rechazarOrdenPorLimite(Long orderId);
}
