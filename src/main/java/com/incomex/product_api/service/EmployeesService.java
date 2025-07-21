package com.incomex.product_api.service;

import com.incomex.product_api.dto.EmployeesDTO;
import java.util.List;

public interface EmployeesService {
    EmployeesDTO create(EmployeesDTO dto);
    EmployeesDTO getById(Long id);
    List<EmployeesDTO> getAll();
    EmployeesDTO update(Long id, EmployeesDTO dto);
    void delete(Long id);
}