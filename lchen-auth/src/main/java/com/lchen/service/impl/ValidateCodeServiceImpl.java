package com.lchen.service.impl;

import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.exception.BaseException;
import com.lchen.common.core.utils.Base64;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.core.utils.UUIDUtils;
import com.lchen.common.redis.util.RedisUtils;
import com.lchen.service.ValidateCodeService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisUtils redisUtil;

    @Override
    public Map<String, Object> createCaptcha() {
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        String uuid = UUIDUtils.simpleUUID();
        String text = specCaptcha.text().toLowerCase();
        redisUtil.set(String.format(Constants.CAPTCHA_CODE_KEY, uuid), text, Constants.CAPTCHA_EXPIRATION*60);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // 输出图片流
        specCaptcha.out(stream);
        String captcha = Base64.encode(stream.toByteArray());
        Map<String, Object> map = new HashMap<>();
        map.put("captcha", "data:image/jpeg;base64,"+ captcha);
        map.put("uuid", uuid);
        map.put("code", text);
        return map;
    }

    @Override
    public void checkCaptcha(String uuid, String code) {
        if (StringUtil.isAnyBlank(code, uuid)){
            throw new BaseException("验证码/uuid参数不能为空!", 201);
        }
        String verifyKey = String.format(Constants.CAPTCHA_CODE_KEY, uuid);
        Object o = redisUtil.get(verifyKey);
        if (o == null){
            throw new BaseException("验证码已失效!", 202);
        }
        redisUtil.del(verifyKey);
        if (!o.toString().equals(code)){
            throw new BaseException("验证码不匹配!", 203);
        }
    }
}
