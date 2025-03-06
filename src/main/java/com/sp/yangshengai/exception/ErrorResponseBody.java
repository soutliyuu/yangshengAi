package com.sp.yangshengai.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponseBody {
    private HttpStatus status;
    private String msg;
}
