package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.*;
import com.sp.yangshengai.service.EPlanService;
import com.sp.yangshengai.service.PlanService;
import com.sp.yangshengai.service.UserService;
import com.sp.yangshengai.service.YszsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin")
@RequiredArgsConstructor
@Tag(name = "管理接口")
public class AdminController {

    private final UserService userService;

    private final YszsService yszsService;

    private final PlanService planService;

    private final EPlanService ePlanService;

    @GetMapping("/users")
    @Operation(summary = "获取用户列表")
    public TableDataInfo<User> getUsers(PageQuery pageQuery){

        return userService.getUsers(pageQuery);
    }
    @Operation(summary = "修改密码")
    @PutMapping("/updatePassword")
    public R<Void> updatePassword(String id, String password){
        userService.updatePassword(id,password);
        return R.ok();
    }

    @Operation(summary = "设置养生知识")
    @PutMapping("/setYszs")
    public R<Void> setYszs(@RequestBody Yszs yszs){
        yszsService.saveOrUpdate(yszs);
        return R.ok();
    }

    @Operation(summary = "设置为推荐计划,仅管理员可设置,膳食计划的推荐计划")
    @PutMapping("setsuggestplan")
    public R<Void> setSuggestPlan(Integer id){

        planService.setSuggestPlan(id);

        return R.ok();

    }
    @Operation(summary = "移除推荐计划,仅管理员可设置,膳食计划的推荐计划")
    @PutMapping("removesuggestplan")
    public R<Void> removeSuggestPlan(Integer id){
        planService.removeSuggestPlan(id);
        return R.ok();
    }
    @Operation(summary = "设置为推荐计划,仅管理员可设置,运动计划的推荐计划")
    @PutMapping("setsuggesteplan")
    public R<Void> setSuggestEPlan(Integer id){
        ePlanService.setSuggestEPlan(id);
        return R.ok();
    }
    @Operation(summary = "移除推荐计划,仅管理员可设置,运动计划的推荐计划")
    @PutMapping("removesuggesteplan")
    public R<Void> removeSuggestEPlan(Integer id){
        ePlanService.removeSuggestEPlan(id);
        return R.ok();
    }

}
