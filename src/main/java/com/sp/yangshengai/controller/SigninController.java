package com.sp.yangshengai.controller;

import com.sp.yangshengai.service.SigninService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SigninController {

    private final SigninService signinService;

    @GetMapping("/getmonth")
    public String getmonth(String str){
        signinService.getEchartsData(TimeUtils.getTimeEnum(str), PersonEnum.MONTH);
        return
    }

}
