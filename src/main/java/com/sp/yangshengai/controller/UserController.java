package com.sp.yangshengai.controller;

import com.sp.yangshengai.pojo.entity.R;
import com.sp.yangshengai.pojo.entity.bo.UserBo;
import com.sp.yangshengai.pojo.entity.vo.TokenVO;
import com.sp.yangshengai.pojo.entity.vo.UserVo;
import com.sp.yangshengai.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody UserBo bo){

        userService.signup(bo);
    }


    @PostMapping("/login")
    public R<TokenVO> login(@RequestBody UserBo bo){

        return R.ok(userService.login(bo));
    }

    @GetMapping("/info")
    public R<UserVo> info(){

        return R.ok(userService.info());
    }
}
