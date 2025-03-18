package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.EBigtype;
import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.vo.EBigTypeAndSmallVo;
import com.sp.yangshengai.service.EBigtypeService;
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
 * @since 2025-03-05
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/eBigtype")
@Tag(name = "运动大类")
public class EBigtypeController {

    private final EBigtypeService ebigtypeService;
    @Operation(summary = "获取大类")
    @GetMapping("/list")
    public R<List<EBigtype>> get()
    {
        return R.ok(ebigtypeService.list()) ;
    }

    @Operation(summary = "获取大类小类全部")
    @GetMapping("/getAll")
    public R<List<EBigTypeAndSmallVo>> getAll()
    {
        return R.ok(ebigtypeService.getAll());
    }

    @Operation(summary = "新增大类")
    @PutMapping("/add")
    public R<Void> addBigType(EBigtype bigtype)
    {
       ebigtypeService.save(bigtype);
        return R.ok();
    }

    @Operation(summary = "修改大类")
    @PutMapping ("/edit")
    public R<Void> editBigType(EBigtype bigtype)
    {
        ebigtypeService.saveOrUpdate(bigtype);
        return R.ok();
    }

}
