package com.lchen.user.feign;

import com.lchen.common.core.dto.R;
import com.lchen.user.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "lchen-user", contextId = "UserFeign")
public interface UserFeign {

    @PostMapping("/user/loginOrRegister")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "isLogin", value = "是否登录", paramType = "form", dataType = "Boolean"),
    })
    public R<User> loginOrRegister(@RequestParam(value = "phone") String phone, @RequestParam(value = "isLogin") Boolean isLogin);

}
