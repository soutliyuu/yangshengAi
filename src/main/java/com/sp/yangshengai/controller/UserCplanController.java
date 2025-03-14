package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.vo.CplanVo;
import com.sp.yangshengai.service.EPlanService;
import com.sp.yangshengai.service.PlanService;
import com.sp.yangshengai.service.UserCplanService;
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
@RequestMapping("/userCplan")
@RequiredArgsConstructor
public class UserCplanController {


    private final UserCplanService userCplanService;

    @GetMapping("/get")
    public R<CplanVo> get(){
        return R.ok(userCplanService.getcplan());
    }

}
