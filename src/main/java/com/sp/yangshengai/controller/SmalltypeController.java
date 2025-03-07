package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.Smalltype;
import com.sp.yangshengai.service.SmalltypeService;
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
 * @since 2025-03-04
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/smalltype")
public class SmalltypeController {
    private final SmalltypeService smalltypeService;
    @GetMapping("/list")
    public R<List<Smalltype>> getByBigtypeId(Integer bigTypeId)
    {
           return R.ok(smalltypeService.list(new LambdaQueryWrapper<Smalltype>().eq(Smalltype::getBigTypeId,bigTypeId)));
    }

}
