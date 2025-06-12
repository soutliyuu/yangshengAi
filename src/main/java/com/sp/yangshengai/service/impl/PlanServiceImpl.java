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


/**
 * 添加一个膳食计划
 *
 * 此方法负责将用户提交的膳食计划基本信息、小计划关联信息和用户计划关联信息保存到数据库中
 * 它首先构建一个Plan对象，然后保存它，接着根据小计划ID列表构建并保存PlanSmall对象列表，
 * 最后创建并保存UserPlan对象以关联用户和膳食计划
 *
 * @param planBo 包含膳食计划基本信息、小计划ID列表和用户ID的膳食计划构建对象
 */
@Override
public void add(PlanBo planBo) {
    // 根据传入的膳食计划构建对象创建一个Plan对象，并设置其状态为"0"
    Plan plan = Plan.builder()
            .planName(planBo.getPlanName())
            .description(planBo.getDescription())
            .status("0")
            .build();

    // 保存膳食计划到数据库
    this.save(plan);

    // 将小计划ID列表转换为PlanSmall对象列表，关联当前创建的膳食计划
    List<PlanSmall> planSmallList = planBo.getSmallTypeIds().stream().map(id -> PlanSmall.builder()
            .planId(plan.getId())
            .sId(id)
            .build()).toList();

    // 批量保存小计划关联信息到数据库
    planSmallService.saveBatch(planSmallList);

    // 创建一个UserPlan对象，关联当前用户和新创建的膳食计划
    UserPlan userPlan = UserPlan.builder()
            .planId(plan.getId())
            .userId(Math.toIntExact(SecurityUtils.getUserId()))
            .build();

    // 保存用户计划关联信息到数据库
    userPlanService.save(userPlan);


    // UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
    // if (userCplan == null){
    //     userCplan = UserCplan.builder()
    //             .userId(Math.toIntExact(SecurityUtils.getUserId()))
    //             .cPlanid(plan.getId())
    //             .cEplanid(null)
    //             .build();
    // }else {
    //     userCplan.setCPlanid(plan.getId());
    //     userCplanService.updateById(userCplan);
    // }
}


    /**
     * 根据页面查询对象获取分页数据
     * 此方法主要用于处理分页查询请求，返回特定用户的计划信息
     *
     * @param pageQuery 包含分页信息的查询对象，用于指定查询的页码和页面大小等信息
     * @return 返回一个TableDataInfo对象，其中包含查询到的计划信息列表如果查询结果为空，则返回null
     */
    @Override
    public TableDataInfo<PlanVo> getpage(PageQuery pageQuery) {
        // 查询当前用户的所有计划
        List<UserPlan> list = userPlanService.list(new LambdaQueryWrapper<UserPlan>().eq(UserPlan::getUserId, SecurityUtils.getUserId()));
        if (list != null){
            // 将查询到的用户计划列表转换为计划详情列表，并按照计划ID降序排序
            List<PlanVo> planVos = list.stream().map(userPlan -> {
                // 根据计划ID获取计划详情
                Plan plan = this.getById(userPlan.getPlanId());
                // 构建计划详情对象
                PlanVo planVo = PlanVo.builder()
                        .id(plan.getId())
                        .planName(plan.getPlanName())
                        .description(plan.getDescription())
                        .status(plan.getStatus())
                        .build();
                return planVo;
            }).sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).toList();
           // 构建并返回表格数据信息对象
           return TableDataInfo.build(planVos);
        }
        // 如果没有查询到任何计划，则返回null
        return null;
    }

    /**
     * 根据计划ID获取计划详细信息
     *
     * 此方法首先检查传入的ID是否为空如果ID不为空，它会通过ID获取计划的基本信息
     * 然后，它查询与该计划相关的所有小计划，并进一步获取每个小计划的详细信息
     * 最后，将计划信息和相关的小计划信息封装到一个Vo对象中并返回
     *
     * @param id 计划的唯一标识符
     * @return 返回包含计划详细信息的Vo对象，如果没有找到相应的计划或ID为空，则返回null
     */
    @Override
    public PlanDetailVo getDetailById(Integer id) {
        // 检查传入的ID是否为空
        if (id != null){
            // 通过ID获取计划的基本信息
            Plan plan = this.getById(id);
            // 构建PlanDetailVo对象，封装计划的基本信息
            PlanDetailVo planDetailVo = PlanDetailVo.builder()
                    .id(plan.getId())
                    .name(plan.getPlanName())
                    .description(plan.getDescription())
                    .build();

            // 查询与该计划相关的所有小计划
            List<PlanSmall> planSmallList = planSmallService.list(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, id));
            // 提取小计划的ID
            List<Integer> smallTypeIds = planSmallList.stream().map(PlanSmall::getSId).toList();
            // 检查小计划ID列表是否为空
            if (smallTypeIds != null){
                // 根据小计划的ID列表获取所有相关的小类型信息
                List<Smalltype> smalltypeList = smalltypeService.listByIds(smallTypeIds);
                // 将小类型信息封装到Vo对象中
                List<PlanDetailVo.SmallTypeDetailVo>  smallTypeDetailVos= smalltypeList.stream().map(smalltype -> {
                     PlanDetailVo.SmallTypeDetailVo smallTypeDetailVo = PlanDetailVo.SmallTypeDetailVo.builder()
                            .id(smalltype.getId())
                            .name(smalltype.getSName())
                            .image(smalltype.getImage())
                            .build();
                   return smallTypeDetailVo;
                }).toList();
                // 将小类型信息的Vo对象设置到计划详情Vo中
                planDetailVo.setSmalltypes(smallTypeDetailVos);
                // 返回封装好的计划详情Vo对象
                return planDetailVo;
            }
        }
        // 如果ID为空或没有找到相应的计划，返回null
        return null;
    }

    /**
     * 获取建议计划列表
     *
     * 本方法用于从数据库中查询所有状态为"1"的计划，表示这些计划是建议或推荐的
     * 它首先执行数据库查询以获取符合条件的计划列表，然后将每个计划对象转换为PlanVo对象，
     * 以便于后续的展示或处理
     *
     * @return 建议计划的列表，如果列表为空则返回null
     */
    @Override
    public List<PlanVo> getSuggestPlan() {
        // 查询数据库中所有状态为"1"的计划
        List<Plan> list = this.list(new LambdaQueryWrapper<Plan>().eq(Plan::getStatus, "1"));
        if (list != null){
            // 将查询到的计划列表转换为PlanVo对象列表
            List<PlanVo> planVos = list.stream().map(plan -> {
                // 创建PlanVo对象并赋值
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

    /**
     * 根据计划ID删除计划及其相关数据
     * 此方法不仅删除计划本身，还会删除与该计划相关的子计划和用户计划关联记录
     *
     * @param id 计划的唯一标识符
     */
    @Override
    public void delete(Integer id) {
        // 删除计划本身
        this.removeById(id);

        // 删除与计划相关的子计划
        planSmallService.remove(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, id));

        // 删除当前用户与计划的关联记录
        userPlanService.remove(new LambdaQueryWrapper<UserPlan>().eq(UserPlan::getUserId, SecurityUtils.getUserId()).eq(UserPlan::getPlanId, id));
    }

    /**
     * 更新计划信息
     *
     * 该方法首先将传入的PlanBo对象转换为Plan对象，并更新数据库中的计划信息
     * 然后，它会删除与该计划关联的所有小计划信息，以便更新小计划列表
     * 最后，根据传入的PlanBo对象中的小计划类型ID列表，创建新的小计划对象列表，并保存到数据库中
     *
     * @param planBo 包含更新后的计划信息和小计划类型ID列表的计划业务对象
     */
    @Override
    public void updatePlan(PlanBo planBo) {
        // 根据PlanBo对象构建Plan对象，并更新数据库中的计划信息
        Plan plan = Plan.builder().id(planBo.getId())
                .planName(planBo.getPlanName())
                .description(planBo.getDescription())
                .build();
        this.updateById(plan);

        // 删除与计划关联的所有小计划信息，以便更新小计划列表
        planSmallService.remove(new LambdaQueryWrapper<PlanSmall>().eq(PlanSmall::getPlanId, planBo.getId()));

        // 根据PlanBo中的小计划类型ID列表，创建新的小计划对象列表
        List<PlanSmall> planSmallList = planBo.getSmallTypeIds().stream().map(id -> PlanSmall.builder()
                .planId(plan.getId())
                .sId(id)
                .build()).toList();

        // 将新的小计划对象列表保存到数据库中
        planSmallService.saveBatch(planSmallList);
    }

    /**
     * 设置建议计划
     * 此方法用于将特定计划设置为建议计划状态
     * 它首先检查系统中当前有多少个状态为"1"（即已激活/推荐）的计划如果已激活的计划数量超过九个，则抛出异常
     * 否则，它将指定的计划设置为已激活/推荐状态
     *
     * @param id 计划的ID，用于标识要设置为建议计划的特定计划
     * @throws RuntimeException 如果系统中已存在的建议计划数量达到九个，则抛出运行时异常，表明不能再添加更多的建议计划
     */
    @Override
    public void setSuggestPlan(Integer id) {
        // 查询当前所有状态为"1"的计划，即已激活/推荐的计划
        List<Plan> list = this.list(new LambdaQueryWrapper<Plan>().eq(Plan::getStatus, "1"));
        // 检查已激活的计划数量是否达到上限
        if (list.size()>9){
            throw  new RuntimeException("只能设置九条推荐计划");
        }
        // 更新指定计划的状态为"1"，即设置为已激活/推荐
        this.update(new LambdaUpdateWrapper<Plan>().eq(Plan::getId,id)
                                                    .set(Plan::getStatus, "1"));

    }

    /**
     * 根据ID移除建议计划
     * 该方法通过更新计划的状态为"0"来逻辑删除计划，而不是物理删除
     * 使用LambdaUpdateWrapper实现条件更新
     *
     * @param id 计划的ID，用于标识要删除的计划
     */
    @Override
    public void removeSuggestPlan(Integer id) {
        this.update(new LambdaUpdateWrapper<Plan>().eq(Plan::getId,id)
            .set(Plan::getStatus, "0"));
    }
}
