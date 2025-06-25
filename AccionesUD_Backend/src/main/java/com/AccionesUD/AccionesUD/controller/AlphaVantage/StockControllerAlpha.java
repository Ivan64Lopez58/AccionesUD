package com.AccionesUD.AccionesUD.controller.AlphaVantage;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AccionesUD.AccionesUD.application.AlphaVantage.StockService;
import com.AccionesUD.AccionesUD.dto.AlphaVantage.StockDTO;


@RestController
@RequestMapping("/api/stocks/alpha") // cambia el prefijo
public class StockControllerAlpha {

    private final StockService stockService;

    public StockControllerAlpha(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStock(@PathVariable String symbol) {
        return ResponseEntity.ok(stockService.getStockInfo(symbol));
    }
}
