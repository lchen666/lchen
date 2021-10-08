package com.lchen.common.core.feign;

import com.lchen.common.core.utils.ServletUtils;
import com.lchen.common.core.utils.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * feign 请求拦截器
 *
 * @author lchen
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtil.isNotNull(httpServletRequest))
        {
            // 传递请求头，防止丢失
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    Enumeration<String> values = httpServletRequest.getHeaders(name);
                    while (values.hasMoreElements()) {
                        String value = values.nextElement();
                        requestTemplate.header(name, value);
                    }
                }
            }
        }
    }
}
