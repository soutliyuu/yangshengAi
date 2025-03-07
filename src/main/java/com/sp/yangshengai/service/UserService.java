package com.sp.yangshengai.service;

import com.sp.yangshengai.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sp.yangshengai.pojo.entity.bo.UserBo;
import com.sp.yangshengai.pojo.entity.vo.TokenVO;
import com.sp.yangshengai.pojo.entity.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
public interface UserService extends IService<User> {

    void signup(UserBo bo);

    TokenVO login(UserBo bo);

    UserVo info();

    void logout();
}
