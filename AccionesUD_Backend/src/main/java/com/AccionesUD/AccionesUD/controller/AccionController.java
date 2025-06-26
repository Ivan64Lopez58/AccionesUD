package com.AccionesUD.AccionesUD.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AccionesUD.AccionesUD.application.AccionService;

@RestController
@RequestMapping("/acciones")

public class AccionController {

    private final AccionService accionService;

    public AccionController(AccionService accionService) {
        this.accionService = accionService;
    }

@PostMapping("/scrap")
public ResponseEntity<List<Map<String, Object>>> scrapearDesdeFront(@RequestBody List<Map<String, String>> empresas) {

    // ✅ Imprimir las empresas recibidas desde el frontend
    System.out.println("📥 Empresas recibidas desde el frontend:");
    for (Map<String, String> empresa : empresas) {
        System.out.println(empresa);
    }

    List<Map<String, Object>> resultado = accionService.scrapearDesdeFrontend(empresas);
    return ResponseEntity.ok(resultado);
}


}