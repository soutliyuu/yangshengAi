package com.sp.yangshengai.utils;



import com.sp.yangshengai.pojo.entity.UserThreadLocalParam;

import java.lang.ref.SoftReference;

/**
 * 从 ThreadLocal 中获取用户信息
 */

public class SecurityUtils {

    /**
     * 允许子线程继承父线程的线程本地变量
     * 但是由于线程池很少是新建线程，此时变量继承就会失效
     */
    public static final InheritableThreadLocal<SoftReference<UserThreadLocalParam>> USER_THREAD_LOCAL = new InheritableThreadLocal<>();


    private static UserThreadLocalParam getUserThreadLocalParam() {
        return USER_THREAD_LOCAL.get() == null ? null : USER_THREAD_LOCAL.get().get();
    }

    public static Long getUserId() {
        return getUserThreadLocalParam() == null ? null : getUserThreadLocalParam().getId();
    }

    public static String getUsername(){
        return getUserThreadLocalParam() == null ? null : getUserThreadLocalParam().getUsername();
    }

    public static String getPassword(){
        return getUserThreadLocalParam() == null ? null : getUserThreadLocalParam().getPassword();
    }

//    public static List<RoleEnum> getRoles(){
//        return getUserThreadLocalParam() == null ? Lists.newArrayList() : getUserThreadLocalParam().getRoles();
//    }
//
//    public static List<AuthEnum> getAuths(){
//        return getUserThreadLocalParam() == null ? Lists.newArrayList() : getUserThreadLocalParam().getAuths();
//    }


}
