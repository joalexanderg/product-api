package com.incomex.product_api.service.impl;

import com.incomex.product_api.dto.OrderDetailsDTO;
import com.incomex.product_api.model.entity.OrderDetails;
import com.incomex.product_api.model.entity.OrderDetailsId;
import com.incomex.product_api.model.entity.Orders;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.repository.*;
import com.incomex.product_api.service.OrderDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository repository;
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository repository, OrdersRepository ordersRepository, ProductsRepository productsRepository) {
        this.repository = repository;
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
    }

    @Override
    public OrderDetailsDTO create(OrderDetailsDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public OrderDetailsDTO getById(Long orderId, Long productId) {
        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(orderId);
        id.setProductId(productId);

        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));
    }

    @Override
    public List<OrderDetailsDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDetailsDTO update(Long orderId, Long productId, OrderDetailsDTO dto) {
        // Validar que exista antes de intentar actualizar
        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(orderId);
        id.setProductId(productId);

        OrderDetails existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order detail not found"));

        // Actualizar valores
        dto.orderId = orderId;
        dto.productId = productId;

        OrderDetails updated = toEntity(dto);
        updated.setId(id); // Asegura que el ID se mantenga igual

        return toDTO(repository.save(updated));
    }


    @Override
    public void delete(Long orderId, Long productId) {
        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(orderId);
        id.setProductId(productId);
        repository.deleteById(id);
    }

    private OrderDetails toEntity(OrderDetailsDTO dto) {
        OrderDetails od = new OrderDetails();

        // Crear ID compuesto
        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(dto.orderId);
        id.setProductId(dto.productId);
        od.setId(id);

        // Obtener entidades desde la BD
        Orders order = ordersRepository.findById(dto.orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Products product = productsRepository.findById(dto.productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        od.setOrder(order);
        od.setProduct(product);
        od.setUnitPrice(dto.unitPrice);
        od.setQuantity(dto.quantity);
        od.setDiscount(dto.discount);

        return od;
    }


    private OrderDetailsDTO toDTO(OrderDetails od) {
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.orderId = od.getOrder().getOrderId();
        dto.productId = od.getProduct().getProductId();
        dto.unitPrice = od.getUnitPrice();
        dto.quantity = od.getQuantity();
        dto.discount = od.getDiscount();
        return dto;
    }
}