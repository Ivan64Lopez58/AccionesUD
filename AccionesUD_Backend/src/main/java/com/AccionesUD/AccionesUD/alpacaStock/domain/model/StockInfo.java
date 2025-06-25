package com.AccionesUD.AccionesUD.alpacaStock.domain.model;

public class StockInfo {
    private String symbol;
    private double price;
    private int size;

     public StockInfo(String symbol, double price, int size) {
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
