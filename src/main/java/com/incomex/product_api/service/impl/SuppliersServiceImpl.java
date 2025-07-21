package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.SuppliersDTO;
import com.incomex.product_api.model.entity.Suppliers;
import com.incomex.product_api.repository.SuppliersRepository;
import com.incomex.product_api.service.SuppliersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuppliersServiceImpl implements SuppliersService {

    private final SuppliersRepository repository;

    public SuppliersServiceImpl(SuppliersRepository repository) {
        this.repository = repository;
    }

    @Override
    public SuppliersDTO create(SuppliersDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SuppliersDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));
    }

    @Override
    public List<SuppliersDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SuppliersDTO update(Long id, SuppliersDTO dto) {
        dto.supplierId = id;
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Suppliers toEntity(SuppliersDTO dto) {
        Suppliers s = new Suppliers();
        s.setSupplierId(dto.supplierId);
        s.setCompanyName(dto.companyName);
        s.setContactName(dto.contactName);
        s.setContactTitle(dto.contactTitle);
        s.setAddress(dto.address);
        s.setCity(dto.city);
        s.setRegion(dto.region);
        s.setPostalCode(dto.postalCode);
        s.setCountry(dto.country);
        s.setPhone(dto.phone);
        s.setFax(dto.fax);
        s.setHomePage(dto.homePage);
        return s;
    }

    private SuppliersDTO toDTO(Suppliers s) {
        SuppliersDTO dto = new SuppliersDTO();
        dto.supplierId = s.getSupplierId();
        dto.companyName = s.getCompanyName();
        dto.contactName = s.getContactName();
        dto.contactTitle = s.getContactTitle();
        dto.address = s.getAddress();
        dto.city = s.getCity();
        dto.region = s.getRegion();
        dto.postalCode = s.getPostalCode();
        dto.country = s.getCountry();
        dto.phone = s.getPhone();
        dto.fax = s.getFax();
        dto.homePage = s.getHomePage();
        return dto;
    }
}