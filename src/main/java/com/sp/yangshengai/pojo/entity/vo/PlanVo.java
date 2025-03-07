package com.sp.yangshengai.pojo.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanVo {


    private Integer id;



    private String planName;


    private String status;


    private String description;
}
