package com.sp.yangshengai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import lombok.RequiredArgsConstructor;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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
    public void signup(UserBo bo) {
        if (isUsernameExist(bo.getUsername())) {
            throw CustomException.of("用户已经存在");
        }
        User user = BeanUtil.copyProperties(bo, User.class);
        save(user);

    }
    private boolean isUsernameExist(String username) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        return user != null;
    }

    @Override
    public TokenVO login(UserBo bo) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(bo.getUsername(),bo.getPassword())
        );
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername(), Long.valueOf(userDetails.getId()));

        return TokenVO.builder().token(token).build();
    }

    @Override
    public UserVo info() {

         User user = Optional.ofNullable(userMapper.selectById(SecurityUtils.getUserId()))
                .orElseThrow(() -> CustomException.of("用户不存在"));

        return  UserVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }



}
