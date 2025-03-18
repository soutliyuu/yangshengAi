package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.Bigtype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.vo.BigTypeAndSmallVo;
import com.sp.yangshengai.service.BigtypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@Tag(name = "膳食大类")
public class BigtypeController {
    private final BigtypeService bigtypeService;
    @Operation(summary = "获取大类")
    @GetMapping("/list")
    public R<List<Bigtype>> get()
    {
        return R.ok(bigtypeService.list()) ;
    }

    @Operation(summary = "获取大类小类全部")
    @GetMapping("/getAll")
    public R<List<BigTypeAndSmallVo>> getAll()
    {
        return R.ok(bigtypeService.getAll());
    }

    @Operation(summary = "新增大类")
    @PutMapping ("/add")
    public R<Void> addBigType(Bigtype bigtype)
    {
        bigtypeService.save(bigtype);
        return R.ok();
    }

    @Operation(summary = "修改大类")
    @PutMapping ("/edit")
    public R<Void> editBigType(Bigtype bigtype)
    {
        bigtypeService.saveOrUpdate(bigtype);
        return R.ok();
    }

}
