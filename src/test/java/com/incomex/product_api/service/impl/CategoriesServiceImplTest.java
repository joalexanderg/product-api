package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.CategoriesDTO;
import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.repository.CategoriesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriesServiceImplTest {

    @Mock
    private CategoriesRepository repository;

    @InjectMocks
    private CategoriesServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CategoriesDTO createDTO() {
        CategoriesDTO dto = new CategoriesDTO();
        dto.setCategoryId(1L);
        dto.setCategoryName("SERVIDORES");
        dto.setDescription("Equipos físicos");
        dto.setPicture("https://img.com/1");
        return dto;
    }

    private Categories createEntity() {
        Categories entity = new Categories();
        entity.setCategoryId(1L);
        entity.setCategoryName("SERVIDORES");
        entity.setDescription("Equipos físicos");
        entity.setPicture("https://img.com/1");
        return entity;
    }

    @Test
    void testCreate() {
        Categories entity = createEntity();
        CategoriesDTO dto = createDTO();

        when(repository.save(any())).thenReturn(entity);

        CategoriesDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("SERVIDORES", result.getCategoryName());
        verify(repository).save(any(Categories.class));
    }

    @Test
    void testGetById_found() {
        Categories entity = createEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        CategoriesDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("SERVIDORES", result.getCategoryName());
        verify(repository).findById(1L);
    }

    @Test
    void testGetById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testGetAll() {
        Categories entity1 = createEntity();
        Categories entity2 = createEntity();
        entity2.setCategoryId(2L);
        entity2.setCategoryName("CLOUD");

        when(repository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        List<CategoriesDTO> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("SERVIDORES", result.get(0).getCategoryName());
        assertEquals("CLOUD", result.get(1).getCategoryName());
    }

    @Test
    void testUpdate() {
        Categories existing = createEntity();
        existing.setCategoryName("SERVIDORES GAMA MEDIA");
        CategoriesDTO dto = createDTO();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        CategoriesDTO updated = service.update(1L, dto);

        assertNotNull(updated);
        assertEquals("SERVIDORES GAMA MEDIA", updated.getCategoryName());
        verify(repository).findById(1L);
        verify(repository).save(any(Categories.class));
    }

    @Test
    void testDelete() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testUpdate_notFound() {
        CategoriesDTO dto = createDTO();
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1L, dto));
        verify(repository).findById(1L);
    }

    @Test
    void testDelete_whenEntityNotFound_shouldStillCallDelete() {
        // Simula el comportamiento por defecto (Spring no lanza excepción si no existe)
        doNothing().when(repository).deleteById(99L);

        assertDoesNotThrow(() -> service.delete(99L));
        verify(repository).deleteById(99L);
    }

    @Test
    void testCreate_withNullCategoryName_shouldThrowException() {
        CategoriesDTO dto = new CategoriesDTO();
        dto.setCategoryName(null);  // obligatorio en la entidad
        dto.setDescription("Descripción sin nombre");
        dto.setPicture("https://img.com/error");

        // Simulamos una excepción desde el repositorio (como si Hibernate fallara)
        when(repository.save(any())).thenThrow(new IllegalArgumentException("categoryName is required"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.create(dto));
        assertEquals("categoryName is required", ex.getMessage());
        verify(repository).save(any());
    }

    @Test
    void testCreate_whenRepositoryFails_shouldThrowRuntimeException() {
        CategoriesDTO dto = createDTO();
        when(repository.save(any())).thenThrow(new RuntimeException("DB failure"));

        Exception exception = assertThrows(RuntimeException.class, () -> service.create(dto));
        assertTrue(exception.getMessage().contains("DB failure"));
        verify(repository).save(any());
    }

}

