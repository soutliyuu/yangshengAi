package com.sp.yangshengai.pojo.entity.vo;

import com.sp.yangshengai.pojo.entity.ESmalltype;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EBigTypeAndSmallVo {

    private Integer id;

    private String name;

    private List<ESmalltype> eSmalltypes;
}
