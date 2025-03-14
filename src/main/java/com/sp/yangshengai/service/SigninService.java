package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.Signin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.vo.SiginVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
public interface SigninService extends IService<Signin> {

    void add();

    List<SiginVo> getmonth(String str);
}
