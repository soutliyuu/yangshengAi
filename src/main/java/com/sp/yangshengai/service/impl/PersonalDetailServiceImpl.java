package com.sp.yangshengai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.PersonalDetail;
import com.sp.yangshengai.mapper.PersonalDetailMapper;
import com.sp.yangshengai.pojo.entity.bo.PersonalDetailBo;
import com.sp.yangshengai.pojo.entity.vo.PersonalDetailVo;
import com.sp.yangshengai.service.PersonalDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
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
        return null;
    }
}
