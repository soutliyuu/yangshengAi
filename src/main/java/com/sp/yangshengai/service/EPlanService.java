package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.EPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.EPlanBo;
import com.sp.yangshengai.pojo.entity.vo.EPlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
public interface EPlanService extends IService<EPlan> {

    void add(EPlanBo eplanBo);

    void updatePlan(EPlanBo eplanBo);

    void delete(Integer id);

    TableDataInfo<EPlanVo> getpage(PageQuery pageQuery);

    EPlanDetailVo getDetailById(Integer id);

    List<EPlanVo> getSuggestPlan();

    void setSuggestEPlan(Integer id);

    void removeSuggestEPlan(Integer id);
}
