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


    /**
     * 注册用户
     *
     * 此方法用于处理用户注册请求它首先检查用户名是否已存在如果用户名已存在，
     * 则抛出一个自定义异常表示用户已经存在如果用户名不存在，它将密码进行加密，
     * 然后将用户信息保存到数据库中
     *
     * @param bo 用户信息对象，包含用户的基本信息如用户名和密码
     * @throws CustomException 如果用户名已存在时抛出此异常
     */
    @Override
    @AnonApi
    @Operation(summary = "注册")
    public void signup(UserBo bo) {
        // 检查用户名是否已存在
        if (isUsernameExist(bo.getUsername())) {
            throw CustomException.of("用户已经存在");
        }
        // 加密密码
        String encodedPassword = passwordEncoder.encode(bo.getPassword());
        // 将用户信息对象转换为User实体对象
        User user = BeanUtil.copyProperties(bo, User.class);
        user.setPassword(encodedPassword);
        // 设置用户角色为普通用户
        user.setRole(0);
        // 保存用户到数据库
        save(user);
    }
    /**
     * 检查用户名是否已存在于数据库中
     *
     * @param username 待检查的用户名
     * @return 如果用户名已存在，则返回true；否则返回false
     */
    private boolean isUsernameExist(String username) {
        // 通过用户名查询用户信息，如果查询结果不为null，则说明用户名已存在
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 根据查询结果判断用户名是否存在
        return user != null;
    }

    @Override
    @AnonApi
    @Operation(summary = "登陆")
    /**
     * 用户登录方法
     * 该方法主要用于用户身份验证，并生成访问令牌
     *
     * @param bo 用户输入信息，包含用户名和密码
     * @return 返回一个包含令牌的TokenVO对象
     */
    public TokenVO login(UserBo bo) {
        // 尝试使用用户提供的用户名和密码进行身份验证
        Authentication authentication = this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(bo.getUsername(),bo.getPassword())
        );

        // 获取验证通过的用户详细信息
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        // 生成JWT令牌
        String token = jwtUtils.generateToken(userDetails.getUsername(), Long.valueOf(userDetails.getId()));

        // 构建并返回包含令牌的TokenVO对象
        return TokenVO.builder().token(token).build();
    }

    /**
     * 重写获取用户信息的方法
     *
     * 此方法用于获取当前安全上下文中用户的信息，并将其封装为UserVo对象返回
     * 主要步骤包括：
     * 1. 获取当前用户ID
     * 2. 根据用户ID查询用户信息，如果用户不存在，则抛出异常
     * 3. 将查询到的用户信息封装为UserVo对象
     *
     * @return UserVo对象，包含用户信息
     * @throws CustomException 如果用户不存在
     */
    @Override
    @Operation(summary = "获取用户信息")
    public UserVo info() {
        // 获取当前用户ID
        Integer id = SecurityUtils.getUserId();
        log.info("id :{}", id);

        // 根据用户ID查询用户信息，如果用户不存在，则抛出异常
        User user = Optional.ofNullable(userMapper.selectById(SecurityUtils.getUserId()))
                .orElseThrow(() -> CustomException.of("用户不存在"));

        // 将查询到的用户信息封装为UserVo对象
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

    /**
     * 根据分页查询参数获取用户列表
     *
     * @param pageQuery 分页查询对象，包含分页信息和查询条件
     * @return TableDataInfo<User> 包含用户数据和分页信息的对象
     */
    @Override
    public TableDataInfo<User> getUsers(PageQuery pageQuery) {

        // 使用传入的分页查询参数构建查询条件，并从数据库中分页查询用户数据
        IPage<User> page = userMapper.selectPage(pageQuery.build(), new QueryWrapper<User>());

        // 将查询结果封装成TableDataInfo对象返回
        return TableDataInfo.build(page);


    }

    /**
     * 根据用户ID更新用户密码
     *
     * @param id 用户ID，用于定位需要更新密码的用户
     * @param password 新密码，明文形式，将会被加密后存储
     */
    @Override
    public void updatePassword(String id, String password) {
        // 查询用户信息，根据用户ID
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
        // 加密密码并更新到用户对象中
        user.setPassword(passwordEncoder.encode(password));
        // 更新用户信息到数据库
        updateById(user);
    }



}
