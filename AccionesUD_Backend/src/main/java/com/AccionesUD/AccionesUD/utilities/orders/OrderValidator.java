package com.AccionesUD.AccionesUD.utilities.orders;

import com.AccionesUD.AccionesUD.dto.orders.OrderRequestDTO;

public class OrderValidator {

    public static void validate(OrderRequestDTO dto) {
        if (dto.getSymbol() == null || dto.getSymbol().trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'symbol' es obligatorio y no puede estar vacío.");
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad ('quantity') debe ser mayor que cero.");
        }
        if (dto.getOrderType() == null) {
            throw new IllegalArgumentException("El campo 'orderType' es obligatorio.");
        }

        switch (dto.getOrderType()) {
            case MARKET:
                if (dto.getLimitPrice() != null) {
                    throw new IllegalArgumentException("Para Market orders, NO se debe enviar 'limitPrice'.");
                }
                if (dto.getStopLossPrice() != null) {
                    throw new IllegalArgumentException("Para Market orders, NO se debe enviar 'stopLossPrice'.");
                }
                if (dto.getTakeProfitPrice() != null) {
                    throw new IllegalArgumentException("Para Market orders, NO se debe enviar 'takeProfitPrice'.");
                }
                break;

            case LIMIT:
                if (dto.getLimitPrice() == null) {
                    throw new IllegalArgumentException("Para Limit orders, 'limitPrice' es obligatorio.");
                }
                if (dto.getLimitPrice().doubleValue() <= 0) {
                    throw new IllegalArgumentException("'limitPrice' debe ser mayor que cero.");
                }
                if (dto.getStopLossPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'stopLossPrice' en una Limit order.");
                }
                if (dto.getTakeProfitPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'takeProfitPrice' en una Limit order.");
                }
                break;

            case STOP_LOSS:
                if (dto.getStopLossPrice() == null) {
                    throw new IllegalArgumentException("Para Stop Loss orders, 'stopLossPrice' es obligatorio.");
                }
                if (dto.getStopLossPrice().doubleValue() <= 0) {
                    throw new IllegalArgumentException("'stopLossPrice' debe ser mayor que cero.");
                }
                if (dto.getLimitPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'limitPrice' en una Stop Loss order.");
                }
                if (dto.getTakeProfitPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'takeProfitPrice' en una Stop Loss order.");
                }
                break;

            case TAKE_PROFIT:
                if (dto.getTakeProfitPrice() == null) {
                    throw new IllegalArgumentException("Para Take Profit orders, 'takeProfitPrice' es obligatorio.");
                }
                if (dto.getTakeProfitPrice().doubleValue() <= 0) {
                    throw new IllegalArgumentException("'takeProfitPrice' debe ser mayor que cero.");
                }
                if (dto.getLimitPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'limitPrice' en una Take Profit order.");
                }
                if (dto.getStopLossPrice() != null) {
                    throw new IllegalArgumentException("No se debe enviar 'stopLossPrice' en una Take Profit order.");
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de orden desconocido: " + dto.getOrderType());
        }
    }
}
