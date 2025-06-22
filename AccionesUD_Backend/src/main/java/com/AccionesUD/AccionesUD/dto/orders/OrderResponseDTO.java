package com.AccionesUD.AccionesUD.dto.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;

public class OrderResponseDTO {

    private Long id;
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
                            String symbol,
                            Integer quantity,
                            OrderType orderType,
                            BigDecimal limitPrice,
                            BigDecimal stopLossPrice,
                            BigDecimal takeProfitPrice,
                            OrderStatus status,
                            LocalDateTime createdAt) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.orderType = orderType;
        this.limitPrice = limitPrice;
        this.stopLossPrice = stopLossPrice;
        this.takeProfitPrice = takeProfitPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public BigDecimal getTakeProfitPrice() {
        return takeProfitPrice;
    }

    public void setTakeProfitPrice(BigDecimal takeProfitPrice) {
        this.takeProfitPrice = takeProfitPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
