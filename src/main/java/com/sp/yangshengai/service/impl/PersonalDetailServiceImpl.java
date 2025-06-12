package com.sp.yangshengai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.mapper.PersonalDetailMapper;
import com.sp.yangshengai.pojo.entity.PersonalDetail;
import com.sp.yangshengai.pojo.entity.bo.PersonalDetailBo;
import com.sp.yangshengai.pojo.entity.vo.EchartsResult;
import com.sp.yangshengai.pojo.entity.vo.PersonByTimeVo;
import com.sp.yangshengai.pojo.entity.vo.PersonalDetailVo;
import com.sp.yangshengai.service.PersonalDetailService;
import com.sp.yangshengai.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sp.yangshengai.utils.PersonEnum.SUGER;

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
@Slf4j
public class PersonalDetailServiceImpl extends ServiceImpl<PersonalDetailMapper, PersonalDetail> implements PersonalDetailService {

   private final PersonalDetailMapper personalDetailMapper;
    /**
     * 添加个人详细信息
     *
     * 该方法用于添加个人详细信息，包括血糖、尿酸和体重等数据
     * 它确保用户每天只能上传一次信息，以避免重复记录
     *
     * @param personalDetailbo 包含用户输入的个人详细信息的对象
     */
    @Override
    public void add(PersonalDetailBo personalDetailbo) {
        // 获取当前日期的开始时间（00:00:00）
        LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        // 获取当前日期的结束时间（23:59:59）
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);

        // 查询数据库中是否存在当天的个人详细信息记录
        PersonalDetail personalDetail = getOne(new LambdaQueryWrapper<PersonalDetail>().eq(PersonalDetail::getUserId, SecurityUtils.getUserId()).between(PersonalDetail::getDate, startOfDay, endOfDay));
        if (personalDetail == null) {
            // 如果当天没有记录，创建新的个人详细信息对象并保存到数据库
            personalDetail = PersonalDetail.builder()
                    .userId(SecurityUtils.getUserId())
                    .bloodSugar(personalDetailbo.getBloodSugar())
                    .uricAcid(personalDetailbo.getUricAcid())
                    .weight(personalDetailbo.getWeight())
                    .date(LocalDateTime.now())
                    .build();
            save(personalDetail);
        } else {
            // 如果当天已有记录，抛出异常提示用户今天已经上传信息
            throw new RuntimeException("今天已经上传信息");
        }
    }

    /**
     * 获取当前用户今天的个人信息详情
     *
     * 该方法主要用于获取当前用户在今天（从凌晨0点到午夜23:59:59）的个人信息详情
     * 如果今天没有上传任何信息，将抛出运行时异常
     *
     * @return PersonalDetailVo 返回个人信息详情的视图对象，包含用户今天的个人信息
     * @throws RuntimeException 如果今天没有上传信息，则抛出运行时异常
     */
    @Override
    public PersonalDetailVo getTodayDetail() {
        // 获取今天开始的时间点（凌晨0点）
        LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        // 获取今天结束的时间点（午夜23:59:59）
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);

        // 查询当前用户今天的信息
        PersonalDetail personalDetail = getOne(new LambdaQueryWrapper<PersonalDetail>()
                                                                        .eq(PersonalDetail::getUserId ,SecurityUtils.getUserId())
                                                                        .between(PersonalDetail::getDate, startOfDay, endOfDay));
        // 如果没有找到今天的个人信息，抛出异常
        if (personalDetail == null) {
           throw new RuntimeException("今天还没有上传信息");
        }

        // 将找到的个人信息转换为视图对象并返回
        return personalDetail != null ? BeanUtil.copyProperties(personalDetail, PersonalDetailVo.class) : null;
    }

    /**
     * 根据指定的时间范围和人员类型，获取Echarts图表所需的数据
     *
     * @param timeEnum 时间范围枚举，用于指定数据的时间范围
     * @param personEnum 人员类型枚举，用于指定需要获取的数据类型（如血糖、体重、尿酸）
     * @return 返回一个EchartsResult对象，包含根据时间和人员类型查询到的数据
     */
    @Override
    public EchartsResult<String, String> getEchartsData(TimeEnum timeEnum, PersonEnum personEnum) {
        // 根据时间枚举获取开始和结束时间
        StartAndEnd startAndEnd = new StartAndEnd(timeEnum);

        // 获取人员类型的描述，用于后续的数据处理
        String personStr = personEnum.getDesc();
        // 根据不同的人员类型枚举，执行相应的数据查询和处理
        switch (personEnum){
            case SUGER:
                // 查询并收集指定时间范围内用户的血糖数据
                Map<LocalDateTime, Double> bloodSugarMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                        .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                        .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getBloodSugar));

                // 处理血糖数据并返回结果
                return Handledata(bloodSugarMap,startAndEnd);
            case WEIGHT:
                // 查询并收集指定时间范围内用户的体重数据
                Map<LocalDateTime, Double> weightMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                                .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                                .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getWeight));

                // 处理体重数据并返回结果
                return Handledata(weightMap,startAndEnd);
            case URIC:
                // 查询并收集指定时间范围内用户的尿酸数据
                Map<LocalDateTime, Double> uricMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                                .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                                .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getUricAcid));

                // 处理尿酸数据并返回结果
                return Handledata(uricMap,startAndEnd);
        }
        // 如果人员类型不符合任何已知条件，返回null
        return null;
    }

    /**
     * 处理数据以生成Echarts所需的格式
     * 此方法主要负责将给定的数据映射到Echarts图表所需的结构中，包括X轴标签和系列数据
     *
     * @param dataMap 包含时间点和对应数值的数据映射
     * @param startAndEnd 包含开始和结束时间的对象，用于获取时间列表和格式化X轴标签
     * @return 返回一个EchartsResult对象，包含格式化后的数据
     */
    private EchartsResult<String, String> Handledata(Map<LocalDateTime, Double> dataMap,StartAndEnd startAndEnd) {
        // 获取指定时间范围内的所有时间点列表
        List<String> timeListBetween = startAndEnd.getTimeListBetween();
        // 创建日期时间格式化器，用于将时间点格式化为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        // 对时间列表进行排序，确保时间顺序
        timeListBetween.sort((TimeUtils::compareStr));
        log.info("{}",timeListBetween);

        // 初始化Echarts结果对象
        EchartsResult<String, String> result = new EchartsResult<String, String>();
        // 初始化系列数据列表
        List<EchartsResult.SeriesCount<String>> seriesCountList = new ArrayList<>();

        // 创建一个新映射，将时间点转换为字符串格式，以便于后续处理
        Map<String, Double> stringKeyDataMap = new HashMap<>();
        dataMap.forEach((key, value) -> {
            String formattedKey = key.format(formatter);// 将 LocalDateTime 转换为字符串
            log.info("{}",formattedKey);
            stringKeyDataMap.put(formattedKey, value);
        });

        // 遍历每个时间点，构建X轴标签和系列数据
        timeListBetween.stream().forEach(time -> {
            result.getXAxis().add(startAndEnd.getXAxisTimeStr(time));

            Double value = stringKeyDataMap.getOrDefault(time, 0.0);

            EchartsResult.SeriesCount seriesCount = new EchartsResult.SeriesCount();
            seriesCount.setName(startAndEnd.getXAxisTimeStr(time));
            seriesCount.setValue(value);
            seriesCountList.add(seriesCount);
        });
        result.setSeriesCount(seriesCountList);

        return result;
    }


}
