package com.lchen.common.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.constant.HttpStatus;
import com.lchen.common.core.dto.R;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    /**
     * 返回json
     * @param response
     */
    public static void respJson(HttpServletResponse response, int code, String message) throws IOException {

        R<?> result = R.fail(code, message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Constants.UTF8);
        response.setStatus(HttpStatus.SUCCESS);


        String jsonString = JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue);
        OutputStream out = response.getOutputStream();
        out.write(jsonString.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}
