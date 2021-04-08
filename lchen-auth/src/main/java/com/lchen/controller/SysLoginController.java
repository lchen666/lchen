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


@Api(tags = "系统管理员登录")
@RestController
@Slf4j
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
    @PostMapping("/system/login")
    public R<?> login(@RequestBody LoginFormDto loginForm){
        try {
            return R.ok(loginService.login(loginForm));
        }catch (BaseException e){
            return R.fail(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation("登出")
    @DeleteMapping("/system/loginOut")
    public R<?> loginOut(HttpServletRequest request, HttpServletResponse response){
        try {
            loginService.loginOut(request, response);
            return R.ok();
        }catch (Exception e){
            log.error("SysLoginController loginOut error: " + e.getMessage(), e);
            return R.fail();
        }
    }
}
