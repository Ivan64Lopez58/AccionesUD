package com.AccionesUD.AccionesUD.alpacaStock.infrastructure;

import net.jacobpeterson.alpaca.AlpacaAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.alpacaStock.application.StockService;
import com.AccionesUD.AccionesUD.alpacaStock.domain.model.StockInfo;

import net.jacobpeterson.alpaca.model.util.apitype.MarketDataWebsocketSourceType;
import net.jacobpeterson.alpaca.model.util.apitype.TraderAPIEndpointType;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockFeed;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockTrade;
import net.jacobpeterson.alpaca.openapi.marketdata.ApiException;

@Service
public class AlpacaStockServiceImpl implements StockService{

     private final AlpacaAPI alpacaAPI;
    
     public AlpacaStockServiceImpl(
            @Value("${alpaca.api.key}") String key,
            @Value("${alpaca.api.secret}") String secret) {

        this.alpacaAPI = new AlpacaAPI(key, secret,
                TraderAPIEndpointType.PAPER,
                MarketDataWebsocketSourceType.IEX);
    }

    @Override
    public StockInfo getLatestTrade(String symbol) {
        try {
            StockTrade trade = alpacaAPI.marketData().stock()
                    .stockLatestTradeSingle(symbol.toUpperCase(), StockFeed.IEX, null)
                    .getTrade();

            return new StockInfo(symbol.toUpperCase(), trade.getP(), trade.getS());
        } catch (ApiException e) {
            throw new RuntimeException("Error fetching trade for symbol: " + symbol, e);
        }
    }
}
