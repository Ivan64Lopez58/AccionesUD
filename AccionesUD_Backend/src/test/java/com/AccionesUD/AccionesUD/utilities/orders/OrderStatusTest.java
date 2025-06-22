package com.AccionesUD.AccionesUD.utilities.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void allOrderStatusesExist() {
        assertNotNull(OrderStatus.valueOf("PENDING"));
        assertNotNull(OrderStatus.valueOf("SENT"));
        assertNotNull(OrderStatus.valueOf("EXECUTED"));
        assertNotNull(OrderStatus.valueOf("CANCELLED"));
        assertNotNull(OrderStatus.valueOf("REJECTED"));
        assertNotNull(OrderStatus.valueOf("EXPIRED"));
        assertNotNull(OrderStatus.valueOf("CREATED"));
    }
}
