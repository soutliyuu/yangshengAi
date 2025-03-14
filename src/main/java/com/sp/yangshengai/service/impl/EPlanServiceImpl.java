package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.*;
import com.sp.yangshengai.mapper.EPlanMapper;
import com.sp.yangshengai.pojo.entity.bo.EPlanBo;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
import com.sp.yangshengai.pojo.entity.vo.EPlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanVo;
import com.sp.yangshengai.pojo.entity.vo.PlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;
import com.sp.yangshengai.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
@Service
@RequiredArgsConstructor
public class EPlanServiceImpl extends ServiceImpl<EPlanMapper, EPlan> implements EPlanService {

    private final EPlanSmallService planSmallService;

    private final EUserPlanService userPlanService;

    private final ESmalltypeService smalltypeService;

    private final UserCplanService userCplanService;

    @Override
    public void add(EPlanBo planBo) {
        EPlan eplan = EPlan.builder()
                .planName(planBo.getEPlanName())
                .description(planBo.getDescription())
                .status("0")
                .build();

        this.save(eplan);
        List<EPlanSmall> planSmallList = planBo.getESmallTypeIds().stream().map(id -> EPlanSmall.builder()
                .planId(eplan.getId())
                .sId(id)
                .build()).toList();
        planSmallService.saveBatch(planSmallList);
        EUserPlan userPlan = EUserPlan.builder()
                .planId(eplan.getId())
                .userId(Math.toIntExact(SecurityUtils.getUserId()))
                .build();
        userPlanService.save(userPlan);
//
//        UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
//        if (userCplan == null){
//            userCplan = UserCplan.builder()
//                    .userId(Math.toIntExact(SecurityUtils.getUserId()))
//                    .cPlanid(null)
//                    .cEplanid(eplan.getId())
//                    .build();
//        }else {
//            userCplan.setCEplanid(eplan.getId());
//            userCplanService.updateById(userCplan);
//        }


    }

    @Override
    public TableDataInfo<EPlanVo> getpage(PageQuery pageQuery) {
        List<EUserPlan> list = userPlanService.list(new LambdaQueryWrapper<EUserPlan>().eq(EUserPlan::getUserId, SecurityUtils.getUserId()));
        if (list != null){
            List<EPlanVo> planVos = list.stream().map(userPlan -> {
                EPlan plan = this.getById(userPlan.getPlanId());
                EPlanVo planVo = EPlanVo.builder()
                        .id(plan.getId())
                        .ePlanName(plan.getPlanName())
                        .description(plan.getDescription())
                        .status(plan.getStatus())
                        .build();
                return planVo;
            }).sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).toList();
            return TableDataInfo.build(planVos);
        }
        return null;
    }

    @Override
    public EPlanDetailVo getDetailById(Integer id) {
        if (id != null){
            EPlan plan = this.getById(id);
            EPlanDetailVo planDetailVo = EPlanDetailVo.builder()
                    .name(plan.getPlanName())
                    .description(plan.getDescription())
                    .build();

            List<EPlanSmall> planSmallList = planSmallService.list(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, id));
            List<Integer> smallTypeIds = planSmallList.stream().map(EPlanSmall::getSId).toList();
            if (smallTypeIds != null){
                List<ESmalltype> smalltypeList = smalltypeService.listByIds(smallTypeIds);
                List<EPlanDetailVo.SmallTypeDetailVo> sTypeDetailVos = smalltypeList.stream().map(smalltype -> {
                    EPlanDetailVo.SmallTypeDetailVo smallTypeDetailVo = EPlanDetailVo.SmallTypeDetailVo.builder()
                            .id(smalltype.getId())
                            .name(smalltype.getEsName())
                            .image(smalltype.getImage())
                            .build();
                    return smallTypeDetailVo;
                }).toList();
                planDetailVo.setSmalltypes(sTypeDetailVos);
                return planDetailVo;
            }
        }
        return null;
    }

    @Override
    public List<EPlanVo> getSuggestPlan() {
        List<EPlan> list = this.list(new LambdaQueryWrapper<EPlan>().eq(EPlan::getStatus, "1"));
        if (list != null){
            List<EPlanVo> planVos = list.stream().map(plan -> {
                EPlanVo planVo = EPlanVo.builder()
                        .id(plan.getId())
                        .ePlanName(plan.getPlanName())
                        .description(plan.getDescription())
                        .status(plan.getStatus())
                        .build();
                return planVo;
            }).toList();
            return planVos;
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        this.removeById(id);
        planSmallService.remove(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, id));
    }

    @Override
    public void updatePlan(EPlanBo planBo) {
        EPlan plan = EPlan.builder().id(planBo.getId())
                .planName(planBo.getEPlanName())
                .description(planBo.getDescription())
                .build();
        this.updateById(plan);
        planSmallService.remove(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, planBo.getId()));
        List<EPlanSmall> planSmallList = planBo.getESmallTypeIds().stream().map(id -> EPlanSmall.builder()
                .planId(plan.getId())
                .sId(id)
                .build()).toList();
        planSmallService.saveBatch(planSmallList);
    }

}
