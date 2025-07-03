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

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Quién coloca la orden */
    @Column(nullable = false)
    private String username;

    /** Mercado o bolsa (NASDAQ, NYSE, etc.) */
    @Column(nullable = false)
    private String market;

    @Column(nullable = false)
    private String symbol;

    /** Empresa / ticker (AAPL, MSFT, etc.) */
    @Column(nullable = false)
    private String company;

    /** Precio de mercado al crear la orden */
    @Column(nullable = false)
    private Double marketPrice;

    /** Cantidad de acciones */
    @Column(nullable = false)
    private Double quantity;

    /** Tipo de orden */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    /** Precio límite (solo para LIMIT) */
    @Column(precision = 19, scale = 4)
    private BigDecimal limitPrice;

    /** Precio stop loss (solo para STOP_LOSS) */
    @Column(precision = 19, scale = 4)
    private BigDecimal stopLossPrice;

    /** Precio take profit (solo para TAKE_PROFIT) */
    @Column(precision = 19, scale = 4)
    private BigDecimal takeProfitPrice;

    /** Estado de la orden */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /** Fecha y hora de creación */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Order() {}

    public Order(String username,
                 String market,
                 String symbol,
                 String company,
                 Double marketPrice,
                 Double quantity,
                 OrderType orderType,
                 BigDecimal limitPrice,
                 BigDecimal stopLossPrice,
                 BigDecimal takeProfitPrice, 
                 OrderStatus status,
                 LocalDateTime createdAt) {
        this.username         = username;
        this.market           = market;
        this.company          = company;
        this.marketPrice      = marketPrice;
        this.quantity         = quantity;
        this.orderType        = orderType;
        this.limitPrice       = limitPrice;
        this.stopLossPrice    = stopLossPrice;
        this.takeProfitPrice  = takeProfitPrice;
        this.status           = status;
        this.createdAt        = createdAt;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getMarketPrice() {
        return marketPrice;
    } 

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }



}