package com.AccionesUD.AccionesUD.alpacaStream.infrastructure;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.AccionesUD.AccionesUD.alpacaStock.domain.model.StockInfo;
import com.AccionesUD.AccionesUD.alpacaStream.application.StockStreamService;

import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.util.apitype.MarketDataWebsocketSourceType;
import net.jacobpeterson.alpaca.model.util.apitype.TraderAPIEndpointType;
import net.jacobpeterson.alpaca.model.websocket.marketdata.streams.stock.model.trade.StockTradeMessage;
import net.jacobpeterson.alpaca.websocket.marketdata.streams.stock.StockMarketDataListenerAdapter;

@Component
public class AlpacaStockStreamService implements StockStreamService {

    private final AlpacaAPI alpacaAPI;

    public AlpacaStockStreamService(
        @Value("${alpaca.api.key}") String key,
        @Value("${alpaca.api.secret}") String secret
    ) {
        this.alpacaAPI = new AlpacaAPI(key, secret,
            TraderAPIEndpointType.PAPER,
            MarketDataWebsocketSourceType.IEX);
    }

    @Override
    public void connectAndStream(Set<String> symbols, Consumer<StockInfo> onPriceUpdate) {
        try {
            var stream = alpacaAPI.stockMarketDataStream();
            stream.setListener(new StockMarketDataListenerAdapter() {
                @Override
                public void onTrade(StockTradeMessage trade) {
                    StockInfo info = new StockInfo(
                        trade.getSymbol(),
                        trade.getPrice().doubleValue(),
                        trade.getSize().intValue()
                    );
                    onPriceUpdate.accept(info);
                }
            });

            stream.connect();

            if (!stream.waitForAuthorization(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Authorization failed");
            }

            stream.setTradeSubscriptions(symbols);

        } catch (Exception e) {
            throw new RuntimeException("Error connecting to Alpaca stream", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            alpacaAPI.stockMarketDataStream().disconnect();
            alpacaAPI.closeOkHttpClient();
        } catch (Exception e) {
            throw new RuntimeException("Error disconnecting from stream", e);
        }
    }
}
