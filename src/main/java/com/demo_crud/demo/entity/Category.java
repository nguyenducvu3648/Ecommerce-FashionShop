package com.demo_crud.demo.entity;

import jakarta.persistence.*;

import java.util.Set;
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
