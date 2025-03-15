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

    @Override
    public void addbyeplan(Integer id) {
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        if (userCplan == null) {
            userCplan = UserCplan.builder()
                    .userId(SecurityUtils.getUserId())
                    .cEplanid(id)
                    .build();
            save(userCplan);
        }else {
            userCplan.setCEplanid(id);
            updateById(userCplan);
        }


    }

    @Override
    public void addbyplan(Integer id) {
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        if (userCplan == null){
            userCplan = UserCplan.builder()
                    .userId(SecurityUtils.getUserId())
                    .cPlanid(id)
                    .build();
            save(userCplan);
        }else {
            userCplan.setCPlanid(id);
            updateById(userCplan);
        }

    }

    @Override
    public CplanVo getcplan() {
        UserCplan userCplan = getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));
        CplanVo cplanVo = new CplanVo();
        if (userCplan == null){
            throw new RuntimeException("没有当前计划");
        }
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
