package com.lchen.user.feign;

import com.lchen.common.core.dto.R;
import com.lchen.user.entity.SysUser;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "lchen-user", contextId = "SysUserFeign")
public interface SysUserFeign {

    @GetMapping("/sysUser/info/{userName}")
    @ApiImplicitParam(name = "userName", value = "用户名", paramType = "path", dataType = "String")
    public R<SysUser> getSysUserByUserName(@PathVariable("userName") String userName);

}
