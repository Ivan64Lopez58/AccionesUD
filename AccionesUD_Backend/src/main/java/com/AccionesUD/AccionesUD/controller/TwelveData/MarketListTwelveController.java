package com.AccionesUD.AccionesUD.controller.TwelveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AccionesUD.AccionesUD.repository.TwelveData.TwelveDataApiClient;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/api/stocks/twelve/markets")
public class MarketListTwelveController {

    private final TwelveDataApiClient apiClient;

    public MarketListTwelveController(TwelveDataApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping
    public ResponseEntity<?> getMarkets(@RequestParam(defaultValue = "toyota") String keyword) {
        try {
            JSONObject response = apiClient.searchSymbol(keyword);
            JSONArray data = response.optJSONArray("data");

            Set<String> countries = new TreeSet<>();
            Set<String> micCodes = new TreeSet<>();

            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    countries.add(item.optString("country", "Unknown"));
                    micCodes.add(item.optString("mic_code", "Unknown"));
                }
            }

            JSONObject result = new JSONObject();
            result.put("countries", countries);
            result.put("mic_codes", micCodes);

            return ResponseEntity.ok(result.toString(2));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener mercados: " + e.getMessage());
        }
    }
}
