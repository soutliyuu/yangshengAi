package com.sp.yangshengai.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("e_user_plan")
@Builder
public class EUserPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;


    private Integer userId;


    private Integer planId;

    @TableField("create_time")
    private LocalDateTime createTime;


}
