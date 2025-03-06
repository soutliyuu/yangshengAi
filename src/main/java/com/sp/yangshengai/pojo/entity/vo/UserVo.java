package com.sp.yangshengai.pojo.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVo {
    private Integer id;
    private String username;
    private String password;
    private Integer role;
}
