package com.incomex.product_api.controller;

import com.incomex.product_api.dto.CustomersDTO;
import com.incomex.product_api.service.CustomersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final CustomersService service;

    public CustomersController(CustomersService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomersDTO> create(@RequestBody CustomersDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomersDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomersDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomersDTO> update(@PathVariable Long id, @RequestBody CustomersDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

