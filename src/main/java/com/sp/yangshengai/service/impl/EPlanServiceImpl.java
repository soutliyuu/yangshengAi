package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
/**
 * 添加一个运动计划
 *
 * 此方法负责将用户提交的运动计划信息持久化到数据库中它主要包括以下几个步骤：
 * 1. 创建一个EPlan对象，设置基本信息如计划名称、描述和状态
 * 2. 保存EPlan对象到数据库
 * 3. 根据计划详情中的小类型ID列表，创建对应的EPlanSmall对象列表，并保存到数据库
 * 4. 创建一个EUserPlan对象，关联当前用户和新创建的运动计划，并保存到数据库
 *
 * @param planBo 运动计划的业务对象，包含计划的基本信息、描述、以及小类型ID列表
 */
@Override
public void add(EPlanBo planBo) {
    // 创建一个新的运动计划对象，设置基本信息
    EPlan eplan = EPlan.builder()
            .planName(planBo.getEPlanName())
            .description(planBo.getDescription())
            .status("0")
            .build();

    // 保存运动计划到数据库
    this.save(eplan);

    // 根据计划详情中的小类型ID列表，创建并保存对应的EPlanSmall对象列表
    List<EPlanSmall> planSmallList = planBo.getESmallTypeIds().stream().map(id -> EPlanSmall.builder()
            .planId(eplan.getId())
            .sId(id)
            .build()).toList();
    planSmallService.saveBatch(planSmallList);

    // 创建一个用户运动计划对象，关联当前用户和新创建的运动计划，并保存到数据库
    EUserPlan userPlan = EUserPlan.builder()
            .planId(eplan.getId())
            .userId(Math.toIntExact(SecurityUtils.getUserId()))
            .build();
    userPlanService.save(userPlan);


    //
    // UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
    // if (userCplan == null){
    //     userCplan = UserCplan.builder()
    //             .userId(Math.toIntExact(SecurityUtils.getUserId()))
    //             .cPlanid(null)
    //             .cEplanid(eplan.getId())
    //             .build();
    // }else {
    //     userCplan.setCEplanid(eplan.getId());
    //     userCplanService.updateById(userCplan);
    // }
}

    /**
     * 获取当前用户的运动计划分页查询
     * 此方法根据当前用户ID查询该用户所有的运动计划，并进行分页处理
     * @param pageQuery 分页查询参数对象，包含页码、页大小等信息
     * @return 返回一个TableDataInfo对象，其中包含分页查询结果
     */
    @Override
    public TableDataInfo<EPlanVo> getpage(PageQuery pageQuery) {
        // 查询当前用户所有的运动计划
        List<EUserPlan> list = userPlanService.list(new LambdaQueryWrapper<EUserPlan>().eq(EUserPlan::getUserId, SecurityUtils.getUserId()));
        if (list != null){
            // 将查询到的运动计划转换为VO对象，并按计划ID降序排序
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
            // 构建并返回分页数据信息
            return TableDataInfo.build(planVos);
        }
        // 如果没有查询到任何运动计划，返回null
        return null;
    }
    //获取该计划的详情
    @Override
    public EPlanDetailVo getDetailById(Integer id) {
        //检查传入的ID是否为空
        if (id != null){
            //根据ID获取计划信息
            EPlan plan = this.getById(id);
            //构建计划详情对象
            EPlanDetailVo planDetailVo = EPlanDetailVo.builder()
                    .id(plan.getId())
                    .name(plan.getPlanName())
                    .description(plan.getDescription())
                    .build();

            //查询与该计划相关的子计划列表
            List<EPlanSmall> planSmallList = planSmallService.list(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, id));
            //如果子计划列表为空，则返回null
            if (planSmallList == null){
                return null;
            }
            //提取子计划列表中的小类型ID
            List<Integer> smallTypeIds = planSmallList.stream().map(EPlanSmall::getSId).toList();
            //如果小类型ID列表不为空
            if (smallTypeIds != null){
                //根据小类型ID列表获取小类型信息列表
                List<ESmalltype> smalltypeList = smalltypeService.listByIds(smallTypeIds);
                //将小类型信息列表转换为小类型详情列表
                List<EPlanDetailVo.SmallTypeDetailVo> sTypeDetailVos = smalltypeList.stream().map(smalltype -> {
                    EPlanDetailVo.SmallTypeDetailVo smallTypeDetailVo = EPlanDetailVo.SmallTypeDetailVo.builder()
                            .id(smalltype.getId())
                            .name(smalltype.getEsName())
                            .image(smalltype.getImage())
                            .build();
                    return smallTypeDetailVo;
                }).toList();
                //将小类型详情列表设置到计划详情对象中并返回
                planDetailVo.setSmalltypes(sTypeDetailVos);
                return planDetailVo;
            }
        }
        //如果传入的ID为空或其他条件不满足，则返回null
        return null;
    }
    /**
     * 获取管理员设计的推荐运动计划
     *
     * 此方法用于从数据库中查询所有状态为"1"（推荐）的运动计划，
     * 并将它们映射到EPlanVo对象中，以便于展示层展示
     *
     * @return 如果存在状态为"1"的运动计划，则返回对应的EPlanVo列表；否则返回null
     */
    @Override
    public List<EPlanVo> getSuggestPlan() {
        // 查询所有状态为"1"的运动计划
        List<EPlan> list = this.list(new LambdaQueryWrapper<EPlan>().eq(EPlan::getStatus, "1"));
        if (list != null){
            // 将查询到的运动计划映射为EPlanVo对象列表
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
    // 管理员设计运动计划设为推荐计划
    @Override
    public void setSuggestEPlan(Integer id) {
        // 查询当前所有状态为推荐的运动计划
        List<EPlan> list = this.list(new LambdaQueryWrapper<EPlan>().eq(EPlan::getStatus, "1"));

        // 检查当前推荐计划数量是否已达到上限（9条）
        if (list.size() > 9) {
            // 如果超过上限，抛出异常提示用户
            throw new RuntimeException("只能设置九条推荐计划");
        }

        // 更新指定ID的运动计划状态为推荐
        this.update(new LambdaUpdateWrapper<EPlan>().eq(EPlan::getId, id)
                .set(EPlan::getStatus, "1"));
    }
    /**
     * 删除一个推荐计划
     *
     * @param id 推荐计划的ID，用于标识要删除的计划
     */
    @Override
    public void removeSuggestEPlan(Integer id) {
        // 通过更新计划的状态为"0"来逻辑删除推荐计划
        this.update(new LambdaUpdateWrapper<EPlan>().eq(EPlan::getId,id)
                .set(EPlan::getStatus, "0"));
    }
    // 删除运动计划
    // 此方法用于删除指定ID的运动计划，包括直接删除计划、相关的小计划以及用户与该计划的关联
    @Override
    public void delete(Integer id) {
        // 删除主计划
        this.removeById(id);
        // 删除与主计划关联的小计划
        planSmallService.remove(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, id));
        // 删除用户与计划的关联，确保删除的是当前用户的相关数据
        userPlanService.remove(new LambdaQueryWrapper<EUserPlan>().eq(EUserPlan::getUserId, SecurityUtils.getUserId()).eq(EUserPlan::getPlanId, id));
    }

    /**
     * 更新计划信息
     * 此方法首先将计划信息的更新内容转换为实体对象，然后更新数据库中的计划记录
     * 接着，它会删除与该计划关联的所有小类型记录，并根据新的小类型ID列表重新创建这些关联
     *
     * @param planBo 包含计划更新信息的业务对象
     */
    @Override
    public void updatePlan(EPlanBo planBo) {
        // 将计划信息BO转换为EPlan对象，用于数据库操作
        EPlan plan = EPlan.builder().id(planBo.getId())
                .planName(planBo.getEPlanName())
                .description(planBo.getDescription())
                .build();

        // 更新数据库中的计划记录
        this.updateById(plan);

        // 删除与当前计划关联的所有小类型记录，以便重新关联新的小类型
        planSmallService.remove(new LambdaQueryWrapper<EPlanSmall>().eq(EPlanSmall::getPlanId, planBo.getId()));

        // 根据新的小类型ID列表，创建新的EPlanSmall对象列表
        List<EPlanSmall> planSmallList = planBo.getESmallTypeIds().stream().map(id -> EPlanSmall.builder()
                .planId(plan.getId())
                .sId(id)
                .build()).toList();

        // 批量保存新的EPlanSmall对象列表，重新建立计划与小类型的关联
        planSmallService.saveBatch(planSmallList);
    }

}
