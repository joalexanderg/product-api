package com.incomex.product_api.controller;

import com.incomex.product_api.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Generar token JWT sin login")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Obtener token JWT", description = "Devuelve un token JWT válido para acceder a los endpoints protegidos.")
    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        String token = jwtUtil.generateToken("test-user");
        return ResponseEntity.ok(token);
    }
}
