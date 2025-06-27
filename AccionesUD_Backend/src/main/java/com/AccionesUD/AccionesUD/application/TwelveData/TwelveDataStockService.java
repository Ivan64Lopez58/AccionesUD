package com.AccionesUD.AccionesUD.application.TwelveData;

import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;
import com.AccionesUD.AccionesUD.repository.TwelveData.TwelveDataApiClient;
import com.AccionesUD.AccionesUD.utilities.CachedStockData;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TwelveDataStockService {

    private final TwelveDataApiClient apiClient;
    private final Map<String, CachedStockData> cache = new ConcurrentHashMap<>();
    private final long ttlMillis = 60_000; // 1 minuto

    public TwelveDataStockService(TwelveDataApiClient apiClient) {
        this.apiClient = apiClient;
    }

public StockDTO getStockInfo(String symbol) {
    CachedStockData cached = cache.get(symbol);
    if (cached != null && !cached.isExpired()) {
        System.out.println("[CACHE HIT] TwelveData: " + symbol);
        return cached.getData();
    }

    System.out.println("[CACHE MISS] TwelveData: " + symbol);
    JSONObject quote = apiClient.fetchQuote(symbol);

    if (quote.has("code")) {
        throw new RuntimeException("Error al obtener cotización: " + quote.optString("message"));
    }

    String ticker = quote.optString("symbol", symbol);
    String companyName = quote.optString("name", "N/A");
    String sector = quote.optString("exchange", "N/A"); // Usa exchange como sector si no hay uno mejor
    Double price = quote.has("close") ? Double.parseDouble(quote.getString("close")) : null;
    Long volume = quote.has("volume") ? Long.parseLong(quote.getString("volume")) : null;
    Double marketCap = quote.has("market_cap") ? Double.parseDouble(quote.getString("market_cap")) : null;

    StockDTO dto = new StockDTO(ticker, companyName, sector, price, volume, marketCap);

    cache.put(symbol, new CachedStockData(dto, ttlMillis));
    return dto;
}

}
