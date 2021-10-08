package com.lchen.controller;

import com.lchen.common.core.dto.R;
import com.lchen.service.SysUserService;
import com.lchen.user.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统用户管理")
@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiModelProperty("根据用户名获取用户信息")
    @GetMapping("/info/{userName}")
    @ApiImplicitParam(name = "userName", value = "用户名", paramType = "path", dataType = "String")
    public R<SysUser> getSysUserInfo(@PathVariable("userName") String userName){
        return R.ok(sysUserService.getSysUserByUserName(userName));
    }
}
