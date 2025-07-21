package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.CategoriesDTO;
import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.repository.CategoriesRepository;
import com.incomex.product_api.service.CategoriesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository repository;

    public CategoriesServiceImpl(CategoriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoriesDTO create(CategoriesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public CategoriesDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public List<CategoriesDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoriesDTO update(Long id, CategoriesDTO dto) {
        Categories existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        dto.setCategoryId(id);
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Categories toEntity(CategoriesDTO dto) {
        Categories c = new Categories();
        c.setCategoryId(dto.getCategoryId());
        c.setCategoryName(dto.getCategoryName());
        c.setDescription(dto.getDescription());
        c.setPicture(dto.getPicture());
        return c;
    }

    private CategoriesDTO toDTO(Categories entity) {
        CategoriesDTO dto = new CategoriesDTO();
        dto.setCategoryId(entity.getCategoryId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setDescription(entity.getDescription());
        dto.setPicture(entity.getPicture());
        return dto;
    }
}
