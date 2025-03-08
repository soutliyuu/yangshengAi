package com.sp.yangshengai.pojo.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EPlanVo {


    private Integer id;



    private String ePlanName;


    private String status;


    private String description;
}
