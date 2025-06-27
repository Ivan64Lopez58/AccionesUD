package com.AccionesUD.AccionesUD.utilities;

import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;

public class CachedStockData {
    private final StockDTO data;
    private final long expirationTimeMillis;

    public CachedStockData(StockDTO data, long ttlMillis) {
        this.data = data;
        this.expirationTimeMillis = System.currentTimeMillis() + ttlMillis;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTimeMillis;
    }

    public StockDTO getData() {
        return data;
    }
}
