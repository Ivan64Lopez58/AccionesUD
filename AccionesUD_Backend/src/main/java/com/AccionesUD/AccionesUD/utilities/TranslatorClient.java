package com.AccionesUD.AccionesUD.utilities;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Component
public class TranslatorClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public TranslatorClient(RestTemplate restTemplate,
                             @Value("${traductor.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public String traducir(String texto, String idioma) {
        String url = baseUrl + "/traducir";

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
            return texto;
        }
    }

    public List<String> traducirLote(List<String> textos, String idioma) {
        String url = baseUrl + "/traducir-lote";

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
            return textos;
        }
    }
}
