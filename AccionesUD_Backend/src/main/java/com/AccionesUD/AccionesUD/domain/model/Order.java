package com.AccionesUD.AccionesUD.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Column(precision = 19, scale = 4)
    private BigDecimal limitPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal stopLossPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal takeProfitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Order() {
        
    }

    public Order(String symbol,
                 Integer quantity,
                 OrderType orderType,
                 BigDecimal limitPrice,
                 BigDecimal stopLossPrice,
                 BigDecimal takeProfitPrice,
                 OrderStatus status,
                 LocalDateTime createdAt) {
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