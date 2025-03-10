package com.sp.yangshengai.pojo.entity.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PlanBo {
    @Schema(name= "计划id，添加的时候不用传，修改的时候传")
    private Integer id;

    @Schema(name= "计划名字")
    private String planName;
    @Schema(name= "计划描述")
    private String description;

    @Schema(name= "计划内的小类，传他们的id")
    private List<Integer> smallTypeIds;
}
