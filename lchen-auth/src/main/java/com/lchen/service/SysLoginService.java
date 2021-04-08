package com.lchen.service;


import com.lchen.entity.dto.LoginFormDto;
import com.lchen.entity.vo.SysUserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SysLoginService {

    SysUserVO login(LoginFormDto formDto);

    void loginOut(HttpServletRequest request, HttpServletResponse response);
}
