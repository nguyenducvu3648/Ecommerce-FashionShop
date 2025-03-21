package com.demo_crud.demo.repository.CartItem;

import com.demo_crud.demo.entity.CartItem;
import com.demo_crud.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    
    List<CartItem> findByIdInAndCartUser(List<String> cartItemIds, User user);
}
