package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.vo.SiginVo;
import com.sp.yangshengai.service.SigninService;
import io.swagger.v3.oas.annotations.Operation;
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
 * @since 2025-03-10
 */
@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SigninController {

    private final SigninService signinService;

    @GetMapping("/getmonth")
    @Operation(summary = "1 - 12 月的大写，如：Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec")
    public List<SiginVo> getmonth(String str){
        return signinService.getmonth(str);
    }

}
