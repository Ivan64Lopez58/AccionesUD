package com.AccionesUD.AccionesUD.controller.AlphaVantage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.AccionesUD.AccionesUD.repository.AlphaVantage.StockApiClient;

@RestController
@RequestMapping("/api/stocks/search")
@RequiredArgsConstructor
public class SymbolSearchController {

    private final StockApiClient stockApiClient;

    @GetMapping("/{keyword}")
    public ResponseEntity<?> searchSymbol(@PathVariable String keyword) {
        try {
            JSONObject response = stockApiClient.searchSymbol(keyword);
            JSONArray matches = response.optJSONArray("bestMatches");
            return ResponseEntity.ok(matches != null ? matches.toList() : "No matches found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar símbolo: " + e.getMessage());
        }
    }
}
