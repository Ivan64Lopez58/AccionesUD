package com.AccionesUD.AccionesUD.alpacaStream.controller;

import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import com.AccionesUD.AccionesUD.alpacaStream.application.StockStreamService;

@Controller
public class StockWebSocketController {
    
    private final StockStreamService stockStreamService;
    private final SimpMessagingTemplate messagingTemplate;
    
    public StockWebSocketController(StockStreamService stockStreamService,
                                    SimpMessagingTemplate messagingTemplate) {
        this.stockStreamService = stockStreamService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/subscribe-stock")
    public void subscribeToStock(String symbol) {
        System.out.println("Suscribiendo al símbolo: " + symbol);

        stockStreamService.connectAndStream(Set.of(symbol.toUpperCase()), stockInfo -> {
            messagingTemplate.convertAndSend("/topic/stocks", stockInfo);
        });
    }
}
