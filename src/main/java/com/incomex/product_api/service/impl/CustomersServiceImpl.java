package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.CustomersDTO;
import com.incomex.product_api.model.entity.Customers;
import com.incomex.product_api.repository.CustomersRepository;
import com.incomex.product_api.service.CustomersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomersServiceImpl implements CustomersService {

    private final CustomersRepository repository;

    public CustomersServiceImpl(CustomersRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomersDTO create(CustomersDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public CustomersDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public List<CustomersDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomersDTO update(Long id, CustomersDTO dto) {
        dto.customerId = id;
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Customers toEntity(CustomersDTO dto) {
        Customers c = new Customers();
        c.setCustomerId(dto.customerId);
        c.setCompanyName(dto.companyName);
        c.setContactName(dto.contactName);
        c.setContactTitle(dto.contactTitle);
        c.setAddress(dto.address);
        c.setCity(dto.city);
        c.setRegion(dto.region);
        c.setPostalCode(dto.postalCode);
        c.setCountry(dto.country);
        c.setPhone(dto.phone);
        c.setFax(dto.fax);
        return c;
    }

    private CustomersDTO toDTO(Customers c) {
        CustomersDTO dto = new CustomersDTO();
        dto.customerId = c.getCustomerId();
        dto.companyName = c.getCompanyName();
        dto.contactName = c.getContactName();
        dto.contactTitle = c.getContactTitle();
        dto.address = c.getAddress();
        dto.city = c.getCity();
        dto.region = c.getRegion();
        dto.postalCode = c.getPostalCode();
        dto.country = c.getCountry();
        dto.phone = c.getPhone();
        dto.fax = c.getFax();
        return dto;
    }
}