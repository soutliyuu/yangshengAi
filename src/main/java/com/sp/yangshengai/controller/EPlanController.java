package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.EPlanBo;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
import com.sp.yangshengai.pojo.entity.vo.EPlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanVo;
import com.sp.yangshengai.pojo.entity.vo.PlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;
import com.sp.yangshengai.service.EPlanService;
import com.sp.yangshengai.service.PlanService;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ePlan")

public class EPlanController {

    private final EPlanService eplanService;
    @ApiModelProperty(value = "添加")
    @PutMapping("/add")
    public R<Void> add(EPlanBo eplanBo) {
        eplanService.add(eplanBo);
        return R.ok();
    }
    @ApiModelProperty(value = "修改")
    @PutMapping("/update")
    public R<Void> update(EPlanBo eplanBo) {
        eplanService.updatePlan(eplanBo);
        return R.ok();
    }
    @ApiModelProperty(value = "删除，传计划id")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        eplanService.delete(id);
        return R.ok();
    }
    @ApiModelProperty(value = "分页")
    @GetMapping("/list")
    public TableDataInfo<EPlanVo> list(PageQuery pageQuery) {
        return eplanService.getpage(pageQuery);
    }

    @ApiModelProperty(value = "详情，传计划id")
    @GetMapping("/getDetail")
    public R<List<EPlanDetailVo>> getDetail(Integer id) {
        return R.ok(eplanService.getDetailById(id));
    }

    @ApiModelProperty(value = "推荐计划")
    @GetMapping("/getSuggestPlan")
    public R<List<EPlanVo>> getSuggestPlan() {
        return R.ok(eplanService.getSuggestPlan());
    }

}
