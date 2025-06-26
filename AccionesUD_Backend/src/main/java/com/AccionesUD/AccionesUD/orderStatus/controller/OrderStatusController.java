package com.AccionesUD.AccionesUD.orderStatus.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AccionesUD.AccionesUD.orderStatus.application.OrderStatusService;
import com.AccionesUD.AccionesUD.orderStatus.dto.OrderStatusHistoryDTO;

@RestController
@RequestMapping("/api/order-history")
public class OrderStatusController {
    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderStatusHistoryDTO>> getOrderStatusHistory(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderStatusService.getHistoryByOrderId(orderId));
    }
}
