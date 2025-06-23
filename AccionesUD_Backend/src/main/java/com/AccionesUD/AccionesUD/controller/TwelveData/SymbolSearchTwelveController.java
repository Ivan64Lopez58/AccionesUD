package com.AccionesUD.AccionesUD.controller.TwelveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.AccionesUD.AccionesUD.repository.TwelveData.TwelveDataApiClient;

@RestController
@RequestMapping("/api/stocks/twelve/search")
public class SymbolSearchTwelveController {

    private final TwelveDataApiClient apiClient;

    public SymbolSearchTwelveController(TwelveDataApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<?> search(@PathVariable String keyword, @RequestParam(required = false) String country, @RequestParam(required = false) String mic) {
        try {
            JSONObject response = apiClient.searchSymbol(keyword);
            JSONArray data = response.optJSONArray("data");
            JSONArray filtradas = new JSONArray();

            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    boolean matchCountry = (country == null || country.equalsIgnoreCase(item.optString("country")));
                    boolean matchMic = (mic == null || mic.equalsIgnoreCase(item.optString("mic_code")));
                    if (matchCountry && matchMic) {
                        filtradas.put(item);
                    }
                }
            }

            return ResponseEntity.ok(filtradas.toString(2));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
