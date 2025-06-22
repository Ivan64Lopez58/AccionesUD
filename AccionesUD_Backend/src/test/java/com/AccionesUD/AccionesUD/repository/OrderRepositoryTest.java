package com.AccionesUD.AccionesUD.repository;

import com.AccionesUD.AccionesUD.domain.model.Order;
import com.AccionesUD.AccionesUD.utilities.orders.OrderStatus;
import com.AccionesUD.AccionesUD.utilities.orders.OrderType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order buildOrder() {
        return new Order(
                "AAPL",
                10,
                OrderType.MARKET,
                new BigDecimal("150.00"),
                new BigDecimal("140.00"),
                new BigDecimal("160.00"),
                OrderStatus.PENDING,
                LocalDateTime.now()
        );
    }

    @Test
    void testSaveAndFindById() {
        Order saved = orderRepository.save(buildOrder());
        Optional<Order> result = orderRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getSymbol());
    }

    @Test
    void testDeleteById() {
        Order saved = orderRepository.save(buildOrder());
        Long id = saved.getId();

        orderRepository.deleteById(id);
        Optional<Order> result = orderRepository.findById(id);

        assertFalse(result.isPresent());
    }
}
