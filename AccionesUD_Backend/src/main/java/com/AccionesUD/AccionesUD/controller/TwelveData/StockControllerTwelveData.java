package com.AccionesUD.AccionesUD.controller.TwelveData;

import com.AccionesUD.AccionesUD.application.TwelveData.TwelveDataStockService;
import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks/twelve")
public class StockControllerTwelveData {

    private final TwelveDataStockService stockService;

    public StockControllerTwelveData(TwelveDataStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStock(@PathVariable String symbol) {
        return ResponseEntity.ok(stockService.getStockInfo(symbol));
    }
}
