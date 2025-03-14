package com.sp.yangshengai.pojo.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlanDetailVo {

   private String name;

   private String description;

   private List<SmallTypeDetailVo> smalltypes;

   @Data
   @Builder
   public static class SmallTypeDetailVo {
       private Integer id;

       private String name;

       private String image;

   }
}

