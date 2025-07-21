package com.incomex.product_api.controller;

import com.incomex.product_api.dto.CategoriesDTO;
import com.incomex.product_api.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "Gestionar categorias.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/category")
public class CategoriesController {

    private final CategoriesService service;

    public CategoriesController(CategoriesService service) {
        this.service = service;
    }

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponse(
            responseCode = "200",
            description = "Categoría creada correctamente",
            content = @Content(schema = @Schema(implementation = CategoriesDTO.class))
    )
    @PostMapping
    public ResponseEntity<CategoriesDTO> create(@RequestBody CategoriesDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Obtener una categoría por su ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada",
                    content = @Content(schema = @Schema(implementation = CategoriesDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriesDTO> getById(
            @Parameter(description = "ID de la categoría", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías",
            content = @Content(schema = @Schema(implementation = CategoriesDTO.class))
    )
    @GetMapping
    public ResponseEntity<List<CategoriesDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Actualizar una categoría existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoriesDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriesDTO> update(
            @Parameter(description = "ID de la categoría a actualizar", example = "1") @PathVariable Long id,
            @RequestBody CategoriesDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar una categoría por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría a eliminar", example = "1") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}