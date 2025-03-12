package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.UserBo;
import com.sp.yangshengai.pojo.entity.vo.TokenVO;
import com.sp.yangshengai.pojo.entity.vo.UserVo;
import com.sp.yangshengai.service.UserService;
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
 * @since 2025-03-05
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户")
public class UserController {

    private final UserService userService;
    @Operation(summary = "注册")
    @PostMapping("/signup")
    public R<Void> signup(@RequestBody UserBo bo){
        userService.signup(bo);
        return R.ok();
    }

    @Operation(summary = "登陆")
    @PostMapping("/login")
    public R<TokenVO> login(@RequestBody UserBo bo){

        return R.ok(userService.login(bo));
    }
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public R<UserVo> info(){

        return R.ok(userService.info());
    }
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public void logout(){
        userService.logout();
    }
}
