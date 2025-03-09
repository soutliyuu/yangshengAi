package com.sp.yangshengai.pojo.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class EPlanBo {
    @ApiParam(value = "计划id，添加的时候不用传，修改的时候传")
    private Integer id;

    @ApiParam(value = "计划名字")
    private String ePlanName;
    @ApiParam(value = "计划描述")
    private String description;

    @ApiParam(value = "计划内的小类，传他们的id")
    private List<Integer> eSmallTypeIds;

}
