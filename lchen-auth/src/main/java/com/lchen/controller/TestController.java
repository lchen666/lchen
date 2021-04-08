package com.lchen.controller;

import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.dto.SessionContext;
import com.lchen.common.core.utils.ServletUtils;
import com.lchen.common.redis.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    private RedisUtils redisUtil;

    @GetMapping("/test")
    public String test(){
        JSONObject jsonObject = SessionContext.getContext(redisUtil).getSession(ServletUtils.getRequest());
        return jsonObject.toJSONString();
    }
}
