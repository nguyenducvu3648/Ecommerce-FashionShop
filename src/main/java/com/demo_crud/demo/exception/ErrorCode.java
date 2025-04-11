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
    PRODUCT_NOT_EXISTED( 1010,  "product not existed",HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED( 1011,  "product existed", HttpStatus.BAD_REQUEST),
    CART_NOT_EXISTED( 1012, "cart does not exist", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK( 1013, "insufficient stock", HttpStatus.BAD_REQUEST),
    INVALID_ACTION(1014,"invalid action" , HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_EXISTED(1016, "cart item not existed",HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(1017,"category has been existed",HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(1018,"category not existed",HttpStatus.NOT_FOUND),
    NO_CART_ITEMS_SELECTED(1019,"no cart items selected",HttpStatus.NOT_FOUND ),
    NO_CART_ITEMS_FOUND(1020,"no cart item found",HttpStatus.NOT_FOUND ),
    ADDRESS_NOT_FOUND(1021,"address not found",HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1022,"order not found",HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(1023,"orderItem not found",HttpStatus.NOT_FOUND),
    ORDER_CANNOT_BE_CONFIRMED(1024 ,"order cannot be confirm" ,HttpStatus.BAD_REQUEST ),
    ORDER_CANNOT_BE_DELETED(1025,"cannot delete order" ,HttpStatus.BAD_REQUEST );



    int code;
    String message;
    HttpStatus statusCode;
}
