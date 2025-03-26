package com.demo_crud.demo.service.Address;

import com.demo_crud.demo.entity.Address;
import com.demo_crud.demo.repository.AddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
//    AddressRepository addressRepository;
//    public List<Address> getAllAddresses() {
//        return addressRepository.findAll();
//    }
}
