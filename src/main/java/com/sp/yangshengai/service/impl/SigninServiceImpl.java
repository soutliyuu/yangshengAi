package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.Signin;
import com.sp.yangshengai.mapper.SigninMapper;
import com.sp.yangshengai.pojo.entity.UserCplan;
import com.sp.yangshengai.service.SigninService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
public class SigninServiceImpl extends ServiceImpl<SigninMapper, Signin> implements SigninService {

    private final UserCplanServiceImpl userCplanService;

    @Override
    public void add() {
        Signin signin1 = baseMapper.selectOne(new LambdaQueryWrapper<Signin>().between(Signin::getDate,
                LocalDateTime.now().with(LocalDateTime.MIN), LocalDateTime.now().with(LocalDateTime.MAX)));
        if (signin1 != null) {
            throw new RuntimeException("今天已经签过到了");
        }

        UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));

        if (userCplan == null) {
            throw new RuntimeException("请先设置计划");
        }
        Signin signin = Signin.builder()
                .userId(SecurityUtils.getUserId())
                .cPlanid(userCplan.getCPlanid())
                .cEplanid(userCplan.getCEplanid())
                .date(LocalDateTime.now())
                .build();
    }
}
