package com.incomex.product_api.controller;

import com.incomex.product_api.dto.ProductDTO;
import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Products", description = "Gestionar productos.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/Products")
public class ProductsController {

    private final ProductsService service;

    public ProductsController(ProductsService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un producto")
    @ApiResponse(responseCode = "200", description = "Producto creado correctamente",
            content = @Content(schema = @Schema(implementation = ProductDTO.class)))
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Generar productos aleatorios de forma asincrónica")
    @ApiResponse(responseCode = "200", description = "Proceso de carga iniciado, devuelve el JobId")
    @PostMapping("/Product")
    public ResponseEntity<String> createRandom(
            @Parameter(description = "Cantidad de productos a generar (máx. 100000)", example = "5000")
            @RequestParam int count
    ) {
        UUID jobId = service.startAsyncProductGeneration(count);
        return ResponseEntity.ok("Carga iniciada. Consulta el estado con JobId: " + jobId);
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @Parameter(description = "ID del producto a actualizar", example = "1") @PathVariable Long id,
            @RequestBody ProductDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar un producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto a eliminar", example = "1") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar el estado de una carga asincrónica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado consultado exitosamente"),
            @ApiResponse(responseCode = "404", description = "JobId no encontrado")
    })
    @GetMapping("/Product/status/{jobId}")
    public ResponseEntity<String> getJobStatus(
            @Parameter(description = "UUID del proceso de carga asincrónica", example = "c0a8012e-7e6e-4d0f-8f6c-52c9d8a8fc03")
            @PathVariable UUID jobId
    ) {
        JobStatus status = service.getJobStatus(jobId);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Estado del JobId " + jobId + ": " + status);
    }

    @Operation(summary = "Buscar productos con filtros, paginación y ordenamiento")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @Parameter(description = "Nombre del producto para buscar") @RequestParam(required = false) String name,
            @Parameter(description = "ID de la categoría") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "ID del proveedor") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "Número de página", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar", example = "productName") @RequestParam(defaultValue = "productName") String sortBy,
            @Parameter(description = "Dirección de ordenamiento ASC/DESC", example = "asc") @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(service.searchProducts(name, categoryId, supplierId, page, size, sortBy, direction));
    }

}