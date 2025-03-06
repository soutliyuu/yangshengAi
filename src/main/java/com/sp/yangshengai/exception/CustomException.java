package com.sp.yangshengai.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorResponseBody errorResponseBody;

    private CustomException(HttpStatus status, String msg){
        super(msg);
        this.errorResponseBody = new ErrorResponseBody(status, msg);
    }

    public static CustomException of(ErrorEnum error) {
        return new CustomException(error.getStatus(), error.getMsg());
    }

    public static CustomException of(String message) {
        return new CustomException(HttpStatus.BAD_REQUEST, message);
    }

    public CustomException with(String message){
        this.errorResponseBody.setMsg(String.format("%s:%s", this.errorResponseBody.getMsg(), message));
        return this;
    }
}
