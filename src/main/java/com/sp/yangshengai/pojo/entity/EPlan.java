package com.sp.yangshengai.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
@Getter
@Setter
@TableName("e_plan")
@Builder
public class EPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @TableField("plan_name")
    private String planName;

    @TableField("status")
    private String status;

    @TableField("description")
    private String description;


}
