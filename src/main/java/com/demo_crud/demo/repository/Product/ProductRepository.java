package com.demo_crud.demo.repository.Product;

import com.demo_crud.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);
    List<Product> findByCategoryId(String categoryId);
    List<Product>  findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword);
}
