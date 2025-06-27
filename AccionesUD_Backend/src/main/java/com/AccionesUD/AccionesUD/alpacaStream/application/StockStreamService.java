package com.AccionesUD.AccionesUD.alpacaStream.application;

import java.util.Set;
import java.util.function.Consumer;

import com.AccionesUD.AccionesUD.alpacaStock.domain.model.StockInfo;

public interface StockStreamService {
    void connectAndStream(Set<String> symbols, Consumer<StockInfo> onPriceUpdate);
    void disconnect();
}
