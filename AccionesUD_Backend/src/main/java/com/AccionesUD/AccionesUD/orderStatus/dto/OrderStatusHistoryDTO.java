package com.AccionesUD.AccionesUD.orderStatus.dto;

import java.time.LocalDateTime;

import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;

public class OrderStatusHistoryDTO {
    private OrderStatus status;
    private LocalDateTime changedAt;

    public OrderStatusHistoryDTO(OrderStatus status, LocalDateTime changedAt) {
        this.status = status;
        this.changedAt = changedAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
