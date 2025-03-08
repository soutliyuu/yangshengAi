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

    @PutMapping("/add")
    public R<Void> add(EPlanBo eplanBo) {
        eplanService.add(eplanBo);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(EPlanBo eplanBo) {
        eplanService.updatePlan(eplanBo);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Integer id) {
        eplanService.delete(id);
        return R.ok();
    }

    @GetMapping("/list")
    public TableDataInfo<EPlanVo> list(PageQuery pageQuery) {
        return eplanService.getpage(pageQuery);
    }

    @GetMapping("/getDetail")
    public R<List<EPlanDetailVo>> getDetail(Integer id) {
        return R.ok(eplanService.getDetailById(id));
    }

    @GetMapping("/getSuggestPlan")
    public R<List<EPlanVo>> getSuggestPlan() {
        return R.ok(eplanService.getSuggestPlan());
    }

}
