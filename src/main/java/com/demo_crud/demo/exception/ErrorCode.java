package com.demo_crud.demo.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1006, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED( 1001, "user has been existed", HttpStatus.BAD_REQUEST),
    USER_INVALID( 1002, "username must be at least 8 character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED( 1003, "user does not exist", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(1004, "password must be at least 8 character", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED( 1005, " unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS( 1007, "you do not have permission", HttpStatus.FORBIDDEN),
    ADMIN_NOT_EXISTED( 1008, "admin does not exist", HttpStatus.NOT_FOUND),
    INVALID_DOB( 1009, "invalid date", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED( 1001,  "user not existed",HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED( 1002,  "user existed", HttpStatus.BAD_REQUEST),
    ;
    int code;
    String message;
    HttpStatus statusCode;
}
