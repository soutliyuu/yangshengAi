package com.sp.yangshengai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.EBigtype;
import com.sp.yangshengai.pojo.entity.vo.EBigTypeAndSmallVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
public interface EBigtypeService extends IService<EBigtype> {

    List<EBigTypeAndSmallVo> getAll();

    void deleteBigType(Integer id);
}
