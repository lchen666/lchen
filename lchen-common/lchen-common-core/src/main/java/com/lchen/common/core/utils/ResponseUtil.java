package com.lchen.common.core.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    /**
     * 返回json
     * @param response
     */
    public static void respJson(HttpServletResponse response, Integer code, String message) throws IOException {

        Map<Object,Object> map = new HashMap<>();
        map.put("code",code);
        map.put("message",message);

        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code);


        String userJson = JSONObject.toJSONString(map);
        OutputStream out = response.getOutputStream();
        out.write(userJson.getBytes("UTF-8"));
        out.flush();
    }
}
