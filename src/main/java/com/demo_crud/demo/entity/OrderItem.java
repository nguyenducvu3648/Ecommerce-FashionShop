package com.demo_crud.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItem {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    Product product;

    @ManyToOne
    Order order;

    int quantity;

    double orderedProductPrice;


}
