package com.sp.yangshengai.pojo.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CplanVo {

    private PlanVo planVo;

    private EPlanVo ePlanVo;
}
