package com.demo_crud.demo.repository;

import com.demo_crud.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);// kiem tra username dung trong viec dang ki
    Optional<User> findByUsername(String username);// tim ten username dung trong viec dang nhap
}
