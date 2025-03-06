package com.sp.yangshengai.filter;




import com.sp.yangshengai.utils.JsonUtil;
import com.sp.yangshengai.exception.CustomException;
import com.sp.yangshengai.exception.ErrorResponseBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理
 */
@Component
@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            try {
                output(e, response);
            } catch (Exception ex) {
                log.error("系统未能处理异常:", ex);
            }
        }
    }

    /**
     * 异常输出
     */
    private void output(Exception e, HttpServletResponse response) throws IOException {

        if (e.getCause() != null && e.getCause() instanceof Exception) {
            // 如果异常被其他异常包装，需拆开
            e = (Exception) e.getCause();
        }

        ErrorResponseBody body;
        if (e instanceof CustomException ce) {
            // 自定义异常
            body = ce.getErrorResponseBody();
        } else if (e instanceof BindException be) {
            // 参数校验异常
            FieldError fieldError = be.getFieldError();
            Assert.notNull(fieldError, "未能获取到校验异常字段");
            body = new ErrorResponseBody(HttpStatus.BAD_REQUEST,
                    String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()));
        } else {
            // 其他异常
            log.warn("系统未明确定义异常", e);
            body = new ErrorResponseBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(body.getStatus().value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtil.toJSONString(body));
        writer.flush();
        writer.close();
    }

}
