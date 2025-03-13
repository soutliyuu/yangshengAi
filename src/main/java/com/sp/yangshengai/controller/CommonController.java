package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.PersonalDetailService;
import com.sp.yangshengai.service.SigninService;
import com.sp.yangshengai.service.UserCplanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Tag(name = "通用接口")
public class CommonController {

    private final SigninService signinService;

    private final UserCplanService userCplanService;

    private final PersonalDetailService personalDetailService;
    @PutMapping("/signin")
    @Operation(summary = "签到")
    private R<Void> signin(){
        signinService.add();
        return R.ok();

    }
    @Operation(summary = "设置当前运动计划")
    @PutMapping("/setcplanByeplan/{id}")
    private R<Void> setcplan(@PathVariable Integer id){
        userCplanService.addbyeplan(id);
        return R.ok();
    }
    @Operation(summary = "设置当前膳食计划")
    @PutMapping("/setcplanByplan/{id}")
    private R<Void> setplan(@PathVariable Integer id){
        userCplanService.addbyplan(id);
        return R.ok();
    }
}
