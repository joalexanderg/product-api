package com.incomex.product_api.controller;

import com.incomex.product_api.dto.ShippersDTO;
import com.incomex.product_api.service.ShippersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/shippers")
public class ShippersController {

    private final ShippersService service;

    public ShippersController(ShippersService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShippersDTO> create(@RequestBody ShippersDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippersDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShippersDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippersDTO> update(@PathVariable Long id, @RequestBody ShippersDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}