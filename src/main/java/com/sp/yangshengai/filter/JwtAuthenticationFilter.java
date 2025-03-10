package com.sp.yangshengai.filter;


import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;


import com.sp.yangshengai.exception.AnonApi;
import com.sp.yangshengai.exception.CustomException;
import com.sp.yangshengai.exception.ErrorEnum;
import com.sp.yangshengai.pojo.entity.MyUserDetails;
import com.sp.yangshengai.service.impl.UserServiceImpl;
import com.sp.yangshengai.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private UserServiceImpl getMyUserDetailsService() {
        return SpringUtil.getBean(UserServiceImpl.class);
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {


        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/v3/api-docs") ||
                requestUri.startsWith("/swagger-ui") ||
                requestUri.startsWith("/webjars") ||
                requestUri.startsWith("/swagger-resources")) {
            log.info("放行 Swagger UI 请求: {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        final String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            // token 不存在，放行，交由 SpringSecurity 鉴权
            filterChain.doFilter(request, response);
            return;
        }


        HandlerMethod handlerMethod = getHandlerMethod(request);
        if (handlerMethod != null) {
            AnonApi anonApi = handlerMethod.getMethod().getAnnotation(AnonApi.class);
            if (anonApi != null) {
                // 公共接口，放行
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 用户信息
        MyUserDetails userDetails;
        try {
            String username = jwtUtils.extractAllClaims(token).getSubject();
            userDetails = (MyUserDetails) this.getMyUserDetailsService().loadUserByUsername(username);
        } catch (Exception e) {
            throw CustomException.of(ErrorEnum.LOGIN_STATUS_INVALID);
        }

        // 用户被禁用
        if (!userDetails.isEnabled()){
            throw CustomException.of(ErrorEnum.LOGIN_STATUS_INVALID);
        }

        // 互踢
//        String cacheToken = jwtUtils.getCacheToken(userDetails.getUserId());
//        if (cacheToken == null || !cacheToken.equals(token)) {
//            throw CustomException.of(ErrorEnum.LOGIN_STATUS_INVALID);
//        }


        // 认证成功，往 Security 上下文存入用户信息
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    private HandlerMethod getHandlerMethod(HttpServletRequest request) {
        HandlerMethod handlerMethod = null;
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            if (handlerExecutionChain != null) {
                Object handler = handlerExecutionChain.getHandler();
                if (handler instanceof HandlerMethod) {
                    handlerMethod = (HandlerMethod) handler;
                }
            }
        } catch (Exception e) {
            log.error("HandlerMethod 获取失败", e);
        }
        return handlerMethod;
    }

}
