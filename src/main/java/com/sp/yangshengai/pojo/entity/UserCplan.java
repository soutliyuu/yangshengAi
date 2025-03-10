package com.sp.yangshengai.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
@TableName("user_cplan")
@Builder
public class UserCplan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;


    private Integer userId;


    private Integer cPlanid;


    private Integer cEplanid;
}
