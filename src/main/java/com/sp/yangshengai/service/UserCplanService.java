package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.UserCplan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.vo.CplanVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
public interface UserCplanService extends IService<UserCplan> {

    void addbyeplan(Integer id);

    void addbyplan(Integer id);

    CplanVo getcplan();
}
