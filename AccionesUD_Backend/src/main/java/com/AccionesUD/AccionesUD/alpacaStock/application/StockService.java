package com.AccionesUD.AccionesUD.alpacaStock.application;

import com.AccionesUD.AccionesUD.alpacaStock.domain.model.StockInfo;

public interface StockService {
    StockInfo getLatestTrade(String symbol);
}
