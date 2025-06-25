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
    private String market;
    private String company;
    private String symbol;
    private BigDecimal marketPrice;
    private Integer quantity;
    private OrderType orderType;
    private BigDecimal limitPrice;
    private BigDecimal stopLossPrice;
    private BigDecimal takeProfitPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    // Constructor completo
    public OrderResponseDTO(
        Long id,
        String username,
        String market,
        String company,
        String symbol,
        BigDecimal marketPrice,
        Integer quantity,
        OrderType orderType,
        BigDecimal limitPrice,
        BigDecimal stopLossPrice,
        BigDecimal takeProfitPrice,
        OrderStatus status,
        LocalDateTime createdAt
    ) {
        this.id               = id;
        this.username         = username;
        this.market           = market;
        this.company          = company;
        this.symbol           = symbol;
        this.marketPrice      = marketPrice;
        this.quantity         = quantity;
        this.orderType        = orderType;
        this.limitPrice       = limitPrice;
        this.stopLossPrice    = stopLossPrice;
        this.takeProfitPrice  = takeProfitPrice;
        this.status           = status;
        this.createdAt        = createdAt;
    }
}