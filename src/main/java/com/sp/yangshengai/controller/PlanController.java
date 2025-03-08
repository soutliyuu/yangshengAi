package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
import com.sp.yangshengai.pojo.entity.vo.PlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;
import com.sp.yangshengai.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-04
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/plan")
public class PlanController {

    private final PlanService planService;

    @PutMapping("/add")
    public R<Void> add(PlanBo planBo) {
        planService.add(planBo);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(PlanBo planBo) {
        planService.updatePlan(planBo);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        planService.delete(id);
        return R.ok();
    }

    @GetMapping("/list")
    public TableDataInfo<PlanVo> list(PageQuery pageQuery) {
        return planService.getpage(pageQuery);
    }

    @GetMapping("/getDetail")
    public R<List<PlanDetailVo>> getDetail(Integer id) {
        return R.ok(planService.getDetailById(id));
    }

    @GetMapping("/getSuggestPlan")
    public R<List<PlanVo>> getSuggestPlan() {
        return R.ok(planService.getSuggestPlan());
    }



}
