package com.sp.yangshengai.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    LOGIN_STATUS_INVALID(HttpStatus.UNAUTHORIZED,"登录状态已失效，请重新登录！"),
    AUTHENTICATION_FAILURE(HttpStatus.BAD_REQUEST,"认证失败"),
    AUTHORIZATION_FAILURE(HttpStatus.BAD_REQUEST, "没有权限访问"),
    USER_IS_DISABLED(HttpStatus.BAD_REQUEST, "此账号已被禁用"),
    USERNAME_OR_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "用户名或密码错误"),

    IP_UTIL_INIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ip2region 初始化异常"),
    GENERATE_CAPTCHA_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "验证码生成失败"),
    UPLOAD_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "文件上传失败"),
    DATA_EXPORT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "数据导出失败"),
    ;

    private final HttpStatus status;
    private final String msg;


}
