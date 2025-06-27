package com.AccionesUD.AccionesUD.orderStatus.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.orderStatus.application.OrderStatusService;
import com.AccionesUD.AccionesUD.orderStatus.domain.model.OrderStatusHistory;
import com.AccionesUD.AccionesUD.orderStatus.dto.OrderStatusHistoryDTO;
import com.AccionesUD.AccionesUD.orderStatus.repository.OrderStatusHistoryRepository;

@Service
public class OrderStatusServiceImpl implements OrderStatusService{
    private final OrderStatusHistoryRepository historyRepository;

    public OrderStatusServiceImpl(OrderStatusHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<OrderStatusHistoryDTO> getHistoryByOrderId(Long orderId) {
        List<OrderStatusHistory> historyList = historyRepository.findByOrderIdOrderByChangedAtAsc(orderId);
        return historyList.stream()
                .map(history -> new OrderStatusHistoryDTO(
                        history.getStatus(),
                        history.getChangedAt()))
                .collect(Collectors.toList());
    }
}
