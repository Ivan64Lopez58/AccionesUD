package com.AccionesUD.AccionesUD.utilities.orders;

import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private OrderRequestDTO baseDTO() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setSymbol("AAPL");
        dto.setQuantity(10);
        return dto;
    }

    @Test
    void validateMarketOrder_Success() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.MARKET);
        OrderValidator.validate(dto);
    }

    @Test
    void validateLimitOrder_Success() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.LIMIT);
        dto.setLimitPrice(BigDecimal.valueOf(150));
        OrderValidator.validate(dto);
    }

    @Test
    void validateStopLossOrder_Success() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.STOP_LOSS);
        dto.setStopLossPrice(BigDecimal.valueOf(140));
        OrderValidator.validate(dto);
    }

    @Test
    void validateTakeProfitOrder_Success() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.TAKE_PROFIT);
        dto.setTakeProfitPrice(BigDecimal.valueOf(160));
        OrderValidator.validate(dto);
    }

    @Test
    void validate_ThrowsOnMissingSymbol() {
        OrderRequestDTO dto = baseDTO();
        dto.setSymbol(" ");
        dto.setOrderType(OrderType.MARKET);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertEquals("El campo 'symbol' es obligatorio y no puede estar vacío.", ex.getMessage());
    }

    @Test
    void validate_ThrowsOnInvalidQuantity() {
        OrderRequestDTO dto = baseDTO();
        dto.setQuantity(0);
        dto.setOrderType(OrderType.MARKET);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertEquals("La cantidad ('quantity') debe ser mayor que cero.", ex.getMessage());
    }

    @Test
    void validateLimitOrder_ThrowsIfMissingLimitPrice() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.LIMIT);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertEquals("Para Limit orders, 'limitPrice' es obligatorio.", ex.getMessage());
    }

    @Test
    void validateMarketOrder_ThrowsIfLimitPricePresent() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.MARKET);
        dto.setLimitPrice(BigDecimal.valueOf(100));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertTrue(ex.getMessage().contains("Market"));
    }

    @Test
    void validateStopLossOrder_ThrowsIfNegativePrice() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.STOP_LOSS);
        dto.setStopLossPrice(BigDecimal.valueOf(-1));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertEquals("'stopLossPrice' debe ser mayor que cero.", ex.getMessage());
    }

    @Test
    void validateTakeProfitOrder_ThrowsIfExtraField() {
        OrderRequestDTO dto = baseDTO();
        dto.setOrderType(OrderType.TAKE_PROFIT);
        dto.setTakeProfitPrice(BigDecimal.valueOf(200));
        dto.setLimitPrice(BigDecimal.valueOf(150));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
        assertEquals("No se debe enviar 'limitPrice' en una Take Profit order.", ex.getMessage());
    }

    @Test
void validate_ThrowsOnNullOrderType() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(null);
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("El campo 'orderType' es obligatorio.", ex.getMessage());
}

@Test
void validateLimitOrder_ThrowsIfLimitPriceIsZero() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.LIMIT);
    dto.setLimitPrice(BigDecimal.ZERO);
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("'limitPrice' debe ser mayor que cero.", ex.getMessage());
}

@Test
void validateStopLossOrder_ThrowsIfLimitPricePresent() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.STOP_LOSS);
    dto.setStopLossPrice(BigDecimal.valueOf(100));
    dto.setLimitPrice(BigDecimal.valueOf(90));
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("No se debe enviar 'limitPrice' en una Stop Loss order.", ex.getMessage());
}

@Test
void validateStopLossOrder_ThrowsIfTakeProfitPresent() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.STOP_LOSS);
    dto.setStopLossPrice(BigDecimal.valueOf(100));
    dto.setTakeProfitPrice(BigDecimal.valueOf(150));
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("No se debe enviar 'takeProfitPrice' en una Stop Loss order.", ex.getMessage());
}

@Test
void validateTakeProfitOrder_ThrowsIfStopLossPresent() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.TAKE_PROFIT);
    dto.setTakeProfitPrice(BigDecimal.valueOf(150));
    dto.setStopLossPrice(BigDecimal.valueOf(120));
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("No se debe enviar 'stopLossPrice' en una Take Profit order.", ex.getMessage());
}

@Test
void validateMarketOrder_ThrowsIfStopLossPresent() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.MARKET);
    dto.setStopLossPrice(BigDecimal.valueOf(99));
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("Para Market orders, NO se debe enviar 'stopLossPrice'.", ex.getMessage());
}

@Test
void validateMarketOrder_ThrowsIfTakeProfitPresent() {
    OrderRequestDTO dto = baseDTO();
    dto.setOrderType(OrderType.MARKET);
    dto.setTakeProfitPrice(BigDecimal.valueOf(120));
    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("Para Market orders, NO se debe enviar 'takeProfitPrice'.", ex.getMessage());
}

@Test
void validate_ThrowsOnUnknownOrderType() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");         // Obligatorio
    dto.setQuantity(10);           // Obligatorio
    dto.setOrderType(OrderType.UNKNOWN);  // Orden inválida

    Exception ex = assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(dto));
    assertEquals("Tipo de orden desconocido: UNKNOWN", ex.getMessage());
}


@Test
void validate_LimitOrderWithStopLossPrice_ShouldThrow() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");
    dto.setQuantity(1);
    dto.setOrderType(OrderType.LIMIT);
    dto.setLimitPrice(new BigDecimal("150.00"));
    dto.setStopLossPrice(new BigDecimal("140.00")); // No permitido

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> OrderValidator.validate(dto)
    );

    assertEquals("No se debe enviar 'stopLossPrice' en una Limit order.", ex.getMessage());
}

@Test
void validate_LimitOrderWithTakeProfitPrice_ShouldThrow() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");
    dto.setQuantity(1);
    dto.setOrderType(OrderType.LIMIT);
    dto.setLimitPrice(new BigDecimal("150.00"));
    dto.setTakeProfitPrice(new BigDecimal("160.00")); // No permitido

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> OrderValidator.validate(dto)
    );

    assertEquals("No se debe enviar 'takeProfitPrice' en una Limit order.", ex.getMessage());
}

@Test
void validate_StopLossOrderWithoutPrice_ShouldThrow() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");
    dto.setQuantity(1);
    dto.setOrderType(OrderType.STOP_LOSS);
    dto.setStopLossPrice(null); // Falta obligatorio

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> OrderValidator.validate(dto)
    );

    assertEquals("Para Stop Loss orders, 'stopLossPrice' es obligatorio.", ex.getMessage());
}


@Test
void validate_LimitOrderWithoutLimitPrice_ShouldThrow() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");
    dto.setQuantity(1);
    dto.setOrderType(OrderType.LIMIT);
    dto.setLimitPrice(null); // Falta obligatorio

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> OrderValidator.validate(dto)
    );

    assertEquals("Para Limit orders, 'limitPrice' es obligatorio.", ex.getMessage());
}

@Test
void validate_TakeProfitOrderWithoutPrice_ShouldThrow() {
    OrderRequestDTO dto = new OrderRequestDTO();
    dto.setSymbol("AAPL");
    dto.setQuantity(1);
    dto.setOrderType(OrderType.TAKE_PROFIT);
    dto.setTakeProfitPrice(null); // Falta obligatorio

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> OrderValidator.validate(dto)
    );

    assertEquals("Para Take Profit orders, 'takeProfitPrice' es obligatorio.", ex.getMessage());
}


}
