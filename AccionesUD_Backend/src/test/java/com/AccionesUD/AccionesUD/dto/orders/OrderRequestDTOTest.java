package com.AccionesUD.AccionesUD.dto.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;

public class OrderRequestDTOTest {
    @Test
    void testGettersAndSetters() {
        OrderResponseDTO dto = new OrderResponseDTO();

        Long id = 1L;
        String symbol = "AAPL";
        Integer quantity = 10;
        OrderType orderType = OrderType.LIMIT;
        BigDecimal limitPrice = BigDecimal.valueOf(150.25);
        BigDecimal stopLossPrice = BigDecimal.valueOf(145.00);
        BigDecimal takeProfitPrice = BigDecimal.valueOf(160.00);
        OrderStatus status = OrderStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();

        dto.setId(id);
        dto.setSymbol(symbol);
        dto.setQuantity(quantity);
        dto.setOrderType(orderType);
        dto.setLimitPrice(limitPrice);
        dto.setStopLossPrice(stopLossPrice);
        dto.setTakeProfitPrice(takeProfitPrice);
        dto.setStatus(status);
        dto.setCreatedAt(createdAt);

        assertEquals(id, dto.getId());
        assertEquals(symbol, dto.getSymbol());
        assertEquals(quantity, dto.getQuantity());
        assertEquals(orderType, dto.getOrderType());
        assertEquals(limitPrice, dto.getLimitPrice());
        assertEquals(stopLossPrice, dto.getStopLossPrice());
        assertEquals(takeProfitPrice, dto.getTakeProfitPrice());
        assertEquals(status, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 2L;
        String symbol = "TSLA";
        Integer quantity = 5;
        OrderType orderType = OrderType.MARKET;
        BigDecimal limitPrice = null;
        BigDecimal stopLossPrice = BigDecimal.valueOf(580.00);
        BigDecimal takeProfitPrice = BigDecimal.valueOf(620.00);
        OrderStatus status = OrderStatus.EXECUTED;
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        OrderResponseDTO dto = new OrderResponseDTO(
                id, symbol, quantity, orderType,
                limitPrice, stopLossPrice, takeProfitPrice,
                status, createdAt
        );

        assertEquals(id, dto.getId());
        assertEquals(symbol, dto.getSymbol());
        assertEquals(quantity, dto.getQuantity());
        assertEquals(orderType, dto.getOrderType());
        assertNull(dto.getLimitPrice());
        assertEquals(stopLossPrice, dto.getStopLossPrice());
        assertEquals(takeProfitPrice, dto.getTakeProfitPrice());
        assertEquals(status, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
    }
}
