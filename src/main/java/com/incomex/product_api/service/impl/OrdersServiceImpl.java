package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.OrdersDTO;
import com.incomex.product_api.model.entity.Customers;
import com.incomex.product_api.model.entity.Employees;
import com.incomex.product_api.model.entity.Orders;
import com.incomex.product_api.model.entity.Shippers;
import com.incomex.product_api.repository.*;
import com.incomex.product_api.service.OrdersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final CustomersRepository customersRepository;
    private final EmployeesRepository employeesRepository;
    private final ShippersRepository shippersRepository;

    public OrdersServiceImpl(
            OrdersRepository ordersRepository,
            CustomersRepository customersRepository,
            EmployeesRepository employeesRepository,
            ShippersRepository shippersRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.customersRepository = customersRepository;
        this.employeesRepository = employeesRepository;
        this.shippersRepository = shippersRepository;
    }

    @Override
    public OrdersDTO create(OrdersDTO dto) {
        return toDTO(ordersRepository.save(toEntity(dto)));
    }

    @Override
    public OrdersDTO getById(Long id) {
        return ordersRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @Override
    public List<OrdersDTO> getAll() {
        return ordersRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrdersDTO update(Long id, OrdersDTO dto) {
        dto.orderId = id;
        return toDTO(ordersRepository.save(toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        ordersRepository.deleteById(id);
    }

    private Orders toEntity(OrdersDTO dto) {
        Orders o = new Orders();
        o.setOrderId(dto.orderId);
        o.setOrderDate(dto.orderDate);
        o.setRequiredDate(dto.requiredDate);
        o.setShippedDate(dto.shippedDate);
        o.setFreight(dto.freight);
        o.setShipName(dto.shipName);
        o.setShipAddress(dto.shipAddress);
        o.setShipCity(dto.shipCity);
        o.setShipRegion(dto.shipRegion);
        o.setShipPostalCode(dto.shipPostalCode);
        o.setShipCountry(dto.shipCountry);

        if (dto.customerId != null)
            o.setCustomer(new Customers(dto.customerId));
        if (dto.employeeId != null)
            o.setEmployee(new Employees(dto.employeeId));
        if (dto.shipViaId != null)
            o.setShipVia(new Shippers(dto.shipViaId));

        return o;
    }

    private OrdersDTO toDTO(Orders o) {
        OrdersDTO dto = new OrdersDTO();
        dto.orderId = o.getOrderId();
        dto.orderDate = o.getOrderDate();
        dto.requiredDate = o.getRequiredDate();
        dto.shippedDate = o.getShippedDate();
        dto.freight = o.getFreight();
        dto.shipName = o.getShipName();
        dto.shipAddress = o.getShipAddress();
        dto.shipCity = o.getShipCity();
        dto.shipRegion = o.getShipRegion();
        dto.shipPostalCode = o.getShipPostalCode();
        dto.shipCountry = o.getShipCountry();
        dto.customerId = o.getCustomer() != null ? o.getCustomer().getCustomerId() : null;
        dto.employeeId = o.getEmployee() != null ? o.getEmployee().getEmployeeId() : null;
        dto.shipViaId = o.getShipVia() != null ? o.getShipVia().getShipperId() : null;
        return dto;
    }
}