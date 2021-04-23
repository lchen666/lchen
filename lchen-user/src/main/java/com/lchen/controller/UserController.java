package com.lchen.controller;


import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.annotation.JwtCheck;
import com.lchen.common.core.dto.R;
import com.lchen.common.core.dto.SessionContext;
import com.lchen.common.core.jwt.JwtModel;
import com.lchen.common.core.jwt.JwtSessionUtil;
import com.lchen.common.core.utils.ServletUtils;
import com.lchen.common.core.utils.poi.ExcelUtil;
import com.lchen.common.redis.util.RedisUtils;
import com.lchen.service.UserService;
import com.lchen.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lchen
 * @since 2021-04-08
 */
@Api(tags = "app用户相关")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtil;

    @ApiOperation(value = "登录或注册", notes = "提供feign")
    @PostMapping("/loginOrRegister")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "isLogin", value = "是否登录", paramType = "form", dataType = "Boolean"),
    })
    public R<User> loginOrRegister(@RequestParam(value = "phone") String phone, @RequestParam(value = "isLogin") Boolean isLogin){
        try {
            return R.ok(userService.loginOrRegister(phone , isLogin));
        }catch (Exception e){
            log.error("UserController getUserInfo error: "+e.getMessage(), e);
        }
        return R.fail("系统繁忙！");
    }

    @ApiOperation(value = "导出用户")
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException
    {
        List<User> list = userService.selectUserList();
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.exportExcel(response, list, "用户数据");
    }

    @JwtCheck
    @ApiOperation(value = "测试切面拦截1")
    @GetMapping("/testJwt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "isLogin", value = "是否登录", paramType = "form", dataType = "Boolean"),
    })
    public R<?> testJwt(@RequestParam(value = "phone") String phone, @RequestParam(value = "isLogin") Boolean isLogin){
        JwtModel session = JwtSessionUtil.getSession(ServletUtils.getRequest());
        return R.ok(session);
    }

    @JwtCheck
    @ApiOperation(value = "测试切面拦截2")
    @PutMapping("/testS")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "isLogin", value = "是否登录", paramType = "form", dataType = "Boolean"),
    })
    public R<?> testS(@RequestParam(value = "phone") String phone, @RequestParam(value = "isLogin") Boolean isLogin){
        JSONObject session = SessionContext.getContext(redisUtil).getSession(ServletUtils.getRequest());
        return R.ok(session);
    }


    @ApiOperation(value = "测试切面拦截3")
    @DeleteMapping("/test3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "isLogin", value = "是否登录", paramType = "form", dataType = "Boolean"),
    })
    public R<?> test3(@RequestParam(value = "phone") String phone, @RequestParam(value = "isLogin") Boolean isLogin){
        JSONObject session = SessionContext.getContext(redisUtil).getSession(ServletUtils.getRequest());
        return R.ok(session);
    }


}
