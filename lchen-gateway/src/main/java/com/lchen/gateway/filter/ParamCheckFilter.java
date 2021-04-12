package com.lchen.gateway.filter;


import com.lchen.common.core.utils.StringUtil;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(1)
@ConfigurationProperties("check.sign.url")
@Setter
public class ParamCheckFilter implements GlobalFilter {

    @Autowired
    private AuthFilter authFilter;

    private String[] skipSignUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        log.info("{} ParamCheckFilter request to {}", exchange.getRequest().getMethod(),exchange.getRequest().getURI() );
        if (skipSignUrls != null && StringUtil.matches(url, Arrays.stream(skipSignUrls).collect(Collectors.toList()))){
            return chain.filter(exchange);
        }
        //校验时间戳
        List<String> timestamps = exchange.getRequest().getHeaders().get("timestamp");
        boolean b = StringUtil.isNotEmpty(timestamps) && (Long.valueOf(timestamps.get(0))) + 60000 > new Date().getTime();
        if (!b){
            return authFilter.setUnauthorizedResponse(exchange, "请求超时", 701);
        }
        //校验sign是否存在
        List<String> sign = exchange.getRequest().getHeaders().get("sign");
        if (StringUtil.isEmpty(sign))
            return authFilter.setUnauthorizedResponse(exchange, "签名sign不存在", 702);
        //校验签名
        return chain.filter(exchange);
    }
}
