package com.sp.yangshengai.pojo.entity.bo;

import lombok.Data;

import java.util.List;

@Data
public class EPlanBo {
    private Integer id;

    private String ePlanName;

    private String description;

    private List<Integer> eSmallTypeIds;

}
