package com.sp.yangshengai.pojo.entity.bo;

import lombok.Data;

import java.util.List;

@Data
public class PlanBo {
    private Integer id;

    private String planName;

    private String description;

    private List<Integer> smallTypeIds;

}
