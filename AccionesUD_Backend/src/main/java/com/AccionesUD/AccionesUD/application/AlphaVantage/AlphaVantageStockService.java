package com.AccionesUD.AccionesUD.application.AlphaVantage;

import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;
import com.AccionesUD.AccionesUD.repository.AlphaVantage.StockApiClient;
import com.AccionesUD.AccionesUD.utilities.CachedStockData;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class AlphaVantageStockService implements StockService {

    private final StockApiClient stockApiClient;
    private final Map<String, CachedStockData> cache = new ConcurrentHashMap<>();
    private final long ttlMillis = 60_000; // 1 minuto

    public AlphaVantageStockService(StockApiClient stockApiClient) {
        this.stockApiClient = stockApiClient;
    }

    @Override
    public StockDTO getStockInfo(String symbol) {
        // Revisa si está en caché
        CachedStockData cached = cache.get(symbol);
        if (cached != null && !cached.isExpired()) {
            System.out.println("[CACHE HIT] Usando datos en caché para: " + symbol);
            return cached.getData();
        }

        System.out.println("[CACHE MISS] Llamando a la API externa de Alpha Vantage para: " + symbol);

        // Si no está o está expirado, consulta la API y guarda
        var overview = stockApiClient.fetchOverview(symbol);
        var prices = stockApiClient.fetchDailyPrice(symbol);

        if (!prices.has("Time Series (Daily)")) {
            String errorMsg = prices.has("Note") ? prices.getString("Note") :
                              prices.has("Information") ? prices.getString("Information") :
                              prices.has("Error Message") ? prices.getString("Error Message") :
                              "Respuesta inesperada.";
            System.out.println("[API ERROR] Alpha Vantage respuesta inválida: " + prices.toString(2));
            throw new RuntimeException("Error al obtener precios para el símbolo '" + symbol + "': " + errorMsg);
        }

        var series = prices.getJSONObject("Time Series (Daily)");
        String mostRecentKey = series.keySet().stream()
            .sorted(Comparator.reverseOrder())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No hay datos disponibles"));

        var lastData = series.getJSONObject(mostRecentKey);
        Double price = Double.parseDouble(lastData.getString("4. close"));
        Long volume = Long.parseLong(lastData.getString("5. volume"));

        String name = overview.optString("Name", "N/A");
        String sector = overview.optString("Sector", "N/A");
        Long marketCap = overview.has("MarketCapitalization")
                         ? Long.parseLong(overview.getString("MarketCapitalization"))
                         : null;

        StockDTO dto = new StockDTO(symbol, name, sector, price, volume, marketCap);

        // Guardar en caché
        cache.put(symbol, new CachedStockData(dto, ttlMillis));
        System.out.println("[CACHE STORE] Datos guardados en caché para: " + symbol);

        return dto;
    }
}
