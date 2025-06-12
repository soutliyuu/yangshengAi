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



    /**
     * 重写添加签到方法
     * 本方法用于处理用户每日签到逻辑，确保用户每天只能签到一次，并且已经设置了计划
     */
    @Override
    public void add() {
        // 获取当前日期的开始时间（00:00:00）
        LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        // 获取当前日期的结束时间（23:59:59.999999999）
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);

        // 查询今天是否已经签到
        Signin signin = baseMapper.selectOne(new LambdaQueryWrapper<Signin>().between(Signin::getDate,
               startOfDay,endOfDay));
        // 如果今天已经签到，抛出异常
        if (signin != null) {
            throw new RuntimeException("今天已经签过到了");
        }

        // 获取用户当前的计划
        UserCplan userCplan = userCplanService.getOne(new LambdaQueryWrapper<UserCplan>().eq(UserCplan::getUserId, SecurityUtils.getUserId()));

        // 如果用户没有设置计划，抛出异常
        if (userCplan == null) {
            throw new RuntimeException("请先设置计划");
        }
        // 如果用户未设置膳食计划和运动计划，抛出异常
        if (userCplan.getCPlanid() == null || userCplan.getCEplanid() == null) {
            throw new RuntimeException("请先添加膳食计划和运动计划");
        }

        // 创建签到对象，准备插入签到记录
        signin =  Signin.builder()
                .cPlanid(userCplan.getCPlanid())
                .cEplanid(userCplan.getCEplanid())
                .userId(SecurityUtils.getUserId())
                .date(LocalDateTime.now())
                .build();
        // 插入签到记录到数据库
        baseMapper.insert(signin);
    }

    /**
     * 根据月份字符串获取该月份的签到信息
     *
     * @param monthStr 月份字符串，格式为 "YYYY-MM"
     * @return 返回包含该月份签到信息的SiginVo对象列表
     */
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

    /**
     * 根据月份的缩写获取对应的Month枚举对象
     * 此方法使用一个HashMap来映射月份的缩写到相应的Month枚举对象，以便于快速查找
     * 它首先将输入的月份缩写转换为大写，以确保匹配的一致性，然后从HashMap中获取对应的Month枚举对象
     *
     * @param monthStr 月份的缩写，例如"JAN"代表一月
     * @return 对应的Month枚举对象，如果找不到匹配项，则返回null
     */
    public Month getMonthFromAbbreviation(String monthStr) {
        // 创建一个HashMap来存储月份缩写与Month枚举的映射关系
        Map<String, Month> monthMap = new HashMap<>();
        // 以下代码块初始化月份缩写与Month枚举的映射关系
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

        // 将输入的月份缩写转换为大写，以确保与HashMap中的键匹配
        String upperCaseMonthStr = monthStr.toUpperCase();
        // 根据转换后的月份缩写从HashMap中获取并返回对应的Month枚举对象
        return monthMap.get(upperCaseMonthStr);
    }

    /**
     * 根据起始和结束日期生成每月的签到视图对象列表
     * 此方法用于处理给定月份的每一天，判断每一天是否已签到，并生成相应的签到视图对象
     *
     * @param startDate 月份的起始日期和时间
     * @param endDate 月份的结束日期和时间
     * @param signins 在给定月份内所有用户的签到记录列表
     * @return 返回包含每个月每一天签到信息的SiginVo列表
     */
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
