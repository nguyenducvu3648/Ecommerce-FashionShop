package com.demo_crud.demo.repository.Address;

import com.demo_crud.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
