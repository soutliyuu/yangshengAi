package com.sp.yangshengai.pojo.entity.bo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class PlanBo {
    @ApiParam(value = "计划id，添加的时候不用传，修改的时候传")
    private Integer id;
    @ApiParam(value = "计划名字")
    private String planName;
    @ApiParam(value = "计划描述")
    private String description;
    @ApiParam(value = "小类id")
    private List<Integer> smallTypeIds;

}
