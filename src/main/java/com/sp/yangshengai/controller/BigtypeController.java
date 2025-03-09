package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.Bigtype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.BigtypeService;
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
 * @since 2025-03-04
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/bigtype")
@Api(tags = "大类")
public class BigtypeController {
    private final BigtypeService bigtypeService;

    @GetMapping("/list")
    public R<List<Bigtype>> get()
    {
        return R.ok(bigtypeService.list()) ;
    }

}
