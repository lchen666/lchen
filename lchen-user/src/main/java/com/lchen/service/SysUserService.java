package com.lchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lchen.user.entity.SysUser;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author lchen
 * @since 2021-03-29
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getSysUserByUserName(String userName);

}
