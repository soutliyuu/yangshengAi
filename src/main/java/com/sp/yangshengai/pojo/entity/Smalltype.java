package com.sp.yangshengai.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-04
 */
@Getter
@Setter
@TableName("smalltype")

public class Smalltype implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("s_name")
    private String sName;

    @TableField("image")
    private String image;


    @TableField("status")
    private Integer status;


    private Integer bigTypeId;
}
