package com.AccionesUD.AccionesUD.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class ScraperClient {

    private final RestTemplate restTemplate;

    @Autowired
    public ScraperClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

public List<Map<String, Object>> obtenerDatosScraper(List<Map<String, String>> acciones) {
    String url = "https://scraper-production-491d.up.railway.app/scrap";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<Map<String, String>>> request = new HttpEntity<>(acciones, headers);

    try {
        ResponseEntity<List> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            List.class
        );
        return response.getBody();
    } catch (RestClientException ex) {
        System.err.println("❌ Error al llamar al microservicio scraper: " + ex.getMessage());
        return List.of(Map.of("error", "No se pudo obtener respuesta del microservicio scraper"));
    }
}

}
