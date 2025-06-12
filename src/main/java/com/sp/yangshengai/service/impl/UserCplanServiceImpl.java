package com.sp.yangshengai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.EPlan;
import com.sp.yangshengai.pojo.entity.Plan;
import com.sp.yangshengai.pojo.entity.UserCplan;
import com.sp.yangshengai.mapper.UserCplanMapper;
import com.sp.yangshengai.pojo.entity.vo.CplanVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;
import com.sp.yangshengai.service.EPlanService;
import com.sp.yangshengai.service.PlanService;
import com.sp.yangshengai.service.UserCplanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
@Service
@RequiredArgsConstructor
public class UserCplanServiceImpl extends ServiceImpl<UserCplanMapper, UserCplan> implements UserCplanService {



    private PlanService getPlanService() { return SpringUtil.getBean(PlanService.class); }

    private EPlanService getEPlanService() { return SpringUtil.getBean(EPlanService.class); }
    // 新增一个当前运动计划
    @Override
    public void addbyeplan(Integer id) {
        // 查询当前用户是否已经有一个运动计划
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        if (userCplan == null) {
            // 如果用户没有运动计划，则创建一个新的运动计划并保存
            userCplan = UserCplan.builder()
                    .userId(SecurityUtils.getUserId())
                    .cEplanid(id)
                    .build();
            save(userCplan);
        } else {
            // 如果用户已经有运动计划，则更新运动计划的ID
            userCplan.setCEplanid(id);
            updateById(userCplan);
        }
    }

    /**
     * 根据计划ID添加用户计划关联
     * 如果用户尚未关联任何计划，则创建新的关联记录
     * 如果用户已有关联计划，则更新关联的计划ID
     *
     * @param id 计划ID，用于指定用户要关联的计划
     */
    @Override
    public void addbyplan(Integer id) {
        // 查询当前用户是否已有关联计划
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        if (userCplan == null){
            // 如果用户没有关联计划，创建新的关联记录
            userCplan = UserCplan.builder()
                    .userId(SecurityUtils.getUserId())
                    .cPlanid(id)
                    .build();
            save(userCplan);
        }else {
            // 如果用户已有关联计划，更新关联的计划ID
            userCplan.setCPlanid(id);
            updateById(userCplan);
        }
    }
    /**
     * 获取用户的当前运动计划和膳食计划
     *
     * 此方法首先查询用户当前的运动计划和膳食计划ID，然后根据这些ID获取详细的计划信息，
     * 并将这些信息封装到CplanVo对象中返回如果用户没有当前计划，则抛出异常
     *
     * @return CplanVo对象，包含用户的运动计划和膳食计划信息
     * @throws RuntimeException 如果用户没有当前计划，则抛出运行时异常
     */
    @Override
    public CplanVo getcplan() {

        // 查询用户当前的运动计划和膳食计划ID
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        CplanVo cplanVo = new CplanVo();
        // 如果用户没有当前计划，抛出异常
        if (userCplan == null){
            throw new RuntimeException("没有当前计划");
        }
        // 获取用户的当前运动计划ID，并根据ID获取详细的运动计划信息
        Integer cEplanid = userCplan.getCEplanid();
        if (cEplanid != null){
            EPlan ebyId = this.getEPlanService().getById(cEplanid);
            EPlanVo ePlanVo = EPlanVo.builder()
                    .id(ebyId.getId())
                    .ePlanName(ebyId.getPlanName())
                    .description(ebyId.getDescription())
                    .status(ebyId.getStatus())
                    .build();
            cplanVo.setEPlanVo(ePlanVo);
        }
        // 获取用户的当前膳食计划ID，并根据ID获取详细的膳食计划信息
        if (userCplan.getCPlanid() != null){
            Plan byId = this.getPlanService().getById(userCplan.getCPlanid());
            PlanVo planVo = PlanVo.builder()
                    .id(byId.getId())
                    .planName(byId.getPlanName())
                    .description(byId.getDescription())
                    .status(byId.getStatus())
                    .build();
            cplanVo.setPlanVo(planVo);
        }

        return cplanVo;
    }
}
