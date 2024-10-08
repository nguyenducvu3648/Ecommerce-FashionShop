package com.demo_crud.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}