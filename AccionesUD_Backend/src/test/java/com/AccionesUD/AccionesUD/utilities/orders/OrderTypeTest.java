package com.AccionesUD.AccionesUD.utilities.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTypeTest {

    @Test
    void allOrderTypesExist() {
        assertNotNull(OrderType.valueOf("MARKET"));
        assertNotNull(OrderType.valueOf("LIMIT"));
        assertNotNull(OrderType.valueOf("STOP_LOSS"));
        assertNotNull(OrderType.valueOf("TAKE_PROFIT"));
    }
}
