package com.lchen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.jwt.JwtModel;
import com.lchen.common.core.utils.*;
import com.lchen.common.redis.util.RedisUtils;
import com.lchen.user.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {


    @Autowired
    RedisUtils redisUtils;

    /**
     * 生成web token
     * @return
     */
    public String createToken(SysUser sysUser){
        HttpServletRequest request = ServletUtils.getRequest();
        HttpServletResponse response = ServletUtils.getResponse();
        String uuid = UUIDUtils.fastUUID();
        String ip = IpUtils.getIpAddr(request);
        long timeMillis = System.currentTimeMillis();
        String tokenStr = String.format("%s_%s_%s", ip, uuid, timeMillis);
        Map<Integer, String> map = RSAUtil.genKeyPair();
        String publicKey = map.get(0);
        String privateKey = map.get(1);
        String encryptToken = RSAUtil.encrypt(tokenStr, publicKey);
        response.setHeader(Constants.WEB_AUTH, encryptToken);
        CookieUtil.setCookie(request, response, Constants.WEB_AUTH, encryptToken);
        saveSysUserInfo(sysUser, privateKey, encryptToken);
        saveToken(encryptToken, sysUser.getUserId().toString(),false);
        return encryptToken;
    }


    /**
     * 保存系统登录用户信息到redis
     * @param sysUser
     * @param privateKey
     * @param token
     */
    public void saveSysUserInfo(SysUser sysUser, String privateKey, String token){
        Map<String, Object> map = new HashMap<>();
        map.put("privateKey", privateKey);
        map.put("userId", sysUser.getUserId());
        map.put("userName", sysUser.getUserName());
        map.put("nickName", sysUser.getNickName());
        map.put("avatar", sysUser.getAvatar());
        JSONObject json = new JSONObject(map);
        redisUtils.set(Constants.WEB_TOKEN_KEY + token, json.toJSONString(), Constants.WEB_TOKEN_EXPIRE);
    }

    /**
     * 保存app登录信息
     * @param jwtModel
     * @param token
     */
    public void saveUserInfo(JwtModel jwtModel, String token){
        String jsonString = JSONObject.toJSONString(jwtModel);
        redisUtils.set(Constants.APP_TOKEN_KEY + token, jsonString, Constants.APP_TOKEN_EXPIRE);
        //
        saveToken(token, jwtModel.getUserId().toString(),true);
    }

    /**
     * 保存token
     * @param token
     */
    public void saveToken(String token, String userId, boolean isApp){
        if (isApp){
            redisUtils.set(Constants.APP_LOGIN_USER + userId, JSON.toJSONString(token), Constants.APP_TOKEN_EXPIRE);
        }else {
            redisUtils.set(Constants.WEB_LOGIN_USER + userId, JSON.toJSONString(token), Constants.WEB_TOKEN_EXPIRE);
        }
    }

}
