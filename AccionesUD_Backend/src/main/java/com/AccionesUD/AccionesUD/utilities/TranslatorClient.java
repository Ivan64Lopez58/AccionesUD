package com.AccionesUD.AccionesUD.utilities;


import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class TranslatorClient {

    private final RestTemplate restTemplate;

    public TranslatorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Traducción individual (sigue disponible)
    public String traducir(String texto, String idioma) {
        String url = "https://microservicio-traductor.up.railway.app/traducir";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
            "texto", texto,
            "idioma", idioma
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getBody().get("traduccion").toString();
        } catch (Exception e) {
            return texto; // Fallback si falla la traducción
        }
    }

    // Traducción en lote
    public List<String> traducirLote(List<String> textos, String idioma) {
        String url = "https://microservicio-traductor.up.railway.app/traducir-lote";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "textos", textos,
            "idioma", idioma
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return (List<String>) response.getBody().get("traducciones");
        } catch (Exception e) {
            // Si falla, se retorna la lista original sin traducir
            return textos;
        }
    }
}

