package com.lchen.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.constant.HttpStatus;
import com.lchen.common.core.dto.SessionContext;
import com.lchen.common.core.utils.ServletUtils;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.redis.util.RedisUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties("check.auth.url")
@Slf4j
@Setter
@Order(0)
public class AuthFilter implements GlobalFilter {

    private String[] skipAuthUrls;

    @Autowired
    private RedisUtils redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        log.info("{} AuthFilter request to {}", exchange.getRequest().getMethod(),exchange.getRequest().getURI() );
        if (skipAuthUrls != null && StringUtil.matches(url, Arrays.stream(skipAuthUrls).collect(Collectors.toList()))){
            return chain.filter(exchange);
        }
        if (exchange.getRequest().getHeaders().containsKey(Constants.APP_AUTH)){
            //app端 直接放行
        }else {
            //web 端
            if (!exchange.getRequest().getCookies().containsKey(Constants.WEB_AUTH)){
                return unauthorizedResponse(exchange, "令牌不能为空", HttpStatus.UNAUTHORIZED);
            }
            String token = exchange.getRequest().getCookies().getFirst(Constants.WEB_AUTH).getValue();
            if (StringUtil.isEmpty(token)){
                return unauthorizedResponse(exchange, "令牌不能为空", HttpStatus.UNAUTHORIZED);
            }
            JSONObject jsonObject = redisUtil.getAndExpire(Constants.WEB_TOKEN_KEY + token, JSONObject.class, Constants.WEB_TOKEN_EXPIRE);
            if (jsonObject == null){
                return unauthorizedResponse(exchange, "令牌已过期", HttpStatus.FORBIDDEN);
            }
            if (SessionContext.getContext(redisUtil).checkToken(jsonObject.getString("privateKey"), token) == null) {
                return unauthorizedResponse(exchange, "令牌验证失败", HttpStatus.UNAUTHORIZED);
            }
            if (SessionContext.getContext(redisUtil).checkOtherLogin(false, jsonObject.getLong("userId"), token)){
                return unauthorizedResponse(exchange, "此账号在别处登录", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }


    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg, int code)
    {
        log.error("[鉴权异常处理]请求路径:{} {}", exchange.getRequest().getPath(), msg);
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, code);
    }

}
