package com.AccionesUD.AccionesUD.dto.AlphaVantage;

import lombok.Data;

@Data
public class StockDTO {
    public String ticker;
    public String companyName;
    public String sector;
    public Double price;
    public Long volume;
    public Long marketCap;

    public StockDTO(String ticker, String companyName, String sector, Double price, Long volume, Long marketCap) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.sector = sector;
        this.price = price;
        this.volume = volume;
        this.marketCap = marketCap;
    }

    // Getters y setters si necesitas
}
