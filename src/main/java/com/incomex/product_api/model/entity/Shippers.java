package com.incomex.product_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shippers")
@Getter
@Setter
@NoArgsConstructor
public class Shippers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipperId;

    private String companyName;
    private String phone;

    public Shippers(Long shipperId) {
        this.shipperId = shipperId;
    }
}