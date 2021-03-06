package com.lchen.controller;

import com.lchen.common.core.dto.R;
import com.lchen.common.core.exception.BaseException;
import com.lchen.entity.dto.LoginFormDto;
import com.lchen.service.SysLoginService;
import com.lchen.service.ValidateCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(tags = "系统管理员登录")
@RestController
@Slf4j
@RequestMapping("/system")
public class SysLoginController {

    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private SysLoginService loginService;

    @ApiOperation("获取验证码")
    @GetMapping("/getCaptcha")
    public R<?> getCaptcha(){
        return R.ok(validateCodeService.createCaptcha());
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public R<?> login(@RequestBody @Valid LoginFormDto loginForm){
        return R.ok(loginService.login(loginForm));
    }

    @ApiOperation("登出")
    @DeleteMapping("/loginOut")
    public R<?> loginOut(HttpServletRequest request, HttpServletResponse response){
        loginService.loginOut(request, response);
        return R.ok();
    }
}
