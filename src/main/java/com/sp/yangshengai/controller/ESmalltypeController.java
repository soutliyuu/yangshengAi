package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.ESmalltype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.ESmalltypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "运动小类")
public class ESmalltypeController {

    private final ESmalltypeService esmalltypeService;
    @Operation(summary = "获取小类传大类id")
    @GetMapping("/list")
    public R<List<ESmalltype>> getByBigtypeId(Integer ebigTypeId)
    {
        return R.ok(esmalltypeService.list(new LambdaQueryWrapper<ESmalltype>().eq(ESmalltype::getEBigTypeId,ebigTypeId)));
    }

}
