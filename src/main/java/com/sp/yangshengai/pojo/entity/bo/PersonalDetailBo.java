package com.sp.yangshengai.pojo.entity.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class PersonalDetailBo {

    @TableField("blood_sugar")
    private Double bloodSugar;


    private Double uricAcid;

    @TableField("weight")
    private Double weight;
}
