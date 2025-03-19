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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class PersonalDetailServiceImpl extends ServiceImpl<PersonalDetailMapper, PersonalDetail> implements PersonalDetailService {

   private final PersonalDetailMapper personalDetailMapper;
    @Override
    public void add(PersonalDetailBo personalDetailbo) {

        PersonalDetail personalDetail = getOne(new LambdaQueryWrapper<PersonalDetail>().eq(PersonalDetail::getUserId, SecurityUtils.getUserId()));
        if (personalDetail == null) {
            personalDetail = PersonalDetail.builder()
                    .userId(SecurityUtils.getUserId())
                    .bloodSugar(personalDetailbo.getBloodSugar())
                    .uricAcid(personalDetailbo.getUricAcid())
                    .weight(personalDetailbo.getWeight())
                    .id(SecurityUtils.getUserId())
                    .date(LocalDateTime.now())
                    .build();
            save(personalDetail);
        } else {
            throw new RuntimeException("今天已经上传信息");
        }
    }

    @Override
    public PersonalDetailVo getTodayDetail() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);
        PersonalDetail personalDetail = getOne(new LambdaQueryWrapper<PersonalDetail>()
                                                                    .eq(PersonalDetail::getUserId ,SecurityUtils.getUserId())
                                                                    .between(PersonalDetail::getDate, startOfDay, endOfDay));
        if (personalDetail == null) {
           throw new RuntimeException("今天还没有上传信息");
         }
        return personalDetail != null ? BeanUtil.copyProperties(personalDetail, PersonalDetailVo.class) : null;
        }

    @Override
    public EchartsResult<String, String> getEchartsData(TimeEnum timeEnum, PersonEnum personEnum) {
        StartAndEnd startAndEnd = new StartAndEnd(timeEnum);
        switch (personEnum){
            case SUGER:
                 Map<LocalDateTime, Double> bloodSugarMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                        .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                        .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getBloodSugar));

                 return Handledata(bloodSugarMap,startAndEnd);
            case WEIGHT:
                Map<LocalDateTime, Double> weightMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                                .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                                .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getWeight));

                    return Handledata(weightMap,startAndEnd);
            case URIC:
                Map<LocalDateTime, Double> uricMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                                .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                                .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getDate,PersonalDetail::getUricAcid));

                return Handledata(uricMap,startAndEnd);
        }
        return null;
    }

    private EchartsResult<String, String> Handledata(Map<LocalDateTime, Double> dataMap,StartAndEnd startAndEnd) {
        List<String> timeListBetween = startAndEnd.getTimeListBetween();
        timeListBetween.sort((TimeUtils::compareStr));
        EchartsResult<String, String> result = new EchartsResult<String, String>();
        List<EchartsResult.SeriesCount<String>> seriesCountList = new ArrayList<>();
        timeListBetween.stream().forEach(time -> {
            result.getXAxis().add(startAndEnd.getXAxisTimeStr(time));

            Double value = dataMap.getOrDefault(LocalDateTime.parse(time), 0.0);

            EchartsResult.SeriesCount seriesCount = new EchartsResult.SeriesCount();
            seriesCount.setName(startAndEnd.getXAxisTimeStr(time));
            seriesCount.setValue(value);
            seriesCountList.add(seriesCount);
        });
        result.setSeriesCount(seriesCountList);

        return result;


    }


}
