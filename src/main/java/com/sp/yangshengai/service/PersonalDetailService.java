package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.PersonalDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.bo.PersonalDetailBo;
import com.sp.yangshengai.pojo.entity.vo.PersonalDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
public interface PersonalDetailService extends IService<PersonalDetail> {

    void add(PersonalDetailBo personalDetailbo);

    PersonalDetailVo getTodayDetail();
}
