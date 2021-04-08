package com.lchen.common.core.exception.auth;


import com.lchen.common.core.exception.BaseException;

/**
 * 验证码异常处理
 */
public class CaptchaException extends BaseException {

    private static final long serialVersionUID = 7913896324748357128L;

    public CaptchaException(String msg){super(msg);}
}
