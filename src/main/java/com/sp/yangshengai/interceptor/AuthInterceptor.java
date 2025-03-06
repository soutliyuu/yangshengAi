package com.sp.yangshengai.interceptor;




import com.sp.yangshengai.pojo.entity.MyUserDetails;
import com.sp.yangshengai.pojo.entity.UserThreadLocalParam;
import com.sp.yangshengai.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.ref.SoftReference;

/**
 * 将 SpringSecurity 上下文中的用户信息存入线程本地
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        UserThreadLocalParam param = new UserThreadLocalParam();
        SecurityUtils.USER_THREAD_LOCAL.set(new SoftReference<>(param));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 对于未认证用户, SpringSecurity 默认给用户设置了一个匿名认证
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return true;
        }

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        BeanUtils.copyProperties(userDetails, param);

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {

        SecurityUtils.USER_THREAD_LOCAL.remove();
    }
}
