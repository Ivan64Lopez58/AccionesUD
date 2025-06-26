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
public List<Map<String, Object>> scrapearDesdeFrontend(List<Map<String, String>> empresas) {
    List<Map<String, Object>> resultado = scraperClient.obtenerDatosScraper(empresas);

    // ✅ Imprimir resultado en consola
    System.out.println("✅ Resultado del scraping:");
    for (Map<String, Object> accion : resultado) {
        System.out.println(accion);
    }

    return resultado;
}


}
