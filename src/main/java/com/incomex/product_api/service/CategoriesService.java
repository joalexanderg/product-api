package com.incomex.product_api.service;

import com.incomex.product_api.dto.CategoriesDTO;

import java.util.List;

public interface CategoriesService {
    CategoriesDTO create(CategoriesDTO dto);
    CategoriesDTO getById(Long id);
    List<CategoriesDTO> getAll();
    CategoriesDTO update(Long id, CategoriesDTO dto);
    void delete(Long id);
}
