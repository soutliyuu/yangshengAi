package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.PersonalDetail;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.PersonalDetailBo;
import com.sp.yangshengai.pojo.entity.vo.PersonalDetailVo;
import com.sp.yangshengai.service.PersonalDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-10
 */
@RestController
@RequestMapping("/personalDetail")
@Tag(name = "个人详情血糖体重尿酸")
public class PersonalDetailController {

    private PersonalDetailService personalDetailService;
    @PutMapping("/add")
    private R<Void> add(@RequestBody PersonalDetailBo personalDetailbo){
        personalDetailService.add(personalDetailbo);
        return R.ok();
    }

    @GetMapping("/get")
    private R<PersonalDetailVo> get(){

        return R.ok(personalDetailService.getTodayDetail());
    }

}
