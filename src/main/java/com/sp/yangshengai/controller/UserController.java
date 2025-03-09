package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.UserBo;
import com.sp.yangshengai.pojo.entity.vo.TokenVO;
import com.sp.yangshengai.pojo.entity.vo.UserVo;
import com.sp.yangshengai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "用户")
public class UserController {

    private final UserService userService;
    @ApiModelProperty(value = "注册")
    @PostMapping("/signup")
    public void signup(@RequestBody UserBo bo){

        userService.signup(bo);
    }

    @ApiModelProperty(value = "登录")
    @PostMapping("/login")
    public R<TokenVO> login(@RequestBody UserBo bo){

        return R.ok(userService.login(bo));
    }
    @ApiModelProperty(value = "获取个人信息")
    @GetMapping("/info")
    public R<UserVo> info(){

        return R.ok(userService.info());
    }
    @ApiModelProperty(value = "推出登录")
    @PostMapping("/logout")
    public void logout(){
        userService.logout();
    }
}
