package com.incomex.product_api.controller;

import com.incomex.product_api.dto.OrderDetailsDTO;
import com.incomex.product_api.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/order-details")
public class OrderDetailsController {

    private final OrderDetailsService service;

    public OrderDetailsController(OrderDetailsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderDetailsDTO> create(@RequestBody OrderDetailsDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderDetailsDTO> getById(@PathVariable Long orderId, @PathVariable Long productId) {
        return ResponseEntity.ok(service.getById(orderId, productId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderDetailsDTO> update(@PathVariable Long orderId, @PathVariable Long productId,
                                                  @RequestBody OrderDetailsDTO dto) {
        return ResponseEntity.ok(service.update(orderId, productId, dto));
    }

    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId, @PathVariable Long productId) {
        service.delete(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}