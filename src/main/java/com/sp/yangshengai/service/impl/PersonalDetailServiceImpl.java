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

        PersonalDetail personalDetail = personalDetailMapper.selectOne(new LambdaQueryWrapper<PersonalDetail>().eq(PersonalDetail::getUserId, SecurityUtils.getUserId()));
        if (personalDetail == null) {
            personalDetail = PersonalDetail.builder()
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
        PersonalDetail personalDetail = personalDetailMapper.selectOne(new LambdaQueryWrapper<PersonalDetail>()
                                                                    .eq(PersonalDetail::getUserId ,SecurityUtils.getUserId())
                                                                    .between(PersonalDetail::getDate, LocalDateTime.now().with(LocalDateTime.MIN),
                                                                            LocalDateTime.now().with(LocalDateTime.MAX)));
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
                Map<Integer, Map.Entry<LocalDateTime, Double>> bloodSugarMap = personalDetailMapper.selectList(new LambdaQueryWrapper<PersonalDetail>()
                        .between(PersonalDetail::getDate, startAndEnd.getStart(), startAndEnd.getEnd())
                        .eq(PersonalDetail::getUserId, SecurityUtils.getUserId()))
                        .stream().collect(Collectors.toMap(PersonalDetail::getId,detail -> Map.entry(detail.getDate(), detail.getBloodSugar())));
                List<String> timeListBetween = startAndEnd.getTimeListBetween();
                timeListBetween.sort((TimeUtils::compareStr));
                EchartsResult<String, String> result = new EchartsResult<String, String>();
                for (String time : timeListBetween) {
                    Map.Entry<LocalDateTime, Double> entry = bloodSugarMap.get(time);
                    result.getXAxis().add(startAndEnd.getXAxisTimeStr(time));
                    result.getSeriesCount().add(entry != null ? entry.getValue() : 0);
                }


                return


                case WEIGHT:
                    return new EchartsResult<String, String>().setXAxis(startAndEnd.getTimeListBetween())
                            .setSeriesCount(startAndEnd.getTimeListBetween().stream().map(time -> {
                            }).toList());
            case URIC:
                        return new EchartsResult<String, String>().setXAxis(startAndEnd.getTimeListBetween())
                                .setSeriesCount(startAndEnd.getTimeListBetween().stream().map(time -> {
                                }).toList());
        }
        return null;
    }


}
