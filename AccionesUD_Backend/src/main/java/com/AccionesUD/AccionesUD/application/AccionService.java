package com.AccionesUD.AccionesUD.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.AccionesUD.AccionesUD.domain.service.ScraperClient;

@Service
public class AccionService {

    private final ScraperClient scraperClient;

    public AccionService(ScraperClient scraperClient) {
        this.scraperClient = scraperClient;
    }

    public String scrapearEmpresasEjemplo() {
        List<Map<String, String>> empresas = List.of(
            Map.of("empresa", "Apple", "url", "https://www.investing.com/equities/apple-drc"),
            Map.of("empresa", "Microsoft", "url", "https://www.investing.com/equities/microsoft-corp")
        );
        return scraperClient.obtenerDatosScraper(empresas);
    }
}
