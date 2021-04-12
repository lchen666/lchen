package com.lchen.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.dto.R;
import com.lchen.common.core.dto.SessionContext;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.redis.util.RedisUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
                return setUnauthorizedResponse(exchange, "令牌不存在", 605);
            }
            String token = exchange.getRequest().getCookies().getFirst(Constants.WEB_AUTH).getValue();
            if (StringUtil.isEmpty(token)){
                return setUnauthorizedResponse(exchange, "令牌无效", 601);
            }
            JSONObject jsonObject = redisUtil.getAndExpire(Constants.WEB_TOKEN_KEY + token, JSONObject.class, Constants.WEB_TOKEN_EXPIRE);
            if (jsonObject == null || jsonObject.getString("privateKey") == null || jsonObject.getLong("userId") == null){
                return setUnauthorizedResponse(exchange, "令牌无效", 601);
            }
            if (SessionContext.getContext(redisUtil).checkToken(jsonObject.getString("privateKey"), token) == null) {
                return setUnauthorizedResponse(exchange, "令牌验证失败", 602);
            }
            if (SessionContext.getContext(redisUtil).checkOtherLogin(false, jsonObject.getLong("userId"), token)){
                return setUnauthorizedResponse(exchange, "此账号在别处登录", 603);
            }
        }
        return chain.filter(exchange);
    }

    public Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg, int code){
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(R.fail(code, msg)));
        }));
    }


}
