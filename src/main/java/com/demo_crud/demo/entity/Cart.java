package com.demo_crud.demo.entity;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "cart")
    private List<Product> products;
}
