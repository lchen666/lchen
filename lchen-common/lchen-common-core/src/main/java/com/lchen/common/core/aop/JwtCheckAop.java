package com.lchen.common.core.aop;


import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.constant.HttpStatus;
import com.lchen.common.core.dto.SessionContext;
import com.lchen.common.core.utils.CookieUtil;
import com.lchen.common.core.utils.ResponseUtil;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.redis.util.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述:添加了 JwtCheck 注解 的Aop
 *
 * @Auther: lchen
 * @Date: 2021/4/9
 */
@Component
@Aspect
public class JwtCheckAop {

    @Autowired
    private RedisUtils redisUtil;

    @Pointcut("@annotation(com.lchen.common.core.annotation.JwtCheck)")
    public void jwtCheckAspect() {
    }


    @Around(value = "jwtCheckAspect()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = null;
        if (request.getHeader(Constants.APP_AUTH) != null) {
            //app
            token = request.getHeader(Constants.APP_AUTH);
        } else if(CookieUtil.getCookie(request, Constants.WEB_AUTH) != null){
            //web 其实web已经被网关过滤器拦截了 不用在进行判断
            return point.proceed();
        }
        if (StringUtil.isEmpty(token)){
            ResponseUtil.respJson(response, HttpStatus.UNAUTHORIZED, "token不能为空");
            return null;
        }else {
            JSONObject jsonObject = redisUtil.getAndExpire(Constants.APP_TOKEN_KEY + token, JSONObject.class, Constants.APP_TOKEN_EXPIRE);
            if (jsonObject == null){
                ResponseUtil.respJson(response, HttpStatus.FORBIDDEN, "token已过期");
                return null;
            }
            if (SessionContext.getContext(redisUtil).checkOtherLogin(true, jsonObject.getLong("userId"), token)) {
                ResponseUtil.respJson(response, HttpStatus.UNAUTHORIZED, "在其他地方已有登录");
                return null;
            }
        }
        return point.proceed();
    }

}
