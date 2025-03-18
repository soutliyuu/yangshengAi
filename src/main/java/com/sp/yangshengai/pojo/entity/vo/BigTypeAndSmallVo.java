package com.sp.yangshengai.pojo.entity.vo;

import com.sp.yangshengai.pojo.entity.Smalltype;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BigTypeAndSmallVo {

    private Integer id;

    private String name;

    private List<Smalltype> smalltypes;
}
