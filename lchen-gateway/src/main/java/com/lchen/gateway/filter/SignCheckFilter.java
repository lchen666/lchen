package com.lchen.gateway.filter;


import com.lchen.common.core.constant.HttpStatus;
import com.lchen.common.core.utils.ServletUtils;
import com.lchen.common.core.utils.StringUtil;
import io.netty.buffer.ByteBufAllocator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(1)
@ConfigurationProperties("check.sign.url")
@Setter
public class SignCheckFilter implements GlobalFilter {

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
        boolean b = timestamps != null && (Long.parseLong(timestamps.get(0))) + 60000 > new Date().getTime();
        if (!b){
            return signCheckResponse(exchange, "请求超时", HttpStatus.TIME_OUT);
        }
        //校验sign是否存在
        List<String> signs = exchange.getRequest().getHeaders().get("sign");
        if (StringUtil.isEmpty(signs))
            return signCheckResponse(exchange, "签名sign不存在", HttpStatus.NOT_SIGN);
        //校验签名 1.获取请求中的body 表单参数
        String method = exchange.getRequest().getMethodValue();
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)
                && !HttpMethod.GET.equals(method)){
            //
        }
        return chain.filter(exchange);
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    public Boolean checkSign(Map<String, Object> map){

        return false;
    }

    private Mono<Void> signCheckResponse(ServerWebExchange exchange, String msg, int code)
    {
        log.error("[参数校验异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, code);
    }
}
