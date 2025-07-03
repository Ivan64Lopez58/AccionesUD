package com.AccionesUD.AccionesUD.application.orders;


import java.util.List;

import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;

public interface OrderService {
    
    List<OrderResponseDTO> listarTodasLasOrdenes();

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO, String username);

    OrderResponseDTO ejecutarOrden(Long orderId);

    OrderResponseDTO rechazarOrdenPorLimite(Long orderId);

    List<OrderResponseDTO> listarOrdenesPorUsuario(String username);

    long contarOrdenesPorUsuario(String username);

    List<OrderResponseDTO> listarOrdenesPorMercado(String market);

    List<OrderResponseDTO> listarOrdenesPorCompany(String company);

    OrderResponseDTO actualizarEstadoOrden(Long orderId, OrderStatus newStatus);
}
