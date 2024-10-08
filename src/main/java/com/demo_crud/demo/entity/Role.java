package com.demo_crud.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    String name;

    String description;

    @ManyToMany
    private Set<Permission> permissions;
}
