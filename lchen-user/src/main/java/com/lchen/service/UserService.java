package com.lchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lchen.user.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lchen
 * @since 2021-04-08
 */
public interface UserService extends IService<User> {

    User loginOrRegister(String phone, Boolean isLogin);

    List<User> selectUserList();
}
