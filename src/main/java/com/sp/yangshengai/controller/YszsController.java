package com.sp.yangshengai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sp.yangshengai.pojo.entity.Yszs;
import com.sp.yangshengai.service.YszsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-13
 */
@RestController
@RequestMapping("/yszs")
@RequiredArgsConstructor
@Tag(name = "养生知识")
public class YszsController {
    private final YszsService yszsService;
    @RequestMapping("/getYszs")
    public List<Yszs> getYszs(String name) {
        if (name != null) {
            LambdaQueryWrapper<Yszs> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Yszs::getName, name);
            return yszsService.list(queryWrapper);
        }
        return yszsService.list();
    }

}
