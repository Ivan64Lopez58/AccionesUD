package com.AccionesUD.AccionesUD.application.orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.model.Order;
import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import com.AccionesUD.AccionesUD.repository.OrderRepository;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderValidator;
import org.springframework.context.ApplicationEventPublisher;
import com.AccionesUD.AccionesUD.domain.model.notification.NotificationEvent;
import com.AccionesUD.AccionesUD.domain.model.notification.NotificationType;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        
        // 1) Validar los datos de entrada según el tipo de orden
        OrderValidator.validate(requestDTO);

        // 2) Mapear DTO → entidad Order (se usarán campos comunes)
        Order orderEntity = modelMapper.map(requestDTO, Order.class);


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println("-----------------------------"+username);
        orderEntity.setUsername(username);

        // 3) Inicializar campos técnicos
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setCreatedAt(LocalDateTime.now());

        // 4) Persistir en BD
        Order saved = orderRepository.save(orderEntity);

        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                username,
                NotificationType.ORDEN_CREADA, // Asegúrate de que exista en el enum
                "Orden creada",
                "Has creado una orden del tipo " + requestDTO.getOrderType()
            )
        );

        // 5) Mapear entidad guardada → DTO de respuesta
        OrderResponseDTO responseDTO = modelMapper.map(saved, OrderResponseDTO.class);
        return responseDTO;
    }

    @Override
    public OrderResponseDTO ejecutarOrden(Long orderId) {
        Order orden = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));

        orden.setStatus(OrderStatus.EXECUTED);
        Order actualizada = orderRepository.save(orden);

        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                actualizada.getUsername(),
                NotificationType.ORDEN_EJECUTADA,
                "Orden ejecutada",
                "Tu orden para el símbolo " + actualizada.getSymbol() + " fue ejecutada correctamente."
            )
        );

        return modelMapper.map(actualizada, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO rechazarOrdenPorLimite(Long orderId) {
        Order orden = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));

        orden.setStatus(OrderStatus.REJECTED);
        Order actualizada = orderRepository.save(orden);

        eventPublisher.publishEvent(
            new NotificationEvent(
                this,
                actualizada.getUsername(),
                NotificationType.ORDEN_RECHAZADA,
                "Orden rechazada",
                "Tu orden para el símbolo " + actualizada.getSymbol() + " fue rechazada por superar tu límite diario de órdenes."
            )
        );

        return modelMapper.map(actualizada, OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> listarTodasLasOrdenes() {
        return orderRepository.findAll().stream()
            .map(order -> {
                OrderResponseDTO dto = modelMapper.map(order, OrderResponseDTO.class);
                return dto;
            })
            .collect(Collectors.toList());
    }

}
