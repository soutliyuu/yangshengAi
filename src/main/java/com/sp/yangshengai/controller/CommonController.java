package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.PersonalDetailService;
import com.sp.yangshengai.service.SigninService;
import com.sp.yangshengai.service.UserCplanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final SigninService signinService;

    private final UserCplanService userCplanService;

    private final PersonalDetailService personalDetailService;
    @PutMapping("/signin")
    private R<Void> signin(){
        signinService.add();
        return R.ok();

    }

    @PutMapping("/setcplan")
    private R<Void> setcplan(){
        userCplanService.add();
        return R.ok();
    }
}
