package com.sp.yangshengai.pojo.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class PersonalDetailVo {

    private Integer id;

    @TableField("blood_sugar")
    private Double bloodSugar;


    private Double uricAcid;

    @TableField("weight")
    private Double weight;

}
