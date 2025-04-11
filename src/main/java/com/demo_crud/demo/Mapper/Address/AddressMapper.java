package com.demo_crud.demo.Mapper.Address;

import com.demo_crud.demo.dto.request.Address.AddressCreationRequest;
import com.demo_crud.demo.dto.response.Address.AddressResponse;
import com.demo_crud.demo.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toAddressResponse(Address address);

    Address toAddress(AddressCreationRequest request);
}
