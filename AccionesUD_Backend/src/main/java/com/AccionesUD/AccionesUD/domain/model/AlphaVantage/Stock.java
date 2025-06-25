package com.AccionesUD.AccionesUD.domain.model.AlphaVantage;

import lombok.Data;

@Data
public class Stock {
    private String ticker;
    private String companyName;
    private String sector;
    private Double price;
    private Long volume;
    private Long marketCap;

    // Constructors, getters, setters
    public Stock() {}

    public Stock(String ticker, String companyName, String sector, Double price, Long volume, Long marketCap) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.sector = sector;
        this.price = price;
        this.volume = volume;
        this.marketCap = marketCap;
    }

}
