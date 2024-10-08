package com.demo_crud.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission {
    @Id
    @Column(length = 50)
    String name;

    String description;
}
