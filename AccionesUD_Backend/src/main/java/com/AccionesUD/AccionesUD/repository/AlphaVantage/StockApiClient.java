package com.AccionesUD.AccionesUD.repository.AlphaVantage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;


import java.util.Iterator;

@Repository
public class StockApiClient {

    private final RestTemplate restTemplate;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    public StockApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JSONObject fetchOverview(String symbol) {
        String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol + "&apikey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return new JSONObject(response);
    }
public JSONObject fetchDailyPrice(String symbol) {
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey;
    String response = restTemplate.getForObject(url, String.class);
    return new JSONObject(response);
}

public JSONObject searchSymbol(String keyword) {
    String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + keyword + "&apikey=" + apiKey;
    String response = restTemplate.getForObject(url, String.class);
    return new JSONObject(response);
}

}
