package com.lchen.user.feign;

import com.lchen.common.core.dto.R;
import com.lchen.user.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "lchen-user")
public interface SysUserFeign {

    @GetMapping("/sysUser/info/{userName}")
    public R<SysUser> getSysUserByUserName(@PathVariable("userName") String userName);

}
