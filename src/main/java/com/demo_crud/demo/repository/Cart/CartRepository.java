package com.demo_crud.demo.repository.Cart;

import com.demo_crud.demo.entity.Cart;
import com.demo_crud.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findFirstByOrderByIdDesc();
    Optional<Cart> findByUser(User user);
}
