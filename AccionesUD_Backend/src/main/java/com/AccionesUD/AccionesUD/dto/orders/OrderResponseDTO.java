package com.AccionesUD.AccionesUD.dto.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;

import lombok.Data;
@Data

public class OrderResponseDTO {

    private Long id;
    private String username;
    private String symbol;
    private Integer quantity;
    private OrderType orderType;
    private BigDecimal limitPrice;
    private BigDecimal stopLossPrice;
    private BigDecimal takeProfitPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public OrderResponseDTO() {

    }

    public OrderResponseDTO(Long id,
                            String username,
                            String symbol,
                            Integer quantity,
                            OrderType orderType,
                            BigDecimal limitPrice,
                            BigDecimal stopLossPrice,
                            BigDecimal takeProfitPrice,
                            OrderStatus status,
                            LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.symbol = symbol;
        this.quantity = quantity;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.stopLossPrice = stopLossPrice;
        this.takeProfitPrice = takeProfitPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

}