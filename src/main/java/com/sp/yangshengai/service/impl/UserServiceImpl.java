package com.sp.yangshengai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sp.yangshengai.exception.AnonApi;
import com.sp.yangshengai.pojo.entity.PageQuery;
import com.sp.yangshengai.pojo.entity.TableDataInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sp.yangshengai.exception.CustomException;
import com.sp.yangshengai.pojo.entity.MyUserDetails;
import com.sp.yangshengai.pojo.entity.User;
import com.sp.yangshengai.mapper.UserMapper;
import com.sp.yangshengai.pojo.entity.bo.UserBo;
import com.sp.yangshengai.pojo.entity.vo.TokenVO;
import com.sp.yangshengai.pojo.entity.vo.UserVo;
import com.sp.yangshengai.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.utils.JwtUtils;
import com.sp.yangshengai.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.hibernate.Remove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
@Service
@RequiredArgsConstructor
@Tag(name = "用户")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    private final UserMapper userMapper;

    private  final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private AuthenticationManager getAuthenticationManager() {
       return SpringUtil.getBean(AuthenticationManager.class);
   }

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        return new MyUserDetails(user);
    }


    @Override
    @AnonApi
    @Operation(summary = "注册")
    public void signup(UserBo bo) {
        if (isUsernameExist(bo.getUsername())) {
            throw CustomException.of("用户已经存在");
        }
        // 加密密码
        String encodedPassword = passwordEncoder.encode(bo.getPassword());
        User user = BeanUtil.copyProperties(bo, User.class);
        user.setPassword(encodedPassword);
        user.setRole(0);
        save(user);

    }
    private boolean isUsernameExist(String username) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        return user != null;
    }

    @Override
    @AnonApi
    @Operation(summary = "登陆")
    public TokenVO login(UserBo bo) {
        Authentication authentication = this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(bo.getUsername(),bo.getPassword())
        );
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername(), Long.valueOf(userDetails.getId()));

        return TokenVO.builder().token(token).build();
    }

    @Override
    @Operation(summary = "获取用户信息")
    public UserVo info() {
        Integer id = SecurityUtils.getUserId();
        log.info("id :{}", id);


         User user = Optional.ofNullable(userMapper.selectById(SecurityUtils.getUserId()))
                .orElseThrow(() -> CustomException.of("用户不存在"));

        return  UserVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }

    @Override
    @Operation(summary = "登出")
    public void logout() {
        //jwtUtils.delCacheToken(SecurityUtils.getUserId());
    }

    @Override
    public TableDataInfo<User> getUsers(PageQuery pageQuery) {

        IPage<User> page = userMapper.selectPage(pageQuery.build(), new QueryWrapper<User>());

        return TableDataInfo.build(page);


    }

    @Override
    public void updatePassword(String id, String password) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
        user.setPassword(passwordEncoder.encode(password));
        updateById(user);

    }



}
