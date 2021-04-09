package com.lchen.common.core.jwt;

import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.constant.Constants;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class JwtSessionUtil {

    /**
     * 从token里面获取用户信息
     * @param request
     * @return
     */
    public static JwtModel getSession(HttpServletRequest request) {
        String jwtToken = request.getHeader(Constants.APP_AUTH).split("\\|")[0];
        Claims claims = JwtUtil.parseJWT(jwtToken);
        String subject = claims.getSubject();
        JwtModel jwtModel = JSONObject.parseObject(subject, JwtModel.class);
        return jwtModel;
    }

}
