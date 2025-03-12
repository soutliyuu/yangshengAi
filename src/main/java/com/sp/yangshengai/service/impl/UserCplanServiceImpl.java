package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.UserCplan;
import com.sp.yangshengai.mapper.UserCplanMapper;
import com.sp.yangshengai.service.UserCplanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
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
public class UserCplanServiceImpl extends ServiceImpl<UserCplanMapper, UserCplan> implements UserCplanService {

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
}
