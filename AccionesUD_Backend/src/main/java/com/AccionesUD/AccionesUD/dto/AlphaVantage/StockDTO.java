package com.AccionesUD.AccionesUD.dto.AlphaVantage;

import lombok.Data;

@Data
public class StockDTO {
    private String ticker;
    private String companyName;
    private String sector;
    private Double price;
    private Long volume;
    private Double marketCap;

    public StockDTO(String ticker, String companyName, String sector, Double price, Long volume, Double marketCap) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.sector = sector;
        this.price = price;
        this.volume = volume;
        this.marketCap = marketCap;
    }

    // Getters y setters si necesitas
}
