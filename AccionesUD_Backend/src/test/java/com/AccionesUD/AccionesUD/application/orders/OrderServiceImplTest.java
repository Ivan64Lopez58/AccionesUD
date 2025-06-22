package com.AccionesUD.AccionesUD.application.orders;

import com.AccionesUD.AccionesUD.domain.model.Order;
import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import com.AccionesUD.AccionesUD.dto.orders.OrderResponseDTO;
import com.AccionesUD.AccionesUD.repository.OrderRepository;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;
import com.AccionesUD.AccionesUD.utilities.orders.OrderValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        // Simular comportamiento estático de OrderValidator (si fuera necesario en tests avanzados con frameworks como PowerMockito)
    }

   @Test
void createOrder_shouldReturnMappedResponseDTO() {
    // Arrange
    OrderRequestDTO requestDTO = new OrderRequestDTO();
    requestDTO.setSymbol("AAPL");
    requestDTO.setQuantity(10);
    requestDTO.setOrderType(OrderType.MARKET); // si aplica

    Order mappedOrder = new Order();
    mappedOrder.setSymbol("AAPL");
    mappedOrder.setQuantity(10);
    mappedOrder.setStatus(OrderStatus.PENDING);

    Order savedOrder = new Order();
    savedOrder.setId(1L);
    savedOrder.setSymbol("AAPL");
    savedOrder.setQuantity(10);
    savedOrder.setStatus(OrderStatus.PENDING);

    OrderResponseDTO responseDTO = new OrderResponseDTO();
    responseDTO.setId(1L);
    responseDTO.setSymbol("AAPL");
    responseDTO.setQuantity(10);
    responseDTO.setStatus(OrderStatus.PENDING);

    when(modelMapper.map(requestDTO, Order.class)).thenReturn(mappedOrder);
    when(orderRepository.save(mappedOrder)).thenReturn(savedOrder);
    when(modelMapper.map(savedOrder, OrderResponseDTO.class)).thenReturn(responseDTO);

    // Act
    OrderResponseDTO result = orderService.createOrder(requestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("AAPL", result.getSymbol());
    assertEquals(10, result.getQuantity());
    assertEquals(OrderStatus.PENDING, result.getStatus());

    verify(orderRepository).save(mappedOrder);
    verify(modelMapper).map(requestDTO, Order.class);
    verify(modelMapper).map(savedOrder, OrderResponseDTO.class);
}
}
