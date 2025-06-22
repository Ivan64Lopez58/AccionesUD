package com.AccionesUD.AccionesUD.domain.model;

import com.AccionesUD.AccionesUD.domain.model.Order;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testGettersAndSetters() {
        Order order = new Order();

        Long id = 123L;
        String symbol = "AAPL";
        Integer quantity = 100;
        OrderType orderType = OrderType.LIMIT;
        BigDecimal limitPrice = new BigDecimal("150.50");
        BigDecimal stopLossPrice = new BigDecimal("140.00");
        BigDecimal takeProfitPrice = new BigDecimal("160.00");
        OrderStatus status = OrderStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();

        // Setters
        order.setSymbol(symbol);
        order.setQuantity(quantity);
        order.setOrderType(orderType);
        order.setLimitPrice(limitPrice);
        order.setStopLossPrice(stopLossPrice);
        order.setTakeProfitPrice(takeProfitPrice);
        order.setStatus(status);
        order.setCreatedAt(createdAt);

        // Getters
        assertNull(order.getId()); // ID is set by DB
        assertEquals(symbol, order.getSymbol());
        assertEquals(quantity, order.getQuantity());
        assertEquals(orderType, order.getOrderType());
        assertEquals(limitPrice, order.getLimitPrice());
        assertEquals(stopLossPrice, order.getStopLossPrice());
        assertEquals(takeProfitPrice, order.getTakeProfitPrice());
        assertEquals(status, order.getStatus());
        assertEquals(createdAt, order.getCreatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        String symbol = "TSLA";
        Integer quantity = 10;
        OrderType orderType = OrderType.STOP_LOSS;
        BigDecimal limitPrice = new BigDecimal("0.00"); // optional for this type
        BigDecimal stopLossPrice = new BigDecimal("200.00");
        BigDecimal takeProfitPrice = new BigDecimal("250.00");
        OrderStatus status = OrderStatus.EXECUTED;
        LocalDateTime createdAt = LocalDateTime.now();

        Order order = new Order(symbol, quantity, orderType, limitPrice, stopLossPrice, takeProfitPrice, status, createdAt);

        assertEquals(symbol, order.getSymbol());
        assertEquals(quantity, order.getQuantity());
        assertEquals(orderType, order.getOrderType());
        assertEquals(limitPrice, order.getLimitPrice());
        assertEquals(stopLossPrice, order.getStopLossPrice());
        assertEquals(takeProfitPrice, order.getTakeProfitPrice());
        assertEquals(status, order.getStatus());
        assertEquals(createdAt, order.getCreatedAt());
    }
}
