package com.AccionesUD.AccionesUD.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/scrap")
    public ResponseEntity<String> scrapearEmpresas() {
        String resultado = accionService.scrapearEmpresasEjemplo();
        return ResponseEntity.ok(resultado);
    }
}
