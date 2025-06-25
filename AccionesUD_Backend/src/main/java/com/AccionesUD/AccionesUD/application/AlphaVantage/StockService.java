package com.AccionesUD.AccionesUD.application.AlphaVantage;

import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;

public interface StockService {
    StockDTO getStockInfo(String symbol);
}