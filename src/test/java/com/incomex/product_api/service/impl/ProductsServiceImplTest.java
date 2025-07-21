package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.ProductDTO;
import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.model.entity.Suppliers;
import com.incomex.product_api.repository.ProductsRepository;
import com.incomex.product_api.service.async.AsyncProductsGeneratorService;
import com.incomex.product_api.service.job.JobRegistry;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductsServiceImplTest {

    @Mock
    private ProductsRepository repository;

    @InjectMocks
    private ProductsServiceImpl service;

    @Mock
    private JobRegistry jobRegistry;

    @Mock
    private AsyncProductsGeneratorService asyncGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        ProductDTO dto = createSampleDTO();
        Products savedEntity = createSampleEntity();

        when(repository.save(any(Products.class))).thenReturn(savedEntity);

        ProductDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(dto.getProductName(), result.getProductName());
        verify(repository).save(any(Products.class));
    }

    @Test
    void testGetByIdFound() {
        Products entity = createSampleEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        ProductDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        verify(repository).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void testUpdate() {
        Products existing = createSampleEntity();
        existing.setProductName("New Name");
        ProductDTO dto = createSampleDTO();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Products.class))).thenReturn(existing);

        ProductDTO updated = service.update(1L, dto);

        assertNotNull(updated);
        assertEquals("New Name", updated.getProductName());
        verify(repository).findById(1L);
        verify(repository).save(any(Products.class));
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(createSampleEntity()));

        List<ProductDTO> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void testDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testSearchProducts() {
        Products sample = createSampleEntity();
        Page<Products> page = new PageImpl<>(List.of(sample));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = service.searchProducts("", 1L, 1L, 0, 10, "productName", "ASC");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Product", result.getContent().get(0).getProductName());
    }

    @Test
    void testSearchProducts_NoResults() {
        Page<Products> emptyPage = new PageImpl<>(List.of());

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<ProductDTO> result = service.searchProducts("ProductoInexistente", 99L, 99L, 0, 10, "productName", "ASC");

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void testStartAsyncProductGeneration() {
        UUID jobId = UUID.randomUUID();

        when(jobRegistry.registerJob()).thenReturn(jobId);

        UUID result = service.startAsyncProductGeneration(100);

        assertEquals(jobId, result);
        verify(jobRegistry).registerJob();
        verify(asyncGenerator).generateRandomProducts(jobId, 100);
    }

    @Test
    void testGetJobStatusRunning() {
        UUID jobId = UUID.randomUUID();
        JobStatus status = JobStatus.RUNNING;

        when(jobRegistry.getStatus(jobId)).thenReturn(status);

        JobStatus result = service.getJobStatus(jobId);

        assertNotNull(result);
        assertEquals(status, result);
        verify(jobRegistry).getStatus(jobId);
    }

    private ProductDTO createSampleDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(1L);
        dto.setProductName("Sample Product");
        dto.setSupplierID(1L);
        dto.setCategoryID(2L);
        dto.setQuantityPerUnit("10 units");
        dto.setUnitPrice(new BigDecimal("25.00"));
        dto.setUnitsInStock(100);
        dto.setUnitsOnOrder(50);
        dto.setReorderLevel(10);
        dto.setDiscontinued(false);
        return dto;
    }

    private Products createSampleEntity() {
        Products p = new Products();
        p.setProductId(1L);
        p.setProductName("Sample Product");
        p.setSupplierID(new Suppliers(1L));
        p.setCategoryID(new Categories(2L));
        p.setQuantityPerUnit("10 units");
        p.setUnitPrice(new BigDecimal("25.00"));
        p.setUnitsInStock(100);
        p.setUnitsOnOrder(50);
        p.setReorderLevel(10);
        p.setDiscontinued(false);
        return p;
    }
}
