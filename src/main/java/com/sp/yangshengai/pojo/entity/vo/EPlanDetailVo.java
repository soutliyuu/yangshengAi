package com.sp.yangshengai.pojo.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EPlanDetailVo {

    private Integer id;

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
