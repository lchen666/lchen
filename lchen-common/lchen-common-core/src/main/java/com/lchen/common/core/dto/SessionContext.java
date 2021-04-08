package com.lchen.common.core.dto;

import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.exception.BaseException;
import com.lchen.common.core.utils.CookieUtil;
import com.lchen.common.core.utils.RSAUtil;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class SessionContext {

    private static SessionContext context;
    private RedisUtils redisUtil;

    public SessionContext(){}

    public SessionContext(RedisUtils redisUtil){
        this.redisUtil = redisUtil;
    }

    public static synchronized SessionContext getContext(RedisUtils redisUtil) {
        if (context == null) {
            context = new SessionContext(redisUtil);
        }
        return context;
    }


    public JSONObject getSession(HttpServletRequest request){
        Boolean isApp = false;
        String sessionId = CookieUtil.getCookie(request, Constants.WEB_AUTH);//web
        if (StringUtil.isEmpty(sessionId)){
            //app
            isApp = true;
            sessionId = request.getHeader(Constants.APP_AUTH);
        }
        return getSession(isApp, sessionId, request);
    }

    public JSONObject getSession(Boolean isApp, String token, HttpServletRequest request){
        JSONObject jsonObject = null;
        if (isApp){
            //获取app token
            jsonObject = redisUtil.getAndExpire(Constants.APP_TOKEN_KEY + token, JSONObject.class, Constants.APP_TOKEN_EXPIRE);
        }else {
            //获取web token
            jsonObject = redisUtil.getAndExpire(Constants.WEB_TOKEN_KEY + token, JSONObject.class, Constants.WEB_TOKEN_EXPIRE);
        }
        if (jsonObject != null){
            // 取web token缓存
            String privateKey = jsonObject.getString("privateKey");
            if (checkToken(privateKey, token) == null) {
                log.error(" token验证失败！");
                throw new BaseException("token验证失败！", 602);
            }
            if (checkOtherLogin(isApp, jsonObject.getLong("userId"), token)) {
                throw new BaseException("此用户在异地登录！", 603);
            }
            // 取出redis值 放入session
            return jsonObject;
        }else {
            log.info(token + " token 已过期！");
            throw new BaseException("token已失效！", 601);
        }
    }


    public boolean checkOtherLogin(Boolean isApp, Long userId, String token){
        String oldToken = "";
        if (isApp){
            oldToken = redisUtil.getAndExpire(Constants.APP_LOGIN_USER + userId, String.class, Constants.APP_TOKEN_EXPIRE);
        }else {
            oldToken = redisUtil.getAndExpire(Constants.WEB_LOGIN_USER + userId, String.class, Constants.WEB_TOKEN_EXPIRE);
        }
        if (token.equals(oldToken)){
            return false;
        }
        return true;
    }

    public String checkToken(String privateKey, String token){
        String newToken = null;
        try {
            newToken = RSAUtil.decrypt(token, privateKey);
        }catch (Exception e){
            return null;
        }
        if (newToken.split("-").length < 3){
            return null;
        }
        return newToken;
    }

}
