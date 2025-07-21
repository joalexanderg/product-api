package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.EmployeesDTO;
import com.incomex.product_api.model.entity.Employees;
import com.incomex.product_api.repository.EmployeesRepository;
import com.incomex.product_api.service.EmployeesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    private final EmployeesRepository repository;

    public EmployeesServiceImpl(EmployeesRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeesDTO create(EmployeesDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public EmployeesDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    @Override
    public List<EmployeesDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public EmployeesDTO update(Long id, EmployeesDTO dto) {
        dto.employeeId = id;
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Employees toEntity(EmployeesDTO dto) {
        Employees e = new Employees();
        e.setEmployeeId(dto.employeeId);
        e.setLastName(dto.lastName);
        e.setFirstName(dto.firstName);
        e.setTitle(dto.title);
        e.setTitleOfCourtesy(dto.titleOfCourtesy);
        e.setBirthDate(dto.birthDate);
        e.setHireDate(dto.hireDate);
        e.setAddress(dto.address);
        e.setCity(dto.city);
        e.setRegion(dto.region);
        e.setPostalCode(dto.postalCode);
        e.setCountry(dto.country);
        e.setHomePhone(dto.homePhone);
        e.setExtension(dto.extension);
        e.setPhoto(dto.photo);
        e.setNotes(dto.notes);
        if (dto.reportsToId != null) {
            e.setReportsTo(new Employees(dto.reportsToId));
        }
        return e;
    }

    private EmployeesDTO toDTO(Employees e) {
        EmployeesDTO dto = new EmployeesDTO();
        dto.employeeId = e.getEmployeeId();
        dto.lastName = e.getLastName();
        dto.firstName = e.getFirstName();
        dto.title = e.getTitle();
        dto.titleOfCourtesy = e.getTitleOfCourtesy();
        dto.birthDate = e.getBirthDate();
        dto.hireDate = e.getHireDate();
        dto.address = e.getAddress();
        dto.city = e.getCity();
        dto.region = e.getRegion();
        dto.postalCode = e.getPostalCode();
        dto.country = e.getCountry();
        dto.homePhone = e.getHomePhone();
        dto.extension = e.getExtension();
        dto.photo = e.getPhoto();
        dto.notes = e.getNotes();
        dto.reportsToId = e.getReportsTo() != null ? e.getReportsTo().getEmployeeId() : null;
        return dto;
    }
}

