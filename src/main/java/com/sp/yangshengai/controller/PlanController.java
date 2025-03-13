package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.PlanBo;
import com.sp.yangshengai.pojo.entity.vo.PlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.PlanVo;
import com.sp.yangshengai.service.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "计划")
public class PlanController {

    private final PlanService planService;
    @Operation(summary = "添加计划")
    @PutMapping("/add")
    public R<Void> add(@RequestBody PlanBo planBo) {
        planService.add(planBo);
        return R.ok();
    }
    @Operation(summary = "修改计划")
    @PutMapping("/update")
    public R<Void> update(@RequestBody PlanBo planBo) {
        planService.updatePlan(planBo);
        return R.ok();
    }
    @Operation(summary = "删除计划，传计划id")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        planService.delete(id);
        return R.ok();
    }
    @Operation(summary = "个人计划分页查询")
    @GetMapping("/list")
    public TableDataInfo<PlanVo> list(PageQuery pageQuery) {
        return planService.getpage(pageQuery);
    }

    @Operation(summary = "计划详情")
    @GetMapping("/getDetail")
    public R<List<PlanDetailVo>> getDetail(Integer id) {
        return R.ok(planService.getDetailById(id));
    }

    @Operation(summary = "获取推荐计划")
    @GetMapping("/getSuggestPlan")
    public R<List<PlanVo>> getSuggestPlan() {
        return R.ok(planService.getSuggestPlan());
    }



}
