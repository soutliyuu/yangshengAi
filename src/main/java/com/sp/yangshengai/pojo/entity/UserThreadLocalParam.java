package com.sp.yangshengai.pojo.entity;



import lombok.Data;

import java.util.List;

/**
 * 保存用户相关参数的对象，作为 ThreadLocalMap 中的 value
 */
@Data
public class UserThreadLocalParam {
    private Integer Id;
    private String username;
    private String password;

}
