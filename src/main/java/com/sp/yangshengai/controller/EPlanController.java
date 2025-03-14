package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import com.sp.yangshengai.pojo.entity.bo.EPlanBo;
import com.sp.yangshengai.pojo.entity.vo.EPlanDetailVo;
import com.sp.yangshengai.pojo.entity.vo.EPlanVo;
import com.sp.yangshengai.service.EPlanService;
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
 * @since 2025-03-05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ePlan")
@Tag(name = "运动计划")
public class EPlanController {

    private final EPlanService eplanService;
    @Operation(summary = "添加")
    @PutMapping("/add")
    public R<Void> add(@RequestBody EPlanBo eplanBo) {
        eplanService.add(eplanBo);
        return R.ok();
    }
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Void> update(@RequestBody EPlanBo eplanBo) {
        eplanService.updatePlan(eplanBo);
        return R.ok();
    }
    @Operation(summary = "删除，传计划id")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        eplanService.delete(id);
        return R.ok();
    }
    @Operation(summary = "个人计划分页")
    @GetMapping("/list")
    public TableDataInfo<EPlanVo> list(PageQuery pageQuery) {
        return eplanService.getpage(pageQuery);
    }

    @Operation(summary = "详情，传计划id")
    @GetMapping("/getDetail")
    public R<EPlanDetailVo> getDetail(Integer id) {
        return R.ok(eplanService.getDetailById(id));
    }

    @Operation(summary = "推荐计划")
    @GetMapping("/getSuggestPlan")
    public R<List<EPlanVo>> getSuggestPlan() {
        return R.ok(eplanService.getSuggestPlan());
    }

}
