package com.AccionesUD.AccionesUD.controller.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.AccionesUD.AccionesUD.application.auth.AuthApplicationService;
import com.AccionesUD.AccionesUD.dto.auth.AuthResponse;
import com.AccionesUD.AccionesUD.dto.auth.LoginRequest;
import com.AccionesUD.AccionesUD.dto.auth.OtpRequest;
import com.AccionesUD.AccionesUD.dto.auth.RegisterRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

private final AuthApplicationService authApplicationService;

// Paso 1: autentica y envía OTP al correo
@PostMapping("login")
public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
return ResponseEntity.ok(authApplicationService.login(request));
}

// Paso 2: verifica OTP y entrega el JWT
@PostMapping("verify-otp")
public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpRequest request) {
return ResponseEntity.ok(authApplicationService.verifyOtp(request));
}

// Registro
@PostMapping("register")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
try {
return ResponseEntity.ok(authApplicationService.register(request));
} catch (RuntimeException e) {
return ResponseEntity.badRequest().body(e.getMessage());
}
}
}
