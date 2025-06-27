package com.AccionesUD.AccionesUD.alpacaStream.domain.model;

public class StockPriceUpdate {
    private final String symbol;
    private final double price;
    private final long timestamp;

    public StockPriceUpdate(String symbol, double price, long timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    
}
