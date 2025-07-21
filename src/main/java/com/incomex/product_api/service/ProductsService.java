package com.incomex.product_api.service;

import com.incomex.product_api.dto.ProductDTO;
import com.incomex.product_api.model.JobStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductsService {
    ProductDTO create(ProductDTO dto);
    ProductDTO getById(Long id);
    List<ProductDTO> getAll();
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    void generateRandomProducts(int count);
    UUID startAsyncProductGeneration(int count);
    JobStatus getJobStatus(UUID jobId);
    Page<ProductDTO> searchProducts(String name, Long categoryId, Long supplierId, int page, int size, String sortBy, String direction);


}
