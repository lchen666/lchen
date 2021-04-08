package com.lchen.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CookieUtil.class);

    private CookieUtil() {
    }

    public static String getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        } else {
            Cookie[] var6 = cookies;
            int var5 = cookies.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Cookie c = var6[var4];
                if (name.equalsIgnoreCase(c.getName())) {
                    try {
                        return URLDecoder.decode(c.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException var8) {
                        return c.getValue();
                    }
                }
            }

            return null;
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        try {
            Cookie privacy = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            privacy.setMaxAge(-1);
            privacy.setPath("/");
            response.addCookie(privacy);
        } catch (UnsupportedEncodingException var6) {
            LOG.error(var6.getMessage(), var6);
            throw new RuntimeException(var6);
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int maxAge) throws ServletException {
        try {
            Cookie privacy = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            privacy.setMaxAge(maxAge);
            privacy.setPath("/");
            response.addCookie(privacy);
        } catch (UnsupportedEncodingException var7) {
            LOG.error(var7.getMessage(), var7);
            throw new ServletException(var7);
        }
    }

    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) throws ServletException {
        Cookie privacy = new Cookie(cookieName, (String)null);
        privacy.setMaxAge(0);
        privacy.setPath("/");
        response.addCookie(privacy);
    }
    public static void delAllCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookie=request.getCookies();
        if(cookie!=null) {
            for (Cookie c : cookie) {
                c.setMaxAge(0);
                c.setPath("/");
                response.addCookie(c);
            }
        }
    }
}
