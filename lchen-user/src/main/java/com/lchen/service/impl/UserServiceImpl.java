package com.lchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lchen.mapper.UserMapper;
import com.lchen.service.UserService;
import com.lchen.user.entity.User;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lchen
 * @since 2021-04-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User loginOrRegister(String phone, Boolean isLogin) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("del_flag", 0);
        User user = this.getBaseMapper().selectOne(queryWrapper);
        if (user == null && !isLogin){
            //注册
            User addUser = new User();
            addUser.setPhone(phone);
            addUser.setUserName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            this.getBaseMapper().insert(addUser);
            return addUser;
        }
        return user;
    }
}
