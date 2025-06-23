package com.AccionesUD.AccionesUD.application.TwelveData;

import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;
import com.AccionesUD.AccionesUD.repository.TwelveData.TwelveDataApiClient;
import com.AccionesUD.AccionesUD.utilities.CachedStockData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class TwelveDataStockService {

    private final TwelveDataApiClient apiClient;
    private final Map<String, CachedStockData> cache = new ConcurrentHashMap<>();
    private final long ttlMillis = 60_000;

    public TwelveDataStockService(TwelveDataApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public StockDTO getStockInfo(String symbol) {
        CachedStockData cached = cache.get(symbol);
        if (cached != null && !cached.isExpired()) {
            System.out.println("[CACHE HIT] TwelveData para: " + symbol);
            return cached.getData();
        }

        System.out.println("[CACHE MISS] TwelveData API para: " + symbol);
        JSONObject quote = apiClient.fetchQuote(symbol);

        if (quote.has("code")) {
            throw new RuntimeException("Error: " + quote.optString("message"));
        }

        String ticker = quote.optString("symbol", symbol);
        String name = quote.optString("name", "N/A");
        String exchange = quote.optString("exchange", "N/A");
        Double price = quote.has("close") ? Double.parseDouble(quote.getString("close")) : null;
        Long volume = quote.has("volume") ? Long.parseLong(quote.getString("volume")) : null;

        StockDTO dto = new StockDTO(ticker, name, exchange, price, volume, null);

        cache.put(symbol, new CachedStockData(dto, ttlMillis));
        System.out.println("[CACHE STORE] TwelveData: " + symbol);
        return dto;
    }
}
