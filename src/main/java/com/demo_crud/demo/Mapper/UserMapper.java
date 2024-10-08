package com.demo_crud.demo.Mapper;

import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.request.UserUpdateRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(UserCreationRequest request);//mapping dữ liệu từ UserCreationRequest sang User

    @Mapping(source = "firstName", target = "lastName")//trường firstName trong đối tượng nguồn sẽ được ánh xạ và gán cho trường lastName trong đối tượng đích.
    UserResponse toUserResponse(User user); //mapping dữ liệu từ User sang UserResponse

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
//Thông thường, quy trình cập nhật dữ liệu từ client đến cơ sở dữ liệu
//và phản hồi kết quả về client diễn ra như sau:
//

//1---DTO nhận dữ liệu từ client (Request DTO):
// Dữ liệu từ client sẽ được gửi đến server thông qua một đối tượng DTO như UserUpdateRequest.
// Đối tượng này chứa các thông tin mà client muốn cập nhật cho người dùng.
//
//2---Mapping từ DTO sang Entity:
// Sau khi nhận được DTO từ client, dữ liệu sẽ được ánh xạ (mapping) sang entity User.
// Entity này đại diện cho đối tượng cần được cập nhật trong cơ sở dữ liệu.
//
//3---Cập nhật và lưu trữ Entity:
// Entity User đã được cập nhật sẽ được lưu trở lại cơ sở dữ liệu.
//
//4---Tạo DTO phản hồi (Response DTO):
// Sau khi cập nhật thành công, dữ liệu của đối tượng User có thể được chuyển đổi thành một UserResponse
// (một dạng DTO khác) để gửi lại thông tin cập nhật cho client.