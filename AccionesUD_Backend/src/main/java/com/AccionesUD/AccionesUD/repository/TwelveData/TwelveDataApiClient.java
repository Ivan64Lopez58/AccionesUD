package com.AccionesUD.AccionesUD.repository.TwelveData;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Repository
public class TwelveDataApiClient {

    private final RestTemplate restTemplate;

    @Value("${twelvedata.api.key}")
    private String apiKey;

    public TwelveDataApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JSONObject fetchQuote(String symbol) {
        String url = "https://api.twelvedata.com/quote?symbol=" + symbol + "&apikey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return new JSONObject(response);
    }

    public JSONObject searchSymbol(String keyword) {
        String url = "https://api.twelvedata.com/symbol_search?symbol=" + keyword + "&apikey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return new JSONObject(response);
    }
}
