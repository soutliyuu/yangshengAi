package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
import com.sp.yangshengai.pojo.entity.vo.PlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-04
 */
public interface PlanService extends IService<Plan> {

    void add(PlanBo planBo);

    TableDataInfo<PlanVo> getpage(PageQuery pageQuery);

    PlanDetailVo getDetailById(Integer id);

    List<PlanVo> getSuggestPlan();

    void delete(Integer id);

    void updatePlan(PlanBo planBo);
}
