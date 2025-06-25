package com.AccionesUD.AccionesUD.application.orders;


import java.util.List;

import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;

public interface OrderService {
    
    List<OrderResponseDTO> listarTodasLasOrdenes();
    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);
    OrderResponseDTO ejecutarOrden(Long orderId);
    OrderResponseDTO rechazarOrdenPorLimite(Long orderId);
}
