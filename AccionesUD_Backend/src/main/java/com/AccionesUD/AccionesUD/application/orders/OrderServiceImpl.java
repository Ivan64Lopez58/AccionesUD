package com.AccionesUD.AccionesUD.application.orders;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.Order;
import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import com.AccionesUD.AccionesUD.repository.OrderRepository;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderValidator;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        // 1) Validar los datos de entrada según el tipo de orden
        OrderValidator.validate(requestDTO);

        // 2) Mapear DTO → entidad Order (se usarán campos comunes)
        Order orderEntity = modelMapper.map(requestDTO, Order.class);

        // 3) Inicializar campos técnicos
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setCreatedAt(LocalDateTime.now());

        // 4) Persistir en BD
        Order saved = orderRepository.save(orderEntity);

        // 5) Mapear entidad guardada → DTO de respuesta
        OrderResponseDTO responseDTO = modelMapper.map(saved, OrderResponseDTO.class);
        return responseDTO;
    }
}
