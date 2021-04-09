package com.lchen.service.impl;

import com.lchen.common.core.constant.Constants;
import com.lchen.common.core.dto.R;
import com.lchen.common.core.exception.BaseException;
import com.lchen.common.core.jwt.JwtModel;
import com.lchen.common.core.jwt.JwtUtil;
import com.lchen.common.core.utils.PasswordUtil;
import com.lchen.common.core.utils.RSAUtil;
import com.lchen.common.core.utils.StringUtil;
import com.lchen.common.redis.util.RedisUtils;
import com.lchen.entity.vo.UserVO;
import com.lchen.service.UserLoginService;
import com.lchen.user.entity.User;
import com.lchen.user.feign.UserFeign;
import com.lchen.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RefreshScope
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private TokenUtil tokenUtil;

    @Value("${needSend}")
    private boolean needSend;
    @Value("${rsa.private.key}")
    private String privateKey;

    @Override
    public UserVO login(Map<String, Object> map) {
        String phone = (String) map.get("phone");
        if (StringUtil.isEmpty(phone)) {
            throw new BaseException("手机号码不能为空", 101);
        }
        Boolean isLogin = true;
        String verifyCode = (String) map.get("verifyCode");
        if (StringUtil.isNotEmpty(verifyCode)){
            isLogin = false;
            //验证码登录注册
            if (needSend){
                //需要从redis 中获取验证码比对
                Object obj = redisUtil.get(String.format(Constants.VERIFY_CODE_KEY, phone));
                if (obj == null) {
                    throw new BaseException("验证码已过期", 102);
                }
                if (!obj.toString().equals(verifyCode)) {
                    throw new BaseException("验证码不正确", 103);
                }
            }
        }
        R<User> userInfo = userFeign.loginOrRegister(phone, isLogin);
        if (userInfo.getCode() == R.FAIL) {
            throw new BaseException(userInfo.getMsg());
        }
        User user = userInfo.getData();
        String password = (String) map.get("password");
        if (StringUtil.isNotEmpty(password)){
            if (user == null){
                throw new BaseException("手机号不正确", 104);
            }
            //密码校验
            String decryptPassword = RSAUtil.decrypt(password, privateKey);
            String cryptPassword = PasswordUtil.cryptPassword(user.getUserId().toString(), decryptPassword);
            if (!user.getPassword().equals(cryptPassword)){
                throw new BaseException("密码不正确", 105);
            }
        }
        // 生成jwt token
        JwtModel jwtModel = new JwtModel(user.getUserId(), user.getPhone(), user.getUserName());
        String jwtToken = JwtUtil.createJWT(jwtModel, -1);
        tokenUtil.saveUserInfo(jwtModel, jwtToken);
        //
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setToken(jwtToken);
        userVO.setUserName(user.getUserName());
        userVO.setAvatar(user.getAvatar());
        return userVO;
    }
}
