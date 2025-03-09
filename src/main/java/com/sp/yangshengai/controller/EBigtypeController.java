package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.Bigtype;
import com.sp.yangshengai.pojo.entity.EBigtype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.service.EBigtypeService;
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
@RequestMapping("/eBigtype")
@Api(tags = "运动大类")
public class EBigtypeController {

    private final EBigtypeService ebigtypeService;

    @GetMapping("/list")
    public R<List<EBigtype>> get()
    {
        return R.ok(ebigtypeService.list()) ;
    }

}
