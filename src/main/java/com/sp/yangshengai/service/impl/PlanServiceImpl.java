package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sp.yangshengai.pojo.entity.*;
import com.sp.yangshengai.mapper.PlanMapper;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
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
 * @since 2025-03-04
 */
@RequiredArgsConstructor
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    private final PlanSmallService planSmallService;

    private final UserPlanService userPlanService;

    private final SmalltypeService smalltypeService;



    @Override
    public void add(PlanBo planBo) {
        Plan plan = Plan.builder()
                .planName(planBo.getPlanName())
                .description(planBo.getDescription())
                .status("0")
                .build();

        this.save(plan);
        List<PlanSmall> planSmallList = planBo.getSmallTypeIds().stream().map(id -> PlanSmall.builder()
                .planId(plan.getId())
                .sId(id)
                .build()).toList();
        planSmallService.saveBatch(planSmallList);
        UserPlan userPlan = UserPlan.builder()
                .planId(plan.getId())
                .userId(Math.toIntExact(SecurityUtils.getUserId()))
                .build();
        userPlanService.save(userPlan);
//
//        UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
//        if (userCplan == null){
//            userCplan = UserCplan.builder()
//                    .userId(Math.toIntExact(SecurityUtils.getUserId()))
//                    .cPlanid(plan.getId())
//                    .cEplanid(null)
//                    .build();
//        }else {
//            userCplan.setCPlanid(plan.getId());
//            userCplanService.updateById(userCplan);
//        }

    }

    @Override
    public TableDataInfo<PlanVo> getpage(PageQuery pageQuery) {
        List<UserPlan> list = userPlanService.list(new LambdaQueryWrapper<UserPlan>().eq(UserPlan::getUserId, SecurityUtils.getUserId()));
        if (list != null){
            List<PlanVo> planVos = list.stream().map(userPlan -> {
                Plan plan = this.getById(userPlan.getPlanId());
                PlanVo planVo = PlanVo.builder()
                        .id(plan.getId())
                        .planName(plan.getPlanName())
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
    public PlanDetailVo getDetailById(Integer id) {
        if (id != null){
            Plan plan = this.getById(id);
            PlanDetailVo planDetailVo = PlanDetailVo.builder()
                    .id(plan.getId())
                    .name(plan.getPlanName())
                    .description(plan.getDescription())
                    .build();

            List<PlanSmall> planSmallList = planSmallService.list(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, id));
            List<Integer> smallTypeIds = planSmallList.stream().map(PlanSmall::getSId).toList();
            if (smallTypeIds != null){
                List<Smalltype> smalltypeList = smalltypeService.listByIds(smallTypeIds);
                List<PlanDetailVo.SmallTypeDetailVo>  smallTypeDetailVos= smalltypeList.stream().map(smalltype -> {
                     PlanDetailVo.SmallTypeDetailVo smallTypeDetailVo = PlanDetailVo.SmallTypeDetailVo.builder()
                            .id(smalltype.getId())
                            .name(smalltype.getSName())
                            .image(smalltype.getImage())
                            .build();
                   return smallTypeDetailVo;
                }).toList();
                planDetailVo.setSmalltypes(smallTypeDetailVos);
                return planDetailVo;
            }
        }
        return null;
    }

    @Override
    public List<PlanVo> getSuggestPlan() {
        List<Plan> list = this.list(new LambdaQueryWrapper<Plan>().eq(Plan::getStatus, "1"));
        if (list != null){
            List<PlanVo> planVos = list.stream().map(plan -> {
                PlanVo planVo = PlanVo.builder()
                        .id(plan.getId())
                        .planName(plan.getPlanName())
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
        planSmallService.remove(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, id));
    }

    @Override
    public void updatePlan(PlanBo planBo) {
        Plan plan = Plan.builder().id(planBo.getId())
                .planName(planBo.getPlanName())
                .description(planBo.getDescription())
                .build();
        this.updateById(plan);
        planSmallService.remove(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, planBo.getId()));
        List<PlanSmall> planSmallList = planBo.getSmallTypeIds().stream().map(id -> PlanSmall.builder()
                .planId(plan.getId())
                .sId(id)
                .build()).toList();
        planSmallService.saveBatch(planSmallList);
    }

    @Override
    public void setSuggestPlan(Integer id) {
        List<Plan> list = this.list(new LambdaQueryWrapper<Plan>().eq(Plan::getStatus, "1"));
        if (list.size()>9){
            throw  new RuntimeException("只能设置九条推荐计划");
        }
       this.update(new LambdaUpdateWrapper<Plan>().eq(Plan::getId,id)
                                                    .set(Plan::getStatus, "1"));

    }

    @Override
    public void removeSuggestPlan(Integer id) {
        this.update(new LambdaUpdateWrapper<Plan>().eq(Plan::getId,id)
            .set(Plan::getStatus, "0"));

    }
}
