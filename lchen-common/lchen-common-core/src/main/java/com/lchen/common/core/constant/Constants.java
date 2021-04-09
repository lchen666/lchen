package com.lchen.common.core.constant;

/**
 * 通用常量信息
 * 
 * @author lchen
 */
public class Constants {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 1;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:%s";

    /**
     * 手机验证码 redis key
     */
    public static final String VERIFY_CODE_KEY = "verify_codes:%s";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 5;

    /**
     * web令牌有效期（秒）
     */
    public final static long WEB_TOKEN_EXPIRE = 60 * 60 * 12;

    /**
     * app令牌有效期（秒）
     */
    public final static long APP_TOKEN_EXPIRE = 60 * 60 * 24 * 7;

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;

    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * web权限缓存前缀
     */
    public final static String WEB_TOKEN_KEY = "web_tokens:";

    /**
     * app权限缓存前缀
     */
    public final static String APP_TOKEN_KEY = "app_tokens:";

    /**
     * app令牌自定义标识
     */
    public static final String APP_AUTH = "Authorization";

    /**
     * 后台管理令牌标识
     */
    public static final String WEB_AUTH = "AUTH_TICKET";

    /**
     * web登录用户标识
     */
    public static final String WEB_LOGIN_USER  = "web_user:";

    /**
     * app登录用户标识
     */
    public static final String APP_LOGIN_USER  = "app_user:";

}
