package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.ShippersDTO;
import com.incomex.product_api.model.entity.Shippers;
import com.incomex.product_api.repository.ShippersRepository;
import com.incomex.product_api.service.ShippersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippersServiceImpl implements ShippersService {

    private final ShippersRepository repository;

    public ShippersServiceImpl(ShippersRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShippersDTO create(ShippersDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public ShippersDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Shipper not found"));
    }

    @Override
    public List<ShippersDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ShippersDTO update(Long id, ShippersDTO dto) {
        dto.shipperId = id;
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Shippers toEntity(ShippersDTO dto) {
        Shippers s = new Shippers();
        s.setShipperId(dto.shipperId);
        s.setCompanyName(dto.companyName);
        s.setPhone(dto.phone);
        return s;
    }

    private ShippersDTO toDTO(Shippers s) {
        ShippersDTO dto = new ShippersDTO();
        dto.shipperId = s.getShipperId();
        dto.companyName = s.getCompanyName();
        dto.phone = s.getPhone();
        return dto;
    }
}