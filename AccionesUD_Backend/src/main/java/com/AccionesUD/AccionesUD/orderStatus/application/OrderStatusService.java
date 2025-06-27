package com.AccionesUD.AccionesUD.orderStatus.application;

import java.util.List;

import com.AccionesUD.AccionesUD.orderStatus.dto.OrderStatusHistoryDTO;

public interface OrderStatusService {
    List<OrderStatusHistoryDTO> getHistoryByOrderId(Long orderId);
}
