package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.*;
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
}
