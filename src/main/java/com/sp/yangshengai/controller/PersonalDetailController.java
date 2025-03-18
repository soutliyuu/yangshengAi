package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.PersonalDetail;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.PersonalDetailBo;
import com.sp.yangshengai.pojo.entity.vo.EchartsResult;
import com.sp.yangshengai.pojo.entity.vo.PersonalDetailVo;
import com.sp.yangshengai.service.PersonalDetailService;
import com.sp.yangshengai.utils.PersonEnum;
import com.sp.yangshengai.utils.TimeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "个人详情血糖体重尿酸")
public class PersonalDetailController {

    private final PersonalDetailService personalDetailService;
    @PutMapping("/add")
    @Operation(summary = "添加今日")
    private R<Void> add(@RequestBody PersonalDetailBo personalDetailbo){
        personalDetailService.add(personalDetailbo);
        return R.ok();
    }

    @GetMapping("/get")
    @Operation(summary = "获取今日")
    private R<PersonalDetailVo> get(){

        return R.ok(personalDetailService.getTodayDetail());
    }

    @GetMapping("/getEchartsData")
    @Operation(summary = "获取图表数据")
    private R<EchartsResult<String,String>> getEchartsData(TimeEnum timeEnum, PersonEnum personEnum){

        return R.ok(personalDetailService.getEchartsData(timeEnum,personEnum));
    }

}
