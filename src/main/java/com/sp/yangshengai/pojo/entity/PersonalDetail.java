package com.sp.yangshengai.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
@Getter
@Setter
@TableName("personal_detail")
@Builder
public class PersonalDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;


    private Integer userId;

    @TableField("blood_sugar")
    private Double bloodSugar;


    private Double uricAcid;

    @TableField("weight")
    private Double weight;

    @TableField("date")
    private LocalDateTime date;
}
