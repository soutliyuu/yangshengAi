package com.sp.yangshengai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.Signin;
import com.sp.yangshengai.mapper.SigninMapper;
import com.sp.yangshengai.pojo.entity.UserCplan;
import com.sp.yangshengai.pojo.entity.vo.SiginVo;
import com.sp.yangshengai.service.SigninService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<SiginVo> getmonth(String monthStr) {
        // 根据月份字符串获取月份的开始和结束日期
        LocalDateTime[] monthRange = getMonthRange(monthStr);
        LocalDateTime startDate = monthRange[0];
        LocalDateTime endDate = monthRange[1];

        // 使用 MyBatis-Plus 查询该月份内的签到数据
        LambdaQueryWrapper<Signin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Signin::getUserId, SecurityUtils.getUserId())
                .between(Signin::getDate, startDate, endDate);
        List<Signin> signins = baseMapper.selectList(queryWrapper);

        // 生成该月份的所有日期，并标记哪些日期有签到
        List<SiginVo> siginVos = generateSiginVosForMonth(startDate, endDate, signins);

        return siginVos;
    }

    private LocalDateTime[] getMonthRange(String monthStr) {
        // 根据月份字符串获取月份的开始和结束日期
        Month month = getMonthFromAbbreviation(monthStr);
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), month, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        return new LocalDateTime[]{startDate, endDate};
    }

    public Month getMonthFromAbbreviation(String monthStr) {
        Map<String, Month> monthMap = new HashMap<>();
        monthMap.put("JAN", Month.JANUARY);
        monthMap.put("FEB", Month.FEBRUARY);
        monthMap.put("MAR", Month.MARCH);
        monthMap.put("APR", Month.APRIL);
        monthMap.put("MAY", Month.MAY);
        monthMap.put("JUN", Month.JUNE);
        monthMap.put("JUL", Month.JULY);
        monthMap.put("AUG", Month.AUGUST);
        monthMap.put("SEP", Month.SEPTEMBER);
        monthMap.put("OCT", Month.OCTOBER);
        monthMap.put("NOV", Month.NOVEMBER);
        monthMap.put("DEC", Month.DECEMBER);

        String upperCaseMonthStr = monthStr.toUpperCase();
        return monthMap.get(upperCaseMonthStr);
    }

    private List<SiginVo> generateSiginVosForMonth(LocalDateTime startDate, LocalDateTime endDate, List<Signin> signins) {
        List<SiginVo> siginVos = new ArrayList<>();

        // 遍历该月份的所有日期
        LocalDateTime currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            SiginVo siginVo = new SiginVo();
            siginVo.setDateTime(currentDate);
            LocalDate currentLocalDate = currentDate.toLocalDate();

            // 检查该日期是否有签到
            boolean isSignIn = signins.stream()
                    .anyMatch(signin -> signin.getDate().toLocalDate().equals(currentLocalDate));
            siginVo.setIsSignIn(isSignIn);

            siginVos.add(siginVo);
            currentDate = currentDate.plusDays(1);
        }

        return siginVos;
    }


}
