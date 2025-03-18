package com.sp.yangshengai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.Bigtype;
import com.sp.yangshengai.pojo.entity.vo.BigTypeAndSmallVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-04
 */
public interface BigtypeService extends IService<Bigtype> {

    List<BigTypeAndSmallVo> getAll();
}
