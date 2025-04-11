package com.demo_crud.demo.dto.response.Address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
@Builder
public class AddressResponse {
    String id;
    String street;
    String city;
    String country;
}
