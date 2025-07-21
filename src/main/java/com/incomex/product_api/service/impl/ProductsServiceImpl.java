package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.ProductDTO;
import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.model.entity.Suppliers;
import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.repository.ProductsRepository;
import com.incomex.product_api.service.async.AsyncProductsGeneratorService;
import com.incomex.product_api.service.job.JobRegistry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.incomex.product_api.service.ProductsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository repository;
    private final AsyncProductsGeneratorService asyncGenerator;
    private final JobRegistry jobRegistry;
/*
    @Autowired
    private AsyncProductsGeneratorService asyncGenerator;


    @Autowired
    private JobRegistry jobRegistry;*/

    public ProductsServiceImpl(ProductsRepository repository,
                               AsyncProductsGeneratorService asyncGenerator,
                               JobRegistry jobRegistry) {
        this.repository = repository;
        this.asyncGenerator = asyncGenerator;
        this.jobRegistry = jobRegistry;
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Products entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public ProductDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<ProductDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Products existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        dto.setProductId(id);
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Products toEntity(ProductDTO dto) {
        Products p = new Products();
        p.setProductId(dto.getProductId());
        p.setProductName(dto.getProductName());
        p.setSupplierID(dto.getSupplierID() != null ? new Suppliers(dto.getSupplierID()) : null);
        p.setCategoryID(dto.getCategoryID() != null ? new Categories(dto.getCategoryID()) : null);
        p.setQuantityPerUnit(dto.getQuantityPerUnit());
        p.setUnitPrice(dto.getUnitPrice());
        p.setUnitsInStock(dto.getUnitsInStock());
        p.setUnitsOnOrder(dto.getUnitsOnOrder());
        p.setReorderLevel(dto.getReorderLevel());
        p.setDiscontinued(dto.isDiscontinued());
        return p;
    }

    private ProductDTO toDTO(Products p) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(p.getProductId());
        dto.setProductName(p.getProductName());
        dto.setSupplierID(p.getSupplierID() != null ? p.getSupplierID().getSupplierId() : null);
        if (p.getCategoryID() != null) {
            dto.setCategoryID(p.getCategoryID().getCategoryId());
            dto.setCategoryPicture(p.getCategoryID().getPicture());
        }
        dto.setQuantityPerUnit(p.getQuantityPerUnit());
        dto.setUnitPrice(p.getUnitPrice());
        dto.setUnitsInStock(p.getUnitsInStock());
        dto.setUnitsOnOrder(p.getUnitsOnOrder());
        dto.setReorderLevel(p.getReorderLevel());
        dto.setDiscontinued(p.isDiscontinued());
        return dto;
    }

    @Override
    public void generateRandomProducts(int count) {
       // asyncGenerator.generateRandomProducts(count);
    }
    @Override
    public UUID startAsyncProductGeneration(int count) {
        UUID jobId = jobRegistry.registerJob();
        asyncGenerator.generateRandomProducts(jobId, count);
        return jobId;
    }

    @Override
    public JobStatus getJobStatus(UUID jobId) {
        return jobRegistry.getStatus(jobId);
    }

    @Override
    public Page<ProductDTO> searchProducts(String name, Long categoryId, Long supplierId, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<Products> productsPage = repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("productName")), "%" + name.toLowerCase() + "%"));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryID").get("categoryId"), categoryId));
            }
            if (supplierId != null) {
                predicates.add(cb.equal(root.get("supplierID").get("supplierId"), supplierId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return productsPage.map(this::toDTO);
    }
}
