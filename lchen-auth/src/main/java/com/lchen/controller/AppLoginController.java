package com.lchen.controller;

import com.lchen.common.core.dto.R;
import com.lchen.common.core.exception.BaseException;
import com.lchen.common.swagger.annotation.ApiJsonObject;
import com.lchen.common.swagger.annotation.ApiJsonProperty;
import com.lchen.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "app用户登录")
@RestController
@RequestMapping("/app")
public class AppLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @ApiOperation(value = "手机号/密码登录注册", notes = "手机号必填、验证码、密码选填")
    @PostMapping("/phoneLogin")
    @ApiJsonObject(name = "map", value = {
            @ApiJsonProperty(type = String.class, key = "phone",description = "手机号码"),
            @ApiJsonProperty(type = String.class, key = "verifyCode",description = "验证码"),
            @ApiJsonProperty(type = String.class, key = "password",description = "密码")
    })
    public R<?> phoneLogin(@RequestBody Map<String, Object> map){
        return R.ok(userLoginService.login(map));
    }

}
