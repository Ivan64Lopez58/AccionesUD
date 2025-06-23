package com.AccionesUD.AccionesUD.alpacaStock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AccionesUD.AccionesUD.alpacaStock.application.StockService;
import com.AccionesUD.AccionesUD.alpacaStock.domain.model.StockInfo;
import com.AccionesUD.AccionesUD.alpacaStock.dto.StockResponseDTO;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockResponseDTO> getStockTrade(@PathVariable String symbol) {
        StockInfo stockInfo = stockService.getLatestTrade(symbol);
        StockResponseDTO response = new StockResponseDTO(
                stockInfo.getSymbol(),
                stockInfo.getPrice(),
                stockInfo.getSize()
        );
        return ResponseEntity.ok(response);
    }
}
