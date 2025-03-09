package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.ESmalltype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.Smalltype;
import com.sp.yangshengai.service.ESmalltypeService;
import com.sp.yangshengai.service.SmalltypeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/eSmalltype")
@Api(tags = "运动小类")
public class ESmalltypeController {

    private final ESmalltypeService esmalltypeService;
    @GetMapping("/list")
    public R<List<ESmalltype>> getByBigtypeId(Integer ebigTypeId)
    {
        return R.ok(esmalltypeService.list(new LambdaQueryWrapper<ESmalltype>().eq(ESmalltype::getEBigTypeId,ebigTypeId)));
    }

}
