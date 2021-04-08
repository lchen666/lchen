package com.lchen.service;


import java.util.Map;

/**
 * 验证码处理
 * 
 * @author lchen
 */
public interface ValidateCodeService
{
    /**
     * 生成验证码
     */
    Map<String, Object> createCaptcha();

    /**
     * 校验验证码
     */
    void checkCaptcha(String key, String value);
}
