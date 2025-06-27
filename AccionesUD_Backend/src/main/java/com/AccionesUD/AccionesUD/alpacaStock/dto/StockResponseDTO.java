package com.AccionesUD.AccionesUD.alpacaStock.dto;

public class StockResponseDTO {
    private String symbol;
    private double price;
    private int size;

    public StockResponseDTO(String symbol, double price, int size) {
        this.symbol = symbol;
        this.price = price;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }
}
