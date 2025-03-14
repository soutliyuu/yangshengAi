package com.sp.yangshengai.pojo.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SiginVo {

    private LocalDateTime dateTime;

    private Boolean isSignIn;
}
