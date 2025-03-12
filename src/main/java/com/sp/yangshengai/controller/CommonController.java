package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.PersonalDetailService;
import com.sp.yangshengai.service.SigninService;
import com.sp.yangshengai.service.UserCplanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/setcplanByeplan/{id}")
    private R<Void> setcplan(@PathVariable Integer id){
        userCplanService.addbyeplan(id);
        return R.ok();
    }

    @PutMapping("/setcplanByplan/{id}")
    private R<Void> setplan(@PathVariable Integer id){
        userCplanService.addbyplan(id);
        return R.ok();
    }
}
